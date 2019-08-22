package by.students.grsu.controller;

import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.item.ItemInfo;
import by.students.grsu.entities.item.TempItem;
import by.students.grsu.entities.services.AuctionException;
import by.students.grsu.entities.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import java.sql.SQLException;
import java.util.List;

@Controller
//@SessionAttributes("user")
public class ItemController {
    private ItemService itemService;
    //private SecurityContextHolderAwareRequestWrapper contextHolder;

    @PostConstruct
    private void postConstructor(){
        System.out.println("ItemService: OK");
    }

    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

//    @Autowired
//    public void setSecurityContextHolderAwareRequestWrapper(SecurityContextHolderAwareRequestWrapper contextHolder) {
//        this.contextHolder = contextHolder;
//    }

    @RequestMapping(value = "/addItem", method = RequestMethod.GET)
    public ModelAndView addItem() {
        ModelAndView mv = new ModelAndView("addItem");
        mv.addObject("item", new TempItem());
        mv.addObject("back", "freeItems");
        return mv;
    }

    @RequestMapping(value = "/saveItem", method = RequestMethod.POST)
    public String itemInfo(@ModelAttribute("item") TempItem item, SecurityContextHolderAwareRequestWrapper contextHolder,
                           ModelMap model) {
        //TODO fetch user from Security
        //mb add item itself?
        try {
            ItemInfo newItem = itemService.getItemById(itemService.addItem(item.getName(),item.getDescription(), contextHolder));
            model.addAttribute("ID", newItem.getID());
            model.addAttribute("name", newItem.getName());
            model.addAttribute("description", newItem.getDescription());
            model.addAttribute("status", newItem.getStatus());
            model.addAttribute("owner", newItem.getOwner());
            //model.addAttribute("item", item);
//        }catch (SQLException e){
//            model.addAttribute("errMessage", "SQLError. Sorry." + e.getSQLState() + "\n" + e.getErrorCode());
//            System.out.println(e.getMessage());
        }catch (Exception e){
            model.addAttribute("errMessage", "Internal error " + e.getMessage() + ". Sorry.");
            System.out.println(e.getMessage());
        }
        model.addAttribute("back", "freeItems");

        return "item";
    }

    @RequestMapping(value = "/deleteItem", method = RequestMethod.GET)
    public ModelAndView deleteItem(SecurityContextHolderAwareRequestWrapper contextHolder) {
        ModelAndView mv = new ModelAndView("deleteItem");
        try {
            //List<Item> items = itemService.getItemsByOwner(user);
            List<Item> items = itemService.getFreeItemsByOwner(contextHolder);
            mv.addObject("items", items);
        }catch (Exception e){
            mv.addObject("errMessage", "Internal error " + e.getMessage()+ ". Sorry.");
        }

        mv.addObject("back", "freeItems");
        return mv;
    }

    @RequestMapping(value = "/deleteItemPart2", method = RequestMethod.GET)
    public String itemInfo(ModelMap model, ServletRequest request, Authentication authentication, SecurityContextHolderAwareRequestWrapper contextHolder) {
        int num = Integer.parseInt(request.getParameter("item"));

        try {
            ItemInfo item = itemService.getItemById(num);
            if(!item.getOwner().equals(contextHolder.getRemoteUser())){
                model.addAttribute("errMessage", "Sorry, you can't delete this item, because it's not yours.");
                List<Item> items = itemService.getItemsByOwner(contextHolder);
                model.addAttribute("items", items);
                return "deleteItem";
            }
            itemService.deleteItemById(num);
        }catch (SQLException e){
            model.addAttribute("errMessage", "SQLError. Sorry." + e.getSQLState() + "\n" + e.getErrorCode());
            return "deleteItem";
        }catch (AuctionException e){
            model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
            return "deleteItem";
        }catch (Exception e){
            model.addAttribute("errMessage", "Internal error " + e.getMessage()+ ". Sorry.");
            return "deleteItem";
        }

        return "redirect:/freeItems";
    }

    @RequestMapping(value = "/freeItems", method = RequestMethod.GET)
    public String watchFreeItems(ModelMap model, Authentication authentication, SecurityContextHolderAwareRequestWrapper contextHolder) {
        try {
            List<Item> items = itemService.getFreeItemsByOwner(contextHolder);
            model.addAttribute("items", items);
        }catch (Exception e){
            model.addAttribute("errMessage", "Internal error " + e.getMessage()+ ". Sorry.");
        }

        return "freeItemList";
    }
}
