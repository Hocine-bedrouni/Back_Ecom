package fr.insy2s.commerce.shoponlineback.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.insy2s.commerce.shoponlineback.beans.Ordered;
import fr.insy2s.commerce.shoponlineback.enums.AddressType;
import lombok.Data;

import java.util.List;

@Data
public class AddressDTO {

    private Long id;

    private AddressType type;
    
    private String street;

    private String city;

    private int postalCode;

    private Boolean active;
    
    private AccountDTO account;

    @JsonIgnore
    private List<Ordered> ordersDelivered;

    @JsonIgnore
    private List<Ordered> ordersInvoiced;
}
