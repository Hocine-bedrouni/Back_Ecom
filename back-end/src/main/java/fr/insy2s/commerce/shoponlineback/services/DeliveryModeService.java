package fr.insy2s.commerce.shoponlineback.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insy2s.commerce.shoponlineback.beans.DeliveryMode;
import fr.insy2s.commerce.shoponlineback.exceptions.generic_exception.WebservicesGenericServiceException;
import fr.insy2s.commerce.shoponlineback.interfaces.Webservices;
import fr.insy2s.commerce.shoponlineback.repositories.DeliveryModeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryModeService  implements Webservices<DeliveryMode, WebservicesGenericServiceException> {
	private final DeliveryModeRepository deliveryModeRepository;
	
	@Override
	public Page<DeliveryMode> all(Pageable pageable) {
    	return deliveryModeRepository.findAll(pageable);
	}

	@Override
	public void add(DeliveryMode e) throws WebservicesGenericServiceException {
		throw new UnsupportedOperationException();
	}

	@Override
	public DeliveryMode update(Long id, DeliveryMode e) throws WebservicesGenericServiceException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(Long id) throws WebservicesGenericServiceException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<DeliveryMode> getById(Long id) throws WebservicesGenericServiceException {
		throw new UnsupportedOperationException();
	}



}
