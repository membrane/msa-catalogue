package de.predi8.workshop.catalogue.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( NOT_FOUND)
public class NotFoundException extends RuntimeException {
}