package by.students.grsu.controller;

import by.students.grsu.entities.Auction;
import by.students.grsu.entities.Lot;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
public class MyController {
//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String index(ModelMap model) {
//
//        model.addAttribute("message", "Hello Spring MVC 5!");
//        return "index";
//    }
//    @GetMapping("/")
//    public String index(Model model) {
//        model.addAttribute("message", "Hello Spring MVC 5!");
//        return "index";
//    }
//    @RequestMapping(value = "/hello", method = RequestMethod.GET)
//    public String printHello(ModelMap model) {
//        model.addAttribute("message", "Hello Spring MVC Framework!");
//        return "hello";
//    }
//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    public String printList(ModelMap model) {
////        String[]list = {"smth1", "smth2", "smth3", "smth4", "smth5"};
//        ArrayList<String> list = new ArrayList<String>();
//        list.add("smth1");
//        list.add("smth2");
//        list.add("smth3");
//        list.add("smth4");
//        list.add("smth5");
//        model.addAttribute("list", list);
//        return "list";
//    }
    @RequestMapping(value = "/addLot", method = RequestMethod.GET)
    public ModelAndView addLot(@ModelAttribute("auction")Auction auc) {
        ModelAndView mv = new ModelAndView("addLot");
        mv.addObject("auction", auc);
        mv.addObject("lot", new Lot());
        return mv;
    }

    @RequestMapping(value = "/saveLot", method = RequestMethod.POST)
    public String lotInfo(@ModelAttribute("lot") Lot lot, @ModelAttribute("auction") Auction auc,
                             ModelMap model) {
        if(lot.getAuction() != null) lot.setAuction(auc);
//        lot.setStep();
        model.addAttribute("ID", lot.getID());
        model.addAttribute("price", lot.getPrice());
        model.addAttribute("min_price", lot.getMin_price());
        model.addAttribute("description", lot.getDescription());
        model.addAttribute("auction", lot.getAuction());

        return "lot";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(ModelMap model) {
        model.addAttribute("username", "THERE_SHOULD_BE_NAME");
        return "index";
    }

    @RequestMapping(value = "/thanks", method = RequestMethod.GET)
    public String thanks(ModelMap model, @ModelAttribute("lot") Lot lot) {
        model.addAttribute("username", "THERE_SHOULD_BE_NAME");
        model.addAttribute("lot", lot);
        return "thanks";
    }

    @RequestMapping(value = "/lotInfo", method = RequestMethod.GET)
    public String lotInfo(ModelMap model, @ModelAttribute("lot") Lot lot) {
        model.addAttribute("ID", lot.getID());
        model.addAttribute("price", lot.getPrice());
        model.addAttribute("min_price", lot.getMin_price());
        model.addAttribute("description", lot.getDescription());
        model.addAttribute("auction", lot.getAuction());
        return "lot";
    }
}