package app.logistikappbeta.com.logistikapp.Results;

import app.logistikappbeta.com.logistikapp.POJOs.Comment;

/**
 * Created by danie on 26/01/2017.
 */

public class CommentResult extends Result {

  private Comment comment;

  public Comment getComment() {
    return comment;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }
}
