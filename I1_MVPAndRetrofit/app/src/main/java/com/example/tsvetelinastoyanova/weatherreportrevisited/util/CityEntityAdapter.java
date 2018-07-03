package com.example.tsvetelinastoyanova.weatherreportrevisited.util;

import com.example.tsvetelinastoyanova.weatherreportrevisited.City;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.WeatherObject;

import java.util.ArrayList;
import java.util.List;

public class CityEntityAdapter {

    public static List<City> convertCityEnitiesToCities(List<CityEntity> cityEntities) {
        List<City> cities = new ArrayList<>();
        for (CityEntity cityEntity : cityEntities) {
            cities.add(new City(cityEntity.getName(), cityEntity.getLastTemperature(), cityEntity.getLastImageId()));
        }
        return cities;
    }

    public static City convertCityEntityToCity(CityEntity cityEntity) {
        return new City(cityEntity.getName(), cityEntity.getLastTemperature(), cityEntity.getLastImageId());
    }

    public static CityEntity convertWeatherObjectToCityEntity(WeatherObject object) {
        CityEntity c = new CityEntity();
        c.setName(object.getName());
        c.setCityId(object.getId());
        c.setLastTemperature(object.getMain().getTemp());
        String imageId = object.getWeather().get(0).getIcon();
        c.setLastImageId(ImageOperator.getImageIdFromString(imageId));
        return c;
    }

    public static City convertWeatherObjectToCity(WeatherObject weatherObject) {
        String image = weatherObject.getWeather().get(0).getIcon();
        return new City(weatherObject.getName(), weatherObject.getMain().getTemp(), ImageOperator.getImageIdFromString(image));
    }
}
