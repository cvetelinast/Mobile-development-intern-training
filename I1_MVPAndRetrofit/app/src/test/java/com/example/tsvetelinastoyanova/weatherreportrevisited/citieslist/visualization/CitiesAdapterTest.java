package com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.visualization;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.tsvetelinastoyanova.weatherreportrevisited.City;
import com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.CitiesListContract;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RecyclerView.class)
public class CitiesAdapterTest {

// for (int i = 0; i < getCitiesList().size(); i++) {
//        City c = getCitiesList().get(i);
//        if (c.getName().equals(newCity.getName())) {
//            Log.d("tag","REFRESH: city: " + c.getName()+" from "+ c.getTemperature() + " to " + newCity.getTemperature() + " item number "+ i);
//            getCitiesList().set(i,newCity);
//            notifyItemChanged(i);
//            return;
//        }
//    }

    @Mock
    private CitiesAdapter adapter;

//    @Mock
//    private CitiesListContract.Presenter presenter;
//
//    @Mock
//    private OnItemClickListener onItemClickListener;

    @Test
    public void refreshCity() {
        doCallRealMethod().when(adapter).refreshCity(any());
        List a = new ArrayList();
        City testCity = new City("Sofia", 25, 24454);
        a.add(testCity);
        a.add(new City("Varna", 26, 34534));
        a.add(new City("Plovdiv", 23, 43434));
        when(adapter.getCitiesList()).thenReturn(a);
        double expectedTemperature = 54;
        testCity.setTemperature(expectedTemperature);
//        doNothing().when(adapter).notifyItemChanged(anyInt());
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