package by.students.grsu.controller;

import by.students.grsu.entities.auction.AuctionInfo;
import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.lot.Lot;
import by.students.grsu.entities.lot.LotInfo;
import by.students.grsu.entities.services.AuctionException;
import by.students.grsu.entities.services.interfaces.AuctionService;
import by.students.grsu.entities.services.interfaces.ItemService;
import by.students.grsu.entities.services.interfaces.LotService;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LotController {
    private AuctionService auctionService;
    private LotService lotService;
    private ItemService itemService;

    public LotController(AuctionService auctionService, LotService lotService, ItemService itemService){
        this.auctionService = auctionService;
        this.lotService = lotService;
        this.itemService = itemService;
    }

    @PostConstruct
    private void postConstructor(){
        System.out.println("LotController: OK");
    }

    @RequestMapping(value = "/{a_id}/addLot", method = RequestMethod.GET)
    public ModelAndView addLot(@PathVariable("a_id") Integer a_id, SecurityContextHolderAwareRequestWrapper contextHolder, HttpServletRequest request) {
        ModelAndView mv;
            mv = new ModelAndView("addLot");
            mv.addObject("a_id", a_id);
            try {
                List<Item> items = itemService.getFreeItemsByOwner(contextHolder);
                mv.addObject("items", items);
            } catch (Exception e) {
                mv.addObject("errMessage", "Internal error " + e.getMessage() + ". Sorry.");
            }
            mv.addObject("back", a_id + "/lotList");
        return mv;
    }

    @RequestMapping(value = "/{a_id}/saveLot", method = RequestMethod.GET)
    public String lotInfo(@PathVariable("a_id") Integer a_id,
                          ModelMap model, HttpServletRequest request){
            String[] parameters = request.getParameterValues("items");
            int[] ids = new int[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                ids[i] = Integer.parseInt(parameters[i]);
            }

            try {
                LotInfo newLot = lotService.createLot(a_id, request.getParameter("name"), Double.parseDouble(request.getParameter("price")), Double.parseDouble(request.getParameter("min_price")), ids);
                return "redirect:/" + a_id + "/" + newLot.getID() + "/lotInfo";
            } catch (AuctionException e) {
                model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
                System.out.println(e.getMessage() + " controller");
                //mb return to error page
            } catch (Exception e) {
                model.addAttribute("errMessage", "Internal error " + e.getMessage() + ". Sorry.");
                System.out.println(e.getMessage() + " controller");
                //mb return to error page
            }
        return "redirect:/" + a_id + "/lotList";
    }

    @RequestMapping(value = "/{a_id}/{l_id}/lotInfo", method = RequestMethod.GET)
    public String lotInfo(ModelMap model, @PathVariable("a_id") Integer a_id, @PathVariable("l_id") Integer l_id) {
        Lot lot = lotService.getLotById(l_id);
        model.addAttribute("ID", lot.getID());
        model.addAttribute("name", lot.getName());
        model.addAttribute("price", lot.getCurrentPrice());
        model.addAttribute("items", itemService.getItemsByLot(l_id));
        model.addAttribute("back", a_id + "/lotList");
        return "lot";
    }

    @RequestMapping(value = "/{a_id}/deleteLot", method = RequestMethod.GET)
    public ModelAndView deleteLot(@PathVariable("a_id") Integer a_id) {
        ModelAndView mv = new ModelAndView("deleteLot");
        try {
            AuctionInfo auc = auctionService.getAuctionWithLots(a_id);
            mv.addObject("auction", auc);
        }catch (Exception e){
            mv.addObject("errMessage", "Internal error " + e.getMessage()+ ". Sorry.");
        }
        mv.addObject("back", a_id + "/lotList");
        return mv;
    }

    @RequestMapping(value = "/{a_id}/deleteLotPart2", method = RequestMethod.GET)
    public String itemInfo(@PathVariable("a_id") Integer a_id,
                           ModelMap model, ServletRequest request) {
        int num = Integer.parseInt(request.getParameter("lot"));

        try {
            Lot lot = lotService.getLotById(num);
//            if(!item.getOwner().equals(user.getUsername())){
//                model.addAttribute("errMessage", "Sorry, you can't delete this item, because it's not yours.");
//                List<Item> items = itemService.getItemsByOwner(user);
//                model.addAttribute("items", items);
//                return "deleteItem";
//            }
            lotService.deleteLot(num);
            itemService.freeItemsByLot(num);
        }catch (Exception e){
            model.addAttribute("errMessage", "Internal error " + e.getMessage()+ ". Sorry.");
            return "deleteItem";
        }

        return "redirect:/" + a_id + "/lotList";
    }

    @RequestMapping(value = "/allLotList", method = RequestMethod.GET)
    public String watchAllLots(ModelMap model, HttpServletRequest request) {
        List<Lot> lots;
        if(request.isUserInRole("ADMIN"))
            lots = lotService.getAllLots();
        else{
            lots = lotService.getNotSoldLots();
            //todo add lots of user
        }
        model.addAttribute("lots", lots);
        return "allLotList";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchLots(ModelMap model, ServletRequest request) {
        String searchStr = request.getParameter("s");
        List<Lot> lots = lotService.getLotsBySearch(searchStr);
        model.addAttribute("lots", lots);
        model.addAttribute("searchStr", searchStr);
        return "search";
    }
}
