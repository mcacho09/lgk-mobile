package app.logistikappbeta.com.logistikapp.Results;

/**
 * Created by danie on 17/01/2017.
 */

public class Result {

  private Long timestamp;
  private int status;
  private String message;

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
