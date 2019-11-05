package com.baselabs.tiljo.basestationfinder;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendRequest extends AsyncTask<String,Void, String> {

    private int cellId;
    private int localAreaCode;
    private int mobileCountryCode;
    private int mobileNetworkCode;
    String radio;

    // Constructor for SendRequest
    public SendRequest(int cellId, int localAreaCode, int mobileCountryCode,
                       int mobileNetworkCode, String radio) {
        this.cellId = cellId;
        this.localAreaCode = localAreaCode;
        this.mobileCountryCode = mobileCountryCode;
        this.mobileNetworkCode = mobileNetworkCode;
        this.radio = radio;
    }

    @Override
    protected String doInBackground(String... urls) {

        try {
            try {
                return HttpPost(urls[0]); // Send request
            } catch (JSONException e) {
                e.printStackTrace();
                return "Error!";
            }
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    private String HttpPost(String myUrl) throws IOException, JSONException {
        String result = ""; // initialize result

        URL url = new URL(myUrl);   // initialize URL
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // create Connection
        conn.setRequestMethod("POST");  // set type to POST
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        JSONObject jsonObject = new JSONObject();  // create JSONObject which is being sent

        JSONObject jsonObjectCells = new JSONObject(); // create JSONObject for cells array
        jsonObjectCells.put("lac", localAreaCode);
        jsonObjectCells.put("cid", cellId);

        JSONArray jsonArray = new JSONArray(); // JSONArray with cells (lac and cid)
        jsonArray.put(jsonObjectCells);

        jsonObject.accumulate("token", "da4e38badec5d5"); // add all params
        jsonObject.accumulate("radio", radio);
        jsonObject.accumulate("mcc", mobileCountryCode);
        jsonObject.accumulate("mnc", mobileNetworkCode);
        jsonObject.accumulate("cells", jsonArray);

        Log.i("Anfrage", jsonObject.toString());

        setPostRequestContent(conn, jsonObject);  // send the request

        conn.connect();

        InputStream inputStream = conn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        int data = inputStreamReader.read();
        while (data != -1) {
            char current = (char) data;
            result += current;                  // get the response from the server
            data = inputStreamReader.read();
        }
        return result;

    }

    private void setPostRequestContent(HttpURLConnection conn,
                                       JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(MainActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }
}
