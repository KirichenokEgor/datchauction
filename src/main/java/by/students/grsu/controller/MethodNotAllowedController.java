package by.students.grsu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;

@Controller
public class MethodNotAllowedController {

    @PostConstruct
    private void postConstructor(){
        System.out.println("MethodNotAllowedController: OK");
    }


    @RequestMapping(value = { "/saveAuction", "/deleteAuctionPart2", "/{id}/lotList", "/{id}/join",
            "/{id}/makePlanned", "/{id}/subscribe", "/{id}/unsubscribe", "/saveItem", "/deleteItemPart2",
            "/confirmRegistration", "/{a_id}/addLot", "/{a_id}/saveLot", "/{a_id}/{l_id}/lotInfo",
            "/{a_id}/deleteLot", "/{a_id}/deleteLotPart2", "/confirmDeal/{l_id}" }, method = RequestMethod.GET)
    public String watchSoldLots() {
        return "redirect:/accessDenied";
    }
}
