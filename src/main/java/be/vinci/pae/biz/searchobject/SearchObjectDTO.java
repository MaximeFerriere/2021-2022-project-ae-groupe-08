package be.vinci.pae.biz.searchobject;

public interface SearchObjectDTO {

  String getUsernameOfferor();

  void setUsernameOfferor(String usernameOfferor);

  int getNumberOInterest();

  void setNumberOInterest(int numberOInterest);

  int getIdObject();

  void setIdObject(int idObject);

  String getNomObject();

  void setNomObject(String nomObject);

  String geturlPt();

  void seturlPt(String urlPt);

  String getLastName();

  void setLastName(String lastName);

  String getWordingType();

  void setWordingType(String wordingType);

  String getState();

  void setState(String state);

  String getDatePutOnOffer();

  void setDatePutOnOffer(String datePutOnOffer);
}
