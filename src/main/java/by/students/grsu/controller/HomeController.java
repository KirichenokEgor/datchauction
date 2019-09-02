package by.students.grsu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;

@Controller
public class HomeController {

    @PostConstruct
    private void postConstructor(){
        System.out.println("HomeController: OK");
    }

    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")//does not work
    @RequestMapping(value = {"/home", "/"}, method = RequestMethod.GET)
    //@Secured("ROLE_ADMIN")// does not work
    //@RolesAllowed("ROLE_ADMIN") //works with javax.javaee-api
    //@PreAuthorize("hasRole('ROLE_ADMIN')") does not work
    //@PreAuthorize("1 != 1") does not work
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public String accessDenied(){
        return "accessDenied";
    }
}