package mg.tommy.springboot.springbootwebapp.repository.persistent;

import mg.tommy.springboot.springbootwebapp.model.domain.persistent.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);
}
