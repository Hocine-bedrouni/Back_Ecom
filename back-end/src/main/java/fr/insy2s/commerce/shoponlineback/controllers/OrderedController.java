package fr.insy2s.commerce.shoponlineback.controllers;


import fr.insy2s.commerce.shoponlineback.dtos.OrderedDTO;
import fr.insy2s.commerce.shoponlineback.dtos.ProductDTO;
import fr.insy2s.commerce.shoponlineback.exceptions.beansexptions.OrderedNotFoundException;
import fr.insy2s.commerce.shoponlineback.services.OrderedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/shopping-online")
@RequiredArgsConstructor
@Slf4j
public class OrderedController {

    @Autowired
    private final OrderedService orderedService;

    @GetMapping("/no-role/all-ordered-dto")
    public ResponseEntity<Page<OrderedDTO>> allOrderedDTO(Pageable pageable) {
        return ResponseEntity.ok(this.orderedService.all(pageable));
    }
    
    @GetMapping("/accounts/{accountId}/orders")
    public ResponseEntity<Page<OrderedDTO>> getAccountOrders(@PathVariable Long accountId, Pageable pageable) {
    	return ResponseEntity.ok(this.orderedService.allAccountOrders(accountId, pageable));
    }
    
    @GetMapping("/accounts/{accountId}/orders/{orderId}")
    public ResponseEntity<OrderedDTO> getAccountOrders(@PathVariable Long accountId, @PathVariable Long orderId) {
    	return ResponseEntity.ok(this.orderedService.getAccountOrder(accountId, orderId));
    }

    @PostMapping("/no-role/add-ordered-dto")
    public ResponseEntity<OrderedDTO> addOrderedDTO(@Valid @RequestBody OrderedDTO orderedDTO) {

        try {
            this.orderedService.add(orderedDTO);

            return  ResponseEntity.status(HttpStatus.CREATED).body(orderedDTO);
        }catch (ConstraintViolationException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/no-role/update-ordered-dto/{idOrdered}")
    public ResponseEntity<OrderedDTO> updateOrderedDTO(@Validated @PathVariable Long idOrdered, @RequestBody OrderedDTO orderedDTO){

        log.info("Updating ordered with id : {}", idOrdered);

        try {

            OrderedDTO updateOrderedDTO = this.orderedService.update(idOrdered, orderedDTO);

            log.info("Ordered with id : {} updated successfully " ,idOrdered);
            
            return new ResponseEntity<>(updateOrderedDTO, HttpStatus.OK);

        } catch (OrderedNotFoundException exception)
        {
            log.error("Error occured while updating ordered with id: {}. Error: {}", idOrdered, exception.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/no-role/remove-ordered-dto/{idOrdered}")
    public ResponseEntity<Void> removeOrderedDTO(@Validated @PathVariable Long idOrdered) {

        this.orderedService.remove(idOrdered);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-role/get-by-id-ordered/{idOrdered}")
    public ResponseEntity<OrderedDTO> getByIdOrderedDTO (@Validated @PathVariable Long idOrdered) {

        return this.orderedService.getById(idOrdered)
                .map(orderedDTO -> new ResponseEntity<>(orderedDTO, HttpStatus.OK))
                .orElseThrow(() -> new OrderedNotFoundException("Ordered with id " +idOrdered+ " was not found"));
    }
    
    @GetMapping("/no-role/get-ordereds-by-ref-account/{refAccount}")
    public ResponseEntity<List<OrderedDTO>> getOrderedsByRefAccount(@Valid @PathVariable String refAccount){
        return ResponseEntity.ok(this.orderedService.getOrderedsByRefAccount(refAccount));
    }

    @GetMapping("/no-role/all-product-by-ref-ordered/{refOrdered}")
    public ResponseEntity<Page<ProductDTO>> allProductByRefOrdered(@Valid @PathVariable String refOrdered, Pageable pageable)
    {
        return ResponseEntity.ok(this.orderedService.allProductByRefOrdered(refOrdered, pageable));
    }
}
