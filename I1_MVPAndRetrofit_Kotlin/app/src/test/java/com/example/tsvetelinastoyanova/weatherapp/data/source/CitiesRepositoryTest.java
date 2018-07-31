package com.example.tsvetelinastoyanova.weatherapp.data.source;

import com.example.tsvetelinastoyanova.weatherapp.City;
import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity;
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.LocalDataSource;
import com.example.tsvetelinastoyanova.weatherapp.data.source.remote.CitiesRemoteDataSource;
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.Clouds;
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.Coord;
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject;
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.Main;
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.Sys;
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.Weather;
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.Wind;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CitiesRemoteDataSource.class)
public class CitiesRepositoryTest {

    private CitiesRepository citiesRepository;
    private String CITY_NAME = "Sofia";
    private City city = new City("Sofia", 26.4, 10);
    private CityEntity cityEntity;
    private CurrentWeatherObject weatherObject;

    @Mock
    private LocalDataSource localDataSource;

    @Mock
    private CitiesRemoteDataSource remoteDataSource;

    @Mock
    private LocalDataSource.AddCityCallback addCityCallback;

    @Mock
    private CityDataSource.GetCityCallback getCityCallback;

    @Mock
    private LocalDataSource.DeleteCityCallback deleteCityCallback;

    /*
    @Captor
    private ArgumentCaptor<LocalDataSource.AddCityCallback> addCityCallbackArgumentCaptor;*/

    /*
   @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();*/

    @Before
    public void setupRepository() {
        citiesRepository = new CitiesRepository(localDataSource, remoteDataSource);
        initWeatherObject();
        initCityEntity();
    }

    @After
    public void destroyRepositoryInstance() {
        citiesRepository = null;
        cityEntity = null;
        weatherObject = null;
        Mockito.reset(remoteDataSource);
        Mockito.reset(localDataSource);
    }

    @Test
    public void getCitiesWithAndWithoutCache() {
        doReturn(Single.just(weatherObject)).when(remoteDataSource).getWeatherObject(anyString());
        doReturn(Single.just(cityEntity)).when(localDataSource).getCity(anyString());
        List<CityEntity> cities = new ArrayList<>();
        doReturn(Single.just(cities)).when(localDataSource).getCities();

        // without cache
        assertEquals(citiesRepository.getSizeOfCache(), 0);
        citiesRepository.getCities(getCityCallback);
        Mockito.verify(localDataSource, times(1)).getCities();

        // with cache
        citiesRepository.addNotAddedCity(CITY_NAME, addCityCallback);
        cities.add(cityEntity);
        Mockito.verify(remoteDataSource).getWeatherObject(CITY_NAME);
        Mockito.verify(localDataSource).getCity(CITY_NAME);
    }

    @Test
    public void getCityFromLocalDataSourceCalled() {
        doReturn(Single.just(cityEntity)).when(localDataSource).getCity(anyString());
        citiesRepository.getCity(CITY_NAME);
        Mockito.verify(localDataSource).getCity(CITY_NAME);
    }

    @Test
    public void deleteCityFromLocalDataSourceCalled() {
        doReturn(Single.just(weatherObject)).when(remoteDataSource).getWeatherObject(anyString());
        doReturn(Single.just(cityEntity)).when(localDataSource).getCity(anyString());
        doReturn(Single.just(CITY_NAME)).when(localDataSource).deleteCity(anyString());

        // add city
        addCity();

        // delete city
        citiesRepository.deleteCity(CITY_NAME, deleteCityCallback);
        Mockito.verify(localDataSource).deleteCity(CITY_NAME);
    }


    @Test
    public void refreshCitiesCalled() {
        doReturn(Single.just(getSecondWeatherObject())).when(remoteDataSource).getWeatherObject(anyString());
        doReturn(Single.just(cityEntity)).when(localDataSource).getCity(anyString());
        List<City> cities = new ArrayList<>();
        cities.add(city);
        citiesRepository.refreshCities(cities, getCityCallback);
        Mockito.verify(remoteDataSource).getWeatherObject(CITY_NAME);
        Mockito.verify(localDataSource).refreshCity(any());
        assertEquals(citiesRepository.getWeatherObjectWithName(CITY_NAME).getWeather().get(0).getMain(), getSecondWeatherObject().getWeather().get(0).getMain());
        assertEquals(citiesRepository.getWeatherObjectWithName(CITY_NAME).getWeather().get(0).getDescription(), getSecondWeatherObject().getWeather().get(0).getDescription());
    }

