package fr.insy2s.commerce.shoponlineback.beans;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "activation_token")
public class ActivationToken {
    @Id
    @Column(name = "id_account")
    private Long id;
    
    private String token;
    
    @OneToOne()
    @MapsId
    @JoinColumn(name = "id_account")
    private Account account;
    
    @Column(name = "expiration_date")
    private Instant expirationDate;
}
