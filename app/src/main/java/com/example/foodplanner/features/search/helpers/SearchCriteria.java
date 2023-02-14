package com.example.foodplanner.features.search.helpers;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchCriteria implements Parcelable {
    private final Type type;
    private final String criteria;

    public SearchCriteria(Type type, String criteria) {
        this.type = type;
        this.criteria = criteria;
    }

    protected SearchCriteria(Parcel in) {
        type = (Type) in.readSerializable();
        criteria = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(type);
        dest.writeString(criteria);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchCriteria> CREATOR = new Creator<SearchCriteria>() {
        @Override
        public SearchCriteria createFromParcel(Parcel in) {
            return new SearchCriteria(in);
        }

        @Override
        public SearchCriteria[] newArray(int size) {
            return new SearchCriteria[size];
        }
    };

    public Type getType() {
        return type;
    }

    public String getCriteria() {
        return criteria;
    }

    public enum Type {
        QUERY,
        INGREDIENT,
        AREA,
        CATEGORY
    }
}
