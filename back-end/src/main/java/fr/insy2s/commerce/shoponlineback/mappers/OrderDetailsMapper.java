package fr.insy2s.commerce.shoponlineback.mappers;

import fr.insy2s.commerce.shoponlineback.beans.OrderDetails;
import fr.insy2s.commerce.shoponlineback.dtos.OrderDetailsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "default", uses = {ProductMapper.class, OrderedMapper.class})
public interface OrderDetailsMapper {

    OrderDetailsDTO fromOrderDetails(OrderDetails orderDetails);

    OrderDetails fromOrderDetailsDTO(OrderDetailsDTO orderDetailsDTO);
}
