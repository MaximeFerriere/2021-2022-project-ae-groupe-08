package be.vinci.pae.biz.object;

import be.vinci.pae.biz.searchobject.SearchObjectDTO;
import java.util.List;

public interface ObjectUseCaseController {

  /**
   * get the 10 last objects create in the db.
   *
   * @return a list of ObjetDTO
   */
  List<ObjectDTO> getLastObjet();

  /**
   * get all object in the db.
   *
   * @return a list of ObjetDTO
   */
  List<ObjectDTO> getObjet(String status);

  /**
   * get all object of the user of the id in parameter.
   *
   * @param id int
   * @return a list of ObjetDTO
   */
  List<ObjectDTO> getMyObjects(int id);

  /**
   * get all object of the user where he is the recipient
   * and that he already received if argument is set to true.
   * Otherwise, only objects that he didn't receive yet.
   *
   * @param id int
   * @return a list of ObjetDTO
   */
  List<ObjectDTO> getMyObjectsRecipient(int id, boolean received);


  /**
   * get a object with the id of the object in parameter.
   *
   * @param idObjet int
   * @return a ObjetDTO
   */
  ObjectDTO getAObject(int idObjet);


  /**
   * add a interrest to a object and modify the interrest element in the object.
   *
   * @param idObject      Int
   * @param idUser        Int
   * @param plageHorraire String
   * @return a boolean if the methode work or not
   */
  boolean addAInterrest(int idObject, int idUser, String plageHorraire,
      boolean conversationNeeded, String phoneNumber, int versionObject);

  /**
   * add a recipient to an offer.
   *
   * @param idObject    Int
   * @param idRecipient Int
   * @return a boolean if the methode work or not
   */
  boolean addRecipient(int idObject, int idRecipient);


  /**
   * change the state of a object to cancelled.
   *
   * @param idObject int
   * @return true if the state is correctly change
   */
  boolean cancelOffer(int idObject);

  /**
   * create a new objet with the parameters in the db. the methode need to get the type with the
   * type String. the methode create the new object. the methode need to create a default offer for
   * the object.
   *
   * @param offerName          String
   * @param description        String
   * @param urlPhoto           String
   * @param timeSlotsAvailable String
   * @param type               String
   * @param idOfferor          int
   * @return true if the object and the offer are create
   */
  int createObject(String offerName, String description, String urlPhoto,
      String timeSlotsAvailable, String type, int idOfferor);

  /**
   * create a new offer with the object of the idObject.
   *
   * @param idObject int
   * @return true if the offer is create
   */
  boolean createOffer(int idObject);

  /**
   * get the id of a object who have the same attribut of the object in parameter.
   *
   * @param objet ObjetDTO
   * @return the id of the object
   */
  int getIdObject(ObjectDTO objet);

  /**
   * get all types in the db.
   *
   * @return a List of string of wording_type of the types
   */
  List<String> getAllTypes();

  /**
   * create a new type with typeName as wording_type.
   *
   * @param typeName String
   * @return true if the type is create
   */
  boolean createType(String typeName);

  /**
   * edit the description and the time slots available of an object with his id.
   *
   * @param desc String, idObject int ,timeSlot String
   * @return true if all good
   */
  boolean editAOffer(int idObject, String desc, String timeSlot);

  /**
   * change the state of a object to offer.
   *
   * @param idObject int
   * @return true if the state is correctly change
   */
  boolean reOfferObject(int idObject);

  /**
   * change the state of an object to "Donné".
   *
   * @param idObject int
   * @return true if the state is correctly change
   */
  boolean indicateObjectDonated(int idObject, boolean state);

  boolean updateObjectPicture(int idObject, String fileName);

  List<SearchObjectDTO> search(String search);

  List<SearchObjectDTO> searchDate(String search);

  /**
   * evaluate an object.
   *
   * @param idObject int
   * @return true if the object is correctly evaluate.
   */
  boolean evaluateObject(int idObject, int starRate, String remark, int idMember);
}
