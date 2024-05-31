package org.FelipeBert.ecommerce.service.exception;

public class IdsMustBeEqualsException extends BusinessException{
    public IdsMustBeEqualsException(){
        super("Ids must be the same");
    }
}
