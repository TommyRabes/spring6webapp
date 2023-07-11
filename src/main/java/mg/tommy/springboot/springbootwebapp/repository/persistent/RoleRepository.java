package mg.tommy.springboot.springbootwebapp.repository.persistent;

import mg.tommy.springboot.springbootwebapp.domain.persistent.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
