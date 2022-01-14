package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.MessageList;

/**
 * Created by danie on 26/01/2017.
 */

public class MessageListResult extends Result {

  private ArrayList<MessageList> chats;

  public ArrayList<MessageList> getChats() {
    return chats;
  }

  public void setChats(ArrayList<MessageList> chats) {
    this.chats = chats;
  }
}
