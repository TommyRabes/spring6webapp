package mg.tommy.springboot.springbootwebapp.bootstrap;

import jakarta.annotation.PostConstruct;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Post;
import mg.tommy.springboot.springbootwebapp.repository.embedded.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Profile({"Full", "Database"})
@Component
@Scope("singleton")
@DependsOn({"databaseInitializer"})
public class PostDatabaseInitialization {
    private static final Logger logger = LoggerFactory.getLogger(PostDatabaseInitialization.class);
    @Autowired
    PostRepository postRepository;

    @PostConstruct
    void seePosts() {
        logger.info("seePosts method called...");
        for (Post post : postRepository.findAll()) {
            logger.info(post.toString());
        }
    }
}
