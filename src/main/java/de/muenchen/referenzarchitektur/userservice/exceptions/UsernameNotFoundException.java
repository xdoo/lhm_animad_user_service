/*
* Copyright (c): it@M - Dienstleister fuer Informations- und Telekommunikationstechnik
* der Landeshauptstadt Muenchen, 2018
*/
package de.muenchen.referenzarchitektur.userservice.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author roland
 */
@ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason="token or username not found")  // 401
public class UsernameNotFoundException extends Exception {

    private final String message;

    public UsernameNotFoundException(String message) {
        this.message = message;
    }

}
