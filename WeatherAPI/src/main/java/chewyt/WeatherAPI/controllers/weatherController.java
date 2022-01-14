package chewyt.WeatherAPI.controllers;

import java.util.List;
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
import chewyt.WeatherAPI.service.weatherService;

@Controller
@RequestMapping(path = "/weather")
public class weatherController {

    @Autowired
    weatherService service;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String getWeather(@RequestBody MultiValueMap<String, String> form, Model model) {

        String city = form.getFirst("city");
        List<Weather> stationReadings = service.getWeather(city);
        Logger logger = Logger.getLogger(WeatherApiApplication.class.getName());
        logger.log(Level.INFO, "Data: %s".formatted(stationReadings));
        model.addAttribute("city", city);
        model.addAttribute("data", stationReadings);
        return "weather";   
    }   

}
