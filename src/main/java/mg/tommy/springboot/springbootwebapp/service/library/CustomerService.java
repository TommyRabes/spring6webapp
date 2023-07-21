package mg.tommy.springboot.springbootwebapp.service.library;

import mg.tommy.springboot.springbootwebapp.model.dto.CustomerDto;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Iterable<CustomerDto> findAll();
    Optional<CustomerDto> findById(UUID id);

    CustomerDto save(CustomerDto customer);

    Optional<CustomerDto> overwriteById(UUID uuid, CustomerDto customer);

    Optional<CustomerDto> updateById(UUID uuid, CustomerDto customer);

    boolean deleteById(UUID uuid);
}
