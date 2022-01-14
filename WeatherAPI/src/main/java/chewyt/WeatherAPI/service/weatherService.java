package chewyt.WeatherAPI.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import chewyt.WeatherAPI.WeatherApiApplication;
import chewyt.WeatherAPI.model.Weather;
import chewyt.WeatherAPI.repository.weatherRepo;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import static chewyt.WeatherAPI.Constants.*;

@Service
public class weatherService {

    @Autowired
    weatherRepo repo;

    Logger logger = Logger.getLogger(WeatherApiApplication.class.getName());

    public List<Weather> getWeather(String city) {

        city=city.replaceAll(" ", "+");
        logger.log(Level.INFO, System.getenv("WEATHERAPI"));

        String url = UriComponentsBuilder
                .fromUriString(URL_WEATHER)
                .queryParam("q", city)
                .queryParam("appid", ENV_WEATHERAPIKEY)
                .queryParam("units", "metric")
                .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);

        logger.log(Level.INFO, resp.getStatusCode().toString());
        logger.log(Level.INFO, resp.getHeaders().toString());

        JsonObject data;

        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) {
            final JsonReader reader = Json.createReader(is);
            final JsonObject result = reader.readObject();
            
            final JsonArray stationReadings = result.getJsonArray("weather");
            final String cityName = result.getString("name");
            final float temperature = (float)result.getJsonObject("main").getJsonNumber("temp").doubleValue();

            return stationReadings.stream()
                .map(v->(JsonObject)v)   //JsonValue to JSON Object
                // .map(JsonObject::cast)    
                .map(Weather::create)   //every JSON OBject to instantiate a Weather object
                .map(v->{
                    v.setCityName(cityName);
                    v.setTemperature(temperature);
                    return v;
                })
                .collect(Collectors.toList());
                

            // data = reader.readObject();
        } catch (Exception ex) {}

        return Collections.EMPTY_LIST;
        
        // logger.log(Level.INFO, data.toString());

        // logger.log(Level.INFO, data.getJsonArray("weather").toString());
        // logger.log(Level.INFO, data.getJsonArray("weather").getJsonObject(0).getString("description"));
        // logger.log(Level.INFO, data.getJsonObject("main").getJsonNumber("temp").toString());

        // String description = data.getJsonArray("weather").getJsonObject(0).getString("description");
        // double temperature = (data.getJsonObject("main").getJsonNumber("temp")).doubleValue();



        // return data.toString();
    }
}
