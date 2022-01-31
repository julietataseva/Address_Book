package nbu.java.controller;

import nbu.java.exceptions.BadRequestException;
import nbu.java.exceptions.NotFoundException;
import nbu.java.model.pojo.AdditionalField;
import nbu.java.model.pojo.Contact;
import nbu.java.model.dto.AdditionalFieldDTO;
import nbu.java.model.dto.ContactDTO;
import nbu.java.services.AdditionalFieldService;
import nbu.java.services.ContactService;
import nbu.java.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
public class ContactController {

    ContactService contactService;
    AdditionalFieldService additionalFieldService;
    UserService userService;

    @Autowired
    public ContactController(ContactService contactService, AdditionalFieldService additionalFieldService, UserService userService) {
        this.contactService = contactService;
        this.additionalFieldService = additionalFieldService;
        this.userService = userService;
    }

    @GetMapping("/addContact")
    public String showAddContact(HttpSession httpSession) {

        if (httpSession.getAttribute("LOGGED_USER_ID") == null) return "redirect:/login";
        return "addContact";
    }

    @PostMapping("/addContact")
    public String addContact(@ModelAttribute Contact contact, Model model,
                             @RequestParam(value = "title", required = false) String[] title,
                             @RequestParam(value = "text", required = false) String[] text,
                             HttpSession httpSession) throws BadRequestException {

        if (httpSession.getAttribute("LOGGED_USER_ID") == null) return "redirect:/login";

        contact.setUser(userService.findById((Integer)httpSession.getAttribute("LOGGED_USER_ID")));
        contactService.addContact(contact);

        if (title != null  && text != null){
            List<String> titles = Arrays.asList(title);
            List<String> texts = Arrays.asList(text);

            for (int i = 0; i < titles.size(); i++) {
                AdditionalField additionalField = new AdditionalField();
                additionalField.setTitle(titles.get(i));
                additionalField.setText(texts.get(i));
                additionalField.setContact(contact);
                additionalFieldService.addAdditionalField(additionalField);
            }
        }

        return "redirect:/contacts";
    }

    @GetMapping("/contacts")
    public String showContacts(Model model, HttpSession httpSession) throws NotFoundException {

        if (httpSession.getAttribute("LOGGED_USER_ID") == null) return "redirect:/login";
        model.addAttribute("contacts", contactService.findByUserId((Integer) httpSession.getAttribute("LOGGED_USER_ID")));
        return "contacts";
    }

    @GetMapping("/contactPage")
    public String showContactPage(@RequestParam String id, Model model, HttpSession httpSession) throws NotFoundException {

        Contact contact = contactService.findById(Integer.parseInt(id));
        System.out.println(httpSession.getAttribute("LOGGED_USER_ID"));
        if (httpSession.getAttribute("LOGGED_USER_ID") == null || (Integer) httpSession.getAttribute("LOGGED_USER_ID") != contact.getUser().getId()) return "redirect:/contacts";
        model.addAttribute("contact", contact);
        model.addAttribute("additionalFields", additionalFieldService.findByContactId(Integer.parseInt(id)));
        return "contactPage";
    }

    @GetMapping("contacts/edit")
    public String showEditPage(@RequestParam String id, Model model, HttpSession httpSession) throws NotFoundException {

        Contact contact = contactService.findById(Integer.parseInt(id));
        if (httpSession.getAttribute("LOGGED_USER_ID") == null || (Integer) httpSession.getAttribute("LOGGED_USER_ID") != contact.getUser().getId()) return "redirect:/contacts";
        model.addAttribute("contact", contact);
        model.addAttribute("additionalFields", additionalFieldService.findByContactId(Integer.parseInt(id)));
        return "editContactPage";
    }

    @PostMapping("contacts/edit")
    public String editContact(@ModelAttribute ContactDTO contactDTO, @RequestParam String id,
                              @RequestParam(value = "title", required = false) String[] title,
                              @RequestParam(value = "text", required = false) String[] text,
                              HttpSession httpSession) throws NotFoundException {
        Contact contact = contactService.findById(Integer.parseInt(id));
        if (httpSession.getAttribute("LOGGED_USER_ID") == null || (Integer) httpSession.getAttribute("LOGGED_USER_ID") != contact.getUser().getId()) return "redirect:/contacts";

        if (title != null  && text != null) {
            List<String> titles = Arrays.asList(title);
            List<String> texts = Arrays.asList(text);

            List<AdditionalField> additionalFields = additionalFieldService.findByContactId(Integer.parseInt(id));

            for (int i = 0; i < titles.size(); i++) {
                AdditionalFieldDTO additionalFieldDTO = new AdditionalFieldDTO();
                additionalFieldDTO.setTitle(titles.get(i));
                additionalFieldDTO.setText(texts.get(i));
                additionalFieldService.update(additionalFields.get(i).getId(), additionalFieldDTO);
            }
        }

        contactService.updateContact(Integer.parseInt(id), contactDTO);
        return "redirect:/contacts";
    }

    @GetMapping("/contacts/delete")
    public String deleteContact(@RequestParam String id, HttpSession httpSession) throws NotFoundException {
        int contactId = Integer.parseInt(id);
        Contact contact = contactService.findById(contactId);
        if (httpSession.getAttribute("LOGGED_USER_ID") == null || (Integer) httpSession.getAttribute("LOGGED_USER_ID") != contact.getUser().getId()) return "redirect:/contacts";
        List<AdditionalField> additionalFields = additionalFieldService.findByContactId(contactId);
        for (AdditionalField additionalField : additionalFields)
        {
            additionalFieldService.deleteAdditionalField(additionalField);
        }
        contactService.deleteContact(contact);
        return "redirect:/contacts";
    }

    @GetMapping("/contacts/search")
    public String deleteContact()  {

        return "search";
    }
}
