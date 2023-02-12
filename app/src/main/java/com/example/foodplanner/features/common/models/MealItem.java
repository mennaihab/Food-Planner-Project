package com.example.foodplanner.features.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class MealItem implements Parcelable {

    @SerializedName("idMeal")
    private final String id;
    @SerializedName("strMeal")
    private final String name;
    @SerializedName("strMealThumb")
    private final String thumbnail;

    public MealItem(String id, String name, String thumbnail) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
    }

    protected MealItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        thumbnail = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(thumbnail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MealItem> CREATOR = new Creator<MealItem>() {
        @Override
        public MealItem createFromParcel(Parcel in) {
            return new MealItem(in);
        }

        @Override
        public MealItem[] newArray(int size) {
            return new MealItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealItem mealItem = (MealItem) o;
        return Objects.equals(id, mealItem.id) && Objects.equals(name, mealItem.name) && Objects.equals(thumbnail, mealItem.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, thumbnail);
    }
}
