package by.students.grsu.controller;

import by.students.grsu.entities.auction.AuctionInfo;
import by.students.grsu.entities.auction.TempAuction;
import by.students.grsu.entities.lot.Lot;
import by.students.grsu.entities.services.AuctionService;
import by.students.grsu.entities.services.ItemService;
import by.students.grsu.entities.services.LotService;
import by.students.grsu.entities.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("user")
public class AuctionController {
    private AuctionService auctionService;
    private LotService lotService;
    private ItemService itemService;

    @PostConstruct
    private void postConstructor(){
        System.out.println(" ===============================================================================================\n"+
                "|  ______             _          __             _                      _    _                   |\n"+
                "| |_   _ `.          / |_       [  |           / \\                    / |_ (_)                  |\n"+
                "|   | | `. \\ __   _ `| |-'.---.  | |--.       / _ \\    __   _   .---.`| |-'__   .--.   _ .--.   |\n"+
                "|   | |  | |[  | | | | | / /'`\\] | .-. |     / ___ \\  [  | | | / /'`\\]| | [  |/ .'`\\ \\[ `.-. |  |\n"+
                "|  _| |_.' / | \\_/ |,| |,| \\__.  | | | |   _/ /   \\ \\_ | \\_/ |,| \\__. | |, | || \\__. | | | | |  |\n"+
                "| |______.'  '.__.'_/\\__/'.___.'[___]|__] |____| |____|'.__.'_/'.___.'\\__/[___]'.__.' [___||__] |\n"+
                " ===============================================================================================");
        System.out.println("Initializing...");
        System.out.println("AuctionService: OK");
    }

    @Autowired
    public void setAuctionService(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @Autowired
    public void setLotService(LotService lotService) {
        this.lotService = lotService;
    }

    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
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
        //в зависимости от прав юзера юзать разные методы, чтобы достать аукционы

        List<AuctionInfo> auctions = auctionService.getAllAuctions();
        model.addAttribute("auctions", auctions);
        return "auctionList";
    }

    @RequestMapping(value = "/activeAuctionList", method = RequestMethod.GET)
    public String watchActiveAuctions(ModelMap model, @ModelAttribute("user") User user) {
        //в зависимости от прав юзера юзать разные методы, чтобы достать аукционы

        List<AuctionInfo> auctions = auctionService.getActiveAuctions();
        model.addAttribute("auctions", auctions);
        return "activeAuctionList";
    }

    @RequestMapping(value = "/deleteAuction", method = RequestMethod.GET)
    public ModelAndView deleteAuction(@ModelAttribute("user") User user) {
        //TODO check if user is admin
        ModelAndView mv = new ModelAndView("deleteAuction");
        List<AuctionInfo> auctions = auctionService.getAllAuctions();
        mv.addObject("auctions", auctions);
        mv.addObject("back", "auctionList");
        return mv;
    }

    private void deleteAuction(int id){
        auctionService.deleteAuction(id);
        List<Lot> lots = lotService.getLotsByAuctionId(id);
        for(Lot lot : lots){
            itemService.freeItemsByLot(lot.getID());
        }
        lotService.deleteLotsByAuction(id);
    }

    @RequestMapping(value = "/deleteAuctionPart2", method = RequestMethod.GET)
    public String auctionInfo(@ModelAttribute("user") User user,
                              ModelMap model, ServletRequest request) {
        //TODO check if user is admin
        int num = Integer.parseInt(request.getParameter("auc"));
        deleteAuction(num);
        return "redirect:/auctionList";
    }

    @RequestMapping(value = "/{id}/lotList", method = RequestMethod.GET)
    public String watchLots(ModelMap model, @PathVariable("id") Integer id, @ModelAttribute("user") User user) {
        AuctionInfo auc = auctionService.getAuctionWithLots(id);
        //AuctionInfo auc = auctionService.getAuctionWithLots(id);
//        List<Lot> lots = lotService.getLotsByAuctionId(id);
        model.addAttribute("auction", auc);
//        model.addAttribute("lots", lots);

        return "lotList";
    }
}
