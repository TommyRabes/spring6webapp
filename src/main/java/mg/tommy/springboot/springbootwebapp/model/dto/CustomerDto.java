package mg.tommy.springboot.springbootwebapp.model.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import mg.tommy.springboot.springbootwebapp.model.dto.constraint.group.CustomerGroup;
import mg.tommy.springboot.springbootwebapp.model.dto.constraint.group.PartialCustomerGroup;
import mg.tommy.springboot.springbootwebapp.model.dto.constraint.validator.Early;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@Data
public class CustomerDto {
    private UUID id;
    private Integer version;

    @NotNull(groups = CustomerGroup.class)
    @NotBlank(groups = CustomerGroup.class)
    @Size(min = 3, max = 255, groups = {CustomerGroup.class, PartialCustomerGroup.class})
    private String firstName;

    @NotNull(groups = CustomerGroup.class)
    @NotBlank(groups = CustomerGroup.class)
    @Size(min = 3, max = 255, groups = {CustomerGroup.class, PartialCustomerGroup.class})
    private String lastName;

    @NotNull(groups = CustomerGroup.class)
    @NotBlank(groups = CustomerGroup.class)
    @Size(min = 3, max = 320, groups = {CustomerGroup.class, PartialCustomerGroup.class})
    @Email(groups = {CustomerGroup.class, PartialCustomerGroup.class})
    private String email;

    @NotNull(groups = CustomerGroup.class)
    @Early(years = 18, groups = {CustomerGroup.class, PartialCustomerGroup.class})
    private LocalDate birthdate;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
