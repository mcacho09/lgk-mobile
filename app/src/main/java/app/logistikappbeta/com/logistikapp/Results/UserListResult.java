package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.UserChat;

/**
 * Created by danie on 26/01/2017.
 */

public class UserListResult extends Result {

  private ArrayList<UserChat> users;

  public ArrayList<UserChat> getUsers() {
    return users;
  }

  public void setUsers(ArrayList<UserChat> users) {
    this.users = users;
  }
}
