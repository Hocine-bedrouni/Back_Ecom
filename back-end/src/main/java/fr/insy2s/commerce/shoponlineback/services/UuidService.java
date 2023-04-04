package fr.insy2s.commerce.shoponlineback.services;

import fr.insy2s.commerce.shoponlineback.repositories.UuidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UuidService {

    private final UuidRepository uuidRepository;

    public String generateUuid()
    {
        String uuid = UUID.randomUUID().toString();

        if (this.uuidRepository.existsByNameUUID(uuid))
            return generateUuid();

        return uuid;
    }
}
