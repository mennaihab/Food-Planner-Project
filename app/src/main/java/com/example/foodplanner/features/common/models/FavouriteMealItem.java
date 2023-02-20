package com.example.foodplanner.features.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class FavouriteMealItem implements Parcelable {

    private String userId;
    private MealItem meal;
    private boolean active;

    public FavouriteMealItem() {}

    public FavouriteMealItem(String userId, boolean isFavourite, MealItem meal) {
        this.userId = userId;
        this.active = isFavourite;
        this.meal = meal;
    }

    protected FavouriteMealItem(Parcel in) {
        userId = in.readString();
        meal = in.readParcelable(MealItem.class.getClassLoader());
        active = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeParcelable(meal, flags);
        dest.writeByte((byte) (active ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FavouriteMealItem> CREATOR = new Creator<FavouriteMealItem>() {
        @Override
        public FavouriteMealItem createFromParcel(Parcel in) {
            return new FavouriteMealItem(in);
        }

        @Override
        public FavouriteMealItem[] newArray(int size) {
            return new FavouriteMealItem[size];
        }
    };

    public String getId() { return meal.getId(); }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isFavourite() {
        return active;
    }

    public void setFavourite(boolean favourite) {
        active = favourite;
    }

    public MealItem getMeal() {
        return meal;
    }

    public void setMeal(MealItem meal) {
        this.meal = meal;
    }

    @NonNull
    public FavouriteMealItem copy() {
        return new FavouriteMealItem(userId, active, meal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavouriteMealItem item = (FavouriteMealItem) o;
        return active == item.active && Objects.equals(userId, item.userId) && Objects.equals(meal, item.meal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, meal, active);
    }
}
