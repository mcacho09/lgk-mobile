package app.logistikappbeta.com.logistikapp.Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import app.logistikappbeta.com.logistikapp.BuildConfig;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.ProductPV;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by danie on 25/07/2016.
 *
 */
public class Utils {

  public static final String OPTION_NAME_PREFERENCES = "options_preferences";
  public static final String USER_NAME_PREFERENCES = "user_preferences";
  public static final String PROTOCOL_SELECT_PREFERENCES = "protocol_select";
  public static final String IP_SELECT_PREFERENCES = "ip_select";
  public static final String PRIVATE_USER_TOKEN = "user_token";
  public static final String MY_UBICATION_PREFERENCES = "ubication_preferences";
  public static final String SUPPLIER_PREFERENCES = "supplier_preferences";
  public static int TOW_UBICATION_PREFERENCES = 1;

  public static String[] protocolOptions = {"http"};

  public static void createIfNotExistPreferences(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(OPTION_NAME_PREFERENCES, Context.MODE_PRIVATE);
    if (!preferences.contains(PROTOCOL_SELECT_PREFERENCES)) {
      SharedPreferences.Editor editor = preferences.edit();
      editor.putInt(PROTOCOL_SELECT_PREFERENCES, 0);
      editor.putString(IP_SELECT_PREFERENCES, "192.168.0.2:8090");
      editor.commit();
    }
  }

