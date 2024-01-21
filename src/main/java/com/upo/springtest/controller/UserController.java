package com.upo.springtest.controller;


import com.upo.springtest.dto.CarDto;
import com.upo.springtest.dto.EmployeeDto;
import com.upo.springtest.model.*;
import com.upo.springtest.service.EmailService;
import com.upo.springtest.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    // Employee

    @GetMapping("/employees")
    public ModelAndView getEmployees(){
        List<Employee> employees = userService.getEmployees();
        ModelAndView mav = new ModelAndView("employee/employee-list");;
        mav.addObject("employees", employees);
        return mav;
    }



    @GetMapping("/employees/{id}")
    public ModelAndView getSingleEmployee(@PathVariable long id){
        Employee employee = userService.getSingleEmployee(id);
        ModelAndView mav = new ModelAndView("employee/employee-details");;
        mav.addObject("employee", employee);
        return mav;
    }

    @PostMapping("/employees/delete")
    public String deleteCarModel(@RequestParam long id){
        userService.deleteEmployee(id);
        return "redirect:/employees";
    }

    @GetMapping("/employees/update")
    public ModelAndView editEmployee(@RequestParam long employeeId){
        ModelAndView mav = new ModelAndView("employee/employee-update");
        EmployeeDto employeeDto = userService.getSingleEmployeeDto(employeeId);
        mav.addObject("employeeDto", employeeDto);
        mav.addObject("employeeId", employeeId);
        return mav;
    }

    @PostMapping("/employees/update")
    public String editEmployee(@RequestParam long employeeId, @Valid EmployeeDto employeeDto, BindingResult result, Model model){
        if (result.hasErrors()) {
            model.addAttribute("employeeId", employeeId);
            return "employee/employee-update";
        }
        Employee employee = userService.editEmployee(employeeDto, employeeId);
        return "redirect:/employees/" + employee.getId();
    }


    // Customer

    @GetMapping("/customers")
    public ModelAndView getCustomers(){
        List<Customer> customers = userService.getCustomers();
        ModelAndView mav = new ModelAndView("customer/customer-list");;
        mav.addObject("customers", customers);
        return mav;
    }

    @GetMapping("/customers/{id}")
    public ModelAndView getSingleCustomer(@PathVariable long id){
        Customer customer = userService.getSingleCustomer(id);
        ModelAndView mav = new ModelAndView("customer/customer-details");;
        mav.addObject("customer", customer);
        return mav;
    }



    @GetMapping("/account")
    public ModelAndView getLoggedInDetails(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        ModelAndView mav = new ModelAndView("user/user-details");
        mav.addObject("user", user);

        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("CUSTOMER"))){
            Customer customer = userService.getCustomerByUsername(username);
            mav.addObject("customerId", customer.getId());
        }

        return mav;
    }




}
