package fr.insy2s.commerce.shoponlineback.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.insy2s.commerce.shoponlineback.beans.Ordered;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;

@Getter
@Setter
public class InvoiceDTO {

    private Long id;

    private String refInvoice;

    private String name;

    private Date billingDate;

    @JsonIgnore
    private Ordered ordered;
}
