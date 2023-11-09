package be.vinci.pae.dal.objectdao;

import be.vinci.pae.biz.object.ObjectDTO;
import be.vinci.pae.biz.searchobject.SearchObjectDTO;
import java.util.List;

public interface ObjectDAO {

  /**
   * creates a list of the last 10 objects inserted in the DB.
   *
   * @return a list of the last 10 object
   */
  List<ObjectDTO> getLastOffer();


  /**
   * get all object in the db.
   *
   * @return a List of ObjetDTO
   */
  List<ObjectDTO> getObject(String status);


  /**
   * get all object of the user of the id in parameter.
   *
   * @param id int
   * @return a list of ObjetDTO
   */
  List<ObjectDTO> getMyObjects(int id);

  /**
   * get all object of the user where he is recipient of the id in parameter.
   *
   * @param id int
   * @return a list of ObjetDTO
   */
  List<ObjectDTO> getMyObjectsRecipient(int id, boolean received);


  /**
   * get the wording_type of a type with his id in parameter.
   *
   * @param type int
   * @return a String of the wording_type
   */
  String getTypes(int type);


  /**
   * get a object with his id in parameter.
   *
   * @param idObject int
   * @return a ObjetDTO of the object
   */
  ObjectDTO getAObject(int idObject);

  /**
   * get the username of the owner of the object.
   *
   * @param idOfferer int
   * @return a String of the username
   */
  String getOfferer(int idOfferer);

  /**
   * create a new interrest with the id of the object, the id of the user and the times slots
   * avaible.
   *
   * @param idObject      int
   * @param idUser        int
   * @param plageHorraire String
   * @return true if the interrest is create
   */
  boolean addAInterrest(int idObject, int idUser, String plageHorraire, boolean conversationNeeded);

  /**
   * check if the user have already show interrest. --------2 times the methode, one to
   * delete----------.
   *
   * @param idObject int
   * @param idUser   int
   * @return true if the user have already show interrest
   */
  boolean checkIfHeIsAlreadyInterrest(int idObject, int idUser);

  /**
   * check if the object has already a recipient.
   *
   * @param idObject int
   * @return true if the object has already a recipient
   */
  boolean checkIfObjectHasRecipient(int idObject);

  /**
   * check if the recipient is interested in the object.
   *
   * @param idObject    int
   * @param idRecipient int
   * @return true if the recipient is interested in the object
   */
  boolean checkIfRecipientIsInterested(int idObject, int idRecipient);

  /**
   * add a recipient to an object.
   *
   * @param idObject    int
   * @param idRecipient int
   * @return true if the recipient is added
   */
  boolean addRecipient(int idObject, int idRecipient);


  /**
   * get the number of people interrested by the object.
   *
   * @param idObject int
   * @return a int of the number of interrest
   */
  int getNumberOfInterrest(int idObject);

  /**
   * increment the interrestNumber of the object.
   *
   * @param idObject int
   */
  void addAInterrestInObject(int idObject, int numberOfI);

  /**
   * check if the user have already show interrest. --------2 times the methode, one to
   * delete----------.
   *
   * @param idUser   int
   * @param idObject int
   * @return a boolean if he have a interrest
   */
  boolean alreadyShowInterrest(int idUser, int idObject);

  /**
   * find the last date wher the object was put on offer.
   *
   * @param idObject int
   * @return the last date
   */
  String lastDatePutOnOffer(int idObject);

  /**
   * change the state of a object to cancelled.
   *
   * @param idObject Int
   * @return true if the object is canceled
   */
  boolean cancelOffer(int idObject);

  /**
   * create a new type in the db.
   *
   * @param type String
   * @return true if the type is create
   */
  boolean createType(String type);

  /**
   * get the id of a type with his wording_type.
   *
   * @param type String
   * @return the id of the type
   */
  int getTypesByString(String type);

  /**
   * create a new object with a type.
   *
   * @param object ObjectDTO
   * @param idType int
   * @return true if the object is create
   */
  int createObject(ObjectDTO object, int idType);

  /**
   * create a new offer with the object of the idObject.
   *
   * @param idObject int
   * @return true if the offer is create
   */
  boolean createOffer(int idObject);

  /**
   * create an evaluation with the not starRate.
   *
   * @param idObject int
   * @param starRate int
   * @return true if the evaluation is created
   */
  boolean createEvaluation(int idObject, int starRate, String remark, int idMember);

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
   * @return a list of all types
   */
  List<String> getAllTypes();

  /**
   * edit the description and the time slots available of an object with his id.
   *
   * @return true if all good
   */
  boolean editAOffer(int idObject, String desc, String timeSlot);

  /**
   * change the state of a object to Offer.
   *
   * @param idObject Int
   * @return true if the object is re-offer
   */
  boolean reOfferObject(int idObject);

  /**
   * check if the object is in state "Réservé".
   *
   * @param idObject int
   * @return true if the object is in state "Réservé", false otherwise
   */
  boolean checkIfObjectIsReserved(int idObject);

  /**
   * change the state of an object to "Donné".
   *
   * @param idObject Int
   * @return true if the object state is now "Donné"
   */
  boolean indicateObjectDonated(int idObject, boolean state);

  int getVersionObject(int idObject);

  /**
   * check if the object is already evaluated.
   *
   * @param idObject Int
   * @return true if the object is already evaluated
   */
  boolean checkIfObjectIsEvaluated(int idObject);


  List<SearchObjectDTO> search(String search);

  List<SearchObjectDTO> searchDate(String search);

  boolean updateObjectPicture(int idObject, String fileName);

  boolean setObjectToDisableRState(int idObject);

  Boolean deleteAllInterets(int idMember);

  List<Integer> getAllIdObjectOfInterrest(int idMember);

  Boolean decrementNumberInterestObject(int idObject);


}
