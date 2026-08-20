package habsida.spring.boot_security.demo;

import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repository.RoleRepository;
import habsida.spring.boot_security.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(UserRepository userRepository,
	                           RoleRepository roleRepository,
	                           PasswordEncoder passwordEncoder,
	                           @Value("${app.admin.password}") String adminPassword,
	                           @Value("${app.user.password}") String userPassword) {

		return args -> {

			Role userRole = roleRepository.findByName("ROLE_USER");
			if (userRole == null) {
				userRole = roleRepository.save(new Role("ROLE_USER"));
			}

			Role adminRole = roleRepository.findByName("ROLE_ADMIN");
			if (adminRole == null) {
				adminRole = roleRepository.save(new Role("ROLE_ADMIN"));
			}

			if (userRepository.findByUsername("admin") == null) {
				User admin = new User();

				admin.setName("Admin");
				admin.setLastName("Admin");
				admin.setAge(30);
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode(adminPassword));
				admin.setRoles(Set.of(adminRole));

				userRepository.save(admin);
			}

			if (userRepository.findByUsername("user") == null) {
				User user = new User();

				user.setName("User");
				user.setLastName("User");
				user.setAge(25);
				user.setUsername("user");
				user.setPassword(passwordEncoder.encode(userPassword));
				user.setRoles(Set.of(userRole));

				userRepository.save(user);
			}
		};
	}
}