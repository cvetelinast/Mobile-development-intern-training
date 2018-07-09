package com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.visualization;

import android.support.v7.widget.RecyclerView;

import com.example.tsvetelinastoyanova.weatherreportrevisited.City;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RecyclerView.class)
public class CitiesAdapterTest {

    @Mock
    private CitiesAdapter adapter;

    @Test
    public void refreshCity() {
        doCallRealMethod().when(adapter).refreshCity(any());
        List<City> a = new ArrayList<>();
        City testCity = new City("Sofia", 25, 24454);
        a.add(testCity);
        a.add(new City("Varna", 26, 34534));
        a.add(new City("Plovdiv", 23, 43434));
        when(adapter.getCitiesList()).thenReturn(a);
        double expectedTemperature = 54;
        testCity.setTemperature(expectedTemperature);
        doCallRealMethod().when(adapter).notifyItemChanged(anyInt());
        doAnswer(answer -> {
            int index = answer.getArgument(0);
            Assert.assertEquals(0, index);
            return "";
        })
                .when(adapter)
                .notifyItemChanged(anyInt());

        adapter.refreshCity(testCity);
        Assert.assertEquals(expectedTemperature, adapter.getCitiesList().get(0).getTemperature());
    }
}