    @Test
    public void addTheSameCityTwice() {
        doReturn(Single.just(weatherObject)).when(remoteDataSource).getWeatherObject(anyString());
        Mockito.when(localDataSource.getCity(anyString())).thenReturn(Single.error(IllegalStateException::new)).thenReturn(Single.just(cityEntity));
        doReturn(Single.just(cityEntity)).when(localDataSource).addCity(any());

        assertEquals(citiesRepository.getSizeOfCache(), 0);
        citiesRepository.addNotAddedCity(CITY_NAME, addCityCallback);
        citiesRepository.addNotAddedCity(CITY_NAME, addCityCallback);
        Mockito.verify(localDataSource, times(2)).getCity(CITY_NAME);
        Mockito.verify(localDataSource, times(1)).addCity(any());
        assertEquals(citiesRepository.getSizeOfCache(), 1);
    }


    private void addCity() {
        // If the city is not added, it will be added
        assertEquals(citiesRepository.getSizeOfCache(), 0);
        assertNull(citiesRepository.getWeatherObjectWithName(CITY_NAME));
        Mockito.verify(remoteDataSource).getWeatherObject(CITY_NAME);
        Mockito.verify(localDataSource).addCity(any());
        assertEquals(citiesRepository.getSizeOfCache(), 1);
    }

    private void initWeatherObject() {
        Coord coord = new Coord(23.32, 42.7);
        Weather weather = new Weather(803, "Clouds", "broken clouds", "04d");
        List<Weather> weatherList = new ArrayList<>();
        weatherList.add(weather);
        String base = "stations";
        Main main = new Main(297.15, 1010, 64, 297.15, 297.15);
        int visibility = 10000;
        Wind wind = new Wind(2.6, 350);
        Clouds clouds = new Clouds(75);
        int dt = 1532611800;
        Sys sys = new Sys(1, 5444, 0.0021, "BG", 1532574756, 1532627597);
        int id = 727011;
        String name = "Sofia";
        int cod = 200;
        this.weatherObject = new CurrentWeatherObject(coord, weatherList, base, main, visibility, wind, clouds, dt, sys, id, name, cod);
    }

    @Test
    public void getWeatherObjectWithNameFromCache() {
        doReturn(Single.just(weatherObject)).when(remoteDataSource).getWeatherObject(anyString());
        doReturn(Single.just(cityEntity)).when(localDataSource).getCity(anyString());

        // If the city is not added, it will be added
        addCity();

        assertEquals(citiesRepository.getWeatherObjectWithName(CITY_NAME).getWeather().get(0).getDescription(), weatherObject.getWeather().get(0).getDescription());
    }

    private CurrentWeatherObject getSecondWeatherObject() {
        Coord coord = new Coord(23.32, 42.7);
        Weather weather = new Weather(803, "Rain", "moderate rain", "10d");
        List<Weather> weatherList = new ArrayList<>();
        weatherList.add(weather);
        String base = "stations";
        Main main = new Main(297.15, 1010, 64, 297.15, 297.15);
        int visibility = 10000;
        Wind wind = new Wind(2.6, 350);
        Clouds clouds = new Clouds(75);
        int dt = 1532611800;
        Sys sys = new Sys(1, 5444, 0.0021, "BG", 1532574756, 1532627597);
        int id = 727011;
        String name = "Sofia";
        int cod = 200;
        return new CurrentWeatherObject(coord, weatherList, base, main, visibility, wind, clouds, dt, sys, id, name, cod);
    }

    private void initCityEntity() {
        cityEntity = new CityEntity();
        cityEntity.setName("Sofia");
        cityEntity.setCityId(727011);
        cityEntity.setLastImageId(10);
        cityEntity.setLastTemperature(26.4);
    }
}
