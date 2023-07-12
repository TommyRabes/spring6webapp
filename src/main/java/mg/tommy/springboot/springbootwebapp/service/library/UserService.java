package mg.tommy.springboot.springbootwebapp.service.library;

import mg.tommy.springboot.springbootwebapp.domain.persistent.User;

public interface UserService {

    User findByEmail(String email);

}
