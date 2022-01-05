package nbu.java.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Enumerated(EnumType.STRING)
    private Label label;

    @OneToMany(mappedBy = "contact")
    private List<AdditionalField> additionalFields;

    @ManyToOne
    private User user;

    public enum Label {
        GREEN, RED, YELLOW, BLUE, GREY, BLACK
    }
}
