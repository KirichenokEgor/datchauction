package by.students.grsu.controller;

import by.students.grsu.entities.auction.AuctionInfo;
import by.students.grsu.entities.auction.TempAuction;
import by.students.grsu.entities.lot.Lot;
import by.students.grsu.entities.services.AuctionService;
import by.students.grsu.entities.services.ItemService;
import by.students.grsu.entities.services.LotService;
import by.students.grsu.websocket.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

@Controller
//@SessionAttributes("user")
public class AuctionController {
    private AuctionService auctionService;
    private LotService lotService;
    private ItemService itemService;
    private UserSessionService sessionService;

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

    @Autowired
    public void setUserSessionService(UserSessionService sessionService) {
        this.sessionService = sessionService;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')") does not work
    @RequestMapping(value = "/addAuction", method = RequestMethod.GET)
    public ModelAndView addAuction(HttpServletRequest request) {
        ModelAndView mv;
        if(request.isUserInRole("ADMIN")){
        mv = new ModelAndView("addAuction");
        mv.addObject("auc", new TempAuction());
        mv.addObject("back", "auctionList");}
        else {
            mv = new ModelAndView("redirect:/auctionList");
        }
        return mv;
    }

    @RequestMapping(value = "/saveAuction", method = RequestMethod.POST)
    public String auctionInfo(@ModelAttribute("auc") TempAuction auc) {
        try {
            AuctionInfo createdAuction = auctionService.getAuctionInfo
                    (auctionService.addAuction(auc.getDescription(), auc.getMaxLots(), auc.getStartTime(), auc.getMaxDuration()));
            return "redirect:/" + createdAuction.getID() + "/lotList";
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return "redirect:/auctionList";
    }

    @RequestMapping(value = "/auctionList", method = RequestMethod.GET)
    public String watchAuctions(ModelMap model, HttpServletRequest request) {
        //в зависимости от прав юзера юзать разные методы, чтобы достать аукционы
        List<AuctionInfo> auctions;
        if(request.isUserInRole("ADMIN")) {
            auctions = auctionService.getAllAuctions();
        }else {
            auctions = auctionService.getActiveAuctions();
            auctions.addAll(auctionService.getPlannedAuctions());
        }
        model.addAttribute("auctions", auctions);
        return "auctionList";
    }

    @RequestMapping(value = "/activeAuctionList", method = RequestMethod.GET)
    public String watchActiveAuctions(ModelMap model) {
        List<AuctionInfo> auctions = auctionService.getActiveAuctions();
        model.addAttribute("auctions", auctions);
        return "activeAuctionList";
    }

    @RequestMapping(value = "/deleteAuction", method = RequestMethod.GET)
    public ModelAndView deleteAuction(HttpServletRequest request) {
        ModelAndView mv;
        if(request.isUserInRole("ADMIN")) {
            mv = new ModelAndView("deleteAuction");
            List<AuctionInfo> auctions = auctionService.getAllAuctions();
            mv.addObject("auctions", auctions);
            mv.addObject("back", "auctionList");
        }else{
            mv = new ModelAndView("redirect:/auctionList");
        }
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
    public String auctionInfo(HttpServletRequest request) {
        //TODO check if user is admin
        if(request.isUserInRole("ADMIN")) {
            int num = Integer.parseInt(request.getParameter("auc"));
            deleteAuction(num);
        }
        return "redirect:/auctionList";
    }

    @RequestMapping(value = "/{id}/lotList", method = RequestMethod.GET)
    public String watchLots(ModelMap model, @PathVariable("id") Integer id) {
        AuctionInfo auc = auctionService.getAuctionWithLots(id);
        model.addAttribute("auction", auc);
        return "lotList";
    }

    @RequestMapping(value = "/{id}/join", method = RequestMethod.GET)
    public String joinAuction(@PathVariable("id") Integer id,
                              ModelMap model) {
        return "redirect:/{id}/activeAuction";
    }

    @RequestMapping(value = "/{id}/activeAuction", method = RequestMethod.GET)
    public String watchActiveAuction(@PathVariable("id") Integer id,
                              ModelMap model, HttpServletRequest request) {
        AuctionInfo auc = auctionService.getAuctionWithLots(id);
        if(auc.getStringStatus() != "Active") return "redirect:/home";
        try {
            sessionService.waitSession(request.getRemoteUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("auction", auc);
        model.addAttribute("back", "activeAuctionList");
        return "activeAuction";
    }

    @RequestMapping(value = "/{id}/makePlanned", method = RequestMethod.GET)
    public String makePlanned(@PathVariable("id") Integer id,
                                     ModelMap model, HttpServletRequest request) {
        if(request.isUserInRole("ADMIN")) {
            auctionService.setAuctionPlanned(id);
        }
//        AuctionInfo auc = auctionService.getAuctionWithLots(id);
//        if(auc.getStringStatus() != "Active") return "redirect:/home";
//        try {
//            sessionService.waitSession(request.getRemoteUser());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        model.addAttribute("auction", auc);
//        model.addAttribute("back", "activeAuctionList");
        return "redirect:/auctionList";
    }
}
