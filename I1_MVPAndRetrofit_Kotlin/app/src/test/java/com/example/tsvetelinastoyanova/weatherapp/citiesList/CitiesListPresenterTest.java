package com.example.tsvetelinastoyanova.weatherapp.citiesList;

import com.example.tsvetelinastoyanova.weatherapp.City;
import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity;
import com.example.tsvetelinastoyanova.weatherapp.data.source.CitiesRepository;
import com.example.tsvetelinastoyanova.weatherapp.data.source.CityDataSource;
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.LocalDataSource;
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.Clouds;
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.Coord;
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject;
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.Main;
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.Sys;
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.Weather;
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.Wind;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.tsvetelinastoyanova.weatherapp.util.CityEntityConverterKt.convertCityEntityToCity;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalAnswers.answerVoid;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class CitiesListPresenterTest {

    private CitiesListPresenter presenter;
    private CurrentWeatherObject weatherObject;
    private String CITY_NAME = "Sofia";
    private CityEntity cityEntity;
    private City city = new City("Sofia", 26.4, 10);

    @Mock
    private CitiesListContract.View view;

    @Mock
    private CitiesRepository repository;

    @Mock
    private LocalDataSource.AddCityCallback addCityCallback;

    @Captor
    private ArgumentCaptor<LocalDataSource.AddCityCallback> addCityCallbackCaptor = ArgumentCaptor.forClass(LocalDataSource.AddCityCallback.class);

    @Captor
    private ArgumentCaptor<CityDataSource.GetCityCallback> getCityCallbackCaptor = ArgumentCaptor.forClass(CityDataSource.GetCityCallback.class);

    @Captor
    private ArgumentCaptor<LocalDataSource.DeleteCityCallback> deleteCityCallbackCaptor = ArgumentCaptor.forClass(LocalDataSource.DeleteCityCallback.class);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        presenter = new CitiesListPresenter(view, repository);
        initWeatherObject();
        initCityEntity();
    }

    @Test
    public void shouldLoadCities() {
        when(repository.getSizeOfCache()).thenReturn(0);
        doNothing().when(repository).getCitiesFromDataSource(any(CityDataSource.GetCityCallback.class));

        presenter.loadCities();
        verify(repository).getCities(getCityCallbackCaptor.capture());

        getCityCallbackCaptor.getValue().onCityLoaded(cityEntity);
        verify(view).showCityLoaded(convertCityEntityToCity(cityEntity));

        getCityCallbackCaptor.getValue().onCityDoesNotExist();
        verify(view).showErrorLoadingCities();
    }

    @Test
    public void shouldAddNewCity() {
        when(view.getCityFromInputField()).thenReturn(CITY_NAME);

        presenter.addNewCity(CITY_NAME);
        verify(repository).addNotAddedCity(anyString(), addCityCallbackCaptor.capture());

        addCityCallbackCaptor.getValue().onCityAddedSuccessfully(cityEntity);
        verify(view).showNewCityAdded(any());

        addCityCallbackCaptor.getValue().onCityExistsInDatabase();
        verify(view).showErrorAddingAddedCity();

        addCityCallbackCaptor.getValue().onNotValidCity();
        verify(view).showErrorNotValidCityName();
    }

    @Test
    public void shouldDeleteCity() {
        doNothing().when(repository).deleteCity(anyString(), any(LocalDataSource.DeleteCityCallback.class));

        presenter.deleteCity(city);
        verify(repository).deleteCity(anyString(), deleteCityCallbackCaptor.capture());

        deleteCityCallbackCaptor.getValue().onCityDeletedSuccessfully();
        verify(view).showCityDeleted();

        deleteCityCallbackCaptor.getValue().onFail();
        verify(view).showErrorDeleteCity();
    }

    @Test
    public void shouldGetWeatherObjectOnClick() {
        doReturn(weatherObject).when(repository).getWeatherObjectWithName(anyString());
        presenter.getWeatherObjectOnClick(CITY_NAME);
        verify(repository).getWeatherObjectWithName(CITY_NAME);
    }

    @Test
    public void shouldRefreshCities() {
        List<City> cities = new ArrayList<>();
        cities.add(city);
        doNothing().when(repository).refreshCities(anyList(), any(CityDataSource.GetCityCallback.class));

        presenter.refreshCities(cities);
        verify(repository).refreshCities(anyList(), getCityCallbackCaptor.capture());

        getCityCallbackCaptor.getValue().onCityLoaded(cityEntity);
        verify(view).showCityUpdated(convertCityEntityToCity(cityEntity));

        getCityCallbackCaptor.getValue().onCityDoesNotExist();
        verify(view).showErrorLoadingCities();
    }

  /*  @Test
    public void shouldAddNewCity() {
        Mockito.doAnswer(answerVoid((LocalDataSource.AddCityCallback myCallback) -> myCallback.onCityAddedSuccessfully(cityEntity)))
            .when(repository).addNotAddedCity(anyString(), any());

        when(view.getCityFromInputField()).thenReturn(CITY_NAME);

        presenter.addNewCity(CITY_NAME);
        verify(repository).addNotAddedCity(anyString(), any());
       // verify(presenter).addNewCity("Madrid");
        verify(view).showNewCityAdded(any());
    }*/

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

    private void initCityEntity() {
        cityEntity = new CityEntity();
        cityEntity.setName("Sofia");
        cityEntity.setCityId(727011);
        cityEntity.setLastImageId(10);
        cityEntity.setLastTemperature(26.4);
    }

}
