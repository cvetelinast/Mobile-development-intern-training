package com.example.tsvetelinastoyanova.registrationloginapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE username like :username LIMIT 1")
    List<User> getUserWithThisName(String username);

    @Insert
    void insertUser(User user);

}
