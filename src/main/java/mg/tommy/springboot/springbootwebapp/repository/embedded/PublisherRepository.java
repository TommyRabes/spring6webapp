package mg.tommy.springboot.springbootwebapp.repository.embedded;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Publisher;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PublisherRepository extends CrudRepository<Publisher, UUID> {
}
