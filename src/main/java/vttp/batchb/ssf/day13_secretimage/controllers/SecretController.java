package vttp.batchb.ssf.day13_secretimage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp.batchb.ssf.day13_secretimage.models.User;

@Controller
@RequestMapping
public class SecretController {

    private static final String PASSWORD = "SecretImage123";

    @PostMapping("/secret")
    public String postSecret(@Valid @ModelAttribute User user,
            BindingResult bindings, Model model, HttpSession sess) {

        int attempt = (int) sess.getAttribute("attempt");

        // check if there are validation errors
        if (bindings.hasErrors()) {
            model.addAttribute("attempt", attempt);
            return "index";
        }

        // check if authentication is successful
        if (!user.getPassword().equals(PASSWORD)) {

            attempt += 1;
            sess.setAttribute("attempt", attempt);

            if (attempt == 3) {
                String generatedCaptcha = (String) sess.getAttribute("generatedcaptcha");
                model.addAttribute("generatedcaptcha", generatedCaptcha);
                model.addAttribute("attempt", attempt);
                return "captcha";
            }

            FieldError err = new FieldError("user", "password", "Incorrect password");
            bindings.addError(err);
            model.addAttribute("attempt", attempt);
            
            return "index";
        }

        sess.setAttribute("attempt", 0);

        return "secret";

    }

    @PostMapping("/captcha")
    public String postCaptcha(Model model, @RequestBody MultiValueMap<String, String> form, HttpSession sess){

        String captcha = form.getFirst("captcha");
        String password = form.getFirst("password");
        String generatedCaptcha = (String) sess.getAttribute("generatedcaptcha");
        if (captcha.equalsIgnoreCase(generatedCaptcha) && password.equals(PASSWORD)){
            return "secret";
        }

        return "locked";

    }

}
