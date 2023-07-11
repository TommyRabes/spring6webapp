package mg.tommy.springboot.springbootwebapp.domain.persistent;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * It's important to note that using Lombok's annotations on Hibernate's entities is not recommended
 * Lombok doesn't implement hashCode and equals methods as suggested by this post : https://thorben-janssen.com/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate/
 * In a nutshell, hashCode should return a fixed value and equals should be based on primary keys comparison
 * That's why we've added @EqualsAndHashCode(onlyExplicitlyIncluded = true) and @EqualsAndHashCode.Include on the attribute mapped to the primary key
 * Otherwise, we would get some problems while dealing with Set
 * There are other issues apart from that like Lombok's toString implementation, etc..., which enforces this warning
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Entity(name = "book_user") // "user" is a reserved keyword in PostgreSQL
public class User {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String fullName;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
