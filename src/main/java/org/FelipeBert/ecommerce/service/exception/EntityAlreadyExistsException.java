package org.FelipeBert.ecommerce.service.exception;

public class EntityAlreadyExistsException extends BusinessException{
    public EntityAlreadyExistsException(String msg){
        super(msg + " Already Exists");
    }
}
