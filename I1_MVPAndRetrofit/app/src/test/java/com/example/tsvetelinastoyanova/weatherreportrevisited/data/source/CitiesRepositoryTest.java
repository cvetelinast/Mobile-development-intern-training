package com.example.tsvetelinastoyanova.weatherreportrevisited.data.source;

import com.example.tsvetelinastoyanova.weatherreportrevisited.model.Main;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.WeatherObject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CitiesRepository.class)
public class CitiesRepositoryTest {

    @Mock
    CitiesRepository citiesRepository;

    @Test
    public void refreshWeatherObjectInCache() {
        List<WeatherObject> weatherObjectList = new ArrayList<>();

        WeatherObject w1 = new WeatherObject();
        w1.setName("Sofia");
        Main m1 = new Main();
        m1.setTemp(35);
        w1.setMain(m1);

        WeatherObject w2 = new WeatherObject();
        w2.setName("Varna");
        Main m2 = new Main();
        m2.setTemp(30);
        w2.setMain(m2);

        weatherObjectList.add(w1);
        weatherObjectList.add(w2);

        citiesRepository.weatherObjectsCache = weatherObjectList;

        //when(citiesRepository.getWeatherObjectsCache()).thenReturn(weatherObjectList);
        doCallRealMethod().when(citiesRepository).refreshWeatherObjectInCache(any());
        Main m3 = new Main();
        m3.setTemp(25);
        w1.setMain(m3);
        citiesRepository.refreshWeatherObjectInCache(w1);
        Assert.assertEquals(w1.getMain().getTemp(),citiesRepository.weatherObjectsCache.get(0).getMain().getTemp());

        WeatherObject w4 = new WeatherObject();
        w4.setName("Plovdiv");
        Main m4 = new Main();
        m4.setTemp(26);
        w4.setMain(m4);

        citiesRepository.refreshWeatherObjectInCache(w4);
        Assert.assertEquals(w4.getMain().getTemp(), citiesRepository.weatherObjectsCache.get(2).getMain().getTemp());

    }
}