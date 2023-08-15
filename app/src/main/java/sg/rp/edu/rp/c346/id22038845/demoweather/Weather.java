package sg.rp.edu.rp.c346.id22038845.demoweather;

public class Weather {

    private String area;
    private String forecast;

    public Weather(String area, String forecast) {
        this.area = area;
        this.forecast = forecast;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getForecast() {
        return forecast;
    }

    public void setForecast(String forecast) {
        this.forecast = forecast;
    }

    public String toString(){
        String weather = "Area: " + area;
        weather += "\nForecast: " + forecast;
        return weather;
    }
}
