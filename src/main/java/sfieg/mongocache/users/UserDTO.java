package sfieg.mongocache.users;

import java.time.LocalDateTime;

public class UserDTO {

  private Long userId;
  private String login;
  private String pwd;
  private Long personId;
  private LocalDateTime lastUpdate;

  public UserDTO(Long userId, String login, String pwd, Long personId, LocalDateTime lastUpdate) {
    this.userId = userId;
    this.login = login;
    this.pwd = pwd;
    this.personId = personId;
    this.lastUpdate = lastUpdate;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public Long getPersonId() {
    return personId;
  }

  public void setPersonId(Long personId) {
    this.personId = personId;
  }

  public LocalDateTime getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(LocalDateTime lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public UserDTO() {
  }


}
