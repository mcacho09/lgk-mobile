package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danielmarin on 10/11/17.
 */

public class Active {

  private Integer idActives;
  private String code;
  private String image;
  private String visited;
  private String last_visited;
  private String problems;

  public Integer getIdActives() {
    return idActives;
  }

  public void setIdActives(Integer idActives) {
    this.idActives = idActives;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getVisited() {
    return visited;
  }

  public void setVisited(String visited) {
    this.visited = visited;
  }

  public String getLast_visited() {
    return last_visited;
  }

  public void setLast_visited(String last_visited) {
    this.last_visited = last_visited;
  }

  public String getProblems() {
    return problems;
  }

  public void setProblems(String problems) {
    this.problems = problems;
  }
}
