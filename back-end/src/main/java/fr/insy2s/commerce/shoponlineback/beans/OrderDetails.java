package fr.insy2s.commerce.shoponlineback.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.insy2s.commerce.shoponlineback.dtos.OrderDetailsDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "order_details")
public class OrderDetails {

    @EmbeddedId
    private KeyOfOrderDetails keyOfOrderDetails;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne(cascade = CascadeType.MERGE)
    @MapsId("idProduct")
    @JoinColumn(name = "id_product", insertable = false, updatable = false)
    @JsonIgnoreProperties({"orderDetails"})
    private Product product;

    @ManyToOne(cascade = CascadeType.MERGE)
    @MapsId("idOrdered")
    @JoinColumn(name = "id_ordered", insertable = false, updatable = false)
    @JsonIgnoreProperties({"orderDetails"})
    private Ordered ordered;
}
