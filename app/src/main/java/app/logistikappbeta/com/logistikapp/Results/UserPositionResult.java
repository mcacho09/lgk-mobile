package app.logistikappbeta.com.logistikapp.Results;

import app.logistikappbeta.com.logistikapp.POJOs.UserPosition;

/**
 * Created by danie on 27/01/2017.
 */

public class UserPositionResult extends Result {

  private UserPosition user_position;

  public UserPosition getUser_position() {
    return user_position;
  }

  public void setUser_position(UserPosition user_position) {
    this.user_position = user_position;
  }
}
