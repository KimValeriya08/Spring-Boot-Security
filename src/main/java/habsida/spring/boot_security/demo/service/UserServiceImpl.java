package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.repository.RoleRepository;
import habsida.spring.boot_security.demo.repository.UserRepository;
import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> managedRoles = user.getRoles().stream()
                .map(role -> roleRepository.findById(role.getId()).orElse(null))
                .collect(Collectors.toSet());

        user.setRoles(managedRoles);

        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElse(null);

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword(existingUser.getPassword());
        } else if (!user.getPassword().startsWith("$2")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(
                    "Пользователь не найден: " + username
            );
        }

        return user;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();

    }
}