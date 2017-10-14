package com.example.udacity.earthquakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


import static com.example.udacity.earthquakereport.EarthquakeActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils{


    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 15000;

    private QueryUtils(){
    }

    public static ArrayList<Earthquake> fetchEarthquakeData(String requestUrl){

        Log.i(LOG_TAG, "fetchEarthquakeData  \n" + requestUrl);

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        ArrayList<Earthquake> earthquakes = extractEarthquakes(jsonResponse);

        return earthquakes;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch(MalformedURLException e){
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();

            String redirect = urlConnection.getHeaderField("Location");
            if(redirect != null){
                urlConnection = (HttpURLConnection) new URL(redirect).openConnection();
            }
            urlConnection.setReadTimeout(CONNECT_TIMEOUT);
            urlConnection.setConnectTimeout(READ_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch(IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset
                    .forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<Earthquake> extractEarthquakes(String jSonResponse){

        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        try{

            JSONObject mainObj = new JSONObject(jSonResponse);
            JSONArray featuresArray = mainObj.getJSONArray("features");
            JSONObject features;
            JSONObject properties;
            Double magnitude;
            String place;
            Long time;
            String url;
            Earthquake quake;

            if(featuresArray != null){

                for(int i = 0; i < featuresArray.length(); i++){

                    features = featuresArray.getJSONObject(i);
                    properties = features.getJSONObject("properties");
                    magnitude = properties.getDouble("mag");
                    place = properties.getString("place");
                    time = properties.getLong("time");
                    url = properties.getString("url");
                    quake = new Earthquake(magnitude, place, time, url);
                    earthquakes.add(quake);
                }

            }

        }catch(JSONException e){
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return earthquakes;
    }

}
