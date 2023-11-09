package be.vinci.pae.dal.userdao;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.user.AddressDTO;
import be.vinci.pae.biz.user.InterestDTO;
import be.vinci.pae.biz.user.UserDTO;
import be.vinci.pae.dal.DALBackendServices;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserDAOImpl implements UserDAO {

  @Inject
  private Factory myFactory;
  @Inject
  private DALBackendServices service;  //injection

  //--------------------------USER GET AND CREATE-----------------------------------------

  @Override
  public UserDTO getUserWithId(int idUser) {
    UserDTO myUserWithId = myFactory.getUserDTO();
    PreparedStatement userId;

    try {
      userId = service.getPS(
          " SELECT m.id_member,m.username,m.last_name,m.firstname,m.is_admin,m.call_number,"
              + "m.password,m.address,m.reason_for_connection_refusal,"
              + "m.condition,m.number_offer_not_picked_up,m.version_member  "
              + " FROM projet_ae_08.members m " + " WHERE m.id_member=? ");
      userId.setInt(1, idUser);
      ResultSet rs = userId.executeQuery();
      while (rs.next()) {
        myUserWithId.setId(rs.getInt(1));
        myUserWithId.setUsername(rs.getString(2));
        myUserWithId.setLastName(rs.getString(3));
        myUserWithId.setFirstName(rs.getString(4));
        myUserWithId.setAdmin(rs.getBoolean(5));
        myUserWithId.setCallNumber(rs.getString(6));
        myUserWithId.setPassword(rs.getString(7));
        myUserWithId.setAddress(getAddress(rs.getInt(8)));
        myUserWithId.setReasonForConnectionRefusal(rs.getString(9));
        myUserWithId.setCondition(rs.getString(10));
        myUserWithId.setNumberOfferNotPickedUp(rs.getInt(11));
        myUserWithId.setVersionMember(rs.getInt(12));
      }
      userId.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Status.INTERNAL_SERVER_ERROR);
    }
    return myUserWithId;
  }

  @Override
  public UserDTO getUser(String username) {
    UserDTO myUser = myFactory.getUserDTO();
    PreparedStatement user;

    try {
      user = service.getPS(
          " SELECT m.id_member,m.username,m.last_name,m.firstname,m.is_admin,m.call_number,"
              + "m.password,m.address,m.reason_for_connection_refusal,"
              + "m.condition,m.number_offer_not_picked_up,m.version_member "
              + " FROM projet_ae_08.members m " + " WHERE m.username=? ");
      user.setString(1, username);
      ResultSet rs = user.executeQuery();
      while (rs.next()) {
        myUser.setId(rs.getInt(1));
        myUser.setUsername(rs.getString(2));
        myUser.setLastName(rs.getString(3));
        myUser.setFirstName(rs.getString(4));
        myUser.setAdmin(rs.getBoolean(5));
        myUser.setCallNumber(rs.getString(6));
        myUser.setPassword(rs.getString(7));
        myUser.setAddress(getAddress(rs.getInt(8)));
        myUser.setReasonForConnectionRefusal(rs.getString(9));
        myUser.setCondition(rs.getString(10));
        myUser.setNumberOfferNotPickedUp(rs.getInt(11));
        myUser.setVersionMember(rs.getInt(12));
      }
      user.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return myUser;
  }

  @Override
  public boolean createUser(UserDTO user, int idAddress) {
    PreparedStatement userStatement;

    try {
      userStatement = service.getPS(
          " INSERT INTO projet_ae_08.members "
              + " VALUES (DEFAULT, ?, ?, ?, false, ?, ?, "
              + " 'inscription en attente', 'waiting',0, 1, ?) ");
      userStatement.setString(1, user.getUsername());
      userStatement.setString(2, user.getLastName());
      userStatement.setString(3, user.getFirstName());
      userStatement.setString(4, user.getCallNumber());
      userStatement.setString(5, user.getPassword());
      userStatement.setInt(6, idAddress);
      userStatement.execute();
      userStatement.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public boolean addPhoneNumber(int idMember, String phoneNumber) {
    PreparedStatement userStatement;

    try {
      userStatement = service.getPS(
          " UPDATE projet_ae_08.members "
              + " SET call_number = ?, version_member = version_member + 1 "
              + " WHERE id_member = ? ");
      userStatement.setInt(2, idMember);
      userStatement.setString(1, phoneNumber);
      userStatement.execute();
      userStatement.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public boolean changePwd(int idMember, String pwd) {
    PreparedStatement userStatement;

    try {
      userStatement = service.getPS(
          " UPDATE projet_ae_08.members "
              + " SET password = ?, version_member = version_member + 1 "
              + " WHERE id_member = ? ");
      userStatement.setInt(2, idMember);
      userStatement.setString(1, pwd);
      userStatement.execute();
      userStatement.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  //--------------------------------CHECK USER EXIST OR ADMIN-----------------------------
  @Override
  public boolean usernameExist(String username) {
    PreparedStatement user;
    int idMember = -1;
    try {
      user = service.getPS(
          " SELECT m.id_member "
              + " FROM projet_ae_08.members m " + "WHERE m.username=? ");
      user.setString(1, username);
      ResultSet rs = user.executeQuery();
      while (rs.next()) {
        idMember = rs.getInt(1);
      }
      user.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    if (idMember == -1) {
      return false;
    }
    return true;
  }

  @Override
  public boolean getIfUserIsAdmin(int idMember) {
    boolean isAdmin = false;
    PreparedStatement user;
    try {
      user = service.getPS(
          " SELECT m.is_admin "
              + " FROM projet_ae_08.members m " + " WHERE m.id_member=? ");
      user.setInt(1, idMember);
      ResultSet rs = user.executeQuery();
      while (rs.next()) {
        isAdmin = rs.getBoolean(1);
      }
      user.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return isAdmin;
  }

  //-------------------------------MEMBER METHODE -----------------------------------------

  @Override
  public List<InterestDTO> getIdMemberInterested(int idObject) {
    PreparedStatement user;
    List<InterestDTO> interestList = new ArrayList<>();

    try {
      user = service.getPS(
          " SELECT i.time_slots,i.id_member,i.discussion_needed"
              + " FROM projet_ae_08.interests i " + " WHERE i.id_object=? ");
      user.setInt(1, idObject);
      ResultSet rs = user.executeQuery();
      while (rs.next()) {
        UserDTO userInterested = myFactory.getUserDTO();
        userInterested.setId(rs.getInt(2));
        InterestDTO interest = myFactory.getInterestDTO();
        interest.setUserDTO(userInterested);
        interest.setTimeSlots(rs.getString(1));
        interest.setConversationNeeded(rs.getBoolean(3));
        interestList.add(interest);
      }
      user.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return interestList;
  }

  @Override
  public InterestDTO getMemberInterestedCompleteInfo(InterestDTO interest) {
    PreparedStatement user;
    try {
      user = service.getPS(
          " SELECT m.firstname,m.last_name,m.call_number"
              + " FROM projet_ae_08.members m " + " WHERE m.id_member=? ");
      user.setInt(1, interest.getUserDTO().getId());
      ResultSet rs = user.executeQuery();
      while (rs.next()) {
        interest.getUserDTO().setFirstName(rs.getString(1));
        interest.getUserDTO().setLastName(rs.getString(2));
        if (interest.isConversationNeeded()) {
          interest.getUserDTO().setCallNumber(rs.getString(3));
        }
      }
      user.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return interest;
  }

  @Override
  public boolean incrementNumberOfferNotPickedUp(int idRecipient) {
    PreparedStatement user;
    try {
      user = service.getPS(
          " UPDATE projet_ae_08.members "
              + " SET number_offer_not_picked_up = number_offer_not_picked_up + 1,"
              + " version_member = version_member + 1 "
              + " WHERE id_member= ? ");
      user.setInt(1, idRecipient);
      user.executeUpdate();
      user.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  //-------------------------------ADMIN METHODE INSCRIPTION-------------------------------

  @Override
  public boolean confirmRegister(String username) {
    PreparedStatement user;
    try {
      user = service.getPS(
          " UPDATE projet_ae_08.members "
              + " SET condition = ?, reason_for_connection_refusal = NULL,"
              + " version_member = version_member + 1 "
              + " WHERE username= ? ");
      user.setString(1, "valid");
      user.setString(2, username);
      user.executeUpdate();
      user.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public boolean becomeAdmin(String username) {
    PreparedStatement user;
    try {
      user = service.getPS(" UPDATE projet_ae_08.members "
          + " SET condition='valid', reason_for_connection_refusal=NULL, is_admin='true' "
          + " WHERE username=? ");
      user.setString(1, username);
      user.executeUpdate();
      user.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public boolean refuseRegister(String username, String justifie) {
    PreparedStatement user;
    try {
      user = service.getPS(
          " UPDATE projet_ae_08.members "
              + " SET condition='denied', reason_for_connection_refusal=?,"
              + " version_member = version_member + 1 "
              + " WHERE username=? ");
      user.setString(1, justifie);
      user.setString(2, username);
      user.executeUpdate();
      user.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public Boolean changeStatusToDisable(int idUser) {
    PreparedStatement user;
    try {
      user = service.getPS(
          " UPDATE projet_ae_08.members "
              + " SET condition = 'disabled', "
              + " version_member = version_member + 1 "
              + " WHERE id_member = ? ");
      user.setInt(1, idUser);
      user.executeUpdate();
      user.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }


  //------------------------------ADDRESS METHODE---------------------------------------------
  @Override
  public boolean createAddres(String street, int buildingNumber, String unitNumber, int postcode,
      String commune) {
    PreparedStatement addressStatement;
    try {
      addressStatement = service.getPS(" INSERT INTO projet_ae_08.addresses "
          + " VALUES (DEFAULT, ?, ?, ?, ?, ?, 1) ");
      addressStatement.setString(1, street);
      addressStatement.setInt(2, buildingNumber);
      addressStatement.setString(3, unitNumber);
      addressStatement.setInt(4, postcode);
      addressStatement.setString(5, commune);
      addressStatement.execute();
      addressStatement.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public int findAdress(String street, int buildingNumber, String unitNumber, int postcode,
      String commune) {
    PreparedStatement findIdAddressStatement;
    int idAddress = -1;
    try {
      findIdAddressStatement = service.getPS(
          " SELECT a.id_address " + "FROM projet_ae_08.addresses a "
              + " WHERE a.street=? AND a.building_number=? AND "
              + " a.unit_number=? AND a.postcode=? AND a.commune=? ");
      findIdAddressStatement.setString(1, street);
      findIdAddressStatement.setInt(2, buildingNumber);
      findIdAddressStatement.setString(3, unitNumber);
      findIdAddressStatement.setInt(4, postcode);
      findIdAddressStatement.setString(5, commune);
      ResultSet rs = findIdAddressStatement.executeQuery();

      while (rs.next()) {
        idAddress = rs.getInt(1);
      }
      findIdAddressStatement.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return idAddress;
  }

  @Override
  public AddressDTO getAddress(int idAddress) {
    AddressDTO addressUser = myFactory.getAddressDTO();
    PreparedStatement address;

    try {
      address = service.getPS(
          " SELECT a.id_address,a.street,a.building_number," + "a.unit_number,"
              + " a.postcode,a.commune,a.version_address "
              + " FROM projet_ae_08.addresses a " + " WHERE a.id_address=? ");
      address.setInt(1, idAddress);
      ResultSet rs = address.executeQuery();
      while (rs.next()) {
        addressUser.setId(rs.getInt(1));
        addressUser.setStreet(rs.getString(2));
        addressUser.setBuildingNumber(rs.getInt(3));
        addressUser.setUnitNumber(rs.getString(4));
        addressUser.setPostcode(rs.getInt(5));
        addressUser.setCommune(rs.getString(6));
        addressUser.setVersionAddress(rs.getInt(7));
      }
      address.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return addressUser;
  }

  //------------------------------LIST WITH STATUS--------------------------------------------
  @Override
  public List<UserDTO> getListWithStatus(String status) {

    PreparedStatement user;
    List<UserDTO> userList = new ArrayList<>();

    try {
      user = service.getPS(
          " SELECT m.id_member,m.username,m.last_name,m.firstname,m.call_number,"
              + " m.address, m.condition, m.reason_for_connection_refusal, "
              + " m.is_admin,m.number_offer_not_picked_up,m.version_member, "
              + " COUNT(Distinct o1.id_object), COUNT(Distinct o2.id_object),"
              + " COUNT(Distinct o3.id_object) "
              + " FROM projet_ae_08.members m "
              + " LEFT OUTER JOIN projet_ae_08.objects o1 "
              + " ON m.id_member = o1.id_offeror "
              + " AND o1.state = 'Offert' "
              + " LEFT OUTER JOIN projet_ae_08.objects o2 "
              + " ON m.id_member = o2.id_offeror"
              + " AND o2.state = 'Donné' "
              + " LEFT OUTER JOIN projet_ae_08.objects o3 "
              + " ON m.id_member = o3.id_recipient"
              + " AND o3.state = 'Donné' "
              + " WHERE m.condition= ? "
              + " GROUP BY m.id_member ");
      user.setString(1, status);
      ResultSet rs = user.executeQuery();
      while (rs.next()) {
        UserDTO userWaiting = myFactory.getUserDTO();
        userWaiting.setId(rs.getInt(1));
        userWaiting.setUsername(rs.getString(2));
        userWaiting.setLastName(rs.getString(3));
        userWaiting.setFirstName(rs.getString(4));
        userWaiting.setCallNumber(rs.getString(5));
        userWaiting.setAddress(getAddress(rs.getInt(6)));
        userWaiting.setCondition(rs.getString(7));
        userWaiting.setReasonForConnectionRefusal(rs.getString(8));
        userWaiting.setAdmin(rs.getBoolean(9));
        userWaiting.setNumberOfferNotPickedUp(rs.getInt(10));
        userWaiting.setVersionMember(rs.getInt(11));
        userWaiting.setNbrOfferOffert(rs.getInt(12));
        userWaiting.setNbrOfferGiven(rs.getInt(13));
        userWaiting.setNbrOffertReceived(rs.getInt(14));

        userList.add(userWaiting);

      }
      user.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return userList;
  }

  //--------------------------------------EDIT PROFIL----------------------------------------------
  @Override
  public boolean editAProfile(int id, String username, String lastName, String firstname,
      String callNumber) {
    PreparedStatement object;
    try {
      object = service.getPS(
          " UPDATE projet_ae_08.members "
              + " SET username=? , last_name=? , firstname=? , call_number=?, "
              + " version_member = version_member +1 "
              + " WHERE id_member=? ");
      object.setString(1, username);
      object.setString(2, lastName);
      object.setString(3, firstname);
      object.setString(4, callNumber);
      object.setInt(5, id);
      object.executeUpdate();
      object.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public boolean editAAdress(int id, String street, int building, int postcode,
      String unit, String commune) {
    PreparedStatement object;
    try {
      object = service.getPS(
          "UPDATE projet_ae_08.addresses "
              + "SET street=? , building_number=? , unit_number=? , postcode=? , commune=?,"
              + " version_address = version_address + 1  "
              + "WHERE id_address=? ");
      object.setString(1, street);
      object.setInt(2, building);
      object.setString(3, unit);
      object.setInt(4, postcode);
      object.setString(5, commune);
      object.setInt(6, id);
      object.executeUpdate();
      object.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }


  //-----------------------------------------GETTERS VERSION--------------------------------------
  @Override
  public int getVersionMember(String username, int idMember) {
    PreparedStatement version;
    int versionReturn = 1;

    if (idMember == 0) {

      try {
        version = service.getPS(
            " SELECT m.version_member "
                + " FROM projet_ae_08.members m "
                + " WHERE m.username = ? "
        );
        version.setString(1, username);
        ResultSet rs = version.executeQuery();
        while (rs.next()) {
          versionReturn = rs.getInt(1);
        }

        rs.close();
        version.close();

      } catch (SQLException e) {
        throw new WebApplicationException(e.getMessage(),
            Response.Status.INTERNAL_SERVER_ERROR);
      }
      return versionReturn;
    } else {
      try {
        version = service.getPS(
            " SELECT m.version_member "
                + " FROM projet_ae_08.members m "
                + " WHERE m.id_member = ? "
        );
        version.setInt(1, idMember);
        ResultSet rs = version.executeQuery();
        while (rs.next()) {
          versionReturn = rs.getInt(1);
        }

        rs.close();
        version.close();

      } catch (SQLException e) {
        throw new WebApplicationException(e.getMessage(),
            Response.Status.INTERNAL_SERVER_ERROR);
      }
      return versionReturn;
    }
  }

  @Override
  public int getVersionAddress(int idAddress) {
    PreparedStatement versionAddress;
    int versionReturn = 1;

    try {
      versionAddress = service.getPS(
          " SELECT a.version_address "
              + " FROM projet_ae_08.addresses a "
              + " WHERE a.id_address = ? "
      );
      versionAddress.setInt(1, idAddress);
      ResultSet rs = versionAddress.executeQuery();
      while (rs.next()) {
        versionReturn = rs.getInt(1);
      }

      rs.close();
      versionAddress.close();

    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return versionReturn;
  }

  //// Search User

  @Override
  public List<UserDTO> getSearchListWithStatus(String search) {

    PreparedStatement user;
    List<UserDTO> userList = new ArrayList<>();

    try {
      user = service.getPS(
          " SELECT m.id_member,m.username,m.last_name,m.firstname,m.call_number,"
              + " m.address, m.condition, m.reason_for_connection_refusal, "
              + " m.is_admin,m.number_offer_not_picked_up,m.version_member, "
              + " COUNT(Distinct o1.id_object), COUNT(Distinct o2.id_object),"
              + " COUNT(Distinct o3.id_object) "
              + " FROM projet_ae_08.members m "
              + " LEFT OUTER JOIN projet_ae_08.objects o1 "
              + " ON m.id_member = o1.id_offeror "
              + " AND o1.state = 'Offert' "
              + " LEFT OUTER JOIN projet_ae_08.objects o2 "
              + " ON m.id_member = o2.id_offeror "
              + " AND o2.state = 'Donné' "
              + " LEFT OUTER JOIN projet_ae_08.objects o3 "
              + " ON m.id_member = o3.id_recipient "
              + " AND o3.state = 'Donné', "
              + " projet_ae_08.addresses a "
              + " WHERE m.condition = 'valid' "
              + " AND m.address = a.id_address "
              + " AND ( m.last_name LIKE ? OR a.commune LIKE ? OR a.postcode = ? ) "
              + " GROUP BY m.id_member ");

      user.setString(1, "%" + search + "%");
      user.setString(2, "%" + search + "%");

      String example = search;
      char[] ch = example.toCharArray();
      for (char c : ch) {
        if (Character.isDigit(c)) {
          user.setInt(3, Integer.parseInt(search));
        } else {
          user.setInt(3, 0);
        }
      }

      ResultSet rs = user.executeQuery();
      while (rs.next()) {
        UserDTO userWaiting = myFactory.getUserDTO();
        userWaiting.setLastName(rs.getString(3));
        userWaiting.setCallNumber(rs.getString(5));
        userWaiting.setFirstName(rs.getString(4));
        userWaiting.setId(rs.getInt(1));
        userWaiting.setUsername(rs.getString(2));
        userWaiting.setAddress(getAddress(rs.getInt(6)));
        userWaiting.setCondition(rs.getString(7));
        userWaiting.setAdmin(rs.getBoolean(9));
        userWaiting.setReasonForConnectionRefusal(rs.getString(8));
        userWaiting.setVersionMember(rs.getInt(11));
        userWaiting.setNumberOfferNotPickedUp(rs.getInt(10));
        userWaiting.setNbrOfferOffert(rs.getInt(12));
        userWaiting.setNbrOffertReceived(rs.getInt(14));
        userWaiting.setNbrOfferGiven(rs.getInt(13));

        userList.add(userWaiting);

      }
      user.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return userList;
  }

}