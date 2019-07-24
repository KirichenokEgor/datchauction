package by.students.grsu;

import by.students.grsu.entities.Lot;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
public class HelloController {
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String printHello(ModelMap model) {
        model.addAttribute("message", "Hello Spring MVC Framework!");
        return "hello";
    }
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String printList(ModelMap model) {
//        String[]list = {"smth1", "smth2", "smth3", "smth4", "smth5"};
        ArrayList<String> list = new ArrayList<String>();
        list.add("smth1");
        list.add("smth2");
        list.add("smth3");
        list.add("smth4");
        list.add("smth5");
        model.addAttribute("list", list);
        return "list";
    }
    @RequestMapping(value = "/lot", method = RequestMethod.GET)
    public ModelAndView lot() {
        return new ModelAndView("lot_regestration", "command", new Lot());
    }

    @RequestMapping(value = "/addLot", method = RequestMethod.POST)
    public String addItem(Lot lot,
                             ModelMap model) {
        model.addAttribute("ID", lot.getID());
        model.addAttribute("price", lot.getPrice());
        model.addAttribute("min_price", lot.getMin_price());
        model.addAttribute("description", lot.getDescription());

        return "lot";
    }
}