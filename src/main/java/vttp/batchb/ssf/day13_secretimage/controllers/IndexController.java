package vttp.batchb.ssf.day13_secretimage.controllers;

import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import vttp.batchb.ssf.day13_secretimage.models.User;

@Controller
@RequestMapping(path = {"/", "/index.html"})
public class IndexController {
    
    @GetMapping
    public String getLogin(Model model, HttpSession sess){
        
        // initialise or retrieve the number of attempts
        Integer attempt = (Integer) sess.getAttribute("attempt");
        if (attempt == null) {
            attempt = 0;
            sess.setAttribute("attempt", attempt);
        } else {
            attempt = 0;
            sess.setAttribute("attempt", attempt);
        }

        // initialise or retrieve the captcha
        String generatedCaptcha = (String) sess.getAttribute("generatedcaptcha");
        if (generatedCaptcha == null) {
            generatedCaptcha = generateCaptcha();
            sess.setAttribute("generatedcaptcha", generatedCaptcha);
        } else {
            generatedCaptcha = generateCaptcha();
            sess.setAttribute("generatedcaptcha", generatedCaptcha);
        }

        model.addAttribute("user", new User());
        model.addAttribute("attempt", attempt);
        model.addAttribute("generatedcaptcha", generatedCaptcha);

        return "index";
    }

    private String generateCaptcha(){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();

        Random random = new Random();
        int length = 7;

        for (int i = 0; i < length; i++){
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    @PostMapping
    public String postLogout(HttpSession sess, Model model) {

        sess.invalidate();

        model.addAttribute("attempt", 0);
        return "index";
    }
}
