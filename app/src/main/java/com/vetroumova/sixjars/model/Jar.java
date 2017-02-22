package com.vetroumova.sixjars.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by OLGA on 11.09.2016.
 */
public class Jar extends RealmObject {

    public static final String ID_FIELD = "jar_id";
    public static final String NAME_FIELD = "jar_name";
    public static final String INFO_FIELD = "jar_info";
    public static final String CASH_FIELD = "cashflows";
    public static final String TOTAL_FIELD = "totalCash";
    public static final String USER_FIELD = "user";
    //public static final String COLOR_FIELD = "jar_color";

    @PrimaryKey
    private String jar_id;

    private String jar_name;
    private String jar_info;
    private float totalCash;
    private float jar_float_id;
    private User user;
    //private int jar_color;

    public Jar() {
    }

    public String getJar_id() {
        return jar_id;
    }

    public void setJar_id(String jar_id) {
        this.jar_id = jar_id;
    }

    public String getJar_name() {
        return jar_name;
    }

    public void setJar_name(String jar_name) {
        this.jar_name = jar_name;
    }

    public String getJar_info() {
        return jar_info;
    }

    public void setJar_info(String jar_info) {
        this.jar_info = jar_info;
    }

    public float getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(float totalCash) {
        this.totalCash = totalCash;
    }

    public float getJar_float_id() {
        return jar_float_id;
    }

    public void setJar_float_id(float jar_float_id) {
        this.jar_float_id = jar_float_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
