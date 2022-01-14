package app.logistikappbeta.com.logistikapp.Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by danie on 23/07/2016.
 */
public class WebService {

  private static final String TAG = "Logistikapp";

  private String principalUrl = "";
  private URL url;
  private String params;
  private String finalpath;
  private Context context;
  private int mConnectionStatic = 5000;
  private int mReadTimeOut = 5000;


  public WebService(Context context) {

    this.context = context;

    SharedPreferences preferences = context.getSharedPreferences(Utils.OPTION_NAME_PREFERENCES, Context.MODE_PRIVATE);
    String protocol = Utils.protocolOptions[preferences.getInt("protocol_select", 0)];
    String ip = preferences.getString("ip_select", "");

    this.principalUrl = protocol + "://" + ip;

  }
  public void setFinalpath(String finalpath) {
    this.finalpath = finalpath;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public void createURL() throws MalformedURLException {
    this.url = new URL(this.principalUrl + this.finalpath);
  }

  public String getData() {
    if (url != null) {
      BufferedReader reader = null;
      try {
        Log.i(TAG, "URL: " + this.principalUrl + this.finalpath);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        StringBuilder stringBuilder = new StringBuilder();
        reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
        String data = null;
        while ((data = reader.readLine()) != null) {
          stringBuilder.append(data);
        }
        if (reader != null) {
          reader.close();
        }
        return stringBuilder.toString();
      } catch (IOException ex) {
        Log.e(TAG, ex.getMessage());
        return null;
      }
    } else {
      Log.w(TAG, "No se inicio la url");
      return null;
    }
  }

  public String getDataPost() {
    BufferedReader reader = null;
    if (url != null) {
      try {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setConnectTimeout(this.mConnectionStatic);
        conn.setReadTimeout(this.mReadTimeOut);

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.addRequestProperty("Accept-Charset", "UTF-8");
        conn.addRequestProperty("Content-Type", "application/json;charset=UTF-8");

        conn.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));

        byte[] vars = this.params.getBytes("UTF-8");

        wr.write(vars, 0, vars.length);
        wr.flush();
        wr.close();

        conn.connect();

        int responseCode = conn.getResponseCode();
        Log.i(TAG, "Response_CODE: " + responseCode);
        Log.i(TAG, "URL: " + this.principalUrl + this.finalpath);
        Log.i(TAG, "Params: " + this.params);

        reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder builder = new StringBuilder();

        String line = null;

        while ((line = reader.readLine()) != null) {
          builder.append(line);
        }

        return builder.toString();
      } catch (IOException ex) {
        Log.e(TAG, "GETDATAPOST ERROR 1:\n" + ex.getMessage());
        return null;
      } finally {
        try {
          if (reader != null) {
            reader.close();
          }
        } catch (IOException ex) {
          Log.e(TAG, "GETDATAPOST ERROR 2:\n" + ex.getMessage());
          return null;
        }
      }
    } else {
      Log.w(TAG, "No se inicio la url");
      return null;
    }

  }

  public int getmConnectionStatic() {
    return mConnectionStatic;
  }

  public void setmConnectionStatic(int mConnectionStatic) {
    this.mConnectionStatic = mConnectionStatic;
  }

  public int getmReadTimeOut() {
    return mReadTimeOut;
  }

  public void setmReadTimeOut(int mReadTimeOut) {
    this.mReadTimeOut = mReadTimeOut;
  }
}
