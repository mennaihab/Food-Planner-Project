package com.example.foodplanner.features.common.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AreaEntity {

    @PrimaryKey
    @NonNull
    public String name;

}
