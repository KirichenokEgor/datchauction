package by.students.grsu.controller;

import by.students.grsu.entities.services.AuctionException;
import by.students.grsu.entities.services.UserService;
import by.students.grsu.entities.users.TempUser;
import by.students.grsu.entities.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;

@Controller
@SessionAttributes("user")
public class LoginController {
    private UserService userService;

    @PostConstruct
    private void postConstructor(){
        System.out.println("UserLoginService: OK");
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/login","/"})
    public ModelAndView login(SessionStatus status) {
        TempUser tu = new TempUser();
        ModelAndView mv = new ModelAndView("login");
        mv.addObject("loginMessage", "");
        mv.addObject("tempUser", tu);
        return mv;
    }

    @PostMapping("/confirmLogin")
    public String confirmLogin(@ModelAttribute("tempUser") TempUser tu, Model model) {
        try {
            model.addAttribute("user", userService.login(tu.getEmail(),tu.getPassword()));
            return "redirect:/home";
        } catch (AuctionException e) {
            if(e.getCode()==11 || e.getCode()==12){
                model.addAttribute("loginMessage","Wrong email or password. Try again");
            }else model.addAttribute("loginMessage","Internal error. Sorry about that. Try again later");
        }
        return "login";
    }

    @RequestMapping(value = "/finish", method = RequestMethod.GET)
    public String goodbye(@ModelAttribute User user, SessionStatus status) {
        /**
         * store User ...
         */
        status.setComplete();
        return "redirect:/login";
    }
}
