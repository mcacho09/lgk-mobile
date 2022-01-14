package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Users;

/**
 * Created by danielmarin on 10/06/17.
 */

public class GetUsersResult extends Result {

  private ArrayList<Users> users;

  public ArrayList<Users> getUsers() {
    return users;
  }

  public void setUsers(ArrayList<Users> users) {
    this.users = users;
  }
}
