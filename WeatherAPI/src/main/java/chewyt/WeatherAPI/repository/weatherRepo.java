package chewyt.WeatherAPI.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

// import chewyt.WeatherAPI.model.Weather;

@Repository
public class weatherRepo {
    
    @Autowired
    RedisTemplate<String, String> template;

    private final Logger logger = Logger.getLogger(weatherRepo.class.getName());

    //Saving as a JSON string
    public void save(String city, String jsonValue){
        template.opsForValue().set("WEATHERAPI_"+normalise(city), jsonValue, 10L, TimeUnit.MINUTES);
    }

    
    // Getting the JSON string from JSON array
    public Optional<String> get(String cityName){
        String value = template.opsForValue().get("WEATHERAPI_"+normalise(cityName));
        return Optional.ofNullable(value);

    }

    private String normalise(String cityname){
        return cityname.toLowerCase().trim();
    }
}
