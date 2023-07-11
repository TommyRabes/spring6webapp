package mg.tommy.springboot.springbootwebapp.domain.embedded;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class Publisher {
    @Id
    @GeneratedValue
    private UUID uuid;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String city;
    private String state;
    private String zip;
    @OneToMany(mappedBy = "publisher")
    private Set<Book> books;
}
