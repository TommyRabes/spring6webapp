package mg.tommy.springboot.springbootwebapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@Data
public class CustomerDto {
    private UUID id;
    private Integer version;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthdate;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
