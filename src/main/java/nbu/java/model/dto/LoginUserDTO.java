package nbu.java.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@Component
public class LoginUserDTO {
    @NotEmpty()
    @Size(min=2,max=45)
    private String username;
    @NotEmpty()
    @Size(min=2,max=45)
    private String password;
}
