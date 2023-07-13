package mg.tommy.springboot.springbootwebapp.service.library;

import lombok.RequiredArgsConstructor;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Customer;
import mg.tommy.springboot.springbootwebapp.dto.CustomerDto;
import mg.tommy.springboot.springbootwebapp.mapper.CustomerMapper;
import mg.tommy.springboot.springbootwebapp.repository.embedded.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Iterable<CustomerDto> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toCustomerDto)
                .collect(toList());
    }

    @Override
    public Optional<CustomerDto> findById(UUID id) {
        return customerRepository.findById(id).map(customerMapper::toCustomerDto);
    }

    @Override
    public CustomerDto save(CustomerDto customerDto) {
        return customerMapper.toCustomerDto(
                customerRepository.save(customerMapper.toCustomer(customerDto).toBuilder()
                        .createdDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .build())
        );
    }

    @Override
    public CustomerDto overwriteById(UUID uuid, CustomerDto customerDto) {
        Customer customer = customerMapper.toCustomer(customerDto);
        Optional<Customer> customerToUpdate = customerRepository.findById(uuid);
        if (customerToUpdate.isEmpty()) {
            return customerMapper.toCustomerDto(customerRepository.save(customer));
        }
        Customer.CustomerBuilder builder = customerToUpdate.get().toBuilder()
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .email(customerDto.getEmail())
                .birthdate(customerDto.getBirthdate())
                .updateDate(LocalDateTime.now());
        return customerMapper.toCustomerDto(customerRepository.save(builder.build()));
    }

    @Override
    public CustomerDto updateById(UUID uuid, CustomerDto customerDto) {
        Optional<Customer> customerToUpdate = customerRepository.findById(uuid);
        if (customerToUpdate.isEmpty()) {
            return null;
        }
        Customer.CustomerBuilder builder = customerToUpdate.get().toBuilder();
        if (StringUtils.hasText(customerDto.getFirstName())) {
            builder.firstName(customerDto.getFirstName());
        }
        if (StringUtils.hasText(customerDto.getLastName())) {
            builder.lastName(customerDto.getLastName());
        }
        if (StringUtils.hasText(customerDto.getEmail())) {
            builder.email(customerDto.getEmail());
        }
        if (customerDto.getBirthdate() != null) {
            builder.birthdate(customerDto.getBirthdate());
        }
        builder.updateDate(LocalDateTime.now());
        return customerMapper.toCustomerDto(customerRepository.save(builder.build()));
    }

    @Override
    public void deleteById(UUID uuid) {
        customerRepository.deleteById(uuid);
    }
}
