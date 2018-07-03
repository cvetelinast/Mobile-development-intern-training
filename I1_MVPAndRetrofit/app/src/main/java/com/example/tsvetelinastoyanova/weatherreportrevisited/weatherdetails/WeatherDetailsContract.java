package com.example.tsvetelinastoyanova.weatherreportrevisited.weatherdetails;

import com.example.tsvetelinastoyanova.weatherreportrevisited.BasePresenter;
import com.example.tsvetelinastoyanova.weatherreportrevisited.BaseView;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.WeatherObject;

public interface WeatherDetailsContract {

    interface View extends BaseView<Presenter> {
        void changeView(WeatherObject weatherObject);
    }

    interface Presenter extends BasePresenter {

    }
}
