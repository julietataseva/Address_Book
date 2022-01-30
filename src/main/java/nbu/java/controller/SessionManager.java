package nbu.java.controller;

import nbu.java.model.pojo.User;
import nbu.java.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Component
public class SessionManager {
    private static final String LOGGED_USER_ID = "LOGGED_USER_ID";

    @Autowired
    private UserRepository userRepository;

    public User getLoggedUser(HttpSession session) {
        String message = validateLogged(session);

        int userId = (int) session.getAttribute(LOGGED_USER_ID);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        //throw new NotFoundException("User does not exist!");
        return null;
    }

    public void loginUser(HttpSession session, int id) {
        session.setAttribute(LOGGED_USER_ID, id);
    }

    public void logoutUser(HttpSession session) {
        session.invalidate();
    }

    public String validateLogged(HttpSession session) {
        String message = "";
        if (session.isNew() || session.getAttribute(LOGGED_USER_ID) == null) {
            message = "You have to log in!";
        }
        return message;
    }
}
