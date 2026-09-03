package org.company.showbookingapp.exception;

public class DuplicateResourceException extends RuntimeException{

    public DuplicateResourceException(String message)
    {
        super(message);
    }
}
