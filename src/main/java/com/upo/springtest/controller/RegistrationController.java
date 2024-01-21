package com.upo.springtest.controller;

import com.upo.springtest.dto.EmployeeRegisterDto;
import com.upo.springtest.dto.UserDto;
import com.upo.springtest.dto.UserRegisterDto;
import com.upo.springtest.model.User;
import com.upo.springtest.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {
    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String register(Model model, UserRegisterDto userRegisterDto) {
        model.addAttribute("user", userRegisterDto);
        return "customer/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid UserRegisterDto userRegisterDto, BindingResult result) {

        // Check if user of given username already exists
        if(!registrationService.checkIfUsernameAvailable(userRegisterDto.getUsername())){
            result.rejectValue("username", null,
                    "Podana nazwa użytkownika zajęta");
        }

        // Check if given email is not already registered
        if(!registrationService.checkIfEmailAvailable(userRegisterDto.getEmail())){
            result.rejectValue("email", null,
                    "Podany adres email jest już zarejestrowany");
        }

        // Check if 2 given passwords match
        if(!registrationService.checkIfPasswordsMatch(userRegisterDto.getPassword(), userRegisterDto.getPasswordConfirm())){
            result.rejectValue("passwordConfirm", null,
                    "Hasła różnią się");
        }


        if (result.hasErrors()) {
            return "customer/register";
        }
        registrationService.registerUser(userRegisterDto);
        return "redirect:/login";
        //return "process-register";
    }

    @GetMapping("/registerEmployee")
    public String registerEmployee(Model model, EmployeeRegisterDto employeeRegisterDto) {
        model.addAttribute("employee", employeeRegisterDto);
        return "employee/register-employee";
    }

    @PostMapping("/registerEmployee")
    public String registerEmployee(@Valid EmployeeRegisterDto employeeRegisterDto, BindingResult result) {

        // Check if user of given username already exists
        if(!registrationService.checkIfUsernameAvailable(employeeRegisterDto.getUsername())){
            result.rejectValue("username", null,
                    "Podana nazwa użytkownika zajęta");
        }

        // Check if given email is not already registered
        if(!registrationService.checkIfEmailAvailable(employeeRegisterDto.getEmail())){
            result.rejectValue("email", null,
                    "Podany adres email jest już zarejestrowany");
        }

        // Check if 2 given passwords match
        if(!registrationService.checkIfPasswordsMatch(employeeRegisterDto.getPassword(), employeeRegisterDto.getPasswordConfirm())){
            result.rejectValue("passwordConfirm", null,
                    "Hasła różnią się");
        }


        if (result.hasErrors()) {
            return "employee/register-employee";
        }
        registrationService.registerEmployee(employeeRegisterDto);
        return "redirect:/employees";
    }


    @GetMapping("/users/update")
    public ModelAndView updateCustomer(@RequestParam long userId){

        UserDto userDto = registrationService.getUserDto(userId);
        if(!userDto.getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            throw new AccessDeniedException("Access denied");
        }


        ModelAndView mav = new ModelAndView("user/user-update");
        mav.addObject("userDto", userDto);
        mav.addObject("userId", userId);
        return mav;
    }

    @PostMapping("/users/update")
    public String updateCustomer(@RequestParam long userId, @Valid UserDto userDto, BindingResult result) {

        User user = registrationService.getUser(userId);

        if(!registrationService.checkIfUsernameAvailable(userDto.getUsername()) && !(user.getUsername().equals(userDto.getUsername()))){
            result.rejectValue("username", null,
                    "Podana nazwa użytkownika zajęta");
        }

        // Check if given email is not already registered
        if(!registrationService.checkIfEmailAvailable(userDto.getEmail()) && !(user.getEmail().equals(userDto.getEmail()))){
            result.rejectValue("email", null,
                    "Podany adres email jest już zarejestrowany");
        }

        // Check if 2 given passwords match
        if(!registrationService.checkIfPasswordsMatch(userDto.getPassword(), userDto.getPasswordConfirm())){
            result.rejectValue("passwordConfirm", null,
                    "Hasła różnią się");
        }

        if (result.hasErrors()) {
            return "redirect:/users/update?userId=" + userId;
        }

        registrationService.editUser(userDto, userId);
        return "redirect:/account";
    }


}
