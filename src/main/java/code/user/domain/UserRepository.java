package code.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {

    Optional<UserDetails> searchUserDetailsByUsername(String username);
}
