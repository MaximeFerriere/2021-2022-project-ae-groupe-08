package be.vinci.pae.biz.user;


public class AddressImpl implements AddressDTO {

  int id;
  String street;
  int buildingNumber;
  String unitNumber;
  int postcode;
  String commune;
  int versionAddress;

  public int getVersionAddress() {
    return versionAddress;
  }

  public void setVersionAddress(int versionAddress) {
    this.versionAddress = versionAddress;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getStreet() {
    return street;
  }

  @Override
  public void setStreet(String street) {
    this.street = street;
  }

  @Override
  public int getBuildingNumber() {
    return buildingNumber;
  }

  @Override
  public void setBuildingNumber(int buildingNumber) {
    this.buildingNumber = buildingNumber;
  }

  @Override
  public String getUnitNumber() {
    return unitNumber;
  }

  @Override
  public void setUnitNumber(String unitNumber) {
    this.unitNumber = unitNumber;
  }

  @Override
  public int getPostcode() {
    return postcode;
  }

  @Override
  public void setPostcode(int postcode) {
    this.postcode = postcode;
  }

  @Override
  public String getCommune() {
    return commune;
  }

  @Override
  public void setCommune(String commune) {
    this.commune = commune;
  }

  @Override
  public String toString() {
    return "Address{"
        + "id=" + id
        + ", street='" + street + '\''
        + ", buildingNumber=" + buildingNumber
        + ", unitNumber=" + unitNumber
        + ", postcode=" + postcode
        + ", commune='" + commune + '\''
        + '}';
  }
}
