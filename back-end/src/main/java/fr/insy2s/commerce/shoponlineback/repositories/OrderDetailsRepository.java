package fr.insy2s.commerce.shoponlineback.repositories;

import fr.insy2s.commerce.shoponlineback.beans.KeyOfOrderDetails;
import fr.insy2s.commerce.shoponlineback.beans.OrderDetails;
import fr.insy2s.commerce.shoponlineback.beans.Ordered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, KeyOfOrderDetails> {

    List<OrderDetails> findByOrdered(Ordered ordered);
}
