package fr.insy2s.commerce.shoponlineback.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.insy2s.commerce.shoponlineback.enums.OrderedStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ordered")
@ToString
public class Ordered {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ordered", nullable = false)
    private Long idOrdered;
    
    @Column(name = "total_price", nullable = false)
    @NotNull
    private double totalPrice;

    @Column(name = "ref_ordered", nullable = false, length = 250)
    private String refOrdered;

    @Column(name = "ordered_date",nullable = false)
    @NotNull
    private Instant orderedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status",nullable = false, length = 50)
    private OrderedStatus statut;

    @Column(name = "delivery_ordered", nullable = false)
    @NotNull
    private Instant deliveryDate;

    @ManyToOne
    @JoinColumn(name = "id_address_delivery")
    @NotNull
    @JsonIgnoreProperties({"ordersDelivered"})
    private Address deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "id_address_invoiced")
    @JsonIgnoreProperties({"ordersInvoiced"})
    private Address billingAddress;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_account")
    @JsonIgnoreProperties({"ordereds"})
    private Account account;

    @ToString.Exclude
    @OneToMany(mappedBy = "ordered", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"ordered"})
    private List<Invoice> invoices;

    @ToString.Exclude
    @OneToMany(mappedBy = "ordered", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"ordered", "product"})
    private List<OrderDetails> orderDetails;
}
