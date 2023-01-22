package code.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;


interface UserRepository extends JpaRepository<User, String> {

    Optional<UserDetails> searchUserDetailsByUsername(String username);
}
