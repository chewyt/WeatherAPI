package chewyt.WeatherAPI.model;

import jakarta.json.JsonObject;

public class Weather {

    private String cityName;
    private String main;
    private String description;
    private String icon;
    private double temperature;
    private Float latitude;
    private Float longitude;
    private String tempDisplay;

    
    
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
        return "Weather [cityName=" + cityName + ", description=" + description + ", icon=" + icon + ", latitude="
                + latitude + ", longitude=" + longitude + ", main=" + main + ", temperature=" + temperature + "]";
    }



    public static Weather create(JsonObject o){
        Weather w = new Weather();
        w.setMain(o.getString("main"));
        w.setDescription(o.getString("description"));
        w.setIcon(o.getString("icon"));
        return w;
    }
    
}
