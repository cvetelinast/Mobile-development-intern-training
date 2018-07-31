package com.example.tsvetelinastoyanova.weatherapp.citiesList;

import com.example.tsvetelinastoyanova.weatherapp.City;
import com.example.tsvetelinastoyanova.weatherapp.data.source.CitiesRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CitiesListPresenterTest {

    private CitiesListPresenter presenter;
    @Mock
    private CitiesListContract.View view;
    @Mock
    private CitiesRepository repository; // = new CitiesRepository(null, null);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        presenter = new CitiesListPresenter(view, repository);
    }

    @Test
    public void shouldAddNewCity() {

       // when(repository.addCity()).then(weatherObjectList);

        when(view.getCityFromInputField()).thenReturn("Madrid");
        presenter.addNewCity("Madrid");
        verify(presenter).addNewCity("Madrid");
       // verify(view).showNewCityAdded(new City("Madrid", 10, 10));
    }
}
