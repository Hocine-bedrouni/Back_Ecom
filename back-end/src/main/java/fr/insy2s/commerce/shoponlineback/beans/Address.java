package fr.insy2s.commerce.shoponlineback.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import fr.insy2s.commerce.shoponlineback.enums.AddressType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_address", nullable = false, length = 50)
    private Long id;
    
    @Column(name = "type_address", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private AddressType type;

    @Column(name = "street", nullable = false, length = 50)
    @NotNull
    private String street;

    @Column(name = "city", nullable = false, length = 50)
    @NotNull
    @NotBlank(message = "sorry city is required")
    private String city;

    @Column(name = "postal_code", length = 5, nullable = false)
    @NotNull
    private int postalCode;

    @Column(name = "active")
    private Boolean active;
    
    @ManyToOne
    @JoinColumn(name="id_account", nullable=false)
    private Account account;
}
