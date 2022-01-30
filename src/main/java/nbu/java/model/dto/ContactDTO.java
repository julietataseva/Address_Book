package nbu.java.model.dto;

import lombok.Getter;
import lombok.Setter;
import nbu.java.model.pojo.AdditionalField;
import nbu.java.model.pojo.Contact;
import nbu.java.model.pojo.User;
import java.util.List;

@Getter
@Setter
public class ContactDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String companyName;
    private String address;
    private String phoneNumber;
    private String email;
    private String faxNumber;
    private String mobilePhoneNumber;
    private String comment;

    private Contact.Label label;
    private List<AdditionalField> additionalFields;
    private User user;

    public ContactDTO() {
    }
}
