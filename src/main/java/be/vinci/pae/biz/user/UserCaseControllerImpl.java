package be.vinci.pae.biz.user;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.object.ObjectDTO;
import be.vinci.pae.dal.DalServices;
import be.vinci.pae.dal.objectdao.ObjectDAO;
import be.vinci.pae.dal.userdao.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;


public class UserCaseControllerImpl implements UserCaseController {

  @Inject
  private UserDAO userDAO;

  @Inject
  private ObjectDAO objectDAO;

  @Inject
  private Factory factory;

  @Inject
  private DalServices dalServices;

  @Override
  public UserDTO getOne(int idUser) {
    User myUser;
    try {
      dalServices.start();
      myUser = (User) userDAO.getUserWithId(idUser);
      if (myUser == null) {
        throw new WebApplicationException("get user with his id failed",
            Status.INTERNAL_SERVER_ERROR);
      }
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return myUser;
  }

  @Override
  public UserDTO login(String username, String password) {
    User myUserI;
    try {
      dalServices.start();
      myUserI = (User) userDAO.getUser(username);
      if (myUserI == null || !myUserI.checkPassword(password)) {
        throw new WebApplicationException("login failed wrong password or problem with DAL",
            Status.INTERNAL_SERVER_ERROR);
      }
      if (myUserI.getCondition().equals("disabled")) {
        userDAO.confirmRegister(myUserI.getUsername());
      }
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return myUserI;
  }


  @Override
  public boolean registerConfirm(String username, int versionMember) {
    boolean result;
    try {
      dalServices.start();
      if (!(versionMember == userDAO.getVersionMember(username, 0))) {
        throw new WebApplicationException("version actualisée",
            Response.Status.INTERNAL_SERVER_ERROR);
      }
      result = userDAO.confirmRegister(username);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }

    dalServices.commit();
    return result;
  }

  @Override
  public boolean becomeAdmin(String username) {
    boolean result;
    try {
      dalServices.start();
      result = userDAO.becomeAdmin(username);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return result;
  }

  @Override
  public boolean refuseRegister(String username, String justifie, int versionMember) {
    boolean result;
    try {
      dalServices.start();
      if (!(versionMember == userDAO.getVersionMember(username, 0))) {
        throw new WebApplicationException("version actualisée",
            Response.Status.INTERNAL_SERVER_ERROR);
      }
      result = userDAO.refuseRegister(username, justifie);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return result;
  }

  @Override
  public boolean register(String username, String password, String firstname, String lastname,
      String callN,
      String street, int buildingNumber, String unitNumber, int postcode, String commune) {

    if (!checkUsernameAlreadyExist(username)) {
      User user = (User) factory.getUserDTO();
      user.setUsername(username);
      String encryptedPassword = user.cryptPassword(password);
      user.setPassword(encryptedPassword);
      user.setFirstName(firstname);
      user.setLastName(lastname);
      user.setCallNumber(callN);

      dalServices.start();
      try {
        userDAO.createAddres(street, buildingNumber, unitNumber, postcode, commune);
        int idAdress = userDAO.findAdress(street, buildingNumber, unitNumber, postcode, commune);
        userDAO.createUser(user, idAdress);
      } catch (Exception e) {
        dalServices.rollback();
        throw new WebApplicationException(e.getMessage(),
            Response.Status.INTERNAL_SERVER_ERROR);
      }
      dalServices.commit();
    } else {
      throw new WebApplicationException("Login already exist", Response.Status.CONFLICT);
    }
    return true;
  }


  @Override
  public boolean checkUsernameAlreadyExist(String username) {
    boolean result;
    try {
      dalServices.start();
      result = userDAO.usernameExist(username);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return result;
  }

  @Override
  public boolean checkUserIsAdmin(int idMember) {
    boolean result;
    try {
      dalServices.start();
      result = userDAO.getIfUserIsAdmin(idMember);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }

    dalServices.commit();
    return result;
  }

  @Override
  public List<UserDTO> listMemberWithStatus(String typeList) {
    List<UserDTO> result;
    try {
      dalServices.start();
      result = userDAO.getListWithStatus(typeList);//"waiting" or "denied"
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }

    dalServices.commit();
    return result;
  }


  @Override
  public AddressDTO getAddressDTO(int idAddress) {
    AddressDTO result;
    try {
      dalServices.start();
      result = userDAO.getAddress(idAddress);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return result;
  }

  @Override
  public List<InterestDTO> getListMemberInterested(int idObject) {
    List<InterestDTO> listMembers;
    try {
      dalServices.start();
      listMembers = userDAO.getIdMemberInterested(idObject);
      for (InterestDTO member : listMembers) {
        userDAO.getMemberInterestedCompleteInfo(member);
      }
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return listMembers;
  }

  @Override
  public UserDTO getUserProfile(String username) {

    UserDTO result;
    try {
      dalServices.start();
      result = userDAO.getUser(username);
      result.setPassword(null);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return result;
  }

  @Override
  public boolean editAMember(int idMember, String username, String lastName, String firstname,
      String callNumber, int idAdress, String street, int building, int postcode,
      String unit, String commune) {

    try {
      dalServices.start();
      UserDTO user = userDAO.getUserWithId(idMember);
      if (!user.getUsername().equals(username)) {
        if (!userDAO.usernameExist(username)) {
          userDAO.editAProfile(idMember, username, lastName, firstname, callNumber);
          userDAO.editAAdress(idAdress, street, building, postcode, unit, commune);
        } else {
          dalServices.rollback();
          throw new WebApplicationException("Login already exist", Response.Status.CONFLICT);
        }
      } else {
        userDAO.editAProfile(idMember, username, lastName, firstname, callNumber);
        userDAO.editAAdress(idAdress, street, building, postcode, unit, commune);
      }
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return true;
  }

  @Override
  public boolean changePwd(int idMember, String pwd) {

    boolean result;
    try {
      dalServices.start();
      User user = (User) factory.getUserDTO();
      String encryptedPassword = user.cryptPassword(pwd);
      result = userDAO.changePwd(idMember, encryptedPassword);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return result;
  }

  @Override
  public List<UserDTO> getSearchListWithStatus(String search) {

    List<UserDTO> list = null;
    try {
      dalServices.start();
      list = userDAO.getSearchListWithStatus(search);
    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return list;
  }

  @Override
  public Boolean changeStatusToDisable(int idUser, int versionMemberToCheck) {
    Boolean result = false;
    List<ObjectDTO> objectMemberD = new ArrayList<>();
    List<ObjectDTO> objectMemberReceveid = new ArrayList<>();
    List<Integer> idObjectList = new ArrayList<>();
    try {
      dalServices.start();
      if (!(versionMemberToCheck == userDAO.getVersionMember(null, idUser))) {
        throw new WebApplicationException("version actualisée",
            Response.Status.INTERNAL_SERVER_ERROR);
      }
      result = userDAO.changeStatusToDisable(idUser);
      objectMemberD = objectDAO.getMyObjects(idUser);
      for (ObjectDTO o : objectMemberD) {
        if (o.getState() != "Annulé" && o.getState() != "Donné") {
          objectDAO.cancelOffer(o.getIdObjet());
        }
      }
      objectMemberReceveid = objectDAO.getMyObjectsRecipient(idUser, false);
      for (ObjectDTO o : objectMemberReceveid) {
        if (o.getState() != "Annulé") {
          objectDAO.setObjectToDisableRState(o.getIdObjet());
        }
      }

      idObjectList = objectDAO.getAllIdObjectOfInterrest(idUser);

      for (int i : idObjectList) {
        objectDAO.decrementNumberInterestObject(i);
      }

      objectDAO.deleteAllInterets(idUser);

    } catch (Exception e) {
      dalServices.rollback();
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    dalServices.commit();
    return result;
  }

}
