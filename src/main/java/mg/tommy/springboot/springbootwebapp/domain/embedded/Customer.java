package mg.tommy.springboot.springbootwebapp.domain.embedded;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Version
    private Integer version;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthdate;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
