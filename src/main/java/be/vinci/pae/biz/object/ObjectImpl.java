package be.vinci.pae.biz.object;

public class ObjectImpl implements Object {

  private int idObjet;
  private String name;
  private String description;
  private String urlPhoto;
  private String datePutOnOffert;
  private String timeSlotAvailable;
  private int numberOfPeopleInterested;
  private String state;
  private int type;
  private int idOfferor;
  private int idRecipient;
  private String typeStr;
  private String offerorStr;
  private int versionObject;

  public int getVersionObject() {
    return versionObject;
  }

  public void setVersionObject(int versionObject) {
    this.versionObject = versionObject;
  }

  @Override
  public int getIdObjet() {
    return idObjet;
  }

  @Override
  public void setIdObjet(int idObjet) {
    this.idObjet = idObjet;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getUrlPhoto() {
    return urlPhoto;
  }

  @Override
  public void setUrlPhoto(String urlPhoto) {
    this.urlPhoto = urlPhoto;
  }

  @Override
  public String getDatePutOnOffert() {
    return datePutOnOffert;
  }

  @Override
  public void setDatePutOnOffert(String datePutOnOffert) {
    this.datePutOnOffert = datePutOnOffert;
  }

  @Override
  public String getTimeSlotAvailable() {
    return timeSlotAvailable;
  }

  @Override
  public void setTimeSlotAvailable(String timeSlotAvailable) {
    this.timeSlotAvailable = timeSlotAvailable;
  }

  @Override
  public int getNumberOfPeopleInterested() {
    return numberOfPeopleInterested;
  }

  @Override
  public void setNumberOfPeopleInterested(int numberOfPeopleInterested) {
    this.numberOfPeopleInterested = numberOfPeopleInterested;
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
  public int getType() {
    return type;
  }

  @Override
  public void setType(int type) {
    this.type = type;
  }

  @Override
  public int getIdOfferor() {
    return idOfferor;
  }

  @Override
  public void setIdOfferor(int idOfferor) {
    this.idOfferor = idOfferor;
  }

  @Override
  public int getIdRecipient() {
    return idRecipient;
  }

  @Override
  public void setIdRecipient(int idRecipient) {
    this.idRecipient = idRecipient;
  }

  public String getTypeStr() {
    return typeStr;
  }

  public void setTypeStr(String typeStr) {
    this.typeStr = typeStr;
  }

  public String getOfferorStr() {
    return offerorStr;
  }

  public void setOfferorStr(String offerrStr) {
    offerorStr = offerrStr;
  }

  public boolean checkObjectStateEqualsPasDonne() {
    return this.state.equals("Pas donné") || this.state.equals("Annulé");
  }

  public boolean checkObjectStateCanBeReserved() {
    return this.state.equals("Offert") || this.state.equals("Pas donné")
        || this.state.equals("Receveur désactivé");
  }
}
