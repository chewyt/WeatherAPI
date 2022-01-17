package chewyt.WeatherAPI;

public class Constants {
    
    public static final String URL_WEATHER = "https://api.openweathermap.org/data/2.5/weather";
    public static final String URL_COUNTRY = "https://flagcdn.com/en/codes.json";
    

    //Storing in Config VARS of Heroku
    public static final String ENV_WEATHERAPIKEY = System.getenv("ENV_WEATHERAPIKEY");
    public static final String ENV_REDISCLOUD = System.getenv("ENV_REDISCLOUD");
    
    
    
    
    //Storing in ENV path locally
    // public static final String ENV_WEATHERAPIKEY = System.getenv("WEATHERAPI");
    // public static final String ENV_REDISCLOUD = System.getenv("REDIS_PW");


}
