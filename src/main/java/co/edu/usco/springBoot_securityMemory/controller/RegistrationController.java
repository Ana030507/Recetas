package co.edu.usco.springBoot_securityMemory.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.edu.usco.springBoot_securityMemory.model.UserEntity;
import co.edu.usco.springBoot_securityMemory.repository.UserRepository;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final String adminCode;
    private final String chefCode;
    

    public RegistrationController(UserRepository userRepository,
                                  @Value("${registration.admin.code}") String adminCode,
    							@Value("${registration.chef.code}") String chefCode){
        this.userRepository = userRepository;
        this.adminCode = adminCode;
        this.chefCode = chefCode;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register"; // templates/register.html
    }

    @PostMapping("/register")
    public String processRegistration(@RequestParam String username,
                                      @RequestParam String password,
                                      @RequestParam(required = false) String adminCodeInput,
                                      @RequestParam(required = false) String chefCodeInput,
                                      Model model) {

        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "El nombre de usuario ya existe.");
            return "register";
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password); // guardamos la contraseña tal cual (texto plano)
        // si la clave que escribe coincide con la que está en properties -> ADMIN
        if (adminCodeInput != null && adminCodeInput.equals(adminCode)) {
            user.setRole("ADMIN");
        } else if (chefCodeInput != null && chefCodeInput.equals(chefCode)) {
            user.setRole("CHEF");
        } else {
            user.setRole("USER");
        }

        userRepository.save(user);
        return "redirect:/login";
    }
}
