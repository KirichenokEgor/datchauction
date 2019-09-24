package by.students.grsu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @PostConstruct
    private void postConstructor(){
        System.out.println("HomeController: OK");
    }

    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")//does not work
    @RequestMapping(value = {"/home", "/"}, method = RequestMethod.GET)
    //@Secured("ROLE_ADMIN")// does not work
    //@RolesAllowed("ROLE_ADMIN") //works with javax.javaee-api
    //@PreAuthorize("hasRole('ROLE_ADMIN')") does not work
    //@PreAuthorize("1 != 1") does not work
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public String accessDenied(){
        return "accessDenied";
    }

    @RequestMapping("/userManager")
    public String userManager(HttpServletRequest request){
        if(request.isUserInRole("ADMIN"))
            return "userManager";
        else return "error";
    }

    @RequestMapping(value = "/changeLocale", method = RequestMethod.POST)
    public String changeLocale(HttpServletRequest request){
        String lang = request.getParameter("lang");
        String referer = request.getHeader("Referer");
        if(referer.indexOf("?") != -1)
            referer = referer.replace(referer.substring(referer.indexOf("?")), "");
        if(referer.endsWith("/lotList") || referer.endsWith("/addLot") || referer.endsWith("/lotInfo") || referer.endsWith("/deleteLot"))
            request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        if(!lang.equals("ru") && !lang.equals("en"))
            return "redirect:" + referer;
        return "redirect:" + referer + "?lang=" + lang;
    }

}