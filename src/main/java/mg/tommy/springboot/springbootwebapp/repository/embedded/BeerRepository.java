package mg.tommy.springboot.springbootwebapp.repository.embedded;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Beer;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface BeerRepository extends CrudRepository<Beer, UUID> {
}
