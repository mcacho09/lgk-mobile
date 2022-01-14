package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Conversacion;

/**
 * Created by danie on 26/01/2017.
 */

public class MessageResult extends Result {

  private ArrayList<Conversacion> messages;

  public ArrayList<Conversacion> getMessages() {
    return messages;
  }

  public void setMessages(ArrayList<Conversacion> messages) {
    this.messages = messages;
  }
}
