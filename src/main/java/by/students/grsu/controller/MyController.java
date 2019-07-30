package by.students.grsu.controller;

import by.students.grsu.entities.Auction;
import by.students.grsu.entities.Lot;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MyController {
    //Auction au = new Auction();
    List<Auction> aucs = new ArrayList<Auction>();
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
    @RequestMapping(value = "/{id}/addLot", method = RequestMethod.GET)
    public ModelAndView addLot(@PathVariable("id") Integer id) {
        ModelAndView mv = new ModelAndView("addLot");
//        mv.addObject("auction", aucs.get(id-1));
        mv.addObject("id", id);
        mv.addObject("lot", new Lot());
        return mv;
    }

    @RequestMapping(value = "/{id}/saveLot", method = RequestMethod.POST)
    public String lotInfo(@ModelAttribute("lot") Lot lot, @PathVariable("id") Integer id,
                             ModelMap model) {
        Auction auc = aucs.get(id-1);
        if(lot.getAuction() == null) lot.setAuction(auc);
        //there should be adding lot to the db : or better modifying existing auction in db
        //au.addLot(lot);
        auc.addLot(lot);
//        lot.setStep();
        model.addAttribute("ID", lot.getID());
        model.addAttribute("price", lot.getPrice());
        model.addAttribute("min_price", lot.getMin_price());
        model.addAttribute("description", lot.getDescription());
        model.addAttribute("auction", lot.getAuction().getId());

        return "lot";
    }

    @RequestMapping(value = "/addAuction", method = RequestMethod.GET)
    public ModelAndView addAuction() {
        ModelAndView mv = new ModelAndView("addAuction");
        mv.addObject("auc", new Auction());
        return mv;
    }

    @RequestMapping(value = "/saveAuction", method = RequestMethod.POST)
    public String auctionInfo(@ModelAttribute("auc") Auction auc,
                          ModelMap model) {
        auc.setId(auc.getId() - 1);
        Auction.setMinFreeId(Auction.getMinFreeId() - 1);
        //задница какая-то: в 2 раза больше работы, т.к. создаются 2 разных auc
        aucs.add(auc);
        //there should be adding auc to the db : or better modifying existing auction in db
        model.addAttribute("id", auc.getId());
        model.addAttribute("start_time", auc.getStart_time());
        model.addAttribute("durationMin", auc.getDurationMin());
        return "auction";
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

    @RequestMapping(value = "/sorry", method = RequestMethod.GET)
    public String sorry(ModelMap model) {
        model.addAttribute("username", "THERE_SHOULD_BE_NAME");
        return "sorry";
    }

    @RequestMapping(value = "/lotInfo", method = RequestMethod.GET)
    public String lotInfo(ModelMap model, @ModelAttribute("lot") Lot lot) {
        model.addAttribute("ID", lot.getID());
        model.addAttribute("price", lot.getPrice());
        model.addAttribute("min_price", lot.getMin_price());
        model.addAttribute("description", lot.getDescription());
        model.addAttribute("auction", lot.getAuction().getId());
        return "lot";
    }

    @RequestMapping(value = "/auctionList", method = RequestMethod.GET)
    public String watchAuctions(ModelMap model) {
//        TO-DO fetch auctions from DB
//        List<Auction> auctions = new ArrayList<Auction>();
        if(aucs.size() != 10) for(int i = 0; i < 10; i++) aucs.add(new Auction());
        model.addAttribute("auctions", aucs);
//        model.addAttribute();
        return "auctionList";
    }

    @RequestMapping(value = "/{id}/lotList", method = RequestMethod.GET)
    public String watchLots(ModelMap model, @PathVariable("id") Integer id/*, @ModelAttribute("auction") Auction auc*/) {
        //Auction auc = au;//new Auction();
        Auction auc = aucs.get(id-1);
//        for(int i = 0; i < 10; i++) {
//            Lot lot = new Lot();
//            lot.setAuction(auc);
//            auc.addLot(lot);
//        }
        model.addAttribute("auction", auc);
        return "lotList";
    }
}