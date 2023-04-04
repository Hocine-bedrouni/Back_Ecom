package fr.insy2s.commerce.shoponlineback.mappers.config;

import fr.insy2s.commerce.shoponlineback.mappers.AccountMapper;
import fr.insy2s.commerce.shoponlineback.mappers.AccountMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public AccountMapper accountMapper()
    {
        return new AccountMapperImpl();
    }
}
