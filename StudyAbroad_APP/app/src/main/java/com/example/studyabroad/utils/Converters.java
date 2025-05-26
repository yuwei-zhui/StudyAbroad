package com.example.studyabroad.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Type converters for Room database
 */
public class Converters {
    
    private static final Gson gson = new Gson();
    
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
    
    @TypeConverter
    public static List<String> fromString(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return value == null ? new ArrayList<>() : gson.fromJson(value, listType);
    }
    
    @TypeConverter
    public static String fromList(List<String> list) {
        return list == null ? null : gson.toJson(list);
    }
    
    @TypeConverter
    public static List<Integer> fromIntString(String value) {
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return value == null ? new ArrayList<>() : gson.fromJson(value, listType);
    }
    
    @TypeConverter
    public static String fromIntList(List<Integer> list) {
        return list == null ? null : gson.toJson(list);
    }
    
    @TypeConverter
    public static List<Double> fromDoubleString(String value) {
        Type listType = new TypeToken<List<Double>>() {}.getType();
        return value == null ? new ArrayList<>() : gson.fromJson(value, listType);
    }
    
    @TypeConverter
    public static String fromDoubleList(List<Double> list) {
        return list == null ? null : gson.toJson(list);
    }
} 