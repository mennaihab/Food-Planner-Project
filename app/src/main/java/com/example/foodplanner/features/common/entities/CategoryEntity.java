package com.example.foodplanner.features.common.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CategoryEntity {

    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String thumbnail;
    public String description;

}
