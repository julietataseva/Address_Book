package nbu.java.controller;

import nbu.java.model.pojo.User;
import nbu.java.services.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class DownloadController {
    @Autowired
    private DownloadService downloadService;

    @Autowired
    private SessionManager sessionManager;

    @GetMapping("/contacts/download")
    public String getDownloadPage(HttpSession httpSession) {
        if (httpSession.getAttribute("LOGGED_USER_ID") == null) return "redirect:/login";
        return "downloadContacts";
    }

    @PostMapping("/contacts/download")
    public ResponseEntity<byte[]> downloadContacts(@RequestParam(value = "radio", required = false) String choice,
                                                   HttpSession httpSession) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/contacts/download");
        if (choice == null) {
            return new ResponseEntity<byte[]>(headers, HttpStatus.FOUND);
        }

        User loggedUser = sessionManager.getLoggedUser(httpSession);
        if (choice.equals("json")) {
            return downloadService.exportToJson(loggedUser.getId());
        } else if (choice.equals("csv")) {
            return downloadService.exportToCsv(loggedUser.getId());
        } else if (choice.equals("excel")) {
            return downloadService.exportToExcel(loggedUser.getId());
        }
        else {
            return new ResponseEntity<byte[]>(headers, HttpStatus.FOUND);
        }
    }
}
