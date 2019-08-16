package by.students.grsu.controller;

import by.students.grsu.entities.auction.AuctionInfo;
import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.lot.Lot;
import by.students.grsu.entities.lot.LotInfo;
import by.students.grsu.entities.lot.TempLot;
import by.students.grsu.entities.services.AuctionException;
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
public class LotController {
    private LotService lotService;
    private ItemService itemService;
    private AuctionService auctionService;

    @PostConstruct
    private void postConstructor(){
        System.out.println("LotService: OK");
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
    public void setAuctionService(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @RequestMapping(value = "/{a_id}/addLot", method = RequestMethod.GET)
    public ModelAndView addLot(@PathVariable("a_id") Integer a_id, @ModelAttribute("user") User user) {
        ModelAndView mv = new ModelAndView("addLot");
        mv.addObject("a_id", a_id);
        try {
            List<Item> items = itemService.getFreeItemsByOwner(user);
            mv.addObject("items", items);
//        }catch (SQLException e){
//            mv.addObject("errMessage", "SQLError. Sorry." + e.getSQLState() + "\n" + e.getErrorCode());
//        }catch (AuctionException e){
//            mv.addObject("errMessage", "Internal error " + e.getCode() + ". Sorry.");
        }catch (Exception e){
            mv.addObject("errMessage", "Internal error " + e.getMessage()+ ". Sorry.");
        }

        //mv.addObject("lot", new TempLot());
        mv.addObject("back", a_id + "/lotList");
        return mv;
    }

//    @RequestMapping(value = "/{a_id}/saveLot", method = RequestMethod.POST)
//    public String lotInfo(@ModelAttribute("lot") TempLot tempLot,  @ModelAttribute("user") User user, @PathVariable("a_id") Integer a_id,
//                          ModelMap model){
//        int[] items = {1,2,3};//TODO choose items
//        //LotInfo newLot=null;
//        try {
//            LotInfo newLot = lotService.createLot(a_id, tempLot.getName(), tempLot.getPrice(), tempLot.getMin_price(), items);
//
//            model.addAttribute("ID", newLot.getID());
//            model.addAttribute("name", newLot.getName());
//            model.addAttribute("price", newLot.getCurrentPrice());
//        }catch(AuctionException e){
//            model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
//            System.out.println(e.getMessage());
//        } catch (Exception e) {
//            model.addAttribute("errMessage", "Internal error " + e.getMessage() + ". Sorry.");
//            System.out.println(e.getMessage());
//        }
//
//        model.addAttribute("back", a_id + "/lotList");
//        return "lot";
//    }

    @RequestMapping(value = "/{a_id}/saveLot", method = RequestMethod.GET)
    public String lotInfo(@ModelAttribute("user") User user, @PathVariable("a_id") Integer a_id,
                          ModelMap model, ServletRequest request){
        String[] parameters = request.getParameterValues("items");
        int[] ids = new int[parameters.length];
        for(int i = 0; i < parameters.length; i++){
            ids[i] = Integer.parseInt(parameters[i]);
        }

        try {
            LotInfo newLot = lotService.createLot(a_id, request.getParameter("name"), Double.parseDouble(request.getParameter("price")), Double.parseDouble(request.getParameter("min_price")), ids);
            return "redirect:/" + a_id + "/" + newLot.getID() + "/lotInfo";
        }catch(AuctionException e){
            model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
            System.out.println(e.getMessage());
            //mb return to error page
        } catch (Exception e) {
            model.addAttribute("errMessage", "Internal error " + e.getMessage() + ". Sorry.");
            System.out.println(e.getMessage());
            //mb return to error page
        }

        //model.addAttribute("back", a_id + "/lotList");
        return "error";
    }

    @RequestMapping(value = "/{a_id}/{l_id}/lotInfo", method = RequestMethod.GET)
    public String lotInfo(ModelMap model, @ModelAttribute("user") User user, @PathVariable("a_id") Integer a_id, @PathVariable("l_id") Integer l_id) {
        Lot lot = lotService.getLotById(l_id);
        //TODO add list of items as attribute
        model.addAttribute("ID", lot.getID());
        model.addAttribute("name", lot.getName());
        model.addAttribute("price", lot.getCurrentPrice());
        model.addAttribute("items", itemService.getItemsByLot(l_id));
        model.addAttribute("back", a_id + "/lotList");
        //model.addAttribute("min_price", lot.getMin_price());
        //model.addAttribute("description", lot.getDescription());
        //model.addAttribute("auction", lot.getAuction().getId());
        return "lot";
    }

    @RequestMapping(value = "/{a_id}/deleteLot", method = RequestMethod.GET)
    public ModelAndView deleteLot(@ModelAttribute("user") User user, @PathVariable("a_id") Integer a_id) {
        ModelAndView mv = new ModelAndView("deleteLot");
        try {
            AuctionInfo auc = auctionService.getAuctionWithLots(a_id);
            //List<Lot> lots = lotService.getLotsByAuctionId(a_id);
            mv.addObject("auction", auc);
        }catch (Exception e){
            mv.addObject("errMessage", "Internal error " + e.getMessage()+ ". Sorry.");
        }

        //mv.addObject("num", new IntegerWrapper());
        mv.addObject("back", a_id + "/lotList");
        return mv;
    }

    @RequestMapping(value = "/{a_id}/deleteLotPart2", method = RequestMethod.GET)
    public String itemInfo(@ModelAttribute("user") User user, @PathVariable("a_id") Integer a_id,
                           ModelMap model, ServletRequest request) {
        int num = Integer.parseInt(request.getParameter("lot"));

        try {
            Lot lot = lotService.getLotById(num);
            //TODO check if user is admin?
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

        //model.addAttribute("back", "freeItems");

        return "redirect:/" + a_id + "/lotList";
    }

    @RequestMapping(value = "/allLotList", method = RequestMethod.GET)
    public String watchAllLots(ModelMap model, @ModelAttribute("user") User user) {
        //AuctionInfo auc = auctionService.getAuctionWithLots(id);
        List<Lot> lots = lotService.getAllLots();
        model.addAttribute("lots", lots);
        return "allLotList";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchLots(ModelMap model, @ModelAttribute("user") User user, ServletRequest request) {
        String searchStr = request.getParameter("s");
        List<Lot> lots = lotService.getLotsBySearch(searchStr);
        model.addAttribute("lots", lots);
        model.addAttribute("searchStr", searchStr);
        return "search";
    }
}
