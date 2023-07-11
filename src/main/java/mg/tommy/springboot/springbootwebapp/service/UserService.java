package mg.tommy.springboot.springbootwebapp.service;

import mg.tommy.springboot.springbootwebapp.domain.persistent.User;

public interface UserService {

    User findByEmail(String email);

}
