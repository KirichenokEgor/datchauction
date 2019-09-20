package by.students.grsu.controller;

import by.students.grsu.entities.lot.SoldLot;
import by.students.grsu.entities.services.interfaces.SoldLotService;
import by.students.grsu.entities.services.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SoldLotController {
    SoldLotService soldLotService;
    UserService userService;

    public SoldLotController(SoldLotService soldLotService, UserService userService){
        this.soldLotService = soldLotService;
        this.userService = userService;
    }

    @PostConstruct
    private void postConstructor(){
        System.out.println("SoldLotController: OK");
    }

    @RequestMapping(value = "/soldLotList", method = RequestMethod.GET)
    public String watchSoldLots(ModelMap model, HttpServletRequest request) {
        List<SoldLot> soldLots = soldLotService.getSoldLotsByUser(request.getRemoteUser());
        Map<SoldLot, String> slmap = new HashMap<>();
        for(SoldLot lot : soldLots) slmap.put(lot, userService.getEmail(lot.getSellerUsername()));
        model.addAttribute("lots", slmap);
        return "purchase";
    }
    @RequestMapping(value = "/confirmDeal/{l_id}", method = RequestMethod.POST)
    public String confirmDeal(ModelMap model, @PathVariable int l_id){
        soldLotService.tradeComplete(l_id);
        return "redirect:/soldLotList";
    }
}
