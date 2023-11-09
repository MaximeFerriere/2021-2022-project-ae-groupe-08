package be.vinci.pae.dal.objectdao;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.object.ObjectDTO;
import be.vinci.pae.biz.searchobject.SearchObjectDTO;
import be.vinci.pae.dal.DALBackendServices;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ObjectDAOImpl implements ObjectDAO {

  @Inject
  private Factory myFactory;
  @Inject
  private DALBackendServices service;  //injection

  //-------------------------------------------GETTERS-------------------------------------------
  @Override
  public List<ObjectDTO> getLastOffer() {
    PreparedStatement objet;
    ArrayList<ObjectDTO> listeObjet = new ArrayList<>();
    ArrayList<Integer> dejaPresent = new ArrayList<>();
    try {
      objet = service.getPS(
          " SELECT o.id_object,o.name,o.url_photo,o.time_slots_available,"
              + " o.number_of_people_interested,o.state,o.type,o.id_offeror,"
              + " o.id_recipient, of.date_put_on_offer, of.id_offer "
              + " FROM projet_ae_08.objects o, projet_ae_08.offers of "
              + " WHERE o.state='Offert' "
              + " AND of.id_object = o.id_object"
              + " AND of.date_put_on_offer = ( SELECT max(of2.date_put_on_offer)"
              + " FROM projet_ae_08.offers of2 WHERE of2.id_object = o.id_object)"
              + " ORDER BY of.date_put_on_offer DESC, of.id_offer DESC"
              + " LIMIT 10 ");
      ResultSet rs = objet.executeQuery();

      while (rs.next()) {
        if (!dejaPresent.contains(rs.getInt(1))) {
          ObjectDTO myObjet = myFactory.getObjetDTO();

          myObjet.setIdObjet(rs.getInt(1));
          myObjet.setName(rs.getString(2));
          myObjet.setUrlPhoto(rs.getString(3));
          myObjet.setDatePutOnOffert(lastDatePutOnOffer(myObjet.getIdObjet()));
          myObjet.setTimeSlotAvailable(rs.getString(4));
          myObjet.setNumberOfPeopleInterested(rs.getInt(5));
          myObjet.setState(rs.getString(6));
          myObjet.setType(rs.getInt(7));
          myObjet.setIdOfferor(rs.getInt(8));
          myObjet.setIdRecipient(rs.getInt(9));
          listeObjet.add(myObjet);
          dejaPresent.add(myObjet.getIdObjet());
        }
      }
      objet.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return listeObjet;
  }

  @Override
  public List<ObjectDTO> getObject(String status) {
    PreparedStatement objet;
    ArrayList<ObjectDTO> listeObjet = new ArrayList<>();
    ArrayList<Integer> dejaPresent = new ArrayList<>();
    try {
      objet = service.getPS(
          " SELECT o.id_object,o.name,o.description,o.url_photo,o.id_recipient,"
              + " o.number_of_people_interested,o.state,o.type,o.id_offeror,"
              + " o.time_slots_available, of.date_put_on_offer, of.id_offer "
              + " FROM projet_ae_08.objects o, projet_ae_08.offers of "
              + " WHERE o.state = ? "
              + " AND of.id_object = o.id_object"
              + " AND of.date_put_on_offer = ( SELECT max(of2.date_put_on_offer)"
              + " FROM projet_ae_08.offers of2 WHERE of2.id_object = o.id_object)"
              + " ORDER BY of.date_put_on_offer DESC, of.id_offer DESC ");
      objet.setString(1, status);
      ResultSet rs = objet.executeQuery();
      while (rs.next()) {
        if (!dejaPresent.contains(rs.getInt(1))) {
          ObjectDTO myObjet = myFactory.getObjetDTO();
          myObjet.setIdObjet(rs.getInt(1));
          myObjet.setName(rs.getString(2));
          myObjet.setDescription(rs.getString(3));
          myObjet.setUrlPhoto(rs.getString(4));
          myObjet.setDatePutOnOffert(lastDatePutOnOffer(myObjet.getIdObjet()));
          myObjet.setIdRecipient(rs.getInt(5));
          myObjet.setNumberOfPeopleInterested(rs.getInt(6));
          myObjet.setState(rs.getString(7));
          myObjet.setType(rs.getInt(8));
          myObjet.setIdOfferor(rs.getInt(9));
          myObjet.setTimeSlotAvailable(rs.getString(10));
          listeObjet.add(myObjet);
          dejaPresent.add(myObjet.getIdObjet());
        }
      }
      objet.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return listeObjet;
  }

  @Override
  public List<ObjectDTO> getMyObjects(int id) {
    PreparedStatement objet;
    ArrayList<ObjectDTO> listeObjet = new ArrayList<>();
    try {
      objet = service.getPS(
          " SELECT o.id_object,o.name,o.description,o.url_photo,o.time_slots_available,"
              + " o.number_of_people_interested,o.state,o.type,o.id_offeror,o.id_recipient "
              + " FROM projet_ae_08.objects o "
              + " WHERE o.id_offeror = ?"
      );
      objet.setInt(1, id);
      ResultSet rs = objet.executeQuery();
      while (rs.next()) {
        ObjectDTO myObjet = myFactory.getObjetDTO();
        myObjet.setIdObjet(rs.getInt(1));
        myObjet.setDescription(rs.getString(3));
        myObjet.setName(rs.getString(2));
        myObjet.setUrlPhoto(rs.getString(4));
        myObjet.setState(rs.getString(7));
        myObjet.setIdOfferor(rs.getInt(9));
        myObjet.setNumberOfPeopleInterested(rs.getInt(6));
        myObjet.setTimeSlotAvailable(rs.getString(5));
        myObjet.setIdRecipient(rs.getInt(10));
        myObjet.setType(rs.getInt(8));
        listeObjet.add(myObjet);
      }
      objet.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return listeObjet;
  }


  @Override
  public List<ObjectDTO> getMyObjectsRecipient(int id, boolean received) {
    PreparedStatement object;
    ArrayList<ObjectDTO> listObject = new ArrayList<>();
    try {
      if (received) {
        object = service.getPS(
            " SELECT o.id_object,o.name,o.description,o.url_photo,o.time_slots_available,"
                + " o.number_of_people_interested,o.state,o.type,o.id_offeror,o.id_recipient "
                + " FROM projet_ae_08.objects o "
                + " WHERE o.id_recipient = ? AND o.state = 'Donné'"
        );
      } else {
        object = service.getPS(
            " SELECT o.id_object,o.name,o.description,o.url_photo,o.time_slots_available,"
                + " o.number_of_people_interested,o.state,o.type,o.id_offeror,o.id_recipient "
                + " FROM projet_ae_08.objects o "
                + " WHERE o.id_recipient = ? AND o.state <> 'Donné'"
        );
      }
      object.setInt(1, id);
      ResultSet rs = object.executeQuery();
      while (rs.next()) {
        ObjectDTO myObjet = myFactory.getObjetDTO();
        myObjet.setName(rs.getString(2));
        myObjet.setIdObjet(rs.getInt(1));
        myObjet.setDescription(rs.getString(3));
        myObjet.setUrlPhoto(rs.getString(4));
        myObjet.setTimeSlotAvailable(rs.getString(5));
        myObjet.setNumberOfPeopleInterested(rs.getInt(6));
        myObjet.setType(rs.getInt(8));
        myObjet.setState(rs.getString(7));
        myObjet.setIdOfferor(rs.getInt(9));
        myObjet.setIdRecipient(rs.getInt(10));
        listObject.add(myObjet);
      }
      object.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return listObject;
  }

  @Override
  public ObjectDTO getAObject(int idObject) {
    PreparedStatement object;
    ObjectDTO myOffer = myFactory.getObjetDTO();
    try {
      object = service.getPS(
          " SELECT o.name,o.id_object,o.url_photo,o.description,o.time_slots_available,"
              + " o.number_of_people_interested,o.state,o.type,"
              + " o.id_offeror,o.id_recipient,o.version_object "
              + " FROM projet_ae_08.objects o "
              + " WHERE o.id_object=? ");
      object.setInt(1, idObject);
      ResultSet rs = object.executeQuery();
      while (rs.next()) {
        myOffer.setName(rs.getString(1));
        myOffer.setIdObjet(rs.getInt(2));
        myOffer.setDescription(rs.getString(4));
        myOffer.setUrlPhoto(rs.getString(3));
        myOffer.setNumberOfPeopleInterested(rs.getInt(6));
        myOffer.setTimeSlotAvailable(rs.getString(5));
        myOffer.setDatePutOnOffert(lastDatePutOnOffer(idObject));
        myOffer.setIdRecipient(rs.getInt(10));
        myOffer.setState(rs.getString(7));
        myOffer.setIdOfferor(rs.getInt(9));
        myOffer.setType(rs.getInt(8));
        myOffer.setVersionObject(rs.getInt(11));
      }
      object.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return myOffer;
  }

  @Override
  public String getOfferer(int idOfferer) {
    PreparedStatement types;
    String offererString = null;
    try {
      types = service.getPS(
          " SELECT m.username "
              + " FROM projet_ae_08.members m "
              + " WHERE m.id_member=? ");
      types.setInt(1, idOfferer);
      ResultSet rs = types.executeQuery();
      while (rs.next()) {
        offererString = rs.getString(1);
      }
      types.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return offererString;
  }

  @Override
  public int getNumberOfInterrest(int idObject) {
    PreparedStatement number;
    int numberOfI = 0;

    try {
      number = service.getPS(
          " SELECT o.number_of_people_interested "
              + " FROM projet_ae_08.objects o "
              + " WHERE o.id_object=? ");
      number.setInt(1, idObject);
      ResultSet rs = number.executeQuery();
      while (rs.next()) {
        numberOfI = rs.getInt(1);
      }
      number.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return numberOfI;
  }

  @Override
  public String lastDatePutOnOffer(int idObject) {
    PreparedStatement lastDate;
    Date date = null;

    try {
      lastDate = service.getPS(
          " SELECT o.date_put_on_offer "
              + "FROM projet_ae_08.offers o "
              + " WHERE o.date_put_on_offer = ( SELECT max(date_put_on_offer)"
              + " FROM projet_ae_08.offers WHERE id_object = ? ) AND o.id_object = ? ");
      lastDate.setInt(1, idObject);
      lastDate.setInt(2, idObject);
      ResultSet rs = lastDate.executeQuery();

      while (rs.next()) {
        date = rs.getDate(1);
      }
      lastDate.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }

    if (date == null) {
      throw new WebApplicationException("il n'y a pas de date de mise en offre pour cet object",
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return date.toString();
  }

  @Override
  public int getIdObject(ObjectDTO objet) {
    PreparedStatement idObjectStatement;
    int idObject = -1;
    try {
      idObjectStatement = service.getPS(
          " SELECT o.id_object"
              + " FROM projet_ae_08.objects o "
              + " WHERE o.name=? AND o.description=? AND o.url_photo=? AND "
              + " o.time_slots_available=? AND o.state=? AND o.id_offeror=? ");
      idObjectStatement.setString(1, objet.getName());
      idObjectStatement.setString(2, objet.getDescription());
      idObjectStatement.setString(3, objet.getUrlPhoto());
      idObjectStatement.setString(4, objet.getTimeSlotAvailable());
      idObjectStatement.setString(5, objet.getState());
      idObjectStatement.setInt(6, objet.getIdOfferor());
      ResultSet rs = idObjectStatement.executeQuery();
      while (rs.next()) {
        idObject = rs.getInt(1);
      }
      idObjectStatement.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return idObject;
  }

  @Override
  public int getTypesByString(String type) {
    PreparedStatement types;
    int typeId = -1;
    try {
      types = service.getPS(
          " SELECT t.id_type "
              + " FROM projet_ae_08.types t "
              + " WHERE t.wording_type=? ");
      types.setString(1, type);
      ResultSet rs = types.executeQuery();
      while (rs.next()) {
        typeId = rs.getInt(1);
      }
      types.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return typeId;
  }

  @Override
  public String getTypes(int type) {
    PreparedStatement types;
    String typeString = null;
    try {
      types = service.getPS(
          " SELECT t.wording_type "
              + " FROM projet_ae_08.types t "
              + " WHERE t.id_type=? ");
      types.setInt(1, type);
      ResultSet rs = types.executeQuery();
      while (rs.next()) {
        typeString = rs.getString(1);
      }
      types.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return typeString;
  }

  @Override
  public List<String> getAllTypes() {
    PreparedStatement allTypes;
    List<String> reponseTypes = new ArrayList<>();

    try {
      allTypes = service.getPS(
          " SELECT t.wording_type "
              + " FROM projet_ae_08.types t ");
      ResultSet rs = allTypes.executeQuery();
      while (rs.next()) {
        reponseTypes.add(rs.getString(1));
      }
      allTypes.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }

    return reponseTypes;
  }

  @Override
  public int getVersionObject(int idObject) {
    PreparedStatement version;
    int versionReturn = 1;

    try {
      version = service.getPS(
          " SELECT o.version_object "
              + " FROM projet_ae_08.objects o "
              + " WHERE o.id_object = ? "
      );
      version.setInt(1, idObject);
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

  @Override
  public List<Integer> getAllIdObjectOfInterrest(int idMember) {
    PreparedStatement idObject;
    List<Integer> r = new ArrayList<>();

    try {
      idObject = service.getPS(
          " SELECT i.id_object"
              + " FROM projet_ae_08.interests i "
              + " WHERE id_member = ? ");
      idObject.setInt(1, idMember);
      ResultSet rs = idObject.executeQuery();
      while (rs.next()) {
        r.add(rs.getInt(1));
      }
      idObject.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return r;
  }

  //-------------------------------------VERIFICATION---------------------------------------------

  @Override
  public boolean checkIfObjectIsEvaluated(int idObject) {
    PreparedStatement checkEvaluated;
    boolean result = false;

    try {
      checkEvaluated = service.getPS(
          " SELECT r.id_object "
              + " FROM projet_ae_08.ratings r "
              + " WHERE r.id_object=? ");
      checkEvaluated.setInt(1, idObject);
      ResultSet rs = checkEvaluated.executeQuery();
      if (rs.next()) {
        result = true;
      }
      checkEvaluated.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return result;
  }

  @Override
  public boolean checkIfHeIsAlreadyInterrest(int idObject, int idUser) {
    PreparedStatement checkInterrest;

    try {
      checkInterrest = service.getPS(
          " SELECT i.id_member "
              + " FROM projet_ae_08.interests i "
              + " WHERE i.id_member=? AND i.id_object=? ");
      checkInterrest.setInt(1, idUser);
      checkInterrest.setInt(2, idObject);
      checkInterrest.executeQuery();
      ResultSet rs = checkInterrest.executeQuery();
      while (rs.next()) {
        String idMember = rs.getString(1);
        int id = Integer.parseInt(idMember);
        if (id == idUser) {
          return false;
        }
      }
      checkInterrest.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public boolean checkIfObjectHasRecipient(int idObject) {
    PreparedStatement checkRecipient;
    boolean response = true;
    try {
      checkRecipient = service.getPS(
          " SELECT o.id_recipient "
              + " FROM projet_ae_08.objects o "
              + " WHERE o.id_object=? ");
      checkRecipient.setInt(1, idObject);
      checkRecipient.executeQuery();
      ResultSet rs = checkRecipient.executeQuery();
      while (rs.next()) {
        int idRecipient = rs.getInt(1);
        if (idRecipient < 1) {
          response = false;
        } else {
          response = true;
        }
      }
      checkRecipient.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return response;
  }

  @Override
  public boolean checkIfRecipientIsInterested(int idObject, int idRecipient) {
    PreparedStatement checkRecipient;
    boolean response = true;
    try {
      checkRecipient = service.getPS(
          " SELECT i.id_interest "
              + " FROM projet_ae_08.interests i"
              + " WHERE i.id_object=? AND i.id_member=?");
      checkRecipient.setInt(1, idObject);
      checkRecipient.setInt(2, idRecipient);
      checkRecipient.executeQuery();
      ResultSet rs = checkRecipient.executeQuery();
      while (rs.next()) {
        int idInterest = rs.getInt(1);
        if (idInterest < 1) {
          response = false;
        } else {
          response = true;
        }
      }
      checkRecipient.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return response;
  }

  @Override
  public boolean alreadyShowInterrest(int idUser, int idObject) {
    PreparedStatement check;
    Boolean find = false;

    try {
      check = service.getPS(
          " SELECT *  "
              + " FROM projet_ae_08.interests i "
              + " WHERE i.id_object = ? AND i.id_member = ? ");
      check.setInt(1, idObject);
      check.setInt(2, idUser);
      ResultSet rs = check.executeQuery();

      while (rs.next()) {
        find = true;
      }
      check.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return find;
  }

  @Override
  public boolean checkIfObjectIsReserved(int idObject) {
    PreparedStatement checkObjectState;
    boolean response = true;
    try {
      checkObjectState = service.getPS(
          " SELECT o.state "
              + " FROM projet_ae_08.objects o"
              + " WHERE o.id_object=? ");
      checkObjectState.setInt(1, idObject);
      checkObjectState.executeQuery();
      ResultSet rs = checkObjectState.executeQuery();
      while (rs.next()) {
        String state = rs.getString(1);
        response = state.equals("Réservé");
      }
      checkObjectState.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return response;
  }

  //----------------------------------SETTERS-----------------------------------------------------
  @Override
  public boolean addRecipient(int idObject, int idRecipient) {
    PreparedStatement addRecipient;
    try {
      addRecipient = service.getPS(
          " UPDATE projet_ae_08.objects "  //change state aussi
              + " SET id_recipient=?,state='Réservé', version_object = version_object + 1 "
              + " WHERE id_object=? ");
      addRecipient.setInt(1, idRecipient);
      addRecipient.setInt(2, idObject);
      addRecipient.execute();
      addRecipient.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public boolean addAInterrest(int idObject, int idUser,
      String plageHorraire, boolean conversationNeeded) {
    PreparedStatement addInterrest;

    try {
      addInterrest = service.getPS(
          " INSERT INTO projet_ae_08.interests "
              + " VALUES (DEFAULT, ?, ?, ?, ?) ");
      addInterrest.setString(1, plageHorraire);
      addInterrest.setBoolean(2, conversationNeeded);
      addInterrest.setInt(3, idObject);
      addInterrest.setInt(4, idUser);
      addInterrest.execute();
      addInterrest.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public void addAInterrestInObject(int idObject, int numberOfI) {

    PreparedStatement addOne;
    numberOfI += 1;
    try {
      addOne = service.getPS(
          " UPDATE projet_ae_08.objects "
              + " SET number_of_people_interested=?"
              + " WHERE id_object=? ");
      addOne.setInt(1, numberOfI);
      addOne.setInt(2, idObject);
      addOne.executeUpdate();
      addOne.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }

  }

  @Override
  public boolean cancelOffer(int idObject) {
    PreparedStatement object;
    try {
      object = service.getPS(
          "UPDATE projet_ae_08.objects "
              + "SET state='Annulé' , version_object = version_object + 1 "
              + "WHERE id_object=? ");
      object.setInt(1, idObject);
      object.executeUpdate();
      object.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;

  }

  @Override
  public boolean editAOffer(int idObject, String desc, String timeSlot) {
    PreparedStatement object;
    try {
      object = service.getPS(
          "UPDATE projet_ae_08.objects "
              + "SET description=? , time_slots_available=?, version_object = version_object + 1 "
              + "WHERE id_object=? ");
      object.setString(1, desc);
      object.setString(2, timeSlot);
      object.setInt(3, idObject);
      object.executeUpdate();
      object.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public boolean reOfferObject(int idObject) { //remettre une date a jour dans offer
    PreparedStatement object;
    try {
      object = service.getPS(
          "UPDATE projet_ae_08.objects "
              + "SET state='Offert', id_recipient=NULL, version_object = version_object + 1 "
              + "WHERE id_object=? ");
      object.setInt(1, idObject);
      object.executeUpdate();
      object.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;

  }

  @Override
  public boolean indicateObjectDonated(int idObject, boolean state) {
    PreparedStatement object;
    try {
      if (state) {
        object = service.getPS(
            "UPDATE projet_ae_08.objects "
                + "SET state=?, version_object = version_object + 1 "
                + "WHERE id_object=? ");
        object.setInt(2, idObject);
        object.setString(1, "Donné");
      } else {
        object = service.getPS(
            "UPDATE projet_ae_08.objects "
                + "SET state=? , id_recipient=NULL, version_object = version_object + 1 "
                + "WHERE id_object=? ");
        object.setInt(2, idObject);
        object.setString(1, "Pas donné");
      }
      object.executeUpdate();
      object.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public boolean updateObjectPicture(int idObject, String fileName) {
    PreparedStatement picture;
    System.out.println(idObject);
    System.out.println(fileName);
    try {
      picture = service.getPS(
          " UPDATE projet_ae_08.objects"
              + " SET url_photo = ?, version_object = version_object + 1 "
              + " WHERE id_object = ?");
      picture.setString(1, fileName);
      picture.setInt(2, idObject);
      picture.executeUpdate();
      picture.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public boolean setObjectToDisableRState(int idObject) {
    PreparedStatement object;
    try {
      object = service.getPS(
          " UPDATE projet_ae_08.objects "
              + " SET state = 'Receveur désactivé', version_object = version_object + 1 "
              + " WHERE id_object = ? ");
      object.setInt(1, idObject);
      object.executeUpdate();
      object.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return false;
  }

  @Override
  public Boolean deleteAllInterets(int idMember) {
    PreparedStatement interet;
    try {
      interet = service.getPS(
          " DELETE FROM projet_ae_08.interests "
              + " WHERE id_member = ? ");
      interet.setInt(1, idMember);
      interet.execute();
      interet.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public Boolean decrementNumberInterestObject(int idObject) {
    PreparedStatement object;
    try {
      object = service.getPS(
          " UPDATE projet_ae_08.objects "
              + " SET number_of_people_interested = number_of_people_interested - 1, "
              + " version_object = version_object + 1 "
              + " WHERE id_object = ? ");
      object.setInt(1, idObject);
      object.executeUpdate();
      object.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }


  //----------------------------------CREATE---------------------------------------------------
  @Override
  public boolean createType(String type) {
    PreparedStatement typeStatement;
    try {
      typeStatement = service.getPS(" INSERT INTO projet_ae_08.types "
          + " VALUES (DEFAULT, ?) ");
      typeStatement.setString(1, type);
      typeStatement.execute();
      typeStatement.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public int createObject(ObjectDTO object, int idType) {
    PreparedStatement objectStatement;
    int idObject = -1;
    try {
      objectStatement = service.getPS(
          " INSERT INTO projet_ae_08.objects "
              + " VALUES (DEFAULT, ?, ?, ?, ?, 0, 'Offert', 1, ?, ?, NULL)"
              + " RETURNING id_object ");
      objectStatement.setString(1, object.getName());
      objectStatement.setString(2, object.getDescription());
      objectStatement.setString(3, object.getUrlPhoto());
      objectStatement.setString(4, object.getTimeSlotAvailable());
      objectStatement.setInt(5, idType);
      objectStatement.setInt(6, object.getIdOfferor());
      ResultSet rs = objectStatement.executeQuery();
      while (rs.next()) {
        idObject = rs.getInt(1);
        System.out.println(idObject);
      }

      objectStatement.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return idObject;

  }

  @Override
  public boolean createOffer(int idObject) {
    PreparedStatement offerStatement;
    try {
      offerStatement = service.getPS(
          " INSERT INTO projet_ae_08.offers "
              + " VALUES (DEFAULT, ?, ?) ");
      Date date = new Date(System.currentTimeMillis());
      java.sql.Date sqlDate = new java.sql.Date(date.getTime());
      offerStatement.setDate(1, sqlDate);
      offerStatement.setInt(2, idObject);
      offerStatement.execute();
      offerStatement.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public boolean createEvaluation(int idObject, int starRate, String remark, int idMember) {
    PreparedStatement evaluationStatement;
    try {
      evaluationStatement = service.getPS(
          " INSERT INTO projet_ae_08.ratings "
              + " VALUES (?, ?, ?, ?) ");
      evaluationStatement.setInt(1, starRate);
      evaluationStatement.setString(2, remark);
      evaluationStatement.setInt(3, idObject);
      evaluationStatement.setInt(4, idMember);
      evaluationStatement.execute();
      evaluationStatement.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return true;
  }

  @Override
  public List<SearchObjectDTO> search(String search) {
    PreparedStatement searchObject;
    List<SearchObjectDTO> objects = new ArrayList<>();
    try {
      searchObject = service.getPS(
          " SELECT Distinct m.last_name,t.wording_type,o.state,o.name,"
              + " o.id_object,o.url_photo,o.number_of_people_interested,m.username   "
              + " FROM projet_ae_08.objects o, projet_ae_08.types t,"
              + " projet_ae_08.members m,projet_ae_08.offers off "
              + " WHERE (o.id_offeror =m.id_member AND o.type=t.id_type "
              + " AND o.id_object= off.id_object) AND "
              + "      ( m.last_name LIKE ? "
              + "    OR t.wording_type LIKE ? "
              + "    OR o.state LIKE ? ) ");

      searchObject.setString(1, "%" + search + "%");
      searchObject.setString(2, "%" + search + "%");
      searchObject.setString(3, "%" + search + "%");
      //searchObject.setString(4, search);

      ResultSet rs = searchObject.executeQuery();
      while (rs.next()) {
        SearchObjectDTO mysearching = myFactory.getSearchObjectDTO();
        mysearching.setLastName(rs.getString(1));
        mysearching.setWordingType(rs.getString(2));
        mysearching.setState(rs.getString(3));
        mysearching.setNomObject(rs.getString(4));
        mysearching.setIdObject(rs.getInt(5));
        mysearching.seturlPt(rs.getString(6));
        mysearching.setNumberOInterest(rs.getInt(7));
        mysearching.setUsernameOfferor(rs.getString(8));
        objects.add(mysearching);
      }
      searchObject.close();
      rs.close();
    } catch (SQLException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return objects;
  }

  @Override
  public List<SearchObjectDTO> searchDate(String search) {
    PreparedStatement searchObjectDate;
    List<SearchObjectDTO> objects = new ArrayList<>();
    try {
      searchObjectDate = service.getPS(
          " SELECT Distinct m.last_name,t.wording_type,o.state,o.name,"
              + " o.id_object,o.url_photo,o.number_of_people_interested,off.date_put_on_offer "
              + " FROM projet_ae_08.objects o, projet_ae_08.types t,"
              + " projet_ae_08.members m,projet_ae_08.offers off "
              + " WHERE (o.id_offeror =m.id_member AND o.type=t.id_type "
              + " AND o.id_object= off.id_object) AND "
              + "      (off.date_put_on_offer = ? OR off.date_put_on_offer > ? )"
              + " ORDER BY off.date_put_on_offer DESC ");
      Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(search);
      java.sql.Date sqlDate = new java.sql.Date(date1.getTime());
      searchObjectDate.setDate(1, sqlDate);
      searchObjectDate.setDate(2, sqlDate);

      ResultSet rs = searchObjectDate.executeQuery();
      while (rs.next()) {
        SearchObjectDTO mysearchingDate = myFactory.getSearchObjectDTO();
        mysearchingDate.setState(rs.getString(3));
        mysearchingDate.setNomObject(rs.getString(4));
        mysearchingDate.setLastName(rs.getString(1));
        mysearchingDate.setWordingType(rs.getString(2));
        mysearchingDate.setIdObject(rs.getInt(5));
        mysearchingDate.seturlPt(rs.getString(6));
        mysearchingDate.setNumberOInterest(rs.getInt(7));
        objects.add(mysearchingDate);
      }
      searchObjectDate.close();
      rs.close();
    } catch (SQLException | ParseException e) {
      throw new WebApplicationException(e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
    return objects;
  }
}
