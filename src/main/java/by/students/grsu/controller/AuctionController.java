package by.students.grsu.controller;

import by.students.grsu.entities.auction.AuctionInfo;
import by.students.grsu.entities.auction.TempAuction;
import by.students.grsu.entities.services.AuctionService;
import by.students.grsu.entities.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

@Controller
@SessionAttributes("user")
public class AuctionController {
    private AuctionService auctionService;

    @Autowired
    public void setAuctionService(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @RequestMapping(value = "/addAuction", method = RequestMethod.GET)
    public ModelAndView addAuction(@ModelAttribute("user") User user) {
        ModelAndView mv = new ModelAndView("addAuction");
        mv.addObject("auc", new TempAuction());
        mv.addObject("back", "auctionList");
        return mv;
    }

    @RequestMapping(value = "/saveAuction", method = RequestMethod.POST)
    public String auctionInfo(@ModelAttribute("auc") TempAuction auc, @ModelAttribute("user") User user,
                              ModelMap model) {
        try {
            AuctionInfo createdAuction = auctionService.getAuctionInfo
                    (auctionService.addAuction(auc.getDescription(), auc.getMaxLots(), auc.getStartTime(), auc.getMaxDuration()));
            model.addAttribute("id", createdAuction.getID());
            model.addAttribute("start_time", createdAuction.getBeginTime());
            model.addAttribute("maxDuration", createdAuction.getMaxDuration());
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        model.addAttribute("back", "auctionList");
        return "auction";
    }

    @RequestMapping(value = "/auctionList", method = RequestMethod.GET)
    public String watchAuctions(ModelMap model, @ModelAttribute("user") User user) {
        List<AuctionInfo> auctions = auctionService.getAuctions();
        model.addAttribute("auctions", auctions);
        return "auctionList";
    }

    @RequestMapping(value = "/deleteAuction", method = RequestMethod.GET)
    public ModelAndView deleteAuction(@ModelAttribute("user") User user) {
        //TODO check if user is admin
        ModelAndView mv = new ModelAndView("deleteAuction");
        List<AuctionInfo> auctions = auctionService.getAuctions();
        mv.addObject("auctions", auctions);

        mv.addObject("num", new IntegerWrapper());
        mv.addObject("back", "auctionList");
        return mv;
    }

    @RequestMapping(value = "/deleteAuctionPart2", method = RequestMethod.POST)
    public String auctionInfo(@ModelAttribute("num") IntegerWrapper num, @ModelAttribute("user") User user,
                              ModelMap model) {
        //TODO check if user is owner of this item
//        try {
        auctionService.deleteAuction(num.getValue());
//        }catch (SQLException e){
//            model.addAttribute("errMessage", "SQLError. Sorry. " + e.getMessage());
//            return "deleteAuction";
//        }catch(AuctionException e){
//            model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
//        }

        //model.addAttribute("back", "freeItems");

        return "redirect:/auctionList";
    }

    @RequestMapping(value = "/{id}/lotList", method = RequestMethod.GET)
    public String watchLots(ModelMap model, @PathVariable("id") Integer id, @ModelAttribute("user") User user) {
        AuctionInfo auc = auctionService.getAuctionInfo(id);
        model.addAttribute("auction", auc);
        return "lotList";
    }
}
