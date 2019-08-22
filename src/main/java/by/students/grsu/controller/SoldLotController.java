package by.students.grsu.controller;

import by.students.grsu.entities.lot.SoldLot;
import by.students.grsu.entities.services.SoldLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SoldLotController {
    SoldLotService soldLotService;

    @Autowired
    public void setSoldLotService(SoldLotService soldLotService) {
        this.soldLotService = soldLotService;
    }

    @PostConstruct
    private void postConstructor(){
        System.out.println("SoldLotService: OK");
    }

    @RequestMapping(value = "/soldLotList", method = RequestMethod.GET)
    public String watchSoldLots(ModelMap model, HttpServletRequest request) {
        List<SoldLot> soldLots = soldLotService.getSoldLotsByUser(request.getRemoteUser());
        model.addAttribute("lots", soldLots);
        return "purchase";
    }
}
