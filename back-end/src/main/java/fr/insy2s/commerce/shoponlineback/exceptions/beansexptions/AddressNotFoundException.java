package fr.insy2s.commerce.shoponlineback.exceptions.beansexptions;

import fr.insy2s.commerce.shoponlineback.exceptions.generic_exception.WebservicesGenericServiceException;

public class AddressNotFoundException extends WebservicesGenericServiceException {

    public AddressNotFoundException(String message) {
        super(message);
    }
}
