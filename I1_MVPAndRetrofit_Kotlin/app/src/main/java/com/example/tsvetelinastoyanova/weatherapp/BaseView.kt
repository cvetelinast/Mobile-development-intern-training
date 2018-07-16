package com.example.tsvetelinastoyanova.weatherapp

interface BaseView<T> {

    fun setPresenter(presenter: T)

}