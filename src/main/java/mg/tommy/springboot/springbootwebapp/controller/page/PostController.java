package mg.tommy.springboot.springbootwebapp.controller.page;

import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Post;
import mg.tommy.springboot.springbootwebapp.service.library.PostService;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('READER') || hasAuthority('ROLE_ADMIN')")
@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping("/latest")
    public String currentPost(Model model) {
        // fetch latest post
        model.addAttribute("post", postService.getLatestPost());
        return "views/post";
    }

    @RequestMapping("")
    public String listPosts(Model model) {
        model.addAttribute("posts", postService.list());
        return "views/posts-list";
    }

    @RequestMapping("/{slug}")
    public String post(@PathVariable(value = "slug") String slug, Model model) {
        model.addAttribute("post", postService.getBySlug(slug));
        return "views/complete-post";
    }

    @RequestMapping(path="/author/{firstName}/{lastName}")
    @ResponseBody
    public List<Post> findPostByAuthorName(@PathVariable("firstName") String firstName,
                                           @PathVariable("lastName") String lastName) {
        return postService.findPostByAuthorName(firstName, lastName);
    }

    @RequestMapping(path="/keywords/{keyword}")
    @ResponseBody
    public List<Post> findPostByKeyword(@PathVariable("keyword") String keyword) {
        return postService.findPostByKeyword(keyword);
    }
    @RequestMapping(path="/like-keywords/{keyword}")
    @ResponseBody
    public List<Post> findPostByKeywordLike(@PathVariable("keyword") String keyword) {
        return postService.findPostByKeywordLike(keyword);
    }

    @RequestMapping(path="/active")
    @ResponseBody
    public List<Post> findActivePost() {
        return postService.findActivePost();
    }

    @RequestMapping(path="/slug-query/{slug}")
    @ResponseBody
    public Post findBySlug(@PathVariable("slug") String slug) {
        return postService.findQueryBySlug(slug);
    }

    @RequestMapping(path="/slug-sql-query/{slug}")
    @ResponseBody
    public Post findBySlugNamed(@PathVariable("slug") String slug) {
        return postService.findSqlQueryBySlug(slug);
    }
}
