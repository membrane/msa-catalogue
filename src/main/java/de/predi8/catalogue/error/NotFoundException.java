package de.predi8.catalogue.error;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus( NOT_FOUND)
public class NotFoundException extends RuntimeException {
}