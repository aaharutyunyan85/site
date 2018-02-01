package hello;

import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SessionAttributes("user")
@Controller
public class GreetingController {

    @Autowired
    private UserRepository userRepostory;

    @ModelAttribute
    public User initSession(){
        return new User();
    }

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @RequestMapping("/admin")
    public String admin(Model model) {
        List<User> list = userRepostory.getAllUsers();
        model.addAttribute("list", list);
        return "admin";
    }


    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletResponse response) {
        response.addCookie(new Cookie("session","test"));
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginpost(@RequestParam(value = "email") String email,
                            @RequestParam(value = "pass") String pass,
                            @CookieValue("session") String cookie,
                            @ModelAttribute("user") User sessionuser) {
        //  System.out.println(userRepostory.getByEmail(email));
        if (userRepostory.existByEmail(email)) {
            User user = userRepostory.getByEmail(email);
            final String hashed = Hashing.sha256()
                    .hashString(pass, StandardCharsets.UTF_8)
                    .toString();
            if (hashed.equals(user.getPass())) {
                sessionuser.setEmail(user.getEmail());
                sessionuser.setId(user.getId());
                sessionuser.setFname(user.getFname());
                sessionuser.setLname(user.getLname());
                return "redirect:/home";
            } else {
                System.out.println("Wrong Pass");
            }
        } else {
            System.out.println("Wrong Email");
        }
        return "login";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Model model, @ModelAttribute("user") User sessionuser) {
        model.addAttribute("email",sessionuser.getEmail());
        return "home";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerpost(@RequestParam(value = "fname") String fname,
                               @RequestParam(value = "lname") String lname,
                               @RequestParam(value = "email") String email,
                               @RequestParam(value = "pass") String pass,
                               @RequestParam(value = "passrep") String passrep) {
        // System.out.println(fname+"  "+lname+"  "+email+"  "+pass+"  "+passrep+"  ");
        if (pass.equals(passrep)) {
            final String hashed = Hashing.sha256()
                    .hashString(pass, StandardCharsets.UTF_8)
                    .toString();
            userRepostory.create(new User(fname, lname, email, hashed));
            return "redirect:/login";
        } else {
            throw new IllegalArgumentException("Pass not same ");

        }
    }
}
