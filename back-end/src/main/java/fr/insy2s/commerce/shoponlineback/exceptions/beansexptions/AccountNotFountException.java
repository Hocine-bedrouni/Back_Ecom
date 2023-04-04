package fr.insy2s.commerce.shoponlineback.exceptions.beansexptions;

import fr.insy2s.commerce.shoponlineback.exceptions.generic_exception.WebservicesGenericServiceException;

public class AccountNotFountException extends WebservicesGenericServiceException {

    public AccountNotFountException(String message){
        super(message);
    }

}
