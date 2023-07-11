package mg.tommy.springboot.springbootwebapp.repository.embedded;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    // Returns the latest post in the database
    Post findFirstByOrderByPostedOnDesc();

    List<Post> findAllByOrderByPostedOnDesc();

    Post findBySlug(String slug);

    List<Post> findAllByAuthorFirstNameIgnoreCaseAndAuthorLastNameIgnoreCase(String firstName, String lastName);

    List<Post> findAllByKeywordsIgnoreCase(String keyword);

    List<Post> findAllByKeywordsLikeIgnoreCase(String keyword);

    List<Post> findAllByActiveTrue();

    // Beware of case-sensitivity in HQL => 'Post' not 'post'
    @Query("select p from Post p where p.slug = ?1")
    Post findQueryBySlug(String slug);

    @Query("select p from Post p where p.slug = :slug")
    Post findQueryByNamedSlug(@Param("slug") String slug);

    @Query(value = "SELECT * FROM post WHERE slug = :slug", nativeQuery = true)
    Post findSqlQueryBySlug(@Param("slug") String slug);
}
