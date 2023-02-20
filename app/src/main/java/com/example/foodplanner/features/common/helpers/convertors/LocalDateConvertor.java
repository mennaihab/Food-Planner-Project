package com.example.foodplanner.features.common.helpers.convertors;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class LocalDateConvertor {

    @TypeConverter
    public static LocalDate toDate(Long dateLong){
        if (dateLong == null) {
            return null;
        } else {
            return Instant.ofEpochMilli(dateLong).atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

    @TypeConverter
    public static Long fromDate(LocalDate date) {
        if (date == null) {
            return null;
        } else {
            return date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        }
    }
}