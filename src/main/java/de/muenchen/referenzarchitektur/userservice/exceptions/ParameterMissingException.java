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
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="parameter missing") //400
public class ParameterMissingException extends Exception {

    private final String message;

    public ParameterMissingException(String message) {
        this.message = message;
    }

}
