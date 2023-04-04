package fr.insy2s.commerce.shoponlineback.exceptions.beansexptions;

import fr.insy2s.commerce.shoponlineback.exceptions.generic_exception.WebservicesGenericServiceException;

public class ProductNotFoundException extends WebservicesGenericServiceException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}
