package com.example.foodplanner.features.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Category implements Parcelable {

    @SerializedName("idCategory")
    private final String id;
    @SerializedName("strCategory")
    private final String name;
    @SerializedName("strCategoryThumb")
    private final String thumbnail;
    @SerializedName("strCategoryDescription")
    private final String description;

    public Category(String id, String name, String thumbnail, String description) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    protected Category(Parcel in) {
        id = in.readString();
        name = in.readString();
        thumbnail = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(thumbnail);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
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

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name) && Objects.equals(thumbnail, category.thumbnail) && Objects.equals(description, category.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, thumbnail, description);
    }
}
