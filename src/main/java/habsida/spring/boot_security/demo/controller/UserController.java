package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/admin/new")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getAllRoles());
        return "user-form";
    }

    @PostMapping("/admin/save")
    public String saveUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            Model model
    ) {

        User userWithSameUsername = userService.findByUsername(user.getUsername());

        if (userWithSameUsername != null &&
                (user.getId() == null ||
                        !userWithSameUsername.getId().equals(user.getId()))) {

            bindingResult.rejectValue(
                    "username",
                    "error.user",
                    "Пользователь с таким Username уже существует"
            );
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", userService.getAllRoles());
            return "user-form";
        }

        if (user.getId() == null) {
            userService.saveUser(user);
        } else {
            userService.updateUser(user);
        }

        return "redirect:/admin";
    }

    @GetMapping("/admin/edit/{id}")
    public String showEditForm(
            @PathVariable("id") Long id,
            Model model
    ) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", userService.getAllRoles());
        return "user-form";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/user")
    public String userPage(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        return "user";
    }
}