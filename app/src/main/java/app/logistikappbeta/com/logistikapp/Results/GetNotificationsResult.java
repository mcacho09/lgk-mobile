package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Notification;

/**
 * Created by danielmarin on 10/06/17.
 */

public class GetNotificationsResult extends Result {

  private ArrayList<Notification> notifications;

  public ArrayList<Notification> getNotifications() {
    return notifications;
  }

  public void setNotifications(ArrayList<Notification> notifications) {
    this.notifications = notifications;
  }
}
