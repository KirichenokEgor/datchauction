package by.students.grsu.controller;

import by.students.grsu.entities.auction.AuctionInfo;
import by.students.grsu.entities.auction.FollowedAuction;
import by.students.grsu.entities.auction.TempAuction;
import by.students.grsu.entities.services.interfaces.AuctionService;
import by.students.grsu.entities.services.interfaces.FollowedAuctionService;
import by.students.grsu.entities.services.interfaces.ItemService;
import by.students.grsu.entities.services.interfaces.LotService;
import by.students.grsu.websocket.UserSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuctionController {
    private AuctionService auctionService;
    private LotService lotService;
    private ItemService itemService;
    private UserSessionService sessionService;
    private FollowedAuctionService followedAuctionService;

    public AuctionController(AuctionService auctionService, LotService lotService, ItemService itemService,
                             UserSessionService sessionService, FollowedAuctionService followedAuctionService){
        this.auctionService = auctionService;
        this.lotService = lotService;
        this.itemService = itemService;
        this.sessionService = sessionService;
        this.followedAuctionService = followedAuctionService;
    }

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
        System.out.println("AuctionController: OK");
    }

    @RequestMapping(value = "/addAuction", method = RequestMethod.GET)
    public ModelAndView addAuction(HttpServletRequest request) {
        ModelAndView mv;
        mv = new ModelAndView("addAuction");
        mv.addObject("auc", new TempAuction());
        mv.addObject("back", "auctionList");
        return mv;
    }

    @RequestMapping(value = "/saveAuction", method = RequestMethod.POST)
    public ModelAndView auctionInfo(@ModelAttribute("auc") TempAuction auc, HttpServletRequest request) {
        AuctionInfo createdAuction = auctionService.getAuctionInfo
                (auctionService.addAuction(auc.getDescription(), auc.getMaxLots(), auc.getStartTime(), auc.getMaxDuration()));
//        return "redirect:/" + createdAuction.getID() + "/lotList";
        request.setAttribute(
                View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:/" + createdAuction.getID() + "/lotList");
    }
    @RequestMapping(value = "/auctionList", method = RequestMethod.GET)
        public String watchAuctions(ModelMap model, HttpServletRequest request) {
        List<AuctionInfo> iAuctions;
        if(request.isUserInRole("ADMIN")) {
            iAuctions = auctionService.getAllAuctions();
        }else {
            iAuctions = auctionService.getActiveAuctions();
            iAuctions.addAll(auctionService.getPlannedAuctions());
        }
        List<FollowedAuction> auctions;
        auctions = followedAuctionService.auctionsAsFollowed(iAuctions, request.getRemoteUser());
        model.addAttribute("auctions", auctions);
        return "auctionList";
    }

    @RequestMapping(value = "/activeAuctionList", method = RequestMethod.GET)
    public String watchActiveAuctions(ModelMap model, HttpServletRequest request) {
        List<AuctionInfo> iAuctions = auctionService.getActiveAuctions();
        List<FollowedAuction> auctions = followedAuctionService.auctionsAsFollowed(iAuctions, request.getRemoteUser());
        model.addAttribute("auctions", auctions);
        return "activeAuctionList";
    }

    @RequestMapping(value = "/deleteAuction", method = RequestMethod.GET)
    public ModelAndView deleteAuction(HttpServletRequest request) {
        ModelAndView mv;
        mv = new ModelAndView("deleteAuction");
        List<AuctionInfo> iAuctions = auctionService.getAllAuctions();
        List<FollowedAuction> auctions = followedAuctionService.auctionsAsFollowed(iAuctions, request.getRemoteUser());
        mv.addObject("auctions", auctions);
        mv.addObject("back", "auctionList");
        return mv;
    }

    private void deleteAuction(int id){
        auctionService.deleteAuction(id);
        followedAuctionService.deleteFollowedAuctionsById(id);
    }

    @RequestMapping(value = "/deleteAuctionPart2", method = RequestMethod.POST)
    public String auctionInfo(HttpServletRequest request) {
            int num = Integer.parseInt(request.getParameter("auc"));
            deleteAuction(num);
        return "redirect:/auctionList";
    }

    @RequestMapping(value = "/{id}/lotList", method = RequestMethod.POST)
    public String watchLots(ModelMap model, @PathVariable("id") Integer id, HttpServletRequest request) {
        AuctionInfo auc = auctionService.getAuctionWithLots(id);
        if(!auc.getStringStatus().equals("Active") && !auc.getStringStatus().equals("Planned") && !request.isUserInRole("ADMIN")){
            return "redirect:/accessDenied";
        }
        List<AuctionInfo> list = new ArrayList<AuctionInfo>();
        list.add(auc);
        FollowedAuction auction = followedAuctionService.auctionsAsFollowed(list, request.getRemoteUser()).get(0);
        model.addAttribute("auction", auction);
        return "lotList";
    }

    @RequestMapping(value = "/{id}/join", method = RequestMethod.POST)
    public String joinAuction(@PathVariable("id") Integer id,
                              ModelMap model) {
        return "redirect:/{id}/activeAuction";
    }

    @RequestMapping(value = "/{id}/activeAuction", method = RequestMethod.GET)
    public String watchActiveAuction(@PathVariable("id") Integer id,
                                     ModelMap model, HttpServletRequest request) {
        AuctionInfo auc = auctionService.getAuctionWithLots(id);
        if(!auc.getStringStatus().equals("Active")) return "redirect:/home";
        try {
            sessionService.waitSession(request.getRemoteUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("auction", auc);
        //model.addAttribute("back", "activeAuctionList");
        return "activeAuction";
    }

    @RequestMapping(value = "/{id}/makePlanned", method = RequestMethod.POST)
    public String makePlanned(@PathVariable("id") Integer id,
                              ModelMap model, HttpServletRequest request) {
            auctionService.setAuctionPlanned(id);
        return "redirect:/auctionList";
    }

    @RequestMapping(value = "/{id}/subscribe", method = RequestMethod.POST)
    public ModelAndView subscribe(@PathVariable("id") Integer id,
                            HttpServletRequest request) {
        if(auctionService.getAuctionInfo(id).getStringStatus().equals("Planned"))
            followedAuctionService.addFollowedAuction(request.getRemoteUser(), id);
        String referer = request.getHeader("Referer");
        if(!referer.endsWith("/auctionList"))
            request.setAttribute(
                    View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:" + referer);
    }

    @RequestMapping(value = "/{id}/unsubscribe", method = RequestMethod.POST)
    public ModelAndView unsubscribe(@PathVariable("id") Integer id,
                            HttpServletRequest request) {
        if(auctionService.getAuctionInfo(id).getStringStatus().equals("Planned"))
            followedAuctionService.deleteFollowedAuction(request.getRemoteUser(), id);
        String referer = request.getHeader("Referer");
        if(!referer.endsWith("/auctionList"))
            request.setAttribute(
                    View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:" + referer);
    }

    @RequestMapping(value = "/followedAuctions", method = {RequestMethod.GET,RequestMethod.POST})
    public String watchFollowedAuctions(ModelMap model, HttpServletRequest request) {
        List<FollowedAuction> auctions = followedAuctionService.getFollowedAuctionsByUser(request.getRemoteUser());
        model.addAttribute("auctions", auctions);
        return "followedAuctionList";
    }
}
