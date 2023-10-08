package me.krob.controller;

import me.krob.model.User;
import me.krob.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public ModelAndView all() {
        List<User> users = userRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("all");
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView register() {
        User user = new User();
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult result, Model model) {
        return "redirect:/register";
    }
}
