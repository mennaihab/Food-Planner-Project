package com.example.foodplanner.features.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.foodplanner.features.common.helpers.convertors.LocalDateConvertor;
import com.google.firebase.firestore.Exclude;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class PlanMealItem implements Parcelable {

    private String calendarUri;

    private String userId;

    @Exclude
    private LocalDate day;
    private MealItem meal;

    private boolean active;

    public PlanMealItem() {}

    public PlanMealItem(String calendarUri, String userId, LocalDate day, MealItem meal, boolean active) {
        this.calendarUri = calendarUri;
        this.userId = userId;
        this.day = day;
        this.meal = meal;
        this.active = active;
    }

    protected PlanMealItem(Parcel in) {
        calendarUri = in.readString();
        userId = in.readString();
        day = (LocalDate) in.readSerializable();
        meal = in.readParcelable(MealItem.class.getClassLoader());
        active = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(calendarUri);
        dest.writeString(userId);
        dest.writeSerializable(day);
        dest.writeParcelable(meal, flags);
        dest.writeByte((byte) (active ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlanMealItem> CREATOR = new Creator<PlanMealItem>() {
        @Override
        public PlanMealItem createFromParcel(Parcel in) {
            return new PlanMealItem(in);
        }

        @Override
        public PlanMealItem[] newArray(int size) {
            return new PlanMealItem[size];
        }
    };

    public String getPersistenceId() {
        return meal.getId() + "-" + getTimestamp().getTime();
    }

    public String getCalendarUri() {
        return calendarUri;
    }

    public void setCalendarUri(String calendarUri) {
        this.calendarUri = calendarUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Exclude
    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public Date getTimestamp() {
        return new Date(LocalDateConvertor.fromDate(day));
    }

    public void setTimestamp(Date day) {
        this.day = LocalDateConvertor.toDate(day.getTime());
    }

    public MealItem getMeal() {
        return meal;
    }

    public void setMeal(MealItem meal) {
        this.meal = meal;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @NonNull
    public PlanMealItem copy() {
        return new PlanMealItem(calendarUri, userId, day, meal, active);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanMealItem that = (PlanMealItem) o;
        return active == that.active && Objects.equals(calendarUri, that.calendarUri) && Objects.equals(userId, that.userId) && Objects.equals(day, that.day) && Objects.equals(meal, that.meal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calendarUri, userId, day, meal, active);
    }
}
