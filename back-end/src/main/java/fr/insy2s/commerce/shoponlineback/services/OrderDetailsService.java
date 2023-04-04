package fr.insy2s.commerce.shoponlineback.services;

import fr.insy2s.commerce.shoponlineback.beans.KeyOfOrderDetails;
import fr.insy2s.commerce.shoponlineback.beans.OrderDetails;
import fr.insy2s.commerce.shoponlineback.beans.Ordered;
import fr.insy2s.commerce.shoponlineback.beans.Product;
import fr.insy2s.commerce.shoponlineback.dtos.OrderDetailsDTO;
import fr.insy2s.commerce.shoponlineback.exceptions.beansexptions.OrderDetailsNotFoundException;
import fr.insy2s.commerce.shoponlineback.mappers.*;
import fr.insy2s.commerce.shoponlineback.repositories.OrderDetailsRepository;
import fr.insy2s.commerce.shoponlineback.repositories.OrderedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;

    private final OrderedRepository orderedRepository;

    private final OrderDetailsMapper orderDetailsMapper = new OrderDetailsMapperImpl();

    private final ProductMapper productMapper = new ProductMapperImpl();

    private final OrderedMapper orderedMapper = new OrderedMapperImpl();

    public Page<OrderDetailsDTO> all(Pageable pageable)
    {
        return this.orderDetailsRepository.findAll(pageable)
                .map(this.orderDetailsMapper::fromOrderDetails);
    }

    public void add(OrderDetailsDTO orderDetailsDTO)
    {
        this.orderDetailsRepository.save(this.orderDetailsMapper.fromOrderDetailsDTO(orderDetailsDTO));
    }

    public OrderDetailsDTO update(Long idProduct, Long idOrdered, OrderDetailsDTO orderDetailsDTO)
    {
        KeyOfOrderDetails keyOfOrderDetails = new KeyOfOrderDetails(idProduct, idOrdered);

        return this.orderDetailsMapper.fromOrderDetails(this.orderDetailsRepository.findById(keyOfOrderDetails)
                .map(p ->{
                    if (p.getAmount() != null)
                        p.setAmount(orderDetailsDTO.getAmount());
                    if (p.getPrice() != null)
                        p.setPrice(orderDetailsDTO.getPrice());
                    if (p.getProduct() != null)
                    {
                        Product product = this.productMapper.fromProductDTO(orderDetailsDTO.getProduct());
                        p.setProduct(product);
                    }
                    if (p.getOrdered() != null)
                    {
                        // Ordered ordered = this.orderedMapper.fromOrderedDTO(orderDetailsDTO.getOrdered());
                    	//                        p.setOrdered(ordered);
                    }
                    return this.orderDetailsRepository.save(p);
                }).orElseThrow(() -> new OrderDetailsNotFoundException("Order detail with idOrdered " +idProduct+ "," +idOrdered+ " was not found")));
    }

    public void remove(Long idProduct, Long idOrdered)
    {
        KeyOfOrderDetails keyOfOrderDetails = new KeyOfOrderDetails(idProduct, idOrdered);

        Optional<OrderDetails> orderDetails = this.orderDetailsRepository.findById(keyOfOrderDetails);
        if (orderDetails.isEmpty())
            throw  new OrderDetailsNotFoundException("Order detail with idOrdered " +idProduct+ "," +idOrdered+ " was not found");
        this.orderDetailsRepository.deleteById(keyOfOrderDetails);
    }

    public Optional<OrderDetailsDTO> getById(Long idProduct, Long idOrdered)
    {
        KeyOfOrderDetails keyOfOrderDetails = new KeyOfOrderDetails(idProduct, idOrdered);

        return this.orderDetailsRepository.findById(keyOfOrderDetails)
                .map(this.orderDetailsMapper::fromOrderDetails)
                .map(Optional::of)
                .orElseThrow(() -> new OrderDetailsNotFoundException("Order detail with idOrdered " +idProduct+ "," +idOrdered+ " was not found"));
    }

    // get all order detail by ref ordered

    public Page<OrderDetailsDTO> allOrderDetailByRefOrdered(String refOrdered, Pageable pageable)
    {
        Optional<Ordered> ordered = this.orderedRepository.findByRefOrdered(refOrdered);

        if (ordered.isEmpty())
            throw new  OrderDetailsNotFoundException("sorry not found");

        List<OrderDetails> orderDetails = this.orderDetailsRepository.findByOrdered(ordered.get());

        Page<OrderDetails> orderDetailsPage = new PageImpl<>(orderDetails, pageable, orderDetails.size());

        return orderDetailsPage
                .map(this.orderDetailsMapper::fromOrderDetails);
    }


}
