package fr.insy2s.commerce.shoponlineback.mappers;

import fr.insy2s.commerce.shoponlineback.beans.Address;
import fr.insy2s.commerce.shoponlineback.dtos.AddressDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "default",  uses = {AccountMapper.class})
public interface AddressMapper {

    AddressDTO fromAddress(Address address);

    Address fromAddressDTO(AddressDTO addressDTO);
}
