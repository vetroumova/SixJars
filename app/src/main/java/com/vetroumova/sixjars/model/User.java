package com.vetroumova.sixjars.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by OLGA on 11.09.2016.
 */
public class User extends RealmObject {

    public static final String LOGIN_FIELD = "login";
    public static final String PASSWORD_FIELD = "password";
    public static final String EMAIL_FIELD = "email";
    public static final String LANG_FIELD = "language";
    public static final String PRELOAD_FIELD = "preLoad";
    public static final String NEC_FIELD = "necPerc";
    public static final String PLAY_FIELD = "playPerc";
    public static final String GIVE_FIELD = "givePerc";
    public static final String EDU_FIELD = "eduPerc";
    public static final String LTSS_FIELD = "ltssPerc";
    public static final String FFA_FIELD = "ffaPerc";
    public static final String NEC_MAX = "necMaxVolume";
    public static final String PLAY_MAX = "playMaxVolume";
    public static final String GIVE_MAX = "giveMaxVolume";
    public static final String EDU_MAX = "eduMaxVolume";
    public static final String LTSS_MAX = "ltssMaxVolume";
    public static final String FFA_MAX = "ffaMaxVolume";

    @PrimaryKey
    private String login;

    private String password;
    private String email;
    private String language;
    private boolean preLoad;
    private int necPerc;
    private int playPerc;
    private int givePerc;
    private int eduPerc;
    private int ltssPerc;
    private int ffaPerc;

    private float necMaxVolume;
    private float playMaxVolume;
    private float giveMaxVolume;
    private float eduMaxVolume;
    private float ltssMaxVolume;
    private float ffaMaxVolume;

    public User() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isPreLoad() {
        return preLoad;
    }

    public void setPreLoad(boolean preLoad) {
        this.preLoad = preLoad;
    }

    public int getNecPerc() {
        return necPerc;
    }

    public void setNecPerc(int necPerc) {
        this.necPerc = necPerc;
    }

    public int getPlayPerc() {
        return playPerc;
    }

    public void setPlayPerc(int playPerc) {
        this.playPerc = playPerc;
    }

    public int getGivePerc() {
        return givePerc;
    }

    public void setGivePerc(int givePerc) {
        this.givePerc = givePerc;
    }

    public int getEduPerc() {
        return eduPerc;
    }

    public void setEduPerc(int eduPerc) {
        this.eduPerc = eduPerc;
    }

    public int getLtssPerc() {
        return ltssPerc;
    }

    public void setLtssPerc(int ltssPerc) {
        this.ltssPerc = ltssPerc;
    }

    public int getFfaPerc() {
        return ffaPerc;
    }

    public void setFfaPerc(int ffaPerc) {
        this.ffaPerc = ffaPerc;
    }

    public float getNecMaxVolume() {
        return necMaxVolume;
    }

    public void setNecMaxVolume(float necMaxVolume) {
        this.necMaxVolume = necMaxVolume;
    }

    public float getPlayMaxVolume() {
        return playMaxVolume;
    }

    public void setPlayMaxVolume(float playMaxVolume) {
        this.playMaxVolume = playMaxVolume;
    }

    public float getGiveMaxVolume() {
        return giveMaxVolume;
    }

    public void setGiveMaxVolume(float giveMaxVolume) {
        this.giveMaxVolume = giveMaxVolume;
    }

    public float getEduMaxVolume() {
        return eduMaxVolume;
    }

    public void setEduMaxVolume(float eduMaxVolume) {
        this.eduMaxVolume = eduMaxVolume;
    }

    public float getLtssMaxVolume() {
        return ltssMaxVolume;
    }

    public void setLtssMaxVolume(float ltssMaxVolume) {
        this.ltssMaxVolume = ltssMaxVolume;
    }

    public float getFfaMaxVolume() {
        return ffaMaxVolume;
    }

    public void setFfaMaxVolume(float ffaMaxVolume) {
        this.ffaMaxVolume = ffaMaxVolume;
    }
}
