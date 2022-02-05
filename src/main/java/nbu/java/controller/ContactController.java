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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

        contact.setUser(userService.findById((Integer) httpSession.getAttribute("LOGGED_USER_ID")));
        contactService.addContact(contact);

        if (title != null && text != null) {
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
        List<Contact> contacts = contactService.findByUserId((Integer) httpSession.getAttribute("LOGGED_USER_ID")).
                stream().
                sorted(Comparator.comparing(Contact::getFirstName).
                        thenComparing(Contact::getLastName)).
                collect(Collectors.toList());

        model.addAttribute("contacts", contacts);
        return "contacts";
    }

    @GetMapping("/contactPage")
    public String showContactPage(@RequestParam String id, Model model, HttpSession httpSession) throws NotFoundException {

        Contact contact = contactService.findById(Integer.parseInt(id));
        System.out.println(httpSession.getAttribute("LOGGED_USER_ID"));
        if (httpSession.getAttribute("LOGGED_USER_ID") == null || (Integer) httpSession.getAttribute("LOGGED_USER_ID") != contact.getUser().getId())
            return "redirect:/contacts";
        model.addAttribute("contact", contact);
        model.addAttribute("additionalFields", additionalFieldService.findByContactId(Integer.parseInt(id)));
        return "contactPage";
    }

    @GetMapping("contacts/edit")
    public String showEditPage(@RequestParam String id, Model model, HttpSession httpSession) throws NotFoundException {

        Contact contact = contactService.findById(Integer.parseInt(id));
        if (httpSession.getAttribute("LOGGED_USER_ID") == null || (Integer) httpSession.getAttribute("LOGGED_USER_ID") != contact.getUser().getId())
            return "redirect:/contacts";
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
        if (httpSession.getAttribute("LOGGED_USER_ID") == null || (Integer) httpSession.getAttribute("LOGGED_USER_ID") != contact.getUser().getId())
            return "redirect:/contacts";

        if (title != null && text != null) {
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
        if (httpSession.getAttribute("LOGGED_USER_ID") == null || (Integer) httpSession.getAttribute("LOGGED_USER_ID") != contact.getUser().getId())
            return "redirect:/contacts";
        List<AdditionalField> additionalFields = additionalFieldService.findByContactId(contactId);
        for (AdditionalField additionalField : additionalFields) {
            additionalFieldService.deleteAdditionalField(additionalField);
        }
        contactService.deleteContact(contact);
        return "redirect:/contacts";
    }

    @GetMapping("/contacts/search")
    public String searchContacts(HttpSession httpSession) {
        if (httpSession.getAttribute("LOGGED_USER_ID") == null) return "redirect:/login";
        return "search";
    }

    @PostMapping("/contacts/search")
    public String search(@RequestParam(value = "radio", required = false) String choice,
                         @RequestParam(value = "firstName", required = false) String firstName,
                         @RequestParam(value = "lastName", required = false) String lastName,
                         Model model, HttpSession httpSession) {

        if (choice == null) return "search";

        if (choice.equals("allRecords")) {
            model.addAttribute("contacts", contactService.findByUserId((Integer) httpSession.getAttribute("LOGGED_USER_ID")));
        } else if (choice.equals("searchByFirstAndLastName")) {
            model.addAttribute("contacts", contactService.findByFirstNameAndLastNameAndUserId(firstName, lastName,(Integer) httpSession.getAttribute("LOGGED_USER_ID")));
        } else if (choice.equals("mostCommonLabels")) {
            model.addAttribute("contacts", contactService.getContactsWithMostCommonLabels((Integer) httpSession.getAttribute("LOGGED_USER_ID")));
        } else if (choice.equals("sameFirstNames")) {
            model.addAttribute("contacts", contactService.findBySameFirstNameAndDistinctLastName((Integer) httpSession.getAttribute("LOGGED_USER_ID")));
        } else if (choice.equals("sameLastNames")) {
            model.addAttribute("contacts", contactService.findBySameLastNameAndDistinctFirstName((Integer) httpSession.getAttribute("LOGGED_USER_ID")));
        }

        return "search";
    }

    @GetMapping("/contacts/download")
    public String getDownloadPage(HttpSession httpSession) {
        if (httpSession.getAttribute("LOGGED_USER_ID") == null) return "redirect:/login";
        return "downloadContacts";
    }
}
