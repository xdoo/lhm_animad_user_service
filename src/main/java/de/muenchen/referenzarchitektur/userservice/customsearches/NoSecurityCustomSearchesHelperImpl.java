/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.referenzarchitektur.userservice.customsearches;

import de.muenchen.referenzarchitektur.userservice.exceptions.UsernameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author roland
 */
@Configuration
@Profile("no-security")
public class NoSecurityCustomSearchesHelperImpl implements CustomSearchesHelper {

    private static final Logger LOG = LoggerFactory.getLogger(NoSecurityCustomSearchesHelperImpl.class);

    public String getUsernameFromSession() throws UsernameNotFoundException {        
        return "animad_admin";
    }
}
