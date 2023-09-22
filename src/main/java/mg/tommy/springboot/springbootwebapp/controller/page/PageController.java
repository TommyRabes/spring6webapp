package mg.tommy.springboot.springbootwebapp.controller.page;

import mg.tommy.springboot.springbootwebapp.service.library.MarketService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    private MarketService marketService;

    public PageController(MarketService marketService) {
        this.marketService = marketService;
    }

    @PreAuthorize("hasRole('GUEST') || hasRole('CLIENT') || hasAuthority('READER') || hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/home")
    public String home() {
        return "dashboard";
    }

    @PreAuthorize("hasRole('CLIENT') || hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/clients")
    public String users(Model model) {
        model.addAttribute("clients", marketService.getClients());
        return "views/clients";
    }

    @PreAuthorize("hasRole('CLIENT') || hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", marketService.getProducts());
        return "views/products";
    }

    @PreAuthorize("hasRole('CLIENT') || hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", marketService.getOrders());
        return "views/orders";
    }
}
