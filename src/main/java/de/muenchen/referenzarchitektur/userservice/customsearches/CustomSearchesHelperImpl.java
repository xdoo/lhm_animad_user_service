/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.referenzarchitektur.userservice.customsearches;

import de.muenchen.referenzarchitektur.userservice.exceptions.UsernameNotFoundException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

/**
 *
 * @author roland
 */
@Configuration
@Profile("!no-security")
public class CustomSearchesHelperImpl implements CustomSearchesHelper {

    private static final Logger LOG = LoggerFactory.getLogger(CustomSearchesHelperImpl.class);

    @Override
    public String getUsernameFromSession() throws UsernameNotFoundException {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) a.getDetails();
        String token = details.getTokenValue();
        if (token == null) {
            LOG.error("No token found in Security Context!");
            throw new UsernameNotFoundException("No token found in Security Context!");
        }
        Jwt jwt = JwtHelper.decode(token);

        String preferredUsername = null;
        if (jwt != null) {
            String claims = jwt.getClaims();
            if (claims != null) {
                JSONObject json = new JSONObject(claims);
                preferredUsername = json.get("preferred_username").toString();
            } else {
                LOG.error("No claims found for token " + token);
            }
        }

        if (preferredUsername == null) {
            LOG.error("No preferred_username found for token " + token);
            throw new UsernameNotFoundException("No preferred_username found in token.");
        }

        return preferredUsername;
    }
}
