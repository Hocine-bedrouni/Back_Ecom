package fr.insy2s.commerce.shoponlineback.mappers;

import fr.insy2s.commerce.shoponlineback.beans.Invoice;
import fr.insy2s.commerce.shoponlineback.dtos.InvoiceDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "default", uses = {OrderedMapper.class})
public interface InvoiceMapper {

    InvoiceDTO fromInvoice(Invoice invoice);

    Invoice fromInvoiceDTO(InvoiceDTO invoiceDTO);
}
