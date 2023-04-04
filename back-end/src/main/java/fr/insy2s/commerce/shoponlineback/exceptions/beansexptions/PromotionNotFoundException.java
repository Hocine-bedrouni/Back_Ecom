package fr.insy2s.commerce.shoponlineback.exceptions.beansexptions;

import fr.insy2s.commerce.shoponlineback.exceptions.generic_exception.WebservicesGenericServiceException;

public class PromotionNotFoundException extends WebservicesGenericServiceException {

    public PromotionNotFoundException(String message) {
        super(message);
    }
}
