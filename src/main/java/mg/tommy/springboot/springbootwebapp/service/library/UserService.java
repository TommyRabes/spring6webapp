package mg.tommy.springboot.springbootwebapp.service.library;

import mg.tommy.springboot.springbootwebapp.model.domain.persistent.User;

public interface UserService {

    User findByEmail(String email);

}
