package chewyt.WeatherAPI.config;

import java.util.Optional;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
// import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
// import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import chewyt.WeatherAPI.WeatherApiApplication;
import static chewyt.WeatherAPI.Constants.*;

@Configuration
public class WeatherConfig {

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private Optional<Integer> redisPort;
    // @Value("${spring.redis.password}")
    // private Optional<String> redisPassword;

    Logger logger = Logger.getLogger(WeatherApiApplication.class.getName());

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort.get());
        config.setPassword(ENV_REDISCLOUD);
        
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        // RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer(getClass().getClassLoader());
        final RedisTemplate<String, Object> template = new RedisTemplate<>();

        //Ops for Value (Good for storing JSON string)
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        
        //Ops for Hash
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        
        template.setConnectionFactory(redisConnectionFactory());

        return template;
    }

}
