package fr.insy2s.commerce.shoponlineback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insy2s.commerce.shoponlineback.beans.DeliveryMode;

public interface DeliveryModeRepository extends JpaRepository<DeliveryMode, String>{

}
