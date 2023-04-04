package fr.insy2s.commerce.shoponlineback.mappers;

import fr.insy2s.commerce.shoponlineback.beans.Account;
import fr.insy2s.commerce.shoponlineback.dtos.AccountDTO;
import org.mapstruct.Mapper;



import java.util.List;

@Mapper(componentModel = "default", uses= {RoleMapper.class}/*,  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE*/)
public interface AccountMapper {


    AccountDTO fromAccount(Account account);


    Account fromAccountDTO(AccountDTO accountDTO);




}
