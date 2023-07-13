package mg.tommy.springboot.springbootwebapp.service.library;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Customer;
import mg.tommy.springboot.springbootwebapp.dto.CustomerDto;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Iterable<CustomerDto> findAll();
    Optional<CustomerDto> findById(UUID id);

    CustomerDto save(CustomerDto customer);

    CustomerDto overwriteById(UUID uuid, CustomerDto customer);

    CustomerDto updateById(UUID uuid, CustomerDto customer);

    void deleteById(UUID uuid);
}
