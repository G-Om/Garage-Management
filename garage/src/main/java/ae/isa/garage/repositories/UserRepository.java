package ae.isa.garage.repositories;

import ae.isa.garage.entities.UserInfo;
import ae.isa.garage.services.MyUserDetailsService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {

    public UserInfo findByUsername(String username);

}
