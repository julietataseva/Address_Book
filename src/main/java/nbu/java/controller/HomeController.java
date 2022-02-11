package nbu.java.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHome(HttpSession httpSession) {

        if (httpSession.getAttribute("LOGGED_USER_ID") == null) return "redirect:/login";
        return "redirect:/contacts";
    }
}
