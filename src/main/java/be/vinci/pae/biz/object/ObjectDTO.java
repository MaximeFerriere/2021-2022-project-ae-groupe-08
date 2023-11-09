package be.vinci.pae.biz.object;

public interface ObjectDTO {

  int getIdObjet();

  void setIdObjet(int idObjet);

  String getName();

  void setName(String name);

  String getDescription();

  void setDescription(String description);

  String getUrlPhoto();

  void setUrlPhoto(String urlPhoto);

  String getDatePutOnOffert();

  void setDatePutOnOffert(String datePutOnOffert);

  String getTimeSlotAvailable();

  void setTimeSlotAvailable(String timeSlotAvailable);

  int getNumberOfPeopleInterested();

  void setNumberOfPeopleInterested(int numberOfPeopleInterested);

  String getState();

  void setState(String state);

  int getType();

  void setType(int type);

  int getIdOfferor();

  void setIdOfferor(int idOfferor);

  int getIdRecipient();

  void setIdRecipient(int idRecipient);

  String getTypeStr();

  void setTypeStr(String typeStr);

  String getOfferorStr();

  void setOfferorStr(String offerorStr);

  int getVersionObject();

  void setVersionObject(int versionObject);
}
