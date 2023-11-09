package be.vinci.pae.biz.object;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.searchobject.SearchObjectDTO;
import be.vinci.pae.dal.DalServices;
import be.vinci.pae.dal.objectdao.ObjectDAO;
import be.vinci.pae.dal.userdao.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class ObjectUseCaseControllerImpl implements ObjectUseCaseController {

  @Inject
  private ObjectDAO objectDAO;

  @Inject
  private UserDAO userDAO;

  @Inject
  private DalServices dalServices;

  @Inject
  private Factory factory;

  @Override
  public List<ObjectDTO> getLastObjet() {
    List<ObjectDTO> objects = null;
    dalServices.start();
    try {
      objects = objectDAO.getLastOffer();
      if (objects.get(0) == null) {
        return null;
      }
      for (ObjectDTO o : objects) {
        o.setTypeStr(objectDAO.getTypes(o.getType()));
      }
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return objects;
  }

  @Override
  public List<ObjectDTO> getObjet(String status) {
    List<ObjectDTO> objects = null;
    dalServices.start();
    try {
      objects = objectDAO.getObject(status);
      if (objects.get(0) == null) {
        return null;
      }
      for (ObjectDTO o : objects) {
        o.setTypeStr(objectDAO.getTypes(o.getType()));
      }
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return objects;
  }

  @Override
  public List<ObjectDTO> getMyObjects(int id) {
    List<ObjectDTO> objects = null;
    dalServices.start();
    try {
      objects = objectDAO.getMyObjects(id);
      for (ObjectDTO o : objects) {
        o.setTypeStr(objectDAO.getTypes(o.getType()));
      }
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return objects;
  }

  @Override
  public List<ObjectDTO> getMyObjectsRecipient(int id, boolean received) {
    List<ObjectDTO> objects = null;
    dalServices.start();
    try {
      objects = objectDAO.getMyObjectsRecipient(id, received);
      for (ObjectDTO o : objects) {
        o.setTypeStr(objectDAO.getTypes(o.getType()));
      }
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return objects;
  }


  @Override
  public ObjectDTO getAObject(int idObjet) {
    dalServices.start();
    ObjectDTO object = null;
    try {
      object = objectDAO.getAObject(idObjet);
      if (object == null) {
        return null;
      }
      object.setTypeStr(objectDAO.getTypes(object.getType()));
      object.setOfferorStr(objectDAO.getOfferer(object.getIdOfferor()));
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return object;
  }

  @Override
  public boolean addAInterrest(int idObject, int idUser, String plageHorraire,
      boolean conversationNeeded, String phoneNumber, int versionObject) {
    boolean succes = false;
    if (getAObject(idObject).getIdOfferor() != idUser) {
      dalServices.start();
      try {
        succes = objectDAO.checkIfHeIsAlreadyInterrest(idObject, idUser);
        if (succes) {
          if (!(versionObject == objectDAO.getVersionObject(idObject))) {
            throw new WebApplicationException("version actualisée",
                Response.Status.INTERNAL_SERVER_ERROR);
          }
          if (conversationNeeded) {
            userDAO.addPhoneNumber(idUser, phoneNumber);
          }
          objectDAO.addAInterrest(idObject, idUser, plageHorraire, conversationNeeded);
          int num = objectDAO.getNumberOfInterrest(idObject);
          objectDAO.addAInterrestInObject(idObject, num);
        }
      } catch (Exception e) {
        dalServices.rollback();
        throw new WebApplicationException(e.getMessage(),
            Response.Status.INTERNAL_SERVER_ERROR);
      }
      dalServices.commit();
      return succes;
    } else {
      throw new WebApplicationException(
          "the owner of the object can not show a interrest for his own object",
          Response.Status.BAD_REQUEST);
    }
  }

  @Override
  public boolean addRecipient(int idObject, int idRecipient) {
    //check if recipient is interested
    boolean response;
    dalServices.start();
    try {
      Object o = (Object) objectDAO.getAObject(idObject);
      if (!o.checkObjectStateCanBeReserved()) {
        throw new WebApplicationException("bad state of Object", Response.Status.BAD_REQUEST);
      }
      boolean recipient = objectDAO.checkIfObjectHasRecipient(idObject);
      if (!recipient || o.getState().equals("Receveur désactivé")) {
        boolean isInterested = objectDAO.checkIfRecipientIsInterested(idObject, idRecipient);
        if (isInterested) {
          objectDAO.addRecipient(idObject, idRecipient);
          response = true;
        } else {
          response = false;
        }
      } else {
        response = false;
      }
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();

    return response;
  }

  @Override
  public boolean cancelOffer(int idObject) {
    boolean result = false;
    dalServices.start();
    try {
      result = objectDAO.cancelOffer(idObject);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return result;
  }


  @Override
  public int createObject(String offerName, String description, String urlPhoto,
      String timeSlotsAvailable, String type, int idOfferor) {
    ObjectDTO object = factory.getObjetDTO();
    object.setName(offerName);
    object.setDescription(description);
    object.setUrlPhoto(urlPhoto);
    object.setTimeSlotAvailable(timeSlotsAvailable);
    object.setIdOfferor(idOfferor);
    object.setState("offert");
    dalServices.start();
    int result;
    try {
      int idType = objectDAO.getTypesByString(type);
      result = objectDAO.createObject(object, idType);
      objectDAO.createOffer(result);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();

    return result;

  }

  @Override
  public boolean createOffer(int idObject) {
    dalServices.start();
    boolean result;
    try {
      result = objectDAO.createOffer(idObject);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();

    return result;
  }

  @Override
  public int getIdObject(ObjectDTO objet) {
    dalServices.start();
    int idObject;
    try {
      idObject = objectDAO.getIdObject(objet);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();

    return idObject;
  }

  @Override
  public List<String> getAllTypes() {
    dalServices.start();
    List<String> reponseTypes = new ArrayList<>();
    try {
      reponseTypes = objectDAO.getAllTypes();
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();

    return reponseTypes;
  }

  @Override
  public boolean createType(String typeName) {
    dalServices.start();
    boolean reponseCreate;
    try {
      reponseCreate = objectDAO.createType(typeName);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return reponseCreate;
  }

  @Override
  public boolean editAOffer(int idObject, String desc, String timeSlot) {

    boolean reponseCreate;
    try {
      dalServices.start();
      reponseCreate = objectDAO.editAOffer(idObject, desc, timeSlot);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return reponseCreate;
  }

  @Override
  public boolean reOfferObject(int idObject) {
    boolean result = false;
    dalServices.start();
    try {
      Object o = (Object) objectDAO.getAObject(idObject);
      if (!o.checkObjectStateEqualsPasDonne()) {
        throw new WebApplicationException("bad state of Object", Response.Status.BAD_REQUEST);
      }
      result = objectDAO.reOfferObject(idObject);
      objectDAO.createOffer(idObject);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return result;
  }

  @Override
  public boolean indicateObjectDonated(int idObject, boolean state) {
    boolean result;
    dalServices.start();
    try {

      if (objectDAO.checkIfObjectIsReserved(idObject)) {
        if (!state) {
          int idRecipient = objectDAO.getAObject(idObject).getIdRecipient();
          userDAO.incrementNumberOfferNotPickedUp(idRecipient);
        }
        result = objectDAO.indicateObjectDonated(idObject, state);
      } else {
        result = false;
      }

    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return result;
  }

  @Override
  public boolean evaluateObject(int idObject, int starRate, String remark, int idMember) {
    boolean result;
    dalServices.start();
    try {
      if (!objectDAO.checkIfObjectIsEvaluated(idObject)) {
        result = objectDAO.createEvaluation(idObject, starRate, remark, idMember);
      } else {
        result = false;
      }

    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return result;
  }

  @Override
  public boolean updateObjectPicture(int idObject, String fileName) {
    boolean result = false;
    dalServices.start();
    try {
      result = objectDAO.updateObjectPicture(idObject, fileName);
    } catch (Exception e) {
      dalServices.rollback(); //Manque throw
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return result;
  }

  @Override
  public List<SearchObjectDTO> search(String search) {
    dalServices.start();
    List<SearchObjectDTO> result = new ArrayList<>();
    try {
      result = objectDAO.search(search);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return result;
  }

  @Override
  public List<SearchObjectDTO> searchDate(String search) {
    dalServices.start();
    List<SearchObjectDTO> result = new ArrayList<>();
    try {
      result = objectDAO.searchDate(search);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return result;
  }
}
