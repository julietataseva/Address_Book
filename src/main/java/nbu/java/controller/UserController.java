package nbu.java.controller;

import nbu.java.model.dto.*;
import nbu.java.model.pojo.User;
import nbu.java.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SessionManager sessionManager;

    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String register(RegisterRequestUserDTO registerRequestUserDTO, BindingResult bindingResult, Model model) {
        userService.register(registerRequestUserDTO, bindingResult);

        if (bindingResult.hasErrors()){
            return "register";
        }

        return "redirect:/contactPage";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginUserDTO userDTO, BindingResult bindingResult, Model model, HttpSession session) {
        ResponseUserDTO loginResponseUserDTO = userService.login(userDTO, bindingResult);
        if (bindingResult.hasErrors()){
            return "login";
        }
        sessionManager.loginUser(session, loginResponseUserDTO.getId());

        return "redirect:/contactPage";
    }

    @PostMapping("/logout")
    public SuccessDTO logout(HttpSession session) {
        sessionManager.validateLogged(session);
        sessionManager.logoutUser(session);
        return new SuccessDTO("Logging out successful");
    }

    @PostMapping("/editAccount")
    public ResponseUserDTO edit(@RequestBody EditRequestUserDTO userDTO, HttpSession session, BindingResult bindingResult) {
        User loggedUser = sessionManager.getLoggedUser(session);
        return userService.editUser(userDTO, loggedUser, bindingResult);
    }

    @DeleteMapping("/deleteAccount")
    public SuccessDTO delete(HttpSession session) {
        sessionManager.validateLogged(session);
        int userId = sessionManager.getLoggedUser(session).getId();
        SuccessDTO response = userService.deleteUser(userId);
        sessionManager.logoutUser(session);
        return response;
    }
}
