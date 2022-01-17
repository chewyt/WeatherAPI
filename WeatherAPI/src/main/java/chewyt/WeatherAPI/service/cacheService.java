package chewyt.WeatherAPI.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chewyt.WeatherAPI.model.Weather;
import chewyt.WeatherAPI.repository.weatherRepo;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class cacheService {
    
    @Autowired
    weatherRepo repo;

    private final Logger logger = Logger.getLogger(cacheService.class.getName());


    public Optional<List<Weather>> get(String city) {

        Optional<String> opt  = repo.get(city);
        if (opt.isEmpty()) {
            logger.info("Search city term \" %s \"is not found".formatted(city));
            return Optional.empty();
        }
        else{
            //converting Json string back to JSon Array
            JsonArray jsonarray  = parseJsonArray(opt.get());
            //converting into a list of Weather objects
            List<Weather> weather  = jsonarray.stream()
                .map(v->(JsonObject)v)     //cast as a stream of Json Objects
                .map(Weather::createFromCache) //cast as a stream of Weather objects
                .collect(Collectors.toList()); //collect as a Collection List of Weather Objects
            return Optional.of(weather);

        }
    }

    private JsonArray parseJsonArray(String jsonString){
        try (InputStream is = new ByteArrayInputStream(jsonString.getBytes())) {
            JsonReader reader = Json.createReader(is);
            return reader.readArray();            
        } catch (Exception e) {
            //Log errors
        }
        return Json.createArrayBuilder().build();
    }

    //List weater convert to Json array back to Json string
    public void save(String cityName, List<Weather> weather) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        weather.stream()
            .forEach(v->arrayBuilder.add(v.toJson()));
        repo.save(cityName, arrayBuilder.build().toString());
            
    }

}
