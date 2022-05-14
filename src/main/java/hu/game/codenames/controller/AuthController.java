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
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AuthController {

    @Autowired
    private MyUserDetailsService MyUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginForm(User user, Model model){
        model.addAttribute("loginError", false);
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
    public String register(@Valid User user, BindingResult bindingResult){
        Pattern p = Pattern.compile("[^a-z0-9 ]",Pattern.CASE_INSENSITIVE);
        Matcher special = p.matcher(user.getPassword());
        Pattern p2 = Pattern.compile("[0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher number = p2.matcher(user.getPassword());
        if(bindingResult.hasErrors() || !user.getPassword().equals(user.getPassword2()) || !(user.getPassword().length()>=8 && user.getPassword().length()<=20) || !special.find() || !number.find() || MyUserDetailsService.usernameCheck(user.getUsername())){
            return "signup";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword2(passwordEncoder.encode(user.getPassword()));
        MyUserDetailsService.registerUser(user);
        return "redirect:/mygames";
    }

}