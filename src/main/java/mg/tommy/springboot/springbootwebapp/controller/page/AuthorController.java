package mg.tommy.springboot.springbootwebapp.controller.page;

import jakarta.validation.Valid;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Author;
import mg.tommy.springboot.springbootwebapp.service.library.AuthorService;
import mg.tommy.springboot.springbootwebapp.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/authors")
// @PreAuthorize("hasRole('ADMIN')")
public class AuthorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService authorService;
    private final NotificationService notificationService;

    public AuthorController(AuthorService authorService, NotificationService notificationService) {
        this.authorService = authorService;
        this.notificationService = notificationService;
    }

    @RequestMapping({"", "/"})
    public String list(Model model) {
        if (model.containsAttribute("createdAuthor"))
            model.addAttribute("successMessage", "Author saved successfully");
        else if (model.containsAttribute("updatedAuthor"))
            model.addAttribute("successMessage", "Author updated successfully");
        else if (model.containsAttribute("deletedAuthor"))
            model.addAttribute("informationMessage", "Author deleted successfully");
        else if (model.containsAttribute("authorDeletionError"))
            model.addAttribute("errorMessage", "Author couldn't be deleted");
        model.addAttribute("authors", authorService.list());
        return "views/admin/authors";
    }

    @RequestMapping("/create")
    public String form(Model model) {
        model.addAttribute("author", new Author());
        return "views/admin/author-form";
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public String createAuthor(
            @Valid @ModelAttribute Author author,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "views/admin/author-form";
        }

        authorService.save(author);
        try {
            notificationService.sendFancyEmail(author);
            LOGGER.error("An email should have been sent to : " + author.getEmail());
        }
        catch (MailException exception) {
            LOGGER.error("Failed to send email to : " + author.getEmail(), exception);
        }
        redirectAttributes.addFlashAttribute("createdAuthor", author);
        return "redirect:/authors";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String updateAuthor(
            @Valid @ModelAttribute Author author,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "views/admin/author-edit";
        }
        authorService.save(author);
        redirectAttributes.addFlashAttribute("updatedAuthor", author);
        return "redirect:/authors";
    }

    @RequestMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Author> author = authorService.get(id);
        if (author.isEmpty()) {
            // throw new AuthorNotFoundException();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author of id '" + id + "' not found");
        }
        model.addAttribute("author", author.get());
        return "views/admin/author-edit";
    }

    @RequestMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (authorService.delete(id))
            redirectAttributes.addFlashAttribute("deletedAuthor", id);
        else
            redirectAttributes.addFlashAttribute("authorDeletionError", id);
        return "redirect:/authors";
    }
}
