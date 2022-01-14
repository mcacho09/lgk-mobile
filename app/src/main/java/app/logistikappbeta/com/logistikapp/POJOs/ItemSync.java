package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danielmarin on 04/12/17.
 */

public class ItemSync {
  private String title;
  private int state; // 1: No Sync, 2: Sync, 3: Finish

  public ItemSync(String title, int state) {
    this.title = title;
    this.state = state;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }
}
