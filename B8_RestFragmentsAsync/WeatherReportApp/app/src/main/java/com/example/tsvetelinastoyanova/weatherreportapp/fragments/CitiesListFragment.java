package com.example.tsvetelinastoyanova.weatherreportapp.fragments;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tsvetelinastoyanova.weatherreportapp.CitiesAdapter;
import com.example.tsvetelinastoyanova.weatherreportapp.City;
import com.example.tsvetelinastoyanova.weatherreportapp.Constants;
import com.example.tsvetelinastoyanova.weatherreportapp.R;
import com.example.tsvetelinastoyanova.weatherreportapp.database.AppDatabase;
import com.example.tsvetelinastoyanova.weatherreportapp.database.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportapp.models.WeatherReportObject;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CitiesListFragment extends Fragment {
    private List<City> citiesList = new ArrayList<>();
    private List<WeatherReportObject> weatherReportObjects = new ArrayList<>();
    private RecyclerView recyclerView;
    private CitiesAdapter adapter;
    private TextInputLayout cityNameContainer;

    public CitiesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cities_list, container, false);

        addClickListenerToAddCityButton(view.findViewById(R.id.add_city_button));
        setTextContainer(view.findViewById(R.id.new_city_wrapper));
        createRecyclerView(view.findViewById(R.id.recycler_view));
        fillRecyclerView();
        return view;
    }

    private void setTextContainer(TextInputLayout t) {
        cityNameContainer = t;
    }

    private void addClickListenerToAddCityButton(Button addCityButton) {
        addCityButton.setOnClickListener((v) -> {
            buttonToAddNewCityClicked();
        });
    }

    private URL getUrlConnection(String enteredCity) {
        try {
            return new URL(String.format(Constants.WEATHER_URL, enteredCity));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpURLConnection getConnection(URL url) {
        try {
            return (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private WeatherReportObject getWeatherReportObjectFromInputStream(HttpURLConnection urlConnection) {
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String json = convertStreamToString(in);
            Gson gson = new Gson();
            return gson.fromJson(json, WeatherReportObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                urlConnection.disconnect();
            } catch (Exception e) {
            }
        }
        return null;
    }

    private void saveToDatabase(WeatherReportObject weatherReportObject) {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getActivity(), AppDatabase.class, "cities").build();
            CityEntity city = new CityEntity();
            city.setName(weatherReportObject.getName());
            db.cityDao().insertCity(city);
        });
    }

    private void addWeatherReportObjectToRecyclerView(WeatherReportObject weatherReportObject) {
        if (citiesList.isEmpty()) {
            citiesList.add(new City(weatherReportObject.getName(), weatherReportObject.getMain().getTemp(), R.drawable.ic_launcher_background));
        } else {
            boolean isCityInDatabase = false;
            for (City c : citiesList) {
                if (c.getName().equals(weatherReportObject.getName())) {
                    c.setTemperature(weatherReportObject.getMain().getTemp());
                    isCityInDatabase = true;
                }
            }
            if (!isCityInDatabase) {
                citiesList.add(new City(weatherReportObject.getName(), weatherReportObject.getMain().getTemp(), R.drawable.ic_launcher_background));
            }
        }
    }


    private void loadFromApi(String enteredCity) {
        // todo: fix this
        new Thread(() -> {
            URL url = getUrlConnection(enteredCity);
            HttpURLConnection urlConnection = getConnection(url);
            WeatherReportObject weatherReportObject = getWeatherReportObjectFromInputStream(urlConnection);
            if(weatherReportObject !=  null) {
                weatherReportObjects.add(weatherReportObject);
                saveToDatabase(weatherReportObject);
                addWeatherReportObjectToRecyclerView(weatherReportObject);
            }
        }).start();
    }

    private static String convertStreamToString(InputStream in) {
        Scanner s = new Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public byte[] getImage(String code) {
        HttpURLConnection con = null;
        InputStream is = null;
        try {
            con = (HttpURLConnection) (new URL(Constants.IMG_URL + code)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            is = con.getInputStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while (is.read(buffer) != -1)
                baos.write(buffer);

            return baos.toByteArray();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }
        return null;
    }

    private void createRecyclerView(RecyclerView rView) {
        this.recyclerView = rView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new CitiesAdapter(citiesList);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void fillRecyclerView() {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getActivity(), AppDatabase.class, "cities").build();
            List<CityEntity> cityEntities = db.cityDao().getAll();
            for (CityEntity city : cityEntities) {
                loadFromApi(city.getName());
            }
        }).start();
    }

    private void buttonToAddNewCityClicked() {
        CityEntity newCity = new CityEntity();
        String name = cityNameContainer.getEditText().getText().toString();
        if (!name.isEmpty()) {
            loadFromApi(name);
            newCity.setName(name);
           /* new Thread(() -> {
                AppDatabase db = Room.databaseBuilder(getActivity(), AppDatabase.class, "cities").build();
                db.cityDao().insertCity(newCity);
            }).start();*/
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
