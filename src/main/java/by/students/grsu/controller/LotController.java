package by.students.grsu.controller;

import by.students.grsu.entities.auction.AuctionInfo;
import by.students.grsu.entities.lot.Lot;
import by.students.grsu.entities.lot.LotInfo;
import by.students.grsu.entities.lot.TempLot;
import by.students.grsu.entities.services.AuctionException;
import by.students.grsu.entities.services.LotService;
import by.students.grsu.entities.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("user")
public class LotController {
    private LotService lotService;

    @Autowired
    public void setLotService(LotService lotService) {
        this.lotService = lotService;
    }

    @RequestMapping(value = "/{a_id}/addLot", method = RequestMethod.GET)
    public ModelAndView addLot(@PathVariable("a_id") Integer a_id, @ModelAttribute("user") User user) {
        ModelAndView mv = new ModelAndView("addLot");
//        mv.addObject("auction", aucs.get(id-1));
        mv.addObject("a_id", a_id);
        mv.addObject("lot", new TempLot());
        mv.addObject("back", a_id + "/lotList");
        return mv;
    }

    @RequestMapping(value = "/{a_id}/saveLot", method = RequestMethod.POST)
    public String lotInfo(@ModelAttribute("lot") TempLot tempLot,  @ModelAttribute("user") User user, @PathVariable("a_id") Integer a_id,
                          ModelMap model){
        //LotInfo newLot=null;
        try {
            LotInfo newLot = lotService.createLot(a_id, tempLot.getName(), tempLot.getPrice(), tempLot.getMin_price());

            model.addAttribute("ID", newLot.getID());
            model.addAttribute("name", newLot.getName());
            model.addAttribute("price", newLot.getCurrentPrice());
        }catch(AuctionException e){
            model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
            System.out.println(e.getMessage());
        }

        model.addAttribute("back", a_id + "/lotList");
        return "lot";
    }

    @RequestMapping(value = "/lotInfo", method = RequestMethod.GET)
    public String lotInfo(ModelMap model, @ModelAttribute("lot") Lot lot, @ModelAttribute("user") User user) {

        //TODO add list of items as attribute
        model.addAttribute("ID", lot.getID());
        model.addAttribute("name", lot.getName());
        model.addAttribute("price", lot.getCurrentPrice());
        //model.addAttribute("min_price", lot.getMin_price());
        //model.addAttribute("description", lot.getDescription());
        //model.addAttribute("auction", lot.getAuction().getId());
        return "lot";
    }
}
