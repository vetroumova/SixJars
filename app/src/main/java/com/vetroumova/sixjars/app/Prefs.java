package com.vetroumova.sixjars.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by OLGA on 14.09.2016.
 */
public class Prefs {

    private static final String PRE_LOAD = "preLoad";
    private static final String PREFS_NAME = "prefs";
    private static final String PERC_NEC = "NEC";
    private static final String PERC_PLAY = "PLAY";
    private static final String PERC_GIVE = "GIVE";
    private static final String PERC_EDU = "EDU";
    private static final String PERC_LTSS = "LTSS";
    private static final String PERC_FFA = "FFA";
    private static final String MAX_NEC = "max_sum_NEC";
    private static final String MAX_PLAY = "max_sum_PLAY";
    private static final String MAX_GIVE = "max_sum_GIVE";
    private static final String MAX_EDU = "max_sum_EDU";
    private static final String MAX_LTSS = "max_sum_LTSS";
    private static final String MAX_FFA = "max_sum_FFA";
    private static final String PREF_LANGUAGE = "chosen language";
    private static final String PREF_USER = "user";
    private static final String PREF_RESTORE = "restored";
    private static Prefs prefsInstance;
    private final SharedPreferences sharedPreferences;

    public Prefs(Context context) {

        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    public static Prefs with(Context context) {

        if (prefsInstance == null) {
            prefsInstance = new Prefs(context);
        }
        return prefsInstance;
    }

    public boolean getPreLoad() {
        return sharedPreferences.getBoolean(PRE_LOAD, false);
    }

    public void setPreLoad(boolean totalTime) {
        sharedPreferences
                .edit()
                .putBoolean(PRE_LOAD, totalTime)
                .apply();
    }

    public List<Integer> getPercentage() {
        List<Integer> percentJars = new ArrayList<>();
        percentJars.add(sharedPreferences.getInt(PERC_NEC, 55));
        percentJars.add(sharedPreferences.getInt(PERC_PLAY, 10));
        percentJars.add(sharedPreferences.getInt(PERC_GIVE, 5));
        percentJars.add(sharedPreferences.getInt(PERC_EDU, 10));
        percentJars.add(sharedPreferences.getInt(PERC_LTSS, 10));
        percentJars.add(sharedPreferences.getInt(PERC_FFA, 10));
        return percentJars;
    }

    public void setPercentage(List<Integer> percentJars) {
        sharedPreferences
                .edit()
                .putInt(PERC_NEC, percentJars.get(0))
                .putInt(PERC_PLAY, percentJars.get(1))
                .putInt(PERC_GIVE, percentJars.get(2))
                .putInt(PERC_EDU, percentJars.get(3))
                .putInt(PERC_LTSS, percentJars.get(4))
                .putInt(PERC_FFA, percentJars.get(5))
                .apply();
    }

    public int getPercentJar(String id) {
        int percent = 0;
        switch (id) {
            case PERC_NEC: {
                percent = sharedPreferences.getInt(PERC_NEC, 55);
                break;
            }
            case PERC_PLAY: {
                percent = sharedPreferences.getInt(PERC_PLAY, 10);
                break;
            }
            case PERC_GIVE: {
                percent = sharedPreferences.getInt(PERC_GIVE, 5);
                break;
            }
            case PERC_EDU: {
                percent = sharedPreferences.getInt(PERC_EDU, 10);
                break;
            }
            case PERC_LTSS: {
                percent = sharedPreferences.getInt(PERC_LTSS, 10);
                break;
            }
            case PERC_FFA: {
                percent = sharedPreferences.getInt(PERC_FFA, 10);
                break;
            }
        }
        return percent;
    }

    public float[] getMaxVolumes() {
        float[] maxVolumesJars = new float[6];
        maxVolumesJars[0] = sharedPreferences.getFloat(MAX_NEC, 55f);
        maxVolumesJars[1] = sharedPreferences.getFloat(MAX_PLAY, 10f);
        maxVolumesJars[2] = sharedPreferences.getFloat(MAX_GIVE, 10f);
        maxVolumesJars[3] = sharedPreferences.getFloat(MAX_EDU, 5f);
        maxVolumesJars[4] = sharedPreferences.getFloat(MAX_LTSS, 10f);
        maxVolumesJars[5] = sharedPreferences.getFloat(MAX_FFA, 10f);
        return maxVolumesJars;
    }

    public void setMaxVolumes(float[] maxVolumesJars) {
        sharedPreferences
                .edit()
                .putFloat(MAX_NEC, maxVolumesJars[0])
                .putFloat(MAX_PLAY, maxVolumesJars[1])
                .putFloat(MAX_GIVE, maxVolumesJars[2])
                .putFloat(MAX_EDU, maxVolumesJars[3])
                .putFloat(MAX_LTSS, maxVolumesJars[4])
                .putFloat(MAX_FFA, maxVolumesJars[5])
                .apply();
    }

    public void setMaxVolumeInJar(float maxVolume, String id) {

        //TODO check the maxValue scheme - just total after income
        //maxVolume in params - new TotalSum in jar
        String maxVolumePrefID = "max_sum_".concat(id);
        /*float prevMaxVolume = sharedPreferences.getFloat(maxVolumePrefID, maxVolume);
        if (prevMaxVolume > maxVolume) {
            maxVolume = (prevMaxVolume + maxVolume) / 2;
        }*/
        sharedPreferences
                .edit()
                .putFloat(maxVolumePrefID, maxVolume)
                .apply();
        Log.d("VOlga", "new maxVolume: " + maxVolume + " in jar " + id);
    }

    public float getMaxVolumeJar(String id) {
        float maxVolume = 0;
        switch (id) {
            case PERC_NEC: {
                maxVolume = sharedPreferences.getFloat(MAX_NEC, 55f);
                break;
            }
            case PERC_PLAY: {
                maxVolume = sharedPreferences.getFloat(MAX_PLAY, 10f);
                break;
            }
            case PERC_GIVE: {
                maxVolume = sharedPreferences.getFloat(MAX_GIVE, 5f);
                break;
            }
            case PERC_EDU: {
                maxVolume = sharedPreferences.getFloat(MAX_EDU, 10f);
                break;
            }
            case PERC_LTSS: {
                maxVolume = sharedPreferences.getFloat(MAX_LTSS, 10f);
                break;
            }
            case PERC_FFA: {
                maxVolume = sharedPreferences.getFloat(MAX_FFA, 10f);
                break;
            }
        }
        return maxVolume;
    }

    /*public boolean saveSharedPreferencesToFile(File dst) {
        boolean res = false;
        ObjectOutputStream output = null;
        try {
            output = new ObjectOutputStream(new FileOutputStream(dst));
            SharedPreferences pref = sharedPreferences;
            output.writeObject(pref.getAll());

            res = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return res;
    }

    @SuppressWarnings({ "unchecked" })
    public boolean loadSharedPreferencesFromFile(File src) {
        boolean res = false;
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(new FileInputStream(src));
            //SharedPreferences.Editor prefEdit = getSharedPreferences(prefName, MODE_PRIVATE).edit();
            SharedPreferences.Editor prefEdit = sharedPreferences.edit();
            prefEdit.clear();
            Map<String, ?> entries = (Map<String, ?>) input.readObject();
            for (Map.Entry<String, ?> entry : entries.entrySet()) {
                Object v = entry.getValue();
                String key = entry.getKey();

                if (v instanceof Boolean)
                    prefEdit.putBoolean(key, (Boolean) v);
                else if (v instanceof Float)
                    prefEdit.putFloat(key, (Float) v);
                else if (v instanceof Integer)
                    prefEdit.putInt(key, (Integer) v);
                else if (v instanceof Long)
                    prefEdit.putLong(key, (Long) v);
                else if (v instanceof String)
                    prefEdit.putString(key, ((String) v));
            }
            //prefEdit.commit();
            prefEdit.apply();
            res = true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return res;
    }

    //was static
    public boolean restoreUserPrefs(File src) {
        //final File backupFile = new File(context.getExternalFilesDir(null),"preferenceBackup.xml");
        String error = "";

        try {
            *//*SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();*//*
            SharedPreferences.Editor prefEdit = sharedPreferences.edit();
            prefEdit.clear();   //added
            InputStream inputStream = new FileInputStream(src);

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.parse(inputStream);
            Element root = doc.getDocumentElement();

            Node child = root.getFirstChild();
            while(child!=null) {
                if(child.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element)child;

                    String type = element.getNodeName();
                    String name = element.getAttribute("name");

                    // In my app, all prefs seem to get serialized as either "string" or
                    // "boolean" - this will need expanding if yours uses any other types!
                    if(type.equals("string")) {
                        String value = element.getTextContent();
                        prefEdit.putString(name, value);
                    }
                    else if(type.equals("boolean")) {
                        String value = element.getAttribute("value");
                        prefEdit.putBoolean(name, value.equals("true"));
                    }
                    else if(type.equals("float")) {
                        String value = element.getAttribute("value");
                        prefEdit.putFloat(name, Float.parseFloat(value));
                    }
                    else if(type.equals("int")) {
                        String value = element.getAttribute("value");
                        prefEdit.putInt(name, Integer.parseInt(value));
                    }
                }
                child = child.getNextSibling();
            }
            prefEdit.commit();

            *//*Toast toast = Toast.makeText(context, "Restored user prefs from " +
                    backupFile.getAbsolutePath(), Toast.LENGTH_SHORT);
            toast.show();*//*
            return true;

        } catch (FileNotFoundException e) {
            error = e.getMessage();
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            error = e.getMessage();
            e.printStackTrace();
        } catch (SAXException e) {
            error = e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            error = e.getMessage();
            e.printStackTrace();
        }
        *//*Toast toast = Toast.makeText(context, "Failed to restore user prefs from " +
                backupFile.getAbsolutePath()+ " - "+error, Toast.LENGTH_SHORT);
        toast.show();*//*
        return false;
    }*/

    public String getPrefLanguage() {
        return sharedPreferences.getString(PREF_LANGUAGE, "default");
    }

    public void setPrefLanguage(String language) {
        if (language.equals("")) {
            language = "default";
        }
        sharedPreferences
                .edit()
                .putString(PREF_LANGUAGE, language)
                .apply();
    }

    public String getPrefUser() {
        return sharedPreferences.getString(PREF_USER, "test");
    }

    public void setPrefUser(String login) {
        if (login.equals("")) {
            login = "test";
        }
        sharedPreferences
                .edit()
                .putString(PREF_USER, login)
                .apply();
    }

    public boolean getPrefRestoreMark() {
        return sharedPreferences.getBoolean(PREF_RESTORE, false);
    }

    public void setPrefRestoreMark(boolean resroreMark) {
        sharedPreferences
                .edit()
                .putBoolean(PREF_RESTORE, resroreMark)
                .apply();
    }
}
