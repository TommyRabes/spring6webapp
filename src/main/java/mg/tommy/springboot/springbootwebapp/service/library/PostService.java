package mg.tommy.springboot.springbootwebapp.service.library;

import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Post;
import mg.tommy.springboot.springbootwebapp.repository.embedded.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post getLatestPost() {
        return postRepository.findFirstByOrderByPostedOnDesc();
    }

    public List<Post> list() {
        return postRepository.findAllByOrderByPostedOnDesc();
    }

    public Post getBySlug(String slug) {
        return postRepository.findBySlug(slug);
    }

    public List<Post> findPostByAuthorName(String firstName, String lastName) {
        return postRepository.findAllByAuthorFirstNameIgnoreCaseAndAuthorLastNameIgnoreCase(firstName, lastName);
    }

    public List<Post> findPostByKeyword(String keyword) {
        return postRepository.findAllByKeywordsIgnoreCase(keyword);
    }

    public List<Post> findPostByKeywordLike(String keyword) {
        return postRepository.findAllByKeywordsLikeIgnoreCase('%' + keyword + '%');
    }

    public List<Post> findActivePost() {
        return postRepository.findAllByActiveTrue();
    }

    public Post findQueryBySlug(String slug) {
        return postRepository.findQueryBySlug(slug);
    }

    public Post findSqlQueryBySlug(String slug) {
        return postRepository.findSqlQueryBySlug(slug);
    }
}
