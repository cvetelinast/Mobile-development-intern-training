package com.example.tsvetelinastoyanova.cameramapsapp.abstraction

interface BaseView<T> {

    fun setPresenter(presenter: T)
}