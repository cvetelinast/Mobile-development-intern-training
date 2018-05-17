package com.example.tsvetelinastoyanova.tic_tac_toe.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PlayerDao {
   @Query("SELECT * FROM players")
    List<Player> getAll();

    @Query("SELECT * FROM players ORDER BY wins/loses DESC")
    List<Player> getAllOrdered();

    @Query("SELECT COUNT(name) FROM players WHERE name like :playerName LIMIT 1")
     int getUserWithThisName(String playerName);

    @Insert
    void insertPlayer(Player player);

    @Query("UPDATE players set loses = loses + 1 WHERE name like :playerName")
    void decrementNumLosesOfPlayer(String playerName);

    @Query("UPDATE players set wins = wins + 1 WHERE name like :playerName")
    void incrementNumWinsOfPlayer(String playerName);

}