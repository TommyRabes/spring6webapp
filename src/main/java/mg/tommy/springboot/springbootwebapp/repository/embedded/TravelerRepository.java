package mg.tommy.springboot.springbootwebapp.repository.embedded;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Traveler;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelerRepository extends CrudRepository<Traveler, Long> {
}
