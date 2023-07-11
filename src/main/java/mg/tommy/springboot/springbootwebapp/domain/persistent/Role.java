package mg.tommy.springboot.springbootwebapp.domain.persistent;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Entity
public class Role {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    private String role;

    @ManyToMany(cascade = CascadeType.MERGE, mappedBy = "roles")
    @ToString.Exclude
    private Set<User> users = new HashSet<>();
}
