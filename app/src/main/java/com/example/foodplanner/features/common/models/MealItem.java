package com.example.foodplanner.features.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.helpers.mappers.MapperInfo;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.core.Flowable;

public class MealItem implements Parcelable {

    @SerializedName("idMeal")
    private String id;
    @SerializedName("strMeal")
    private String name;
    @SerializedName("strMealThumb")
    private String thumbnail;

    @MapperInfo(ignored = true)
    private transient final boolean isFavourite;

    public MealItem() {
        isFavourite = false;

    }

    public MealItem(String id, String name, String thumbnail) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.isFavourite = false;

    }

    public MealItem(String id, String name, String thumbnail, boolean isFavourite) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.isFavourite = isFavourite;
    }


    protected MealItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        thumbnail = in.readString();
        isFavourite = (boolean) in.readSerializable();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(thumbnail);
        dest.writeSerializable(isFavourite);

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



    public boolean isFavourite() {
        return isFavourite;
    }

    public MealItem setFavourite(boolean favourite) {
        return new MealItem(
                id,
                name,
                thumbnail,
                favourite
        );
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealItem mealItem = (MealItem) o;
        return isFavourite == mealItem.isFavourite && id.equals(mealItem.id) && name.equals(mealItem.name) && thumbnail.equals(mealItem.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, thumbnail, isFavourite);
    }
}