package by.students.grsu.controller;

import by.students.grsu.entities.lot.Lot;
import by.students.grsu.entities.users.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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
    //private Core core;
//    private UserService userService;
//    private ItemService itemService;
//    private LotService lotService;
//    private AuctionService auctionService;
//    private AuctionPlatform auctionPlatform;

//    public MyController(){
//        //System.out.println("Controller: OK");
//    }


//    private void postConstructor(){
//        System.out.println("Services: OK");
//    }

//    @GetMapping({"/login","/"})
//    public ModelAndView login(SessionStatus status) {
//        //status.setComplete();
//        TempUser tu = new TempUser();
//        ModelAndView mv = new ModelAndView("login");
//        mv.addObject("loginMessage", "");
//        mv.addObject("tempUser", tu);
//        return mv;
//    }
//
//    @PostMapping("/confirmLogin")
//    public String confirmLogin(@ModelAttribute("tempUser") TempUser tu, Model model) {
//        try {
//            model.addAttribute("user", userService.login(tu.getEmail(),tu.getPassword()));
//            return "redirect:/home";
//        } catch (AuctionException e) {
//            if(e.getCode()==11 || e.getCode()==12){
//                model.addAttribute("loginMessage","Wrong email or password. Try again");
//            }else model.addAttribute("loginMessage","Internal error. Sorry about that. Try again later");
//        }
//        return "login";
//    }
//
//    @RequestMapping(value = "/finish", method = RequestMethod.GET)
//    public String goodbye(@ModelAttribute User user, SessionStatus status) {
//        /**
//         * store User ...
//         */
//        status.setComplete();
//        return "redirect:/login";
//    }
//
//    ///////////////////////////////////////////
//
//    @RequestMapping("/registration")
//    public String Registration(Model model){
//        if(!model.containsAttribute("tempUser")) {
//            TempUser tu = new TempUser();
//            model.addAttribute("tempUser", tu);
//        }
//        model.addAttribute("registrationMessage", "Please enter all fields correctly");
//        return "registration";
//    }
//
//    @RequestMapping("/confirmRegistration")
//    public String confirmRegistration(@ModelAttribute("tempUser")TempUser tu, Model model){
//        //TODO request registration
//        if(isRegistrationValid(tu)) {
//            try {
//                model.addAttribute("user",userService.registration(tu.getEmail(),tu.getPassword(),tu.getUsername()));
//                return "index";
//            } catch (AuctionException e) {
//                if(e.getCode()==13)model.addAttribute("registrationMessage", "This email is already using!");
//                if(e.getCode()==14)model.addAttribute("registrationMessage", "This nickname is already using. Please create another one");
//            }
//        }
//        else model.addAttribute("registrationMessage", "Password and confirm not match.");
//        return "registration";
//    }
//
//    private boolean isRegistrationValid(TempUser tu){
//        if(!tu.getPassword().equals(tu.getConfirmPassword()))return false;
//        //TODO check nickname,email,passwordDifficult...
//        return true;
//    }
//
//    ////////////////////////////////////////////////
//
//    @RequestMapping(value = "/addItem", method = RequestMethod.GET)
//    public ModelAndView addItem(@ModelAttribute("user") User user) {
//        ModelAndView mv = new ModelAndView("addItem");
//        mv.addObject("item", new TempItem());
//        mv.addObject("back", "freeItems");
//        return mv;
//    }
//
//    @RequestMapping(value = "/saveItem", method = RequestMethod.POST)
//    public String itemInfo(@ModelAttribute("item") TempItem item, @ModelAttribute("user") User user,
//                          ModelMap model) {
//        //there should be adding item to the db
//        try {
//            ItemInfo newItem = itemService.addItem(item.getName(),item.getDescription(), user);
//            model.addAttribute("ID", newItem.getID());
//            model.addAttribute("name", newItem.getName());
//            model.addAttribute("description", newItem.getDescription());
//            model.addAttribute("status", newItem.getStatus());
//            model.addAttribute("owner", newItem.getOwner());
//            //model.addAttribute("item", item);
//        }catch (SQLException e){
//            model.addAttribute("errMessage", "SQLError. Sorry." + e.getSQLState() + "\n" + e.getErrorCode());
//            System.out.println(e.getMessage());
//        }catch (AuctionException e){
//            model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
//            System.out.println(e.getMessage());
//        }
//        model.addAttribute("back", "freeItems");
//
//        return "item";
//    }
//
//    @RequestMapping(value = "/deleteItem", method = RequestMethod.GET)
//    public ModelAndView deleteItem(@ModelAttribute("user") User user) {
//        ModelAndView mv = new ModelAndView("deleteItem");
//        try {
//            List<Item> items = itemService.getItemsByOwner(user);
//            mv.addObject("items", items);
//        }catch (SQLException e){
//            mv.addObject("errMessage", "SQLError. Sorry." + e.getSQLState() + "\n" + e.getErrorCode());
//        }catch (AuctionException e){
//            mv.addObject("errMessage", "Internal error " + e.getCode() + ". Sorry.");
//        }
//        //mv.addObject("items", )
//
//        mv.addObject("num", new IntegerWrapper());
//        mv.addObject("back", "freeItems");
//        return mv;
//    }
//
//    @RequestMapping(value = "/deleteItemPart2", method = RequestMethod.POST)
//    public String itemInfo(@ModelAttribute("num") IntegerWrapper num, @ModelAttribute("user") User user,
//                           ModelMap model) {
//        //TODO check if user is owner of this item
//        try {
//            ItemInfo item = itemService.getItemById(num.getValue());
//            if(!item.getOwner().equals(user.getUsername())){
//                model.addAttribute("errMessage", "Sorry, you can't delete this item, because it's not yours.");
//                List<Item> items = itemService.getItemsByOwner(user);
//                model.addAttribute("items", items);
//                return "deleteItem";
//            }
//            itemService.deleteItemById(num.getValue());
//        }catch (SQLException e){
//            model.addAttribute("errMessage", "SQLError. Sorry." + e.getSQLState() + "\n" + e.getErrorCode());
//            return "deleteItem";
//        }catch (AuctionException e){
//            model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
//            return "deleteItem";
//        }
//
//        model.addAttribute("back", "freeItems");
//
//        return "redirect:/freeItems";
//    }
//
//    @RequestMapping(value = "/freeItems", method = RequestMethod.GET)
//    public String watchFreeItems(ModelMap model, @ModelAttribute("user") User user) {
//        try {
//            List<Item> items = itemService.getItemsByOwner(user);
//            model.addAttribute("items", items);
//        }catch (SQLException e){
//            model.addAttribute("errMessage", "SQLError. Sorry." + e.getSQLState() + "\n" + e.getErrorCode());
//        }catch (AuctionException e){
//            model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
//        }
//
//        return "freeItemList";
//    }
//
//    //////////////////////////////////////////////////
//
//    @RequestMapping(value = "/{a_id}/addLot", method = RequestMethod.GET)
//    public ModelAndView addLot(@PathVariable("a_id") Integer a_id, @ModelAttribute("user") User user) {
//        ModelAndView mv = new ModelAndView("addLot");
////        mv.addObject("auction", aucs.get(id-1));
//        mv.addObject("a_id", a_id);
//        mv.addObject("lot", new TempLot());
//        mv.addObject("back", a_id + "/lotList");
//        return mv;
//    }
//
//    @RequestMapping(value = "/{a_id}/saveLot", method = RequestMethod.POST)
//    public String lotInfo(@ModelAttribute("lot") TempLot tempLot,  @ModelAttribute("user") User user, @PathVariable("a_id") Integer a_id,
//                          ModelMap model){
//        //LotInfo newLot=null;
//        try {
//            LotInfo newLot = lotService.createLot(a_id, tempLot.getName(), tempLot.getPrice(), tempLot.getMin_price());
//
//            model.addAttribute("ID", newLot.getID());
//            model.addAttribute("name", newLot.getName());
//            model.addAttribute("price", newLot.getCurrentPrice());
//        }catch(AuctionException e){
//            model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
//            System.out.println(e.getMessage());
//        }
//
//        model.addAttribute("back", a_id + "/lotList");
//        return "lot";
//    }
//
//    @RequestMapping(value = "/lotInfo", method = RequestMethod.GET)
//    public String lotInfo(ModelMap model, @ModelAttribute("lot") Lot lot, @ModelAttribute("user") User user) {
//
//        //TODO add list of items as attribute
//        model.addAttribute("ID", lot.getID());
//        model.addAttribute("name", lot.getName());
//        model.addAttribute("price", lot.getCurrentPrice());
//        //model.addAttribute("min_price", lot.getMin_price());
//        //model.addAttribute("description", lot.getDescription());
//        //model.addAttribute("auction", lot.getAuction().getId());
//        return "lot";
//    }
//
//    @RequestMapping(value = "/{id}/lotList", method = RequestMethod.GET)
//    public String watchLots(ModelMap model, @PathVariable("id") Integer id, @ModelAttribute("user") User user) {
//        AuctionInfo auc = auctionService.getAuctionInfo(id);
//        model.addAttribute("auction", auc);
//        return "lotList";
//    }
//
//    ///////////////////////////////////////////////////
//
//    @RequestMapping(value = "/addAuction", method = RequestMethod.GET)
//    public ModelAndView addAuction(@ModelAttribute("user") User user) {
//        ModelAndView mv = new ModelAndView("addAuction");
//        mv.addObject("auc", new TempAuction());
//        mv.addObject("back", "auctionList");
//        return mv;
//    }
//
//    @RequestMapping(value = "/saveAuction", method = RequestMethod.POST)
//    public String auctionInfo(@ModelAttribute("auc") TempAuction auc, @ModelAttribute("user") User user,
//                          ModelMap model) {
//        try {
//            AuctionInfo createdAuction = auctionService.getAuctionInfo
//                    (auctionService.addAuction(auc.getDescription(), auc.getMaxLots(), auc.getStartTime(), auc.getMaxDuration()));
//            model.addAttribute("id", createdAuction.getID());
//            model.addAttribute("start_time", createdAuction.getBeginTime());
//            model.addAttribute("maxDuration", createdAuction.getMaxDuration());
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//        }
//        model.addAttribute("back", "auctionList");
//        return "auction";
//    }
//
//    @RequestMapping(value = "/auctionList", method = RequestMethod.GET)
//    public String watchAuctions(ModelMap model, @ModelAttribute("user") User user) {
//        List<AuctionInfo> auctions = auctionService.getAuctions();
//        model.addAttribute("auctions", auctions);
//        return "auctionList";
//    }
//
//    @RequestMapping(value = "/deleteAuction", method = RequestMethod.GET)
//    public ModelAndView deleteAuction(@ModelAttribute("user") User user) {
//        //TODO check if user is admin
//        ModelAndView mv = new ModelAndView("deleteAuction");
//        List<AuctionInfo> auctions = auctionService.getAuctions();
//        mv.addObject("auctions", auctions);
//
//        mv.addObject("num", new IntegerWrapper());
//        mv.addObject("back", "auctionList");
//        return mv;
//    }
//
//    @RequestMapping(value = "/deleteAuctionPart2", method = RequestMethod.POST)
//    public String auctionInfo(@ModelAttribute("num") IntegerWrapper num, @ModelAttribute("user") User user,
//                              ModelMap model) {
//        //TODO check if user is owner of this item
////        try {
//            auctionService.deleteAuction(num.getValue());
////        }catch (SQLException e){
////            model.addAttribute("errMessage", "SQLError. Sorry. " + e.getMessage());
////            return "deleteAuction";
////        }catch(AuctionException e){
////            model.addAttribute("errMessage", "Internal error " + e.getCode() + ". Sorry.");
////        }
//
//        //model.addAttribute("back", "freeItems");
//
//        return "redirect:/auctionList";
//    }
//
    //////////////////////////////////////////////////////

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(ModelMap model, @ModelAttribute("user") User user) {
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


//    ////////////////////////////////////////////////////////
//
//    @Autowired
//    public void setUserService(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Autowired
//    public void setItemService(ItemService itemService) {
//        this.itemService = itemService;
//    }
//
//    @Autowired
//    public void setLotService(LotService lotService) {
//        this.lotService = lotService;
//    }
//
//    @Autowired
//    public void setAuctionService(AuctionService auctionService) {
//        this.auctionService = auctionService;
//    }
//
//    @Autowired
//    public void setAuctionPlatform(AuctionPlatform auctionPlatform) {
//        this.auctionPlatform = auctionPlatform;
//    }


}