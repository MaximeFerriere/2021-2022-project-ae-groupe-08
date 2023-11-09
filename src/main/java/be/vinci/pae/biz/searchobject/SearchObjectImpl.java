package be.vinci.pae.biz.searchobject;

public class SearchObjectImpl implements SearchObjectDTO {

  private String lastName;
  private String wordingType;
  private String state;
  private String datePutOnOffer;
  private int idObject;
  private String urlPt;
  private String nomObject;
  private int numberOInterest;
  private String usernameOfferor;

  public String getUsernameOfferor() {
    return usernameOfferor;
  }

  public void setUsernameOfferor(String usernameOfferor) {
    this.usernameOfferor = usernameOfferor;
  }

  public int getNumberOInterest() {
    return numberOInterest;
  }

  public void setNumberOInterest(int numberOInterest) {
    this.numberOInterest = numberOInterest;
  }

  public int getIdObject() {
    return idObject;
  }

  public void setIdObject(int idObject) {
    this.idObject = idObject;
  }

  public String getNomObject() {
    return nomObject;
  }

  public void setNomObject(String nomObject) {
    this.nomObject = nomObject;
  }

  public String geturlPt() {
    return urlPt;
  }

  public void seturlPt(String urlPt) {
    this.urlPt = urlPt;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public String getWordingType() {
    return wordingType;
  }

  @Override
  public void setWordingType(String wordingType) {
    this.wordingType = wordingType;
  }

  @Override
  public String getState() {
    return state;
  }

  @Override
  public void setState(String state) {
    this.state = state;
  }

  @Override
  public String getDatePutOnOffer() {
    return datePutOnOffer;
  }

  @Override
  public void setDatePutOnOffer(String datePutOnOffer) {
    this.datePutOnOffer = datePutOnOffer;
  }
}
