package nbu.java.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@Component
public class RegisterRequestUserDTO {

    @NotEmpty()
    @Size(min=2,max=45)
    private String username;
    @NotEmpty()
    @Size(min=2,max=45)
    private String email;
    @NotEmpty()
    @Size(min=8,max=45)
    private String password;
    @NotEmpty()
    @Size(min=8,max=45)
    private String confirmPassword;
    @NotEmpty()
    @Size(min=2,max=45)
    private String firstName;
    @NotEmpty()
    @Size(min=2,max=45)
    private String lastName;


}
