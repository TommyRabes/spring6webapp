package mg.tommy.springboot.springbootwebapp.repository.embedded;

import mg.tommy.springboot.springbootwebapp.model.domain.embedded.BeerOrderShipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerOrderShipmentRepository extends JpaRepository<BeerOrderShipment, UUID> {
}
