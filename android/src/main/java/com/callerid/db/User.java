package com.callerid.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "users", indices={@Index(value="number", unique=true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "number")
    public String number;

    @ColumnInfo(name = "i164")
    public String i164;

//    public int getId() {
//        return id;
//    }

//    public String getUserName() {
//        return name;
//    }
//
//    public String getUserNumber() {
//        return number;
//    }
//
//    public void setId(int _id) {
//        this.id = _id;
//    }
//
//    public void setUserName(String _name) {
//        this.name = _name;
//    }
//
//    public void setUserNumber(String _number) {
//        this.number = _number;
//    }

    public User(String name, String number, String i164) {
        this.name = name;
        this.number = number;
        this.i164 = i164;
    }


    // Getters and setters are ignored for brevity,
    // but they're required for Room to work.
}