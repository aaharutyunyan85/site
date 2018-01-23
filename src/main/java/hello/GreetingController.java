package hello;

import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;

@Controller
public class GreetingController {

    @Autowired
    private UserRepository userRepostory;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    @RequestMapping("/hello")
    public String hello(){
return "hello";
    }

    @RequestMapping(value="/register",method= RequestMethod.GET)
    public String register(){
        return "register";
    }
    @RequestMapping(value="/login",method= RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value="/login",method= RequestMethod.POST)
    public String loginpost(@RequestParam(value="email") String email,
                               @RequestParam(value="pass") String pass) {
            System.out.println(userRepostory.getByEmail(email));
        return null;
    }

    @RequestMapping(value="/register",method= RequestMethod.POST)
    public String registerpost(@RequestParam(value="fname") String fname,
                               @RequestParam(value="lname") String lname,
                               @RequestParam(value="email") String email,
                               @RequestParam(value="pass") String pass,
                               @RequestParam(value="passrep") String passrep) {
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
