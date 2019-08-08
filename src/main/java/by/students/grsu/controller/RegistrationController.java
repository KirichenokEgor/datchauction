package by.students.grsu.controller;

import by.students.grsu.entities.services.AuctionException;
import by.students.grsu.entities.services.UserService;
import by.students.grsu.entities.users.TempUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.annotation.PostConstruct;

@Controller
@SessionAttributes("user")
public class RegistrationController {
    private UserService userService;

    @PostConstruct
    private void postConstructor(){
        System.out.println("UserRegistrationService: OK");
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/registration")
    public String Registration(Model model){
        if(!model.containsAttribute("tempUser")) {
            TempUser tu = new TempUser();
            model.addAttribute("tempUser", tu);
        }
        model.addAttribute("registrationMessage", "Please enter all fields correctly");
        return "registration";
    }

    @RequestMapping("/confirmRegistration")
    public String confirmRegistration(@ModelAttribute("tempUser")TempUser tu, Model model){
        //TODO request registration
        if(isRegistrationValid(tu)) {
            try {
                model.addAttribute("user",userService.registration(tu.getEmail(),tu.getPassword(),tu.getUsername()));
                return "index";
            } catch (AuctionException e) {
                if(e.getCode()==13)model.addAttribute("registrationMessage", "This email is already using!");
                if(e.getCode()==14)model.addAttribute("registrationMessage", "This nickname is already using. Please create another one");
            }
        }
        else model.addAttribute("registrationMessage", "Password and confirm not match.");
        return "registration";
    }

    private boolean isRegistrationValid(TempUser tu){
        if(!tu.getPassword().equals(tu.getConfirmPassword()))return false;
        //TODO check nickname,email,passwordDifficult...
        return true;
    }
}
