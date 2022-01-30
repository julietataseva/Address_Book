package nbu.java.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nbu.java.model.pojo.User;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Setter
@Getter
@Component
public class ResponseUserDTO {
    private int id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    public ResponseUserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }
}
