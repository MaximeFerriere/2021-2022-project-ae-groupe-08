package be.vinci.pae.biz.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  int getId();

  void setId(int id);

  String getUsername();

  void setUsername(String userName);

  String getFirstName();

  void setFirstName(String firstName);

  String getLastName();

  void setLastName(String lastName);

  boolean isAdmin();

  void setAdmin(boolean admin);

  AddressDTO getAddress();

  void setAddress(AddressDTO address);

  String getCallNumber();

  void setCallNumber(String phoneNumber);

  String getPassword();

  void setPassword(String password);

  String getReasonForConnectionRefusal();

  void setReasonForConnectionRefusal(String reasonForConnectionRefusalNew);

  String getCondition();

  void setCondition(String condition);

  int getNbrOfferOffert();

  void setNbrOfferOffert(int nbrOfferOffert);

  int getNbrOfferGiven();

  void setNbrOfferGiven(int nbrOfferGiven);

  int getNumberOfferNotPickedUp();

  void setNumberOfferNotPickedUp(int numberOfferNotPickedUp);


  int getNbrOffertReceived();

  void setNbrOffertReceived(int nbrOffertReceived);

  int getVersionMember();

  void setVersionMember(int versionMember);
}
