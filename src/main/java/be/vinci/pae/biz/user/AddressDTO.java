package be.vinci.pae.biz.user;

public interface AddressDTO {

  int getId();

  void setId(int id);

  String getStreet();

  void setStreet(String street);

  int getBuildingNumber();

  void setBuildingNumber(int buildingNumber);

  String getUnitNumber();

  void setUnitNumber(String unitNumber);

  int getPostcode();

  void setPostcode(int postcode);


  String getCommune();

  void setCommune(String commune);

  int getVersionAddress();

  void setVersionAddress(int versionAddress);

  @Override
  String toString();
}
