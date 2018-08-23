package com.example.tsvetelinastoyanova.hackernewsapp.abstraction

interface BaseView<T> {

    fun setPresenter(presenter: T)
}