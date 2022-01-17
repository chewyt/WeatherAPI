package chewyt.WeatherAPI.controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import chewyt.WeatherAPI.WeatherApiApplication;
import chewyt.WeatherAPI.model.Weather;
import chewyt.WeatherAPI.service.cacheService;
import chewyt.WeatherAPI.service.weatherService;

@Controller
@RequestMapping(path = "/weather")
public class weatherController {

    private final Logger logger = Logger.getLogger(WeatherApiApplication.class.getName());
    
    @Autowired
    weatherService service;

    @Autowired
    cacheService cacheService;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String getWeather(@RequestBody MultiValueMap<String, String> form, Model model) {

        
        String city = form.getFirst("city");
        logger.info("City: %s".formatted(city));
        if (city.equals("")) {
            model.addAttribute("error", "Empty field. Please try again.");
            return "index";
        }
        
        // Check if resource is available in Redis cache
        
        Optional<List<Weather>> opt = cacheService.get(city);
        List<Weather> weather = Collections.emptyList();
        
        if (opt.isPresent()) {
            logger.info("Cache hit for %s".formatted(city));
            weather=opt.get();
        }
        else{
            try {
                weather=service.getWeather(city);
                logger.info("Is Weather List empty: %s".formatted(weather.isEmpty()));
                if (weather.size()>0){
                    cacheService.save(city, weather);
                }
            } catch (Exception e) {
                logger.warning("Warning: %s".formatted(e.getMessage()));
                model.addAttribute("error", "City not found. Please try again.");
                return "index";
            } 
        }

        String cityNameforDisplay = weather.get(0).getCityNameInSentenceCase();
        String countryNameforDisplay = weather.get(0).getCountryName();
        String countryCodeforDisplay = weather.get(0).getCountryCode();
       
        logger.log(Level.INFO, "Data: %s".formatted(weather));
        model.addAttribute("city", cityNameforDisplay);
        model.addAttribute("country", countryNameforDisplay);
        model.addAttribute("countryCode", countryCodeforDisplay);
        model.addAttribute("data", weather);
        return "weather";   
    }   

}
