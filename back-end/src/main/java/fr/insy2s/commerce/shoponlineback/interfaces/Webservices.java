package fr.insy2s.commerce.shoponlineback.interfaces;

import fr.insy2s.commerce.shoponlineback.dtos.AccountDTO;
import fr.insy2s.commerce.shoponlineback.exceptions.generic_exception.WebservicesGenericServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface Webservices<T, E extends WebservicesGenericServiceException> {

    Page<T> all(Pageable pageable);

 //TODO Void à modifier en <T>
    void add(T e) throws WebservicesGenericServiceException;

    T update(Long id, T e) throws WebservicesGenericServiceException;

    void remove(Long id) throws WebservicesGenericServiceException;

    Optional<T> getById(Long id) throws WebservicesGenericServiceException;
}
