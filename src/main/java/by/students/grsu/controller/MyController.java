package by.students.grsu.controller;

import by.students.grsu.entities.Auction;
import by.students.grsu.entities.Item;
import by.students.grsu.entities.Lot;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MyController {
    List<Auction> aucs = new ArrayList<Auction>();
    List<Item> freeItems = new ArrayList<Item>();


    @RequestMapping(value = "/addItem", method = RequestMethod.GET)
    public ModelAndView addItem() {
        ModelAndView mv = new ModelAndView("addItem");
        mv.addObject("item", new Item());
        return mv;
    }

    @RequestMapping(value = "/saveItem", method = RequestMethod.POST)
    public String itemInfo(@ModelAttribute("item") Item item,
                          ModelMap model) {
        //there should be adding item to the db
        freeItems.add(item);
        //TO-DO add list of items as attribute
        model.addAttribute("ID", item.getID());
        model.addAttribute("description", item.getDescription());
        model.addAttribute("status", item.getStatus());

        return "item";
    }

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


        auc.addLot(lot);
        //TO-DO add list of items as attribute
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

        //TO-DO add list of items as attribute
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
        if(aucs.size() < 10) for(int i = 0; i < 10; i++) aucs.add(new Auction());
        model.addAttribute("auctions", aucs);
//        model.addAttribute();
        return "auctionList";
    }

    @RequestMapping(value = "/{id}/lotList", method = RequestMethod.GET)
    public String watchLots(ModelMap model, @PathVariable("id") Integer id/*, @ModelAttribute("auction") Auction auc*/) {
        Auction auc = aucs.get(id-1);
        model.addAttribute("auction", auc);
        return "lotList";
    }
}