package fr.insy2s.commerce.shoponlineback.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import fr.insy2s.commerce.shoponlineback.beans.Account;
import fr.insy2s.commerce.shoponlineback.beans.Address;
import fr.insy2s.commerce.shoponlineback.beans.OrderDetails;
import fr.insy2s.commerce.shoponlineback.beans.Ordered;
import fr.insy2s.commerce.shoponlineback.beans.Product;
import fr.insy2s.commerce.shoponlineback.dtos.OrderedDTO;
import fr.insy2s.commerce.shoponlineback.dtos.ProductDTO;
import fr.insy2s.commerce.shoponlineback.enums.OrderedStatus;
import fr.insy2s.commerce.shoponlineback.exceptions.beansexptions.OrderedNotFoundException;
import fr.insy2s.commerce.shoponlineback.exceptions.beansexptions.ProductNotFoundException;
import fr.insy2s.commerce.shoponlineback.exceptions.generic_exception.WebservicesGenericServiceException;
import fr.insy2s.commerce.shoponlineback.interfaces.Webservices;
import fr.insy2s.commerce.shoponlineback.mappers.AddressMapper;
import fr.insy2s.commerce.shoponlineback.mappers.AddressMapperImpl;
import fr.insy2s.commerce.shoponlineback.mappers.OrderedMapper;
import fr.insy2s.commerce.shoponlineback.mappers.OrderedMapperImpl;
import fr.insy2s.commerce.shoponlineback.mappers.ProductMapper;
import fr.insy2s.commerce.shoponlineback.mappers.ProductMapperImpl;
import fr.insy2s.commerce.shoponlineback.repositories.AccountRepository;
import fr.insy2s.commerce.shoponlineback.repositories.OrderDetailsRepository;
import fr.insy2s.commerce.shoponlineback.repositories.OrderedRepository;
import fr.insy2s.commerce.shoponlineback.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderedService implements Webservices<OrderedDTO, WebservicesGenericServiceException> {

    private final OrderedRepository orderedRepository;
    private final AccountRepository accountRepository;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper = new ProductMapperImpl();

    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderedMapper orderedMapper = new OrderedMapperImpl();

    private final AddressMapper addressMapper = new AddressMapperImpl();

    private final UuidService uuidService;


    @Override
    public Page<OrderedDTO> all(Pageable pageable) {
        return this.orderedRepository.findAll(pageable)
                .map(this.orderedMapper::fromOrdered);
    }
    
    public Page<OrderedDTO> allAccountOrders(Long accountId, Pageable pageable) {
    	Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "account doesn't exist"));
    	
    	Account authenticatedAccount = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
    	// Vérifier que le compte authentifié correspond bien à celui demandé
    	if(!account.getId().equals(authenticatedAccount.getId())) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    	}
    	
    	return this.orderedRepository.findAllByAccountId(account.getId(), pageable)
    			.map(this.orderedMapper::fromOrdered);
    }
    
    public OrderedDTO getAccountOrder(Long accountId, Long orderId) {
    	Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "account doesn't exist"));
    	
    	Account authenticatedAccount = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
    	// Vérifier que le compte authentifié correspond bien à celui demandé
    	if(!account.getId().equals(authenticatedAccount.getId())) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    	}
    	
    	final Ordered order = orderedRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order doesn't exist"));
    	
    	// Vérifier que le compte ait bien accès à la commande
    	if(order.getAccount().getId() != account.getId()) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    	}
    	
    	return this.orderedMapper.fromOrdered(order);
    }

    @Override
    public void add(OrderedDTO e) {
        e.setRefOrdered(this.uuidService.generateUuid());
        if (e.getStatut() == null)
            e.setStatut(OrderedStatus.CREATED);
        Ordered ordered = this.orderedMapper.fromOrderedDTO(e);
        this.orderedRepository.save(ordered);
    }

    @Override
    public OrderedDTO update(Long id, OrderedDTO e) {
        return this.orderedMapper.fromOrdered(this.orderedRepository.findById(id)
                .map(p-> {
                    p.setRefOrdered(this.uuidService.generateUuid());
                    if(p.getOrderedDate() != null){
                        p.setOrderedDate(e.getOrderedDate());
                    }
                    if (p.getStatut() != null){
                        p.setStatut(e.getStatut());
                    }
                    if(p.getDeliveryDate() != null){
                        p.setDeliveryDate(e.getDeliveryDate());
                    }

                    if (p.getDeliveryAddress() != null)
                    {
                        Address deliveryAddress = this.addressMapper.fromAddressDTO(e.getDeliveryAddress());
                        p.setDeliveryAddress(deliveryAddress);
                    }

                    if (p.getBillingAddress() != null)
                    {
                        Address billingAddress = this.addressMapper.fromAddressDTO(e.getBillingAddress());
                        p.setBillingAddress(billingAddress);
                    }
/*                    if (p.getInvoices() != null)
                    {
                        List<Invoice> invoices = e.getInvoices().stream().map(this.invoiceMapper::fromInvoiceDTO).collect(Collectors.toList());
                        p.setInvoices(invoices);
                    }*/
                    return this.orderedRepository.save(p);
                }).orElseThrow(() -> new ProductNotFoundException("Ordered with id " +id+ " was not found")));
    }


    @Override
    public void remove(Long id) {

        Optional<Ordered> ordered = this.orderedRepository.findById(id);

        if (ordered.isEmpty())
            throw new OrderedNotFoundException("Ordered with id " +id+ " was not found");
        this.orderedRepository.deleteById(id);
    }

    @Override
    public Optional<OrderedDTO> getById(Long id) {

        return this.orderedRepository.findById(id)
                .map(this.orderedMapper::fromOrdered)
                .map(Optional::of)
                .orElseThrow(() -> new OrderedNotFoundException("Ordered with id " +id+ " was not found"));
    }

    public List<OrderedDTO> getOrderedsByRefAccount(String refAccount){
        Optional<Account> account = this.accountRepository.findByRefAccount(refAccount);
        List<Ordered> listOrdereds = this.orderedRepository.findByAccount(account.get());
        return listOrdereds.stream().map(this.orderedMapper::fromOrdered).collect(Collectors.toList());

    }

    // get All product by

    public Page<ProductDTO> allProductByRefOrdered(String refOrdered, Pageable pageable)
    {
        Optional<Ordered> ordered = this.orderedRepository.findByRefOrdered(refOrdered);

        if (ordered.isEmpty())
            throw  new OrderedNotFoundException("sorry is not found");

        List<Product> products = ordered.get().getOrderDetails().stream().map(OrderDetails::getProduct).collect(Collectors.toList());

        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

        return productPage
                .map(this.productMapper::fromProduct);
    }

}
