package nbu.java.services;

import nbu.java.exceptions.BadRequestException;
import nbu.java.exceptions.NotFoundException;
import nbu.java.model.dto.ContactDTO;
import nbu.java.repositories.ContactRepository;
import nbu.java.model.pojo.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContactService {

    ContactRepository<Contact> contactRepository;

    @Autowired
    public ContactService(ContactRepository<Contact> contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Transactional
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    @Transactional
    public Contact findById(int id) throws NotFoundException {
        Contact contact = contactRepository.findById(id);
        if (contact == null) throw new NotFoundException("This contact doesn't exits!");
        return contact;
    }

    @Transactional
    public List<Contact> findByUserId(int id) {

        return contactRepository.findByUserId(id);
    }

    @Transactional
    public List<Contact> findByFirstNameAndLastName(String firstName, String lastName) {

        return contactRepository.findByFirstNameAndLastName(firstName,lastName);
    }

    @Transactional
    public List<Contact> findBySameFirstNameAndDistinctLastName() {

        return contactRepository.findBySameFirstNameAndDistinctLastName();
    }

    @Transactional
    public List<Contact> findBySameLastNameAndDistinctFirstName() {

        return contactRepository.findBySameLastNameAndDistinctFirstName();
    }

    @Transactional
    public void addContact(Contact contact) throws BadRequestException {

        if (contact == null){
            throw new BadRequestException("The value of the object is null!");
        }

        validate(contact.getFirstName(), 45);

        validate(contact.getLastName(), 45);

        validate(contact.getCompanyName(), 45);

        validate(contact.getAddress(), 125);

        validate(contact.getPhoneNumber(), 45);

        validate(contact.getEmail(), 45);

        validate(contact.getFaxNumber(), 45);

        validate(contact.getMobilePhoneNumber(), 45);

        if(contact.getComment().length() > 255){}

        if(contact.getLabel().name().length() > 45){}

//        if (contact.getUser() == null){}
        contactRepository.save(contact);

    }

    @Transactional
    public void deleteContact(Contact contact){ contactRepository.delete(contact); }

    @Transactional
    public void updateContact(int id, ContactDTO contactDTO) {

        Contact contact = contactRepository.findById(id);

        if (contactDTO.getFirstName() != null && contactDTO.getFirstName().length() <= 45){
            contact.setFirstName(contactDTO.getFirstName());
        }

        if (contactDTO.getLastName() != null && contactDTO.getLastName().length() <= 45){
            contact.setLastName(contactDTO.getLastName());
        }

        if (contactDTO.getCompanyName() != null && contactDTO.getCompanyName().length() <= 45){
            contact.setCompanyName(contactDTO.getCompanyName());
        }

        if (contactDTO.getAddress() != null && contactDTO.getCompanyName().length() <= 125){
            contact.setAddress(contactDTO.getAddress());
        }

        if (contactDTO.getPhoneNumber() != null && contactDTO.getCompanyName().length() <= 45){
            contact.setPhoneNumber(contactDTO.getPhoneNumber());
        }

        if (contactDTO.getEmail() != null && contactDTO.getEmail().length() <= 45){
            contact.setEmail(contactDTO.getEmail());
        }

        if (contactDTO.getFaxNumber() != null && contactDTO.getFaxNumber().length() <= 45){
            contact.setFaxNumber(contactDTO.getFaxNumber());
        }

        if (contactDTO.getMobilePhoneNumber() != null && contactDTO.getMobilePhoneNumber().length() <= 45){
            contact.setMobilePhoneNumber(contactDTO.getMobilePhoneNumber());
        }

        if (contactDTO.getComment() != null && contactDTO.getComment().length() <= 255){
            contact.setComment(contactDTO.getComment());
        }

        if (contactDTO.getLabel() != null && contactDTO.getMobilePhoneNumber().length() <= 45){
            contact.setLabel(contactDTO.getLabel());
        }

        if (contactDTO.getUser() != null){
            contact.setUser(contactDTO.getUser());
        }

        contactRepository.save(contact);
    }

    private void validate(String value, int n) throws BadRequestException {
        if (value == null) {
            throw new BadRequestException("The value of the object is null!");
        }
        if (value.length() > n) {
            throw new BadRequestException("The length of the object is more than " + n + "!");
        }
    }

}
