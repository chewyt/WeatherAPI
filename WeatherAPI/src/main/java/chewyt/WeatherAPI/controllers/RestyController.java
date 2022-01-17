package chewyt.WeatherAPI.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chewyt.WeatherAPI.model.Weather;
import chewyt.WeatherAPI.service.cacheService;
import chewyt.WeatherAPI.service.weatherService;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;

@RestController
@RequestMapping(path = "/weather", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestyController {

    @Autowired
    weatherService service;

    @Autowired
    cacheService cacheService;

    private final Logger logger = Logger.getLogger(RestyController.class.getName());
    
    @GetMapping(path = "/{city}")
    public ResponseEntity<String> getWeatherPath(@PathVariable String city){
        
        logger.info(">>>>>>>>Use Get Mapping Path variable");
        logger.info("City: %s".formatted(city));

        Optional<List<Weather>> opt = cacheService.get(city);
        List<Weather> weather = Collections.emptyList();
        
        if (opt.isPresent()) {
            logger.info("Cache hit for %s".formatted(city));
            weather=opt.get();
        }
        else{
            try {
                logger.info(">>>>>>>>Try catch for using WeatherService");
                
                weather=service.getWeather(city);
                logger.info("Is Weather List empty: %s".formatted(weather.isEmpty()));
                if (weather.size()>0){
                    cacheService.save(city, weather);
                }
            } catch (Exception ex) {
                logger.severe("GetCity: %s".formatted(ex.getMessage()));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("GetCity: %s".formatted(ex.getMessage()));

            } 
        }
        JsonArrayBuilder arrBuildr = Json.createArrayBuilder();
         weather.stream()
                .forEach(v->arrBuildr.add(v.toJson()));
        
        return ResponseEntity.ok(arrBuildr.build().toString());
                
    }
}
