package fr.insy2s.commerce.shoponlineback.mappers;

import fr.insy2s.commerce.shoponlineback.beans.Ordered;
import fr.insy2s.commerce.shoponlineback.dtos.AccountDTO;
import fr.insy2s.commerce.shoponlineback.dtos.AddressDTO;
import fr.insy2s.commerce.shoponlineback.dtos.OrderedDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "default", uses = {AccountMapper.class, AddressMapper.class})
public interface OrderedMapper {

    OrderedDTO fromOrdered(Ordered ordered);

    Ordered fromOrderedDTO(OrderedDTO orderedDTO);


}
