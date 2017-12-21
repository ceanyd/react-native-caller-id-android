package com.callerid.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDAO {

//    @Query("SELECT * FROM user")
//    List<User> getAll();
//
//    @Insert
//    void insertAll(User... users);
//
//    @Update
//    void updateUsers(User... users);
//
//    @Delete
//    void delete(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(User user);

    @Query("select * from users")
    public List<User> getAllUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUsers(List<User> users);

    @Query("select * from users where number = :number or i164 = :number Limit 1")
    public User getUserByNumber(String number);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(User user);

    @Query("delete from users")
    void removeAllUsers();
}