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
    public static final String JAR_FIELD = "jar";
    //public static final String LUTLONG_FIELD = "lutlong";

    @PrimaryKey
    private long id;
    // from "Realm, generating RealmController.getDatabaseManagerInstance().getBooks().size() + System.currentTimeMillis()
    //in SixJars - RealmManager.getDatabaseManagerInstance().getJars().size() + System.currentTimeMillis();

    private Date date;
    private float sum;
    private int currpercent;
    private Jar jar;
    //TODO
    //private String description;
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
