package chewyt.WeatherAPI.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/")
public class indexController {
    
    @GetMapping
    public String getHome(){
        return "index";
    }
}
