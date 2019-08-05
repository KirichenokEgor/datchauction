package by.students.grsu.controller;

import by.students.grsu.entities.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class IntegerWrapper{
    Integer value;
    IntegerWrapper(){
        value = 1;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}

@Controller
@SessionAttributes("user")
public class MyController {
    List<Auction> aucs = new ArrayList<Auction>();
    List<Item> freeItems = new ArrayList<Item>();

    //alexey's code
    private Core core;
    public MyController(){
        System.out.println(" ===============================================================================================\n"+
                "|  ______             _          __             _                      _    _                   |\n"+
                "| |_   _ `.          / |_       [  |           / \\                    / |_ (_)                  |\n"+
                "|   | | `. \\ __   _ `| |-'.---.  | |--.       / _ \\    __   _   .---.`| |-'__   .--.   _ .--.   |\n"+
                "|   | |  | |[  | | | | | / /'`\\] | .-. |     / ___ \\  [  | | | / /'`\\]| | [  |/ .'`\\ \\[ `.-. |  |\n"+
                "|  _| |_.' / | \\_/ |,| |,| \\__.  | | | |   _/ /   \\ \\_ | \\_/ |,| \\__. | |, | || \\__. | | | | |  |\n"+
                "| |______.'  '.__.'_/\\__/'.___.'[___]|__] |____| |____|'.__.'_/'.___.'\\__/[___]'.__.' [___||__] |\n"+
                " ===============================================================================================");
        System.out.println("Initializing...");
        System.out.println("Controller: OK");
        core = Core.Initialize();
        System.out.println("Core: OK");
    }
    @GetMapping({"/login","/"})
    public ModelAndView login() {
        TempUser tu = new TempUser();
        ModelAndView mv = new ModelAndView("login");
        mv.addObject("loginMessage", "");
        mv.addObject("tempUser", tu);
        return mv;
    }
    @PostMapping("/confirmLogin")
    public String confirmLogin(@ModelAttribute("tempUser") TempUser tu, Model model) {
        try {
            model.addAttribute("user", core.login(tu.getEmail(),tu.getPassword()));
            //return "index";
            return "redirect:/home";
        } catch (AuctionException e) {
            if(e.getCode()==11 || e.getCode()==12){
                model.addAttribute("loginMessage","Wrong email or password. Try again");
            }else model.addAttribute("loginMessage","Internal error. Sorry about that. Try again later");
        }
        return "login";
    }
    @RequestMapping("/registration")
    public String Registration(Model model){
        TempUser tu = new TempUser();
        model.addAttribute("tempUser", tu);
        model.addAttribute("registrationMessage", "Please enter all fields correctly");
        return "registration";
    }
    @RequestMapping("/confirmRegistration")
    public String confirmRegistration(@ModelAttribute("tempUser")TempUser tu, Model model){
        //TODO request registration
        if(isRegistrationValid(tu)) {
            try {
                model.addAttribute("user",core.registration(tu.getEmail(),tu.getPassword(),tu.getUsername()));
//                return "index";
                return "redirect:/home";
            } catch (AuctionException e) {
                if(e.getCode()==13)model.addAttribute("registrationMessage", "This email is already in use!");
                if(e.getCode()==14)model.addAttribute("registrationMessage", "This nickname is already in use. Please create another one");
            }
        }
        else model.addAttribute("registrationMessage", "Password and confirm not match.");
        return "registration";
    }
    private boolean isRegistrationValid(TempUser tu){
        if(!tu.getPassword().equals(tu.getConfirmPassword()))return false;
        //TODO check nickname,email,passwordDifficult...
        return true;
    }
    ///////////////////end alexey's code


    @RequestMapping(value = "/addItem", method = RequestMethod.GET)
    public ModelAndView addItem(@ModelAttribute("user") User user) {
        ModelAndView mv = new ModelAndView("addItem");
        mv.addObject("item", new Item());
        mv.addObject("back", "freeItems");
        return mv;
    }

    @RequestMapping(value = "/saveItem", method = RequestMethod.POST)
    public String itemInfo(@ModelAttribute("item") Item item, @ModelAttribute("user") User user,
                          ModelMap model) {
        //there should be adding item to the db
        try {
            item = core.getIM().addItem(item.getName(),item.getDescription(), item.getStatusAsString(), user.getUsername());
            //model.addAttribute("item", item);
        }catch (SQLException e){
            model.addAttribute("errMessage", "SQLError. Sorry." + e.getSQLState() + "\n" + e.getErrorCode());
        }catch (AuctionException e){
            model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
        }

        model.addAttribute("ID", item.getId());
        model.addAttribute("name", item.getName());
        model.addAttribute("description", item.getDescription());
        model.addAttribute("status", item.getStatus());
        model.addAttribute("owner", item.getOwner());

        model.addAttribute("back", "freeItems");

        return "item";
    }

    @RequestMapping(value = "/{a_id}/addLot", method = RequestMethod.GET)
    public ModelAndView addLot(@PathVariable("a_id") Integer a_id, @ModelAttribute("user") User user) {
        ModelAndView mv = new ModelAndView("addLot");
//        mv.addObject("auction", aucs.get(id-1));
        mv.addObject("a_id", a_id);
        mv.addObject("lot", new Lot());
        mv.addObject("back", a_id + "/lotList");
        return mv;
    }

    @RequestMapping(value = "/{a_id}/saveLot", method = RequestMethod.POST)
    public String lotInfo(@ModelAttribute("lot") Lot lot,  @ModelAttribute("user") User user, @PathVariable("a_id") Integer a_id,
                          ModelMap model) {
        Auction auc = aucs.get(a_id-1);
        if(lot.getAuction() == null) lot.setAuction(auc);
        //there should be adding lot to the db : or better modifying existing auction in db

        lot.setId(lot.getId() - 1);
        Lot.setMinFreeId(Lot.getMinFreeId() - 1);
        auc.addLot(lot);
        //TODO add list of items as attribute
        model.addAttribute("ID", lot.getId());
        model.addAttribute("name", lot.getName());
        model.addAttribute("price", lot.getPrice());
        model.addAttribute("min_price", lot.getMin_price());
        model.addAttribute("description", lot.getDescription());
        model.addAttribute("auction", lot.getAuction().getId());

        model.addAttribute("back", "${a_id}/lotList");

        return "lot";
    }

    @RequestMapping(value = "/addAuction", method = RequestMethod.GET)
    public ModelAndView addAuction(@ModelAttribute("user") User user) {
        ModelAndView mv = new ModelAndView("addAuction");
        mv.addObject("auc", new Auction());
        mv.addObject("back", "auctionList");
        return mv;
    }

    @RequestMapping(value = "/saveAuction", method = RequestMethod.POST)
    public String auctionInfo(@ModelAttribute("auc") Auction auc,  @ModelAttribute("user") User user,
                          ModelMap model) {
        auc.setId(auc.getId() - 1);
        Auction.setMinFreeId(Auction.getMinFreeId() - 1);
        //задница какая-то: в 2 раза больше работы, т.к. создаются 2 разных auc
        aucs.add(auc);
        //TODO adding auc to the db
        model.addAttribute("id", auc.getId());
        model.addAttribute("start_time", auc.getStart_time());
        model.addAttribute("durationMin", auc.getDurationMin());

        model.addAttribute("back", "auctionList");
        return "auction";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(ModelMap model, @ModelAttribute("user") User user) {
        model.addAttribute("user", user);
        return "index";
    }

    @RequestMapping(value = "/thanks", method = RequestMethod.GET)
    public String thanks(ModelMap model, @ModelAttribute("lot") Lot lot, @ModelAttribute("user") User user) {
        model.addAttribute("lot", lot);
        return "thanks";
    }

    @RequestMapping(value = "/sorry", method = RequestMethod.GET)
    public String sorry(ModelMap model, @ModelAttribute("user") User user) {
        return "sorry";
    }

    @RequestMapping(value = "/lotInfo", method = RequestMethod.GET)
    public String lotInfo(ModelMap model, @ModelAttribute("lot") Lot lot, @ModelAttribute("user") User user) {

        //TODO add list of items as attribute
        model.addAttribute("ID", lot.getId());
        model.addAttribute("name", lot.getName());
        model.addAttribute("price", lot.getPrice());
        model.addAttribute("min_price", lot.getMin_price());
        model.addAttribute("description", lot.getDescription());
        model.addAttribute("auction", lot.getAuction().getId());
        return "lot";
    }

    @RequestMapping(value = "/auctionList", method = RequestMethod.GET)
    public String watchAuctions(ModelMap model, @ModelAttribute("user") User user) {
//        TODO fetch auctions from DB
//        List<Auction> auctions = new ArrayList<Auction>();
        if(aucs.size() < 10) for(int i = 0; i < 10; i++) aucs.add(new Auction());
        model.addAttribute("auctions", aucs);
//        model.addAttribute();
        return "auctionList";
    }

    @RequestMapping(value = "/{id}/lotList", method = RequestMethod.GET)
    public String watchLots(ModelMap model, @PathVariable("id") Integer id, @ModelAttribute("user") User user/*, @ModelAttribute("auction") Auction auc*/) {
        //TODO fetch auc from db by id
        Auction auc = aucs.get(id-1);
        model.addAttribute("auction", auc);
        return "lotList";
    }

    @RequestMapping(value = "/freeItems", method = RequestMethod.GET)
    public String watchFreeItems(ModelMap model, @ModelAttribute("user") User user) {
        try {
            List<Item> items = core.getIM().getItemsByOwner(user.getUsername());
            model.addAttribute("items", items);
        }catch (SQLException e){
            model.addAttribute("errMessage", "SQLError. Sorry." + e.getSQLState() + "\n" + e.getErrorCode());
        }catch (AuctionException e){
            model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
        }
        return "freeItemList";
    }

    @RequestMapping(value = "/finish", method = RequestMethod.GET)
    public String goodbye(@ModelAttribute User user, SessionStatus status) {
        /**
         * store User ...
         */
        status.setComplete();
        return "redirect:/login";
    }

    @RequestMapping(value = "/deleteItem", method = RequestMethod.GET)
    public ModelAndView deleteItem(@ModelAttribute("user") User user) {
        ModelAndView mv = new ModelAndView("deleteItem");
        try {
            List<Item> items = core.getIM().getItemsByOwner(user.getUsername());
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
            Item item = core.getIM().getItemById(num.getValue());
            if(!item.getOwner().equals(user.getUsername())){
                model.addAttribute("errMessage", "Sorry, you can't delete this item, because it's not yours.");
                List<Item> items = core.getIM().getItemsByOwner(user.getUsername());
                model.addAttribute("items", items);
                return "deleteItem";
            }
            core.getIM().deleteItemById(num.getValue());
            //model.addAttribute("item", item);
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
}