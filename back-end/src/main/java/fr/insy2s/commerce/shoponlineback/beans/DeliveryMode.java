package fr.insy2s.commerce.shoponlineback.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivery_mode")
public class DeliveryMode {
	
    @Id
    @Column(name = "name", nullable = false, length =50)
    private String name;
    
    @Column(name = "price", nullable = false)
    private Double price;
}
