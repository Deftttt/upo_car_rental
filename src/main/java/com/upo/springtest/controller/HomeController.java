package com.upo.springtest.controller;

import com.upo.springtest.model.Employee;
import com.upo.springtest.model.User;
import com.upo.springtest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    String homePage() {
        return "home/index";
    }

    @GetMapping("/info")
    String infoPage() {
        return "home/info";
    }

    @GetMapping("/contact")
    String contactPage() {
        return "home/contact";
    }

    @GetMapping("/dashboard")
    public ModelAndView getDashboard(){
        ModelAndView mav = new ModelAndView("home/dashboard");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"))){
            Employee employee = userService.getEmployeeByUsername(username);
            mav.addObject("employee", employee);
        }
        return mav;
    }
}
