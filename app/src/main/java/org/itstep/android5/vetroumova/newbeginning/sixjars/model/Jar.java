package org.itstep.android5.vetroumova.newbeginning.sixjars.model;

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
    //TODO CHECK necessity of this field
    public static final String TOTAL_FIELD = "totalCash";
    //public static final String COLOR_FIELD = "color";

    @PrimaryKey
    private String jar_id;

    private String jar_name;
    private String jar_info;
    //private RealmList<Cashflow> cashflows;
    //TODO method getCountTotal() summing all of Cashflow sums, initial 0
    private float totalCash;
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

    /*public RealmList<Cashflow> getCashflows() {
        return cashflows;
    }
*/
    /*public void setCashflows(RealmList<Cashflow> cashflows) {
        this.cashflows = cashflows;
    }*/

    public float getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(float totalCash) {
        this.totalCash = totalCash;
    }


    /*public int getJar_color() {
        return jar_color;
    }

    public void setJar_color(int jar_color) {
        this.jar_color = jar_color;
    }*/
}
