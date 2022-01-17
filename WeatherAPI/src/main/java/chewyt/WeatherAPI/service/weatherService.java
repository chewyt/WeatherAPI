package chewyt.WeatherAPI.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    Logger logger = Logger.getLogger(WeatherApiApplication.class.getName());

    public List<Weather> getWeather(String city) {

        city = city.replaceAll(" ", "+");
        // logger.log(Level.INFO, System.getenv("WEATHERAPI"));

        String url = UriComponentsBuilder
                .fromUriString(URL_WEATHER)
                .queryParam("q", city)
                .queryParam("appid", ENV_WEATHERAPIKEY)
                .queryParam("units", "metric")
                .toUriString();

        final RequestEntity<Void> weather_req = RequestEntity.get(url).build();
        final RestTemplate weather_template = new RestTemplate();
        final ResponseEntity<String> weather_resp = weather_template.exchange(weather_req, String.class);
        
        if (weather_resp.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(
                    "Error: status code %s".formatted(weather_resp.getStatusCode().toString()));
        }

        // logger.log(Level.INFO, weather_resp.getStatusCode().toString());
        // logger.log(Level.INFO, weather_resp.getHeaders().toString());
        // logger.log(Level.INFO, weather_resp.getBody().toString());

        try (InputStream is_weather = new ByteArrayInputStream(weather_resp.getBody().getBytes())) {

            final JsonReader weather_reader = Json.createReader(is_weather);
            final JsonObject result = weather_reader.readObject();

            // capturing critical info from API
            final JsonArray stationReadings = result.getJsonArray("weather");
            final String cityName = result.getString("name");

            final float temperature = (float) result.getJsonObject("main").getJsonNumber("temp").doubleValue();
            final String countryCode = result.getJsonObject("sys").getString("country").toLowerCase();

            String url_Clist = UriComponentsBuilder
                    .fromUriString(URL_COUNTRY)
                    .toUriString();

            final RequestEntity<Void> req = RequestEntity.get(url_Clist).build();
            final RestTemplate template = new RestTemplate();
            final ResponseEntity<String> resp = template.exchange(req, String.class);

            if (resp.getStatusCode() != HttpStatus.OK) {
                throw new IllegalArgumentException(
                        "Error: status code %s".formatted(resp.getStatusCode().toString()));
            }

            JsonObject countryList;

            try (InputStream is_Clist = new ByteArrayInputStream(resp.getBody().getBytes())) {
                final JsonReader reader = Json.createReader(is_Clist);
                countryList = reader.readObject();
                is_Clist.close();
            } catch (Exception ex) {
                countryList = Json.createObjectBuilder().build();
            }
            final String countryName = countryList.getString(countryCode.toLowerCase());

            return stationReadings.stream()
                    .map(v -> (JsonObject) v) // JsonValue to JSON Object
                    // .map(JsonObject::cast) Only possible for object classes Json object is not an
                    // object class
                    .map(Weather::create) // every JSON OBject to instantiate a Weather object
                    .map(v -> {
                        v.setCityName(cityName);
                        v.setTemperature(temperature);
                        v.setCountryCode(countryCode);
                        v.setCountryName(countryName);
                        return v; // return back same object after implementing methods to that object
                    })
                    .collect(Collectors.toList());

        } catch (Exception ex) {
        }

        return Collections.EMPTY_LIST;

        // logger.log(Level.INFO, data.toString());

        // logger.log(Level.INFO, data.getJsonArray("weather").toString());
        // logger.log(Level.INFO,
        // data.getJsonArray("weather").getJsonObject(0).getString("description"));
        // logger.log(Level.INFO,
        // data.getJsonObject("main").getJsonNumber("temp").toString());

        // String description =
        // data.getJsonArray("weather").getJsonObject(0).getString("description");
        // double temperature =
        // (data.getJsonObject("main").getJsonNumber("temp")).doubleValue();

        // return data.toString();
    }
}
