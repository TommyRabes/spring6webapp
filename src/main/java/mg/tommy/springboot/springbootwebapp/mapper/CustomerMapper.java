package mg.tommy.springboot.springbootwebapp.mapper;

import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Customer;
import mg.tommy.springboot.springbootwebapp.model.dto.CustomerDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer toCustomer(CustomerDto dto);
    CustomerDto toCustomerDto(Customer customer);
}
