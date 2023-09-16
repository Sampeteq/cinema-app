package com.cinema.catalog.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class ScreeningDateInPastException extends ValidationException {

    public ScreeningDateInPastException() {
        super("Screening date cannot be in the past");
    }
}
