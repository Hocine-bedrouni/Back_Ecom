package fr.insy2s.commerce.shoponlineback.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.insy2s.commerce.shoponlineback.beans.Account;
import fr.insy2s.commerce.shoponlineback.enums.OrderedStatus;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class OrderedDTO {

    private Long idOrdered;

    private String refOrdered;

    private Instant orderedDate;

    private OrderedStatus statut;
    
    private double totalPrice;

    private Instant deliveryDate;

    //@JsonIgnore
    private AddressDTO deliveryAddress;

    //@JsonIgnore
    private AddressDTO billingAddress;

    private AccountDTO account;

    private List<InvoiceDTO> invoices;

    private List<OrderDetailsDTO> orderDetails;

}
