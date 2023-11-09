package be.vinci.pae.biz.user;

public interface User extends UserDTO {

  /**
   * encrypt the password.
   *
   * @param password String
   * @return crypted password
   */
  String cryptPassword(String password);

  /**
   * checks the password.
   *
   * @param password String
   * @return true if password correct, false if not
   */
  boolean checkPassword(String password);

  /**
   * checks if the login is uniq.
   *
   * @param login String
   * @return true if login uniq, false if login already exist
   */
  boolean checkLoginUniq(String login);
}
