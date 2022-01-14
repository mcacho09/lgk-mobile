package app.logistikappbeta.com.logistikapp.Interfaces;

/**
 * Created by danie on 31/07/2016.
 */
public interface TOWInterface {

    /*
        Status 1 = no checkin
        Status 2 = checkin
        Status 3 = checkin fuera de rango
    */
    void changeMarkerState(int position, int status);

    void moveToStoreListPosition(int positionMarker);

    void updateInfoTravel();

}
