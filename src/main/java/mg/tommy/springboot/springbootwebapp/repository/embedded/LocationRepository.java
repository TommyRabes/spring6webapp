package mg.tommy.springboot.springbootwebapp.repository.embedded;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
}
