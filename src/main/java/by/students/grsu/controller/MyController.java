package by.students.grsu.controller;

import by.students.grsu.entities.lot.Lot;
import by.students.grsu.entities.users.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

//class IntegerWrapper{
//    Integer value;
//    IntegerWrapper(){
//        value = 1;
//    }
//
//    public Integer getValue() {
//        return value;
//    }
//
//    public void setValue(Integer value) {
//        this.value = value;
//    }
//}

@Controller
@SessionAttributes("user")
public class MyController {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(ModelMap model, @ModelAttribute("user") User user) {
        return "index";
    }

    @RequestMapping(value = "/thanks", method = RequestMethod.GET)
    public String thanks(ModelMap model, @ModelAttribute("lot") Lot lot, @ModelAttribute("user") User user) {
        model.addAttribute("lot", lot);

        return "thanks";
    }

    @RequestMapping(value = "/sorry", method = RequestMethod.GET)
    public String sorry(ModelMap model, @ModelAttribute("user") User user) {
        return "sorry";
    }

}