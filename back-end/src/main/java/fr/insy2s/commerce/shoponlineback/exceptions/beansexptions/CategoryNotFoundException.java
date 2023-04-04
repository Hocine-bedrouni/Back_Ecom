package fr.insy2s.commerce.shoponlineback.exceptions.beansexptions;

import fr.insy2s.commerce.shoponlineback.exceptions.generic_exception.WebservicesGenericServiceException;

public class CategoryNotFoundException extends WebservicesGenericServiceException {

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
