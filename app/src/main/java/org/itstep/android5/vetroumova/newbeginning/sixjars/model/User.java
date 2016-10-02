package org.itstep.android5.vetroumova.newbeginning.sixjars.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by OLGA on 11.09.2016.
 */
public class User extends RealmObject {

    public static final String LOGIN_FIELD = "login";
    public static final String PASSWORD_FIELD = "password";
    public static final String EMAIL_FIELD = "email";
    public static final String JARS_FIELD = "jars";

    @PrimaryKey
    private String login;

    private String password;
    private String email;
    private RealmList<Jar> jars;

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

    public RealmList<Jar> getJars() {
        return jars;
    }

    public void setJars(RealmList<Jar> jars) {
        this.jars = jars;
    }
}
