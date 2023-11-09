package be.vinci.pae.biz.user;

import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;

public class UserImpl implements User {

  private int id;
  private String username;
  private String firstName;
  private String lastName;
  private boolean isAdmin;
  private AddressDTO address;
  private String callNumber;
  private String password;
  private String reasonForConnectionRefusal;
  private String condition;
  private int nbrOfferOffert;
  private int nbrOfferGiven;
  private int versionMember;
  private int nbrOffertReceived;
  private int numberOfferNotPickedUp;

  public int getVersionMember() {
    return versionMember;
  }

  public void setVersionMember(int versionMember) {
    this.versionMember = versionMember;
  }

  public int getNbrOfferOffert() {
    return nbrOfferOffert;
  }

  public void setNbrOfferOffert(int nbrOfferOffert) {
    this.nbrOfferOffert = nbrOfferOffert;
  }

  public int getNbrOfferGiven() {
    return nbrOfferGiven;
  }

  public void setNbrOfferGiven(int nbrOfferGiven) {
    this.nbrOfferGiven = nbrOfferGiven;
  }

  public int getNbrOffertReceived() {
    return nbrOffertReceived;
  }

  public void setNbrOffertReceived(int nbrOffertReceived) {
    this.nbrOffertReceived = nbrOffertReceived;
  }

  public int getId() {
    return id;
  }


  public void setId(int id) {
    this.id = id;
  }


  public String getUsername() {
    return username;
  }


  public void setUsername(String userName) {
    this.username = userName;
  }


  public String getFirstName() {
    return firstName;
  }


  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }


  public String getLastName() {
    return lastName;
  }


  public void setLastName(String lastName) {
    this.lastName = lastName;
  }


  public boolean isAdmin() {
    return isAdmin;
  }


  public void setAdmin(boolean admin) {
    isAdmin = admin;
  }


  public AddressDTO getAddress() {
    return address;
  }

  @Override
  public void setAddress(AddressDTO address) {
    this.address = address;
  }

  @Override
  public String getCallNumber() {
    return callNumber;
  }

  @Override
  public void setCallNumber(String phoneNumber) {
    this.callNumber = phoneNumber;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  public String getReasonForConnectionRefusal() {
    return reasonForConnectionRefusal;
  }

  public void setReasonForConnectionRefusal(String reasonForConnectionRefusalNew) {
    reasonForConnectionRefusal = reasonForConnectionRefusalNew;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String conditi) {
    condition = conditi;
  }

  public int getNumberOfferNotPickedUp() {
    return numberOfferNotPickedUp;
  }

  public void setNumberOfferNotPickedUp(int numberOfferNotPickedUp) {
    this.numberOfferNotPickedUp = numberOfferNotPickedUp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserImpl user = (UserImpl) o;
    return Objects.equals(username, user.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username);
  }

  @Override
  public String cryptPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }


  @Override
  public boolean checkPassword(String password) {
    return BCrypt.checkpw(password, this.password);
  }

  @Override
  public boolean checkLoginUniq(String login) {
    return false;
  }
}
