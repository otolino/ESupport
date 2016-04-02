package com.brainupco.esupport;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by jsantos on 01/abr/2016.
 */
public class MobileLocationWS extends AsyncTask {

    public final String LOG_TAG = MobileLocationWS.class.getSimpleName();

    @Override
    protected Object doInBackground(Object[] params) {

        try {
            // Send DUMMY Location
            sendLocation("868442014378892", 19.4984447, -99.16916719999999);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void sendLocation(String IMEI, Double latitude, Double longitude) throws Exception {

        Log.d(LOG_TAG, "Sending Location");

        // Use URl Connection to Call Web Service
        HttpURLConnection urlConnection = null;

        try {

//            SoapObject.request = new SoapObject(NAMESPACE, METHOD_NAME);
//            request.addProperty("FullDate", FullDate);

            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String WS_BASE_URL =
                    "http://web-mobilelocation.azurewebsites.net/services/UpdateLocation";
            final String UPDATE_LOCATION_PATH = "/services/LocationServices.asmx";

            Uri builtSOAPUri = Uri.parse(WS_BASE_URL)
                    .buildUpon()
                    .appendPath(UPDATE_LOCATION_PATH).build();
//                    .buildUpon()
//                    .appendQueryParameter(OPTION_PARAM, "UpdateLocation")
//                    .build();


            // Create Request
            URL url = new URL(WS_BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();

            // Create XML Soap Envelope
            StringBuilder soapEnvelope = new StringBuilder("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <s:Header>\n" +
                    "    <Action s:mustUnderstand=\"1\" xmlns=\"http://schemas.microsoft.com/ws/2005/05/addressing/none\">http://web-mobilelocation.azurewebsites.net/services/UpdateLocation</Action>\n" +
                    "  </s:Header>\n" +
                    "  <s:Body>\n" +
                    "    <UpdateLocation xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://web-mobilelocation.azurewebsites.net/services\">\n" +
                    "      <imei>868442014378892</imei>\n" +
                    "      <lat>19.4984418</lat>\n" +
                    "      <lon>-99.16917079999999</lon>\n" +
                    "    </UpdateLocation>\n" +
                    "  </s:Body>\n" +
                    "</s:Envelope>");

//            soapEnvelope.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\n")
//                    .append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">").append("\n")
//                    .append("<soap:Body>").append("\n")
//                    .append("<UpdateLocation xmlns=\"http://web-mobilelocation.azurewebsites.net/services\">").append("\n")
//                    .append("<imei>string</imei>").append("\n")
//                    .append("<lat>string</lat>").append("\n")
//                    .append("<lon>string</lon>").append("\n")
//                    .append("</UpdateLocation>").append("\n")
//                    .append("</soap:Body>").append("\n")
//                    .append("</soap:Envelope>").append("\n")
//                    .append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//                            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
//                            "    <soap:Body>\n" +
//                            "        <UpdateLocation xmlns=\"http://web-mobilelocation.azurewebsites.net/services\">\n" +
//                            "            <imei>string</imei>\n" +
//                            "            <lat>string</lat>\n" +
//                            "            <lon>string</lon>\n" +
//                            "        </UpdateLocation>\n" +
//                            "    </soap:Body>\n" +
//                            "</soap:Envelope>").append("\n")


            urlConnection.setRequestMethod("POST");
//            urlConnection.addRequestProperty("imei", IMEI);
//            urlConnection.addRequestProperty("lat", Double.toString(longitude));
//            urlConnection.addRequestProperty("lon", Double.toString(latitude));
            urlConnection.setDoOutput(true);
//            urlConnection.addRequestProperty("Connection", "keep-alive");
//            urlConnection.addRequestProperty("SendChunked", "True");
            urlConnection.addRequestProperty("Content-Type", "text/xml");
            urlConnection.addRequestProperty("Content-Length", String.valueOf(soapEnvelope.length()));
            urlConnection.addRequestProperty("SOAPAction", "http://web-mobilelocation.azurewebsites.net/Services/LocatorService.asmx");
            //urlConnection.connect();

            // request data in soap format
            Log.v(LOG_TAG, "Request data in soap format");
            OutputStream os = urlConnection.getOutputStream();
            os.write(soapEnvelope.toString().getBytes());

            InputStream response = urlConnection.getInputStream(); //receive response

            // Read response
            StringBuilder responseSB = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            while ( (line = br.readLine()) != null)
                responseSB.append(line);

            // Close streams
            br.close();
            os.close();
            // for now, we just ignore response
            // DO NOTHING

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

    }

}