  public static boolean saveConnectionPreference(Context context, int protocolOptionSelected, String ipOptionSelected) {
    SharedPreferences preferences = context.getSharedPreferences(OPTION_NAME_PREFERENCES, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putInt(PROTOCOL_SELECT_PREFERENCES, protocolOptionSelected);
    editor.putString(IP_SELECT_PREFERENCES, ipOptionSelected);

    return editor.commit();
  }

  public static void saveUserPreferences(Login login, Context context) {
    SharedPreferences preferences = context.getSharedPreferences(USER_NAME_PREFERENCES, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putLong("id", login.getId());
    editor.putString("name", login.getName());
    editor.putString("login", login.getLogin());
    editor.putString("profile", login.getProfile());
    editor.putString("email", login.getEmail());
    editor.putString("sup", login.getSuperuser());
    editor.putString("ubi_time", login.getUbi_time());
    editor.commit();
  }

  public static boolean isLoggginUser(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(USER_NAME_PREFERENCES, Context.MODE_PRIVATE);
    return preferences.contains("id");
  }

  public static Login getSessionInfo(Context context) {
    Login login = null;
    SharedPreferences preferences = context.getSharedPreferences(USER_NAME_PREFERENCES, Context.MODE_PRIVATE);
    if (preferences.contains("id")) {
      login = new Login();
      login.setEmail(preferences.getString("email", ""));
      login.setId(preferences.getLong("id", 0));
      login.setLogin(preferences.getString("login", ""));
      login.setName(preferences.getString("name", ""));
      login.setProfile(preferences.getString("profile", ""));

      login.setUbi_time(preferences.getString("ubi_time", ""));
      login.setSuperuser(preferences.getString("sup", ""));
    }

    return login;
  }

  public static boolean finishSession(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(USER_NAME_PREFERENCES, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    editor.clear();
    return editor.commit();
  }

  public static String getToken() {
    SecureRandom random = new SecureRandom();
    BigInteger integer = new BigInteger(128, random);
    return integer.toString();
  }

  public static String getSaveToken(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(PRIVATE_USER_TOKEN, Context.MODE_PRIVATE);
    String token = null;
    if (!preferences.contains("user_token")) {
      token = getToken();
      SharedPreferences.Editor editor = preferences.edit();
      editor.putString("user_token", token);
      editor.commit();
    } else {
      token = preferences.getString("user_token", getToken());
    }

    return token;
  }

  public static void generateToken(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(PRIVATE_USER_TOKEN, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString("user_token", getToken());
    editor.commit();
  }

  public static boolean saveSupplier(Context context, String supplier) {
    Log.d("LGK", "Saving: " + supplier);
    SharedPreferences preferences = context.getSharedPreferences(SUPPLIER_PREFERENCES, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString("supplier", supplier);
    return editor.commit();
  }

  public static String getSupplierPreferences(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(SUPPLIER_PREFERENCES, Context.MODE_PRIVATE);
    if (!preferences.contains("supplier")) return null;
    return preferences.getString("supplier", null);
  }

  public static void deleteToken(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(PRIVATE_USER_TOKEN, Context.MODE_PRIVATE);
    preferences
        .edit()
        .clear()
        .commit();

  }

  public static void deleteSupplier(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(SUPPLIER_PREFERENCES, Context.MODE_PRIVATE);
    preferences
        .edit()
        .clear()
        .commit();
  }

  public static boolean getMyUbicationPreferences(Context context, int ubication_preferences) {
    SharedPreferences preferences = context.getSharedPreferences(MY_UBICATION_PREFERENCES, context.MODE_PRIVATE);
    switch (ubication_preferences) {
      case 1: {
        if (!preferences.contains("tow_ubication_preferences")) {
          SharedPreferences.Editor editor = preferences.edit();
          editor.putBoolean("tow_ubication_preferences", false);
          editor.commit();
        }
        return preferences.getBoolean("tow_ubication_preferences", true);
      }
    }
    return false;
  }

  public static void setMyUbicationPreferences(Context context, int ubication_preferences, boolean state) {
    SharedPreferences preferences = context.getSharedPreferences(MY_UBICATION_PREFERENCES, context.MODE_PRIVATE);
    switch (ubication_preferences) {
      case 1: {
        if (!preferences.contains("tow_ubication_preferences")) {
          SharedPreferences.Editor editor = preferences.edit();
          editor.putBoolean("tow_ubication_preferences", state);
          editor.commit();
        }
      }
    }
  }

  public static String getVersionApp() {
    return BuildConfig.VERSION_NAME;
  }

  public static boolean isOnline(Context context) {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    return networkInfo != null && networkInfo.isConnectedOrConnecting();
  }

  public static String getURL(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(Utils.OPTION_NAME_PREFERENCES, Context.MODE_PRIVATE);
    String protocol = Utils.protocolOptions[preferences.getInt("protocol_select", 0)];
    String ip = preferences.getString("ip_select", "192.168.0.2:8090");

    return protocol + "://" + (ip.isEmpty() ? "192.168.0.2:8090" : ip);
  }

  public static Retrofit buildRetrofit(Context context) {
    return new Retrofit
        .Builder()
        .baseUrl(getURL(context))
        .client(new OkHttpClient
            .Builder()
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .build())
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  public static String encodeToBas64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    image.compress(compressFormat, quality, byteArrayOutputStream);
    return "data:image/jpeg;base64," + Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
  }

  public static Bitmap decodeBase64(String code) {
    byte[] bytes = Base64.decode(code, Base64.DEFAULT);
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
  }

  /**
   * For status APR' then 'Pagado' when 'NP' then 'No Pagado' else 'Cancelado' end as status
   *
   * @param retail
   * @param date
   * @param status
   * @param seller
   * @param sale
   * @param chg
   * @param dev
   * @return body (String)
   */
  public static String getTicket(
      String supplier, String retail, String store, Date date, String status,
      String seller, ArrayList<ProductPV> sale,
      ArrayList<ProductPV> chg, ArrayList<ProductPV> dev) {

    StringBuilder body;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss a");

    String ticketInfo = "Plaza: " + retail
        + "\n"
        + "Fecha: " + dateFormat.format(date) + "\n" + "Hora: " + hourFormat.format(date)
        + "\n"
        + "Estatus: " + status
        + "\n"
        + "Vendedor: " + seller;

    Long totalProducts = 0l;
    Double totalVenta = 0d;
    Long totalProductsDev = 0l;
    Double totalVentaDev = 0d;
    Long totalProductsChg = 0l;

    String productsVta = "";
    String tmp = "";
    for (ProductPV pv : sale) {
      if(pv.getQtyproducts() > 0){
        tmp = "[" + pv.getQtyproducts() + "] " + pv.getName_short();
        int size = tmp.length() <= 32 ? tmp.length() : 32;
        tmp = tmp.substring(0, size);
        productsVta += tmp + "\n  " + pv.getPrice_sale() + " - "
                + pv.getSale() + "\n";
      }

      totalProducts += pv.getQtyproducts();
      totalVenta += pv.getSale();
      tmp = "";
    }


    String productsCHG = "";
    for (ProductPV pv : chg) {
      if(pv.getQtyproducts() > 0) {
        tmp = "[" + pv.getQtyproducts() + "] " + pv.getName_short();
        int size = tmp.length() <= 32 ? tmp.length() : 32;
        tmp = tmp.substring(0, size);
      }

      totalProductsChg += pv.getQtyproducts();
      productsCHG += tmp + "\n";
      tmp = "";
    }


    String productsDEV = "";
    for (ProductPV pv : dev) {
      if(pv.getQtyproducts() > 0) {
        tmp = "[" + pv.getQtyproducts() + "] " + pv.getName_short();
        int size = tmp.length() <= 32 ? tmp.length() : 32;
        tmp = tmp.substring(0, size);
        productsDEV += tmp + "\n";
      }

      totalProductsDev += pv.getQtyproducts();
      totalVentaDev += pv.getSale();
      tmp = "";
    }

    body = new StringBuilder(supplier.toUpperCase() + "\n\n"
        + store.toUpperCase() + "\n\n" + ticketInfo + "\n\n");

    if (totalProducts > 0) {
      body.append("================================\n"
          + "             Compra\n" + "Producto  -  " + "Precio  -  "
          + "Total" + "\n" + productsVta + "\nTotal Productos: "
          + totalProducts + "\n" + "Total Venta: $" + totalVenta
          + "\n");
    }

    if (totalProductsChg > 0) {
      body.append("\n================================\n"
          + "             Cambio\n" + "Producto" + "\n" + productsCHG);
    }
    if (totalProductsDev > 0) {
      body.append("\n================================\n"
          + "           Devolución\n" + "Producto" + "\n"
          + productsDEV + "\nTotal Productos: " + totalProductsDev
          + "\nTotal Devolucón: $" + totalVentaDev);
    }

    if (totalProducts > 0 && totalProductsDev > 0) {
      body.append("\n================================\n"
          + "          Total Ticket\n" + "$"
          + (totalVenta - totalVentaDev) + "\n"
          + "          TOTAL TICKET:\n"
          + "      VENTAS - DEVOLUCIONES");
    }

    body.append("\n\n"
        + "ESTE DOCUMENTO ES UNA NOTA DE \nVENTA Y NO ES VALIDO COMO \n\"COMPROBANTE FISCAL\""
        + "\n\n"
        + "\"  TICKET OFFLINE   \""
        + "\n\n\n");

    return body.toString();
  }

}
