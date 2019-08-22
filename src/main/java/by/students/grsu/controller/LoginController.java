package by.students.grsu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;

@Controller
//@SessionAttributes("user")
public class LoginController {

    @PostConstruct
    private void postConstructor(){
        System.out.println("LoginController: OK");
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
