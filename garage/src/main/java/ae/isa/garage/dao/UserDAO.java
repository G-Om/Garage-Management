package ae.isa.garage.dao;

import ae.isa.garage.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDAO {

    private String username;

    private String password;

    private UserRole role;
}
