package fr.insy2s.commerce.shoponlineback.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_invoice", nullable = false)
    private Long id;

    @Column(name = "ref_invoice", nullable = true, length = 50) // TODO Mettre nullable en false après modif BDD
    private String refInvoice;

    @Column(name = "name", nullable = false, length = 50)
    @NotEmpty
    private String name;

    @Column(name = "billing_date", nullable = false)
    @NotNull
    private Date billingDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_ordered")
    @JsonIgnoreProperties({"invoices"})
    private Ordered ordered;
}
