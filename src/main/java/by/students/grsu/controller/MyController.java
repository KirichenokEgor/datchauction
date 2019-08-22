package by.students.grsu.controller;

import by.students.grsu.entities.lot.Lot;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class MyController {

    @RequestMapping(value = {"/home", "/"}, method = RequestMethod.GET)
    public String home(ModelMap model) {
        return "index";
    }

    @RequestMapping(value = "/thanks", method = RequestMethod.GET)
    public String thanks(ModelMap model, @ModelAttribute("lot") Lot lot) {
        model.addAttribute("lot", lot);
        return "thanks";
    }

    @RequestMapping(value = "/sorry", method = RequestMethod.GET)
    public String sorry(ModelMap model) {
        return "sorry";
    }
}