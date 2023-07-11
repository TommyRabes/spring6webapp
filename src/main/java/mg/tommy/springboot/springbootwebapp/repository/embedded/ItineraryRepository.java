package mg.tommy.springboot.springbootwebapp.repository.embedded;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Itinerary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItineraryRepository extends CrudRepository<Itinerary, Long> {
}
