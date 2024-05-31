package org.FelipeBert.ecommerce.service.exception;

public class NotFoundException extends BusinessException{
    public NotFoundException(){
        super("Resource Not Found");
    }
}
