package nbu.java.controller;

import nbu.java.exceptions.BadRequestException;
import nbu.java.exceptions.NotFoundException;
import nbu.java.model.pojo.AdditionalField;
import nbu.java.model.pojo.Contact;
import nbu.java.model.pojo.dto.AdditionalFieldDTO;
import nbu.java.model.pojo.dto.ContactDTO;
import nbu.java.services.AdditionalFieldService;
import nbu.java.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class ContactController {

    ContactService contactService;
    AdditionalFieldService additionalFieldService;

    @Autowired
    public ContactController(ContactService contactService, AdditionalFieldService additionalFieldService) {
        this.contactService = contactService;
        this.additionalFieldService = additionalFieldService;
    }

    @GetMapping("/addContact")
    public String showAddContact() {

        return "addContact";
    }

    @PostMapping("/addContact")
    public String addContact(@ModelAttribute Contact contact, Model model,
                             @RequestParam(value = "title", required = false) String[] title,
                             @RequestParam(value = "text", required = false) String[] text) throws BadRequestException {
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
    public String showContacts(Model model) {

        model.addAttribute("contacts", contactService.findAll());
        return "contacts";
    }

    @GetMapping("/contactPage")
    public String showContactPage(@RequestParam String id, Model model) throws NotFoundException {

        model.addAttribute("contact", contactService.findById(Integer.parseInt(id)));
        model.addAttribute("additionalFields", additionalFieldService.findByContactId(Integer.parseInt(id)));
        return "contactPage";
    }

    @GetMapping("contacts/edit")
    public String showEditPage(@RequestParam String id, Model model) throws NotFoundException {

        model.addAttribute("contact", contactService.findById(Integer.parseInt(id)));
        model.addAttribute("additionalFields", additionalFieldService.findByContactId(Integer.parseInt(id)));
        return "editContactPage";
    }

    @PostMapping("contacts/edit")
    public String editContact(@ModelAttribute ContactDTO contactDTO, @RequestParam String id,
                              @RequestParam(value = "title", required = false) String[] title,
                              @RequestParam(value = "text", required = false) String[] text)
    {

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
    public String deleteContact(@RequestParam String id) throws NotFoundException {
        int contactId = Integer.parseInt(id);
        Contact contact = contactService.findById(contactId);
        List<AdditionalField> additionalFields = additionalFieldService.findByContactId(contactId);
        for (AdditionalField additionalField : additionalFields)
        {
            additionalFieldService.deleteAdditionalField(additionalField);
        }
        contactService.deleteContact(contact);
        return "redirect:/contacts";
    }
}
