package fr.insy2s.commerce.shoponlineback.exceptions.beansexptions;

import fr.insy2s.commerce.shoponlineback.exceptions.generic_exception.WebservicesGenericServiceException;

public class OrderDetailsNotFoundException extends WebservicesGenericServiceException {

    public OrderDetailsNotFoundException(String message) {
        super(message);
    }
}
