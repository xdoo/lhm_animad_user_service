/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.referenzarchitektur.userservice.services;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.representation.EntitlementResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

/**
 *
 * @author roland.werner
 */
@Service
@Profile("!no-security")
public class EntitlementsService {

    private static final Logger LOG = Logger.getLogger(EntitlementsService.class.getName());

    @Autowired
    private OAuth2RestTemplate oauth2RestTemplate;

    @Value("${security.oauth2.entitlements.entitlementsUri}")
    private String authUrl;


    /**
     * Holt die Permissions für den aktuellen User. Verwendet dafür das Token
     * aus dem aktuellen SecurityContext.
     *
     * @param useKeyCloakApi ob das KeyCloak-JAR genutzt werden soll oder ein
     * direkter REST-Call erfolgen soll
     * @return die Permissions zu diesem Token
     */
    public Set<String> getPermissions(boolean useKeyCloakApi) {

        String rpt;
        if (useKeyCloakApi) {
            rpt = retrieveRPTviaEntitlementsWithKeyCloakAPI();
        } else {
            rpt = retrieveRPTviaEntitlements();
        }

        Set<String> permissionsSet;
        if (rpt != null) {
            permissionsSet = extractPermissionsFromRPT(rpt);
            LOG.fine("Permissions retrieved from KeyCloak: " + permissionsSet.toString());
        } else {
            permissionsSet = new HashSet<>();
        }

        return permissionsSet;
    }

    /**
     * Ruft die Entitlements-API direkt über RestTemplate auf und holt das
     * RPT-Token.
     *
     * @param token das Token, das an KeyCloak gesendet wird
     * @return das RPT-Token
     */
    private String retrieveRPTviaEntitlements() {
        LOG.fine("Called retrieveRPTviaEntitlements");

        String rpt = null;
        
        try {
            String response = oauth2RestTemplate.getForObject(authUrl, String.class);
            JSONObject responseJSON = new JSONObject(response);

            rpt = responseJSON.getString("rpt");
            LOG.fine("Got this RPT: " + rpt);
        } catch (HttpClientErrorException e) {
            LOG.severe("Error while calling KeyCloak for Entitlements. Status Code: " + e.getStatusText());
        }

        return rpt;
    }

    /**
     * Ruft die Entitlements-API über das KeyCloak-JAR auf und holt das
     * RPT-Token.
     *
     * @param token das Token, das an KeyCloak gesendet wird
     * @return das RPT-Token
     */
    private String retrieveRPTviaEntitlementsWithKeyCloakAPI() {
        LOG.fine("Called retrieveRPTviaEntitlementsWithKeyCloakAPI");

        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) a.getDetails();
        String token = details.getTokenValue();
        AuthzClient authzClient = AuthzClient.create();

        //TODO hier auch ein Try-Catch einbauen falls KeyCloak nicht erreichbar (wie oben)
        EntitlementResponse response = authzClient.entitlement(token)
                .getAll("openIdDemo");
        String rpt = response.getRpt();
        LOG.fine("Got this RPT: " + rpt);
        return rpt;
    }

    /**
     * Parst die Permissions aus dem übergebenen RPT-Token. Das RPT-Token wird
     * dabei zunächst aus dem Base64-Format konvertiert.
     *
     * @param rpt
     * @return
     */
    public Set<String> extractPermissionsFromRPT(String rpt) {
        Set<String> resourceSetList = new HashSet<>();
        Jwt jwt = JwtHelper.decode(rpt);
        if (jwt != null) {
            String claims = jwt.getClaims();
            if (claims != null) {
                JSONObject json = new JSONObject(claims);
                if (json != null) {
                    JSONObject authorization = json.getJSONObject("authorization");
                    if (authorization != null) {
                        JSONArray array = authorization.getJSONArray("permissions");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject resource = (JSONObject) array.get(i);
                                if (resource != null && resource.get("resource_set_name") != null) {
                                    String resourceSetName = resource.get("resource_set_name").toString();
                                    resourceSetList.add(resourceSetName);
                                } else {
                                    throw new RuntimeException("Resource not found");
                                }
                            }
                        } else {
                            throw new RuntimeException("permissions not filled");
                        }
                    } else {
                        throw new RuntimeException("Array not filled");
                    }
                } else {
                    throw new RuntimeException("authorization not filled");
                }
            } else {
                throw new RuntimeException("claims not filled");
            }
        } else {
            throw new RuntimeException("no claims");
        }
        return resourceSetList;
    }

}
