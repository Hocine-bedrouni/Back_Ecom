package fr.insy2s.commerce.shoponlineback.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import fr.insy2s.commerce.shoponlineback.beans.Address;
import fr.insy2s.commerce.shoponlineback.dtos.AddressDTO;
import fr.insy2s.commerce.shoponlineback.exceptions.generic_exception.WebservicesGenericServiceException;
import fr.insy2s.commerce.shoponlineback.interfaces.Webservices;
import fr.insy2s.commerce.shoponlineback.mappers.AddressMapper;
import fr.insy2s.commerce.shoponlineback.mappers.AddressMapperImpl;
import fr.insy2s.commerce.shoponlineback.repositories.AccountRepository;
import fr.insy2s.commerce.shoponlineback.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService implements Webservices<AddressDTO, WebservicesGenericServiceException>{
	private final AccountRepository accountRepository;
	
	private final AddressRepository addressRepository;
	
    private final AddressMapper addressMapper = new AddressMapperImpl();
	
	@Override
	public Page<AddressDTO> all(Pageable pageable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(AddressDTO e) throws WebservicesGenericServiceException {
		accountRepository.findById(e.getAccount().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "account doesn't exist"));
		addressRepository.save(addressMapper.fromAddressDTO(e));
	}

	@Override 
	public AddressDTO update(Long id, AddressDTO e) throws WebservicesGenericServiceException {
		Address address = addressRepository.findById(id).orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "address doesn't exist"));
		
		address.setActive(e.getActive());
		address.setCity(e.getCity());
		address.setPostalCode(e.getPostalCode());
		address.setStreet(e.getStreet());
		address.setType(e.getType());
		
		return addressMapper.fromAddress(addressRepository.save(address));
	}

	@Override
	public void remove(Long id) throws WebservicesGenericServiceException {
		throw new UnsupportedOperationException();	
	}

	@Override
	public Optional<AddressDTO> getById(Long id) throws WebservicesGenericServiceException {
		return Optional.of(addressMapper.fromAddress(addressRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "address doesn't exist"))));
	}
	
	public List<AddressDTO> getByAccountId(Long accountId) {
		return addressRepository.findAllByAccountId(accountId)
				.stream().map(addressMapper::fromAddress)
                .collect(Collectors.toList());
	}

}
