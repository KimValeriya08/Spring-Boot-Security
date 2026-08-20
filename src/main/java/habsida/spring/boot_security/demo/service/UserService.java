package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import habsida.spring.boot_security.demo.model.Role;
import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    User getUserById(Long id);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);

    User findByUsername(String username);

    List<Role> getAllRoles();
}