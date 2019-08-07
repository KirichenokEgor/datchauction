package by.students.grsu.controller;

import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.item.ItemInfo;
import by.students.grsu.entities.item.TempItem;
import by.students.grsu.entities.services.AuctionException;
import by.students.grsu.entities.services.ItemService;
import by.students.grsu.entities.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

@Controller
@SessionAttributes("user")
public class ItemController {
    private ItemService itemService;

    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(value = "/addItem", method = RequestMethod.GET)
    public ModelAndView addItem(@ModelAttribute("user") User user) {
        ModelAndView mv = new ModelAndView("addItem");
        mv.addObject("item", new TempItem());
        mv.addObject("back", "freeItems");
        return mv;
    }

    @RequestMapping(value = "/saveItem", method = RequestMethod.POST)
    public String itemInfo(@ModelAttribute("item") TempItem item, @ModelAttribute("user") User user,
                           ModelMap model) {
        //there should be adding item to the db
        try {
            ItemInfo newItem = itemService.addItem(item.getName(),item.getDescription(), user);
            model.addAttribute("ID", newItem.getID());
            model.addAttribute("name", newItem.getName());
            model.addAttribute("description", newItem.getDescription());
            model.addAttribute("status", newItem.getStatus());
            model.addAttribute("owner", newItem.getOwner());
            //model.addAttribute("item", item);
        }catch (SQLException e){
            model.addAttribute("errMessage", "SQLError. Sorry." + e.getSQLState() + "\n" + e.getErrorCode());
            System.out.println(e.getMessage());
        }catch (AuctionException e){
            model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
            System.out.println(e.getMessage());
        }
        model.addAttribute("back", "freeItems");

        return "item";
    }

    @RequestMapping(value = "/deleteItem", method = RequestMethod.GET)
    public ModelAndView deleteItem(@ModelAttribute("user") User user) {
        ModelAndView mv = new ModelAndView("deleteItem");
        try {
            List<Item> items = itemService.getItemsByOwner(user);
            mv.addObject("items", items);
        }catch (SQLException e){
            mv.addObject("errMessage", "SQLError. Sorry." + e.getSQLState() + "\n" + e.getErrorCode());
        }catch (AuctionException e){
            mv.addObject("errMessage", "Internal error " + e.getCode() + ". Sorry.");
        }
        //mv.addObject("items", )

        mv.addObject("num", new IntegerWrapper());
        mv.addObject("back", "freeItems");
        return mv;
    }

    @RequestMapping(value = "/deleteItemPart2", method = RequestMethod.POST)
    public String itemInfo(@ModelAttribute("num") IntegerWrapper num, @ModelAttribute("user") User user,
                           ModelMap model) {
        //TODO check if user is owner of this item
        try {
            ItemInfo item = itemService.getItemById(num.getValue());
            if(!item.getOwner().equals(user.getUsername())){
                model.addAttribute("errMessage", "Sorry, you can't delete this item, because it's not yours.");
                List<Item> items = itemService.getItemsByOwner(user);
                model.addAttribute("items", items);
                return "deleteItem";
            }
            itemService.deleteItemById(num.getValue());
        }catch (SQLException e){
            model.addAttribute("errMessage", "SQLError. Sorry." + e.getSQLState() + "\n" + e.getErrorCode());
            return "deleteItem";
        }catch (AuctionException e){
            model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
            return "deleteItem";
        }

        model.addAttribute("back", "freeItems");

        return "redirect:/freeItems";
    }

    @RequestMapping(value = "/freeItems", method = RequestMethod.GET)
    public String watchFreeItems(ModelMap model, @ModelAttribute("user") User user) {
        try {
            List<Item> items = itemService.getItemsByOwner(user);
            model.addAttribute("items", items);
        }catch (SQLException e){
            model.addAttribute("errMessage", "SQLError. Sorry." + e.getSQLState() + "\n" + e.getErrorCode());
        }catch (AuctionException e){
            model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
        }

        return "freeItemList";
    }
}
