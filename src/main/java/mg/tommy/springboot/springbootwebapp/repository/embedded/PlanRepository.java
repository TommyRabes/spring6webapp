package mg.tommy.springboot.springbootwebapp.repository.embedded;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Long> {
}
