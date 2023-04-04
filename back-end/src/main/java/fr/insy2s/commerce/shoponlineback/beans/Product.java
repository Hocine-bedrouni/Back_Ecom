package fr.insy2s.commerce.shoponlineback.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product", nullable = false)
    private Long idProduct;

    @Column(name = "ref_product", nullable = false, length = 100)
    private String refProduct;//

    @Column(name = "name", nullable = false, length =50)
    @NotBlank(message = "sorry name of product is required")
    private String name;

    @Column(name = "price_ttc", nullable = false)
   // @NotBlank(message = "sorry price of product is required")
   @NotNull
    private Double priceTTC;

    @Column(name = "product_inventory", nullable = false)
    private Long productInventory;

    @Column(name = "product_description", nullable = true, length = 350)
    private String productDescription;

    @Column(name = "present")
    private Boolean present;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_category")
    @JsonIgnoreProperties({"products"})
    @NotNull
   // @NotBlank(message="category is required")
    private Category category;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_promotion")
    @JsonIgnoreProperties({"products"})
    private Promotion promotion;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"product"})
    private List<Picture> pictures;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"product", "ordered"})
    private List<OrderDetails> orderDetails;

}
