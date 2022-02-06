package nbu.java.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
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
    @Expose
    private int id;

    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String companyName;
    @Expose
    private String address;
    @Expose
    private String phoneNumber;
    @Expose
    private String email;
    @Expose
    private String faxNumber;
    @Expose
    private String mobilePhoneNumber;
    @Expose
    private String comment;

    @Expose
    @Enumerated(EnumType.STRING)
    private Label label;

    @OneToMany(mappedBy = "contact")
    private List<AdditionalField> additionalFields;

    @ManyToOne
    private User user;

    public enum Label {
        WHITE, GREEN, RED, YELLOW, BLUE, GREY, BLACK
    }
}
