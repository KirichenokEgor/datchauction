package by.students.grsu.controller;

import by.students.grsu.entities.services.interfaces.UserService;
import by.students.grsu.entities.users.TempUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;

@Controller
public class LoginAndRegistrationController {
    private UserService userService;
    private UserDetailsService userDetailsManager;

    public LoginAndRegistrationController(UserService userService, UserDetailsService userDetailsManager){
        this.userService = userService;
        this.userDetailsManager = userDetailsManager;
    }

    @PostConstruct
    private void postConstructor(){
        System.out.println("LoginAndRegistrationController: OK");
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/registration")
    public String Registration(Model model){
        model.addAttribute("tempUser", new TempUser());
        model.addAttribute("registrationMessage", "Please enter all fields correctly");
        return "registration";
    }

    @PostMapping("/confirmRegistration")
    public String confirmRegistration(@ModelAttribute("tempUser")TempUser tu, Model model){
        //TODO request registration
        if(isRegistrationValid(tu)) {
                ((JdbcUserDetailsManager)userDetailsManager).createUser(User.withDefaultPasswordEncoder()
                        .username(tu.getUsername())
                        .password(tu.getPassword())
                        .roles("BUYER")
                        .build());
                userService.setEmail(tu.getUsername(), tu.getEmail());
                return "redirect:/login";
        }
        else {
            model.addAttribute("registrationMessage", "Password and confirm not match.");
            model.addAttribute("tempUser", tu);
        }
        return "registration";
    }

    private boolean isRegistrationValid(TempUser tu){
        if(!tu.getPassword().equals(tu.getConfirmPassword()))return false;
        //TODO check nickname,email,passwordDifficult...
        return true;
    }
}
