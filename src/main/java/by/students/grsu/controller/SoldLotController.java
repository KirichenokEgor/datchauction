package by.students.grsu.controller;

import by.students.grsu.entities.lot.SoldLot;
import by.students.grsu.entities.services.interfaces.SoldLotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SoldLotController {
    SoldLotService soldLotService;

    public SoldLotController(SoldLotService soldLotService){
        this.soldLotService = soldLotService;
    }

    @PostConstruct
    private void postConstructor(){
        System.out.println("SoldLotController: OK");
    }

    @RequestMapping(value = "/soldLotList", method = RequestMethod.GET)
    public String watchSoldLots(ModelMap model, HttpServletRequest request) {
        List<SoldLot> soldLots = soldLotService.getSoldLotsByUser(request.getRemoteUser());
        model.addAttribute("lots", soldLots);
        return "purchase";
    }
    @RequestMapping(value = "/confirmDeal/{l_id}", method = RequestMethod.GET)
    public String confirmDeal(ModelMap model, @PathVariable int l_id){
        soldLotService.tradeComplete(l_id);
        return "redirect:/soldLotList";
    }
}
