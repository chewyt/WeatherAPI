package chewyt.WeatherAPI.service;

import java.util.List;

import chewyt.WeatherAPI.model.Weather;

public interface interfaceService {
    public List<Weather> getWeather(String city);
}
