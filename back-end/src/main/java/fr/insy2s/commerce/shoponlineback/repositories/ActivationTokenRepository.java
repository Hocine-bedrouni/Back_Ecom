package fr.insy2s.commerce.shoponlineback.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insy2s.commerce.shoponlineback.beans.ActivationToken;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, Integer>{
	public Optional<ActivationToken> findByToken(String token);
}
