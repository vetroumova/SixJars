package org.itstep.android5.vetroumova.newbeginning.sixjars.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by OLGA on 11.09.2016.
 */
public class Cashflow extends RealmObject {

    public static final String ID_FIELD = "id";
    public static final String DATE_FIELD = "date";
    public static final String SUM_FIELD = "sum";
    public static final String CURR_PERC_FIELD = "currpercent";
    public static final String DESC_FIELD = "description";
    public static final String PHOTO_FIELD = "photo";
    public static final String JAR_FIELD = "jar";
    //public static final String LUTLONG_FIELD = "lutlong";

    @PrimaryKey
    private long id;
    // from "Realm, generating RealmController.getDatabaseManagerInstance().getBooks().size() + System.currentTimeMillis()
    //in SixJars - RealmManager.getDatabaseManagerInstance().getJars().size() + System.currentTimeMillis();

    private Date date;
    private float sum;
    private int currpercent;
    private String description;
    private String photo;   //TODO path to photo
    private Jar jar;
    //TODO
    //private Point lutLong;

    public Cashflow() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public int getCurrpercent() {
        return currpercent;
    }

    public void setCurrpercent(int currpercent) {
        this.currpercent = currpercent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Jar getJar() {
        return jar;
    }

    public void setJar(Jar jar) {
        this.jar = jar;
    }

    /*public Point getLutLong() {
        return lutLong;
    }

    public void setLutLong(Point lutLong) {
        this.lutLong = lutLong;
    }*/
}
