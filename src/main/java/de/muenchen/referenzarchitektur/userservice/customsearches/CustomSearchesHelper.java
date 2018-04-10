/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.referenzarchitektur.userservice.customsearches;

import de.muenchen.referenzarchitektur.userservice.exceptions.UsernameNotFoundException;

/**
 *
 * @author roland
 */
public interface CustomSearchesHelper {
    String getUsernameFromSession()  throws UsernameNotFoundException;
}
