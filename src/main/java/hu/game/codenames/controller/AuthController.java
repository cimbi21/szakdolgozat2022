package hu.game.codenames.controller;

import hu.game.codenames.service.MyUserDetailsService;
import hu.game.codenames.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthController {


    @Autowired
    private MyUserDetailsService MyUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginForm(User user,Model model){
        return "login";
    }

    @PostMapping("/login-error")
    public String loginError(Model model, User user){
        model.addAttribute("loginError", true);
        model.addAttribute("user", user);
        return "login";
    }

    @GetMapping("/signup")
    public String registerForm(User user, Model model){
        return "signup";
    }

    @PostMapping("/signup")
    public String register(@Valid User user, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "signup";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        MyUserDetailsService.registerUser(user);
        return "redirect:/login";
    }

}