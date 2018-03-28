package uk.co.barbuzz.tofind.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import uk.co.barbuzz.tofind.model.Site;

/**
 * Created by andy.barber on 25/03/2018.
 */

public class Utils {

    /**
     * get resource id from a string
     * @param context
     * @param pVariableName
     * @param pResourcename
     * @param pPackageName
     * @return
     */
    public static int getResourceId(Context context, String pVariableName, String pResourcename, String pPackageName)
    {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * read json file from assets into String
     * @param context
     * @param fileName
     * @return - json string
     */
    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * parse a json string with GSON into the Site class
     * @param jsonString
     * @return List<Site> list of locations
     */
    public static List<Site> parseLocationJsonString(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, new TypeToken<List<Site>>(){}.getType());
    }
}
