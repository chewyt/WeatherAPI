package chewyt.WeatherAPI.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Weather {

    private String cityName;
    private String main;
    private String description;
    private String icon;
    private double temperature;
    private Float latitude;
    private Float longitude;
    private String tempDisplay;
    private String countryCode;
    private String countryName;
    

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTempDisplay() {
        return tempDisplay;
    }

    public void setTempDisplay(String tempDisplay) {
        this.tempDisplay = tempDisplay;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
        tempDisplay = "%.2f degrees".formatted(temperature);
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return this.toJson().toString();
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("cityName", cityName)
                .add("description", description)
                .add("icon", icon)
                .add("main", main)
                .add("temperature", temperature)
                .add("countryCode", countryCode)
                .add("countryName", countryName)
                .build();
    }

    public static Weather create(JsonObject o) {
        Weather w = new Weather();
        w.setMain(o.getString("main"));
        w.setDescription(o.getString("description"));
        w.setIcon(o.getString("icon"));
        return w;
    }
    public static Weather createFromCache(JsonObject o) {
        Weather w = new Weather();
        w.setMain(o.getString("main"));
        w.setDescription(o.getString("description"));
        w.setIcon(o.getString("icon"));
        w.setTemperature(o.getJsonNumber("temperature").doubleValue());
        w.setCityName(o.getString("cityName"));
        w.setCountryCode(o.getString("countryCode"));
        w.setCountryName(o.getString("countryName"));

        return w;
    }

    public static Weather create(String jsonString) {
        try (InputStream is = new ByteArrayInputStream(jsonString.getBytes())) {
            final JsonReader reader = Json.createReader(is);
            return create(reader.readObject());

        } catch (Exception e) {
            // TODO: Handle error
            return new Weather();
        }
    }

    public String getCityNameInSentenceCase() {

        String caps = cityName.substring(0,1).toUpperCase();
        String other = cityName.substring(1);
        return caps.concat(other);
    }
}
