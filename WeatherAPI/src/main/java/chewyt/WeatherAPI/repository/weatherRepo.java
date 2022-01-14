package chewyt.WeatherAPI.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import chewyt.WeatherAPI.model.Weather;

@Repository
public class weatherRepo {
    
    @Autowired
    RedisTemplate<String, Object> template;

    public void save(String city, String description, double temp){
        
    }
}
