package habsida.spring.boot_security.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя не должно быть пустым")
    @Pattern(
            regexp = "^[A-Za-zА-Яа-яЁё]+$",
            message = "Имя должно содержать только буквы"
    )
    private String name;

    @NotBlank(message = "Фамилия не должна быть пустой")
    @Pattern(
            regexp = "^[A-Za-zА-Яа-яЁё]+$",
            message = "Фамилия должна содержать только буквы"
    )
    private String lastName;

    @NotNull(message = "Возраст не должен быть пустым")
    @Min(value = 1, message = "Возраст должен быть от 1 до 120")
    @Max(value = 120, message = "Возраст должен быть от 1 до 120")
    private Integer age;

    @NotBlank(message = "Username не должен быть пустым")
    private String username;

    private String password;

    @NotEmpty(message = "Необходимо выбрать хотя бы одну роль")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}