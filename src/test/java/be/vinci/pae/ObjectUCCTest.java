package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.biz.FactoryImpl;
import be.vinci.pae.biz.object.Object;
import be.vinci.pae.biz.object.ObjectDTO;
import be.vinci.pae.biz.object.ObjectUseCaseController;
import be.vinci.pae.biz.searchobject.SearchObjectDTO;
import be.vinci.pae.dal.DalServices;
import be.vinci.pae.dal.objectdao.ObjectDAO;
import be.vinci.pae.dal.userdao.UserDAO;
import be.vinci.pae.utils.Config;
import jakarta.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ObjectUCCTest {


  private static ObjectUseCaseController objectUCC;
  private static ServiceLocator locator;

  static {
    Config.load("ex.properties");
  }


  private ObjectDAO objectDAO;
  private UserDAO userDAO;
  private DalServices dalService;
  private ObjectDTO object1;
  private ObjectDTO objectDTO;
  private SearchObjectDTO searchObjectDTO;
  private FactoryImpl factory;

  /**
   * initialize ApplicationBinder and UserCasController before all test.
   */
  @BeforeAll
  public static void init() {
    locator = ServiceLocatorUtilities.bind(new ApplicationBinder());

    objectUCC = locator.getService(ObjectUseCaseController.class);

  }

  @BeforeEach
  void setUp() {
    objectDAO = locator.getService(ObjectDAO.class);
    Mockito.clearInvocations(objectDAO);
    Mockito.reset(objectDAO);
    userDAO = locator.getService(UserDAO.class);
    Mockito.clearInvocations(userDAO);
    Mockito.reset(userDAO);
    dalService = locator.getService(DalServices.class);
    Mockito.clearInvocations(dalService);
    Mockito.reset(dalService);
  }

  @Test
  void getAObjectWithAWrongId() {
    Mockito.when(objectDAO.getAObject(3)).thenReturn(null);
    assertNull(objectUCC.getAObject(3));
  }

  @Test
  void getAObjectWithAGoofId() {
    ObjectDTO object;
    object = Mockito.mock(ObjectDTO.class);
    Mockito.when(objectDAO.getAObject(1)).thenReturn(object);
    assertEquals(object, objectUCC.getAObject(1));
  }

  @Test
  void getAObjectShouldReturnException() {
    Mockito.when(objectDAO.getAObject(2)).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.getAObject(2);
    });
  }


  @Test
  void cancelAOffertWithAGoodId() {
    Mockito.when(objectDAO.cancelOffer(2)).thenReturn(true);
    assertTrue(objectUCC.cancelOffer(2));
  }

  @Test
  void cancelAOffertWithAFalseId() {
    Mockito.when(objectDAO.cancelOffer(1)).thenReturn(false);
    assertFalse(objectUCC.cancelOffer(1));
  }

  @Test
  void cancelOfferShouldReturnException() {
    Mockito.when(objectDAO.cancelOffer(6)).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.cancelOffer(6);
    });
  }


  @Test
  void createOfferWithGoodId() {

    Mockito.when(objectDAO.createOffer(1)).thenReturn(true);
    assertTrue(objectUCC.createOffer(1));
  }

  @Test
  void createOfferWithNotAGoodId() {

    Mockito.when(objectDAO.createOffer(1)).thenReturn(false);
    assertFalse(objectUCC.createOffer(1));
  }

  @Test
  void createOfferShouldReturnException() {
    Mockito.when(objectDAO.createOffer(7)).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.createOffer(7);
    });
  }

  @Test
  void getIdObjectWithAExistantObject() {
    ObjectDTO object;
    object = Mockito.mock(ObjectDTO.class);
    object.setIdObjet(1);
    Mockito.when(objectDAO.getIdObject(object)).thenReturn(1);
    assertEquals(1, objectUCC.getIdObject(object));
  }

  @Test
  void getIdObjectWithNotAExistantObject() {
    ObjectDTO object;
    object = Mockito.mock(ObjectDTO.class);
    object.setIdObjet(1);
    Mockito.when(objectDAO.getIdObject(object)).thenReturn(-1);
    assertEquals(-1, objectUCC.getIdObject(object));
  }

  @Test
  void getIdObjectShouldReturnException() {
    Mockito.when(objectDAO.getIdObject(objectDTO)).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.getIdObject(objectDTO);
    });
  }

  @Test
  void createTypeWithAllGood() {

    Mockito.when(objectDAO.createType("meuble")).thenReturn(true);
    assertTrue(objectUCC.createType("meuble"));
  }

  @Test
  void createTypeWithNotAllGood() {

    Mockito.when(objectDAO.createType("meuble")).thenReturn(false);
    assertFalse(objectUCC.createType("meuble"));
  }

  @Test
  void createTypeShouldReturnException() {
    Mockito.when(objectDAO.createType("typename")).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.createType("typename");
    });
  }

  @Test
  void getLastObjectWithNoObject() {
    List<ObjectDTO> listObject = new ArrayList<>();
    ObjectDTO object = null;
    listObject.add(object);
    Mockito.when(objectDAO.getLastOffer()).thenReturn(listObject);
    assertEquals(null, objectUCC.getLastObjet());
  }

  @Test
  void getLastObject() {
    List<ObjectDTO> listObject = new ArrayList<>();
    object1 = Mockito.mock(ObjectDTO.class);
    Mockito.when((object1.getType())).thenReturn(2);
    listObject.add(object1);
    Mockito.when(objectDAO.getLastOffer()).thenReturn(listObject);
    Mockito.when(objectDAO.getTypes(2)).thenReturn("meuble");
    assertEquals(listObject, objectUCC.getLastObjet());
  }

  @Test
  void getLastObjectShouldReturnException() {
    Mockito.when(objectDAO.getLastOffer()).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.getLastObjet();
    });
  }

  @Test
  void getObject() {
    List<ObjectDTO> listObject = new ArrayList<>();
    object1 = Mockito.mock(ObjectDTO.class);
    Mockito.when((object1.getType())).thenReturn(1);
    listObject.add(object1);
    Mockito.when(objectDAO.getObject("Offert")).thenReturn(listObject);
    Mockito.when(objectDAO.getTypes(1)).thenReturn("meuble");
    assertEquals(listObject, objectUCC.getObjet("Offert"));
  }

  @Test
  void getObjetShouldReturnException() {
    Mockito.when(objectDAO.getObject("status")).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.getObjet("status");
    });
  }

  @Test
  void getObjectWithNoObject() {
    List<ObjectDTO> listObject = new ArrayList<>();
    ObjectDTO object = null;
    listObject.add(object);
    Mockito.when(objectDAO.getObject("Offert")).thenReturn(listObject);
    assertEquals(null, objectUCC.getObjet("Offert"));
  }

  @Test
  void getAllType() {
    List<String> listType = new ArrayList<>();
    Mockito.when(objectDAO.getAllTypes()).thenReturn(listType);
    assertEquals(listType, objectUCC.getAllTypes());
  }

  @Test
  void getAllTypeShouldReturnException() {
    Mockito.when(objectDAO.getAllTypes()).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.getAllTypes();
    });
  }

  @Test
  void getMyObjectWithNoObject() {
    List<ObjectDTO> listobject = new ArrayList<>();
    Mockito.when(objectDAO.getMyObjects(1)).thenReturn(listobject);
    assertEquals(listobject, objectUCC.getMyObjects(1));
  }

  @Test
  void getMyObject() {
    List<ObjectDTO> listObject = new ArrayList<>();
    object1 = Mockito.mock(ObjectDTO.class);
    Mockito.when((object1.getType())).thenReturn(1);
    listObject.add(object1);
    Mockito.when(objectDAO.getMyObjects(1)).thenReturn(listObject);
    Mockito.when(objectDAO.getTypes(1)).thenReturn("meuble");
    assertEquals(listObject, objectUCC.getMyObjects(1));
  }

  @Test
  void getMyObjectShouldReturnException() {
    Mockito.when(objectDAO.getMyObjects(5)).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.getMyObjects(5);
    });
  }

  /*
  searchObject test with a type "meuble"
   */
  @Test
  void getSearchObject() {
    List<SearchObjectDTO> listObject = new ArrayList<>();
    searchObjectDTO = Mockito.mock(SearchObjectDTO.class);
    listObject.add(searchObjectDTO);
    Mockito.when(objectDAO.search("Meuble")).thenReturn(listObject);
    assertEquals(listObject, objectUCC.search("Meuble"));
  }

  @Test
  void getSearchObjectWithNull() {
    List<SearchObjectDTO> listObject = new ArrayList<>();
    Mockito.when(objectDAO.search(null)).thenReturn(listObject);
    assertEquals(listObject, objectUCC.search(null));
  }

  @Test
  void getSearchShouldReturnException() {
    Mockito.when(objectDAO.search("aa")).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.search("aa");
    });
  }

  /*
  updateobjectpicture correct
   */
  @Test
  void updateObjectPicture() {
    Mockito.when(objectDAO.updateObjectPicture(1, "picture")).thenReturn(true);
    assertTrue(objectUCC.updateObjectPicture(1, "picture"));
  }

  /*
  updateobjectpicture incorrect
   */
  @Test
  void updateObjectPictureFalse() {
    Mockito.when(objectDAO.updateObjectPicture(1, "picture")).thenReturn(false);
    assertFalse(objectUCC.updateObjectPicture(1, "picture"));
  }

  @Test
  void updateObjectPictureShouldReturnException() {
    Mockito.when(objectDAO.updateObjectPicture(1, "picture"))
        .thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.updateObjectPicture(1, "picture");
    });
  }

  /*
   evaluateObject correct
   */
  @Test
  void evaluateObject() {
    Mockito.when(objectDAO.checkIfObjectIsEvaluated(1)).thenReturn(false);
    Mockito.when(objectDAO.createEvaluation(1, 3,
        "very good", 1)).thenReturn(true);
    assertTrue(objectUCC.evaluateObject(1, 3, "very good", 1));
  }

  @Test
  void evaluateObjectfalse() {
    Mockito.when(objectDAO.checkIfObjectIsEvaluated(1)).thenReturn(true);
    assertFalse(objectUCC.evaluateObject(1, 3, "very good", 1));
  }

  @Test
  void evaluateObjectShouldReturnException() {
    Mockito.when(objectDAO.checkIfObjectIsEvaluated(1))
        .thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.evaluateObject(1, 3, "very good", 1);
    });
  }



  /*
  indicateObject correct
   */
  @Test
  void indicateObjectDonatedShouldReturnTrue() {
    object1 = Mockito.mock(ObjectDTO.class);
    Mockito.when(objectDAO.checkIfObjectIsReserved(1)).thenReturn(true);
    Mockito.when(objectDAO.getAObject(1)).thenReturn(object1);
    Mockito.when(object1.getIdRecipient()).thenReturn(1);
    Mockito.when(userDAO.incrementNumberOfferNotPickedUp(2)).thenReturn(true);
    Mockito.when(objectDAO.indicateObjectDonated(1, false)).thenReturn(true);
    assertTrue(objectUCC.indicateObjectDonated(1, false));
  }

  @Test
  void indicateObjectDonatedStateTrueShouldReturnTrue() {
    object1 = Mockito.mock(ObjectDTO.class);
    Mockito.when(objectDAO.checkIfObjectIsReserved(1)).thenReturn(true);
    Mockito.when(objectDAO.indicateObjectDonated(1, true)).thenReturn(true);
    assertTrue(objectUCC.indicateObjectDonated(1, true));
  }


  /*
  indicateObject NotReserved
   */
  @Test
  void indicateObjectDonatedObjectNotReserved() {
    object1 = Mockito.mock(ObjectDTO.class);
    Mockito.when(objectDAO.checkIfObjectIsReserved(1)).thenReturn(false);
    assertFalse(objectUCC.indicateObjectDonated(1, false));
  }

  @Test
  void indicateObjectDonatedShouldReturnException() {
    Mockito.when(objectDAO.checkIfObjectIsReserved(1)).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.indicateObjectDonated(1, false);
    });
  }

  /*
  reofferObject correct
   */
  @Test
  void reofferObjectCorrect() {
    Object object;
    object = Mockito.mock(Object.class);
    Mockito.when(object.checkObjectStateEqualsPasDonne()).thenReturn(true);
    Mockito.when(objectDAO.getAObject(1)).thenReturn(object);
    object1 = Mockito.mock(ObjectDTO.class);
    Mockito.when(objectDAO.reOfferObject(1)).thenReturn(true);
    Mockito.when(objectDAO.createOffer(1)).thenReturn(true);
    assertTrue(objectUCC.reOfferObject(1));
  }


  /*
  reofferObject incorrect
   */
  @Test
  void reofferObjectIncorrect() {
    Object object;
    object = Mockito.mock(Object.class);
    Mockito.when(object.checkObjectStateEqualsPasDonne()).thenReturn(true);
    Mockito.when(objectDAO.getAObject(1)).thenReturn(object);
    object1 = Mockito.mock(ObjectDTO.class);
    Mockito.when(objectDAO.reOfferObject(1)).thenReturn(false);
    Mockito.when(objectDAO.createOffer(1)).thenReturn(true);
    assertFalse(objectUCC.reOfferObject(1));
  }

  @Test
  void reofferObjectIncorrectShouldReturnException() {
    Mockito.when(objectDAO.reOfferObject(1)).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.reOfferObject(1);
    });
  }

  @Test
  void reofferObjectWrongStateShouldReturnException() {
    Object object;
    object = Mockito.mock(Object.class);
    Mockito.when(objectDAO.getAObject(1)).thenReturn(object);
    Mockito.when(object.checkObjectStateEqualsPasDonne()).thenReturn(false);
    Exception exception = Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.reOfferObject(1);
    });
    String expectedMessage = "bad state of Object";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }


  /*
    editAOffer correct
     */
  @Test
  void editAOfferCorrect() {
    object1 = Mockito.mock(ObjectDTO.class);
    Mockito.when(objectDAO.editAOffer(1, "description",
        "lundi")).thenReturn(true);
    assertTrue(objectUCC.editAOffer(1, "description", "lundi"));
  }


  /*
    editAOffer incorrect
     */
  @Test
  void editAOfferIncorrect() {
    object1 = Mockito.mock(ObjectDTO.class);
    Mockito.when(objectDAO.editAOffer(1, "description",
        "lundi")).thenReturn(false);
    assertFalse(objectUCC.editAOffer(1, "description", "lundi"));
  }

  @Test
  void editAOfferIncorrectShouldReturnException() {
    Mockito.when(objectDAO.editAOffer(1, "description",
        "mardi")).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.editAOffer(1, "description", "mardi");
    });
  }


  @Test
  void addRecipientReturnTrue() {
    Object object;
    object = Mockito.mock(Object.class);
    Mockito.when(object.checkObjectStateCanBeReserved()).thenReturn(true);
    Mockito.when(objectDAO.getAObject(1)).thenReturn(object);
    Mockito.when(objectDAO.checkIfObjectHasRecipient(1)).thenReturn(false);
    Mockito.when(objectDAO.checkIfRecipientIsInterested(1, 1)).thenReturn(true);
    Mockito.when(objectDAO.addRecipient(1, 1)).thenReturn(true);
    assertTrue(objectUCC.addRecipient(1, 1));
  }

  @Test
  void addRecipientReturnFalse() {
    Object object;
    object = Mockito.mock(Object.class);
    Mockito.when(object.checkObjectStateCanBeReserved()).thenReturn(true);
    Mockito.when(objectDAO.getAObject(1)).thenReturn(object);
    Mockito.when(objectDAO.checkIfObjectHasRecipient(1)).thenReturn(true);
    Mockito.when(object.getState()).thenReturn("valid");
    assertFalse(objectUCC.addRecipient(1, 1));
  }

  @Test
  void addRecipientReturnFalse2() {
    Object object;
    object = Mockito.mock(Object.class);
    Mockito.when(object.checkObjectStateCanBeReserved()).thenReturn(true);
    Mockito.when(objectDAO.getAObject(1)).thenReturn(object);
    Mockito.when(objectDAO.checkIfObjectHasRecipient(1)).thenReturn(false);
    Mockito.when(objectDAO.checkIfRecipientIsInterested(1, 1)).thenReturn(false);
    assertFalse(objectUCC.addRecipient(1, 1));
  }

  @Test
  void addRecipientShouldReturnException() {
    Mockito.when(objectDAO.checkIfObjectHasRecipient(1)).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.addRecipient(1, 1);
    });
  }

  @Test
  void addRecipientWithObjectStateCantBeReservedShouldReturnException() {
    Object object;
    object = Mockito.mock(Object.class);
    Mockito.when(objectDAO.getAObject(1)).thenReturn(object);
    Mockito.when(object.checkObjectStateCanBeReserved()).thenReturn(false);
    Exception exception = Assertions.assertThrows(WebApplicationException.class, () -> {
      objectUCC.addRecipient(1, 1);
    });
    String expectedMessage = "bad state of Object";
    String actualMessage = exception.getMessage();
    assertEquals(actualMessage,expectedMessage);
  }

  /*
  addAInterrest correct
   */
  @Test
  void addAInterrestReturnTrue() {
    object1 = Mockito.mock(ObjectDTO.class);
    Mockito.when(objectUCC.getAObject(1)).thenReturn(object1);
    Mockito.when(object1.getIdOfferor()).thenReturn(2);
    Mockito.when(objectDAO.checkIfHeIsAlreadyInterrest(1, 1)).thenReturn(true);
    Mockito.when(userDAO.addPhoneNumber(1, "251514814")).thenReturn(true);
    Mockito.when(objectDAO.getVersionObject(1)).thenReturn(0);
    Mockito.when(objectDAO.addAInterrest(1, 1,
        "plageHorraire", true)).thenReturn(true);
    Mockito.when(objectDAO.getNumberOfInterrest(1)).thenReturn(58152525);
    Mockito.doNothing().when(objectDAO).addAInterrestInObject(1, 58152525);
    assertTrue(objectUCC.addAInterrest(1, 1, "plageHorraire",
        true, "251514814", 0));
  }

  @Test
  void addAInterrestWithConversationNotNeededShouldReturnTrue() {
    object1 = Mockito.mock(ObjectDTO.class);
    Mockito.when(objectUCC.getAObject(1)).thenReturn(object1);
    Mockito.when(object1.getIdOfferor()).thenReturn(2);
    Mockito.when(objectDAO.checkIfHeIsAlreadyInterrest(1, 1)).thenReturn(true);
    Mockito.when(userDAO.addPhoneNumber(1, "251514814")).thenReturn(true);
    Mockito.when(objectDAO.getVersionObject(1)).thenReturn(0);
    Mockito.when(objectDAO.addAInterrest(1, 1,
        "plageHorraire", true)).thenReturn(true);
    Mockito.when(objectDAO.getNumberOfInterrest(1)).thenReturn(58152525);
    Mockito.doNothing().when(objectDAO).addAInterrestInObject(1, 58152525);
    assertTrue(objectUCC.addAInterrest(1, 1, "plageHorraire",
        false, "251514814", 0));
  }

  @Test
  void addAInterrestReturnFalseThrow() {
    object1 = Mockito.mock(ObjectDTO.class);
    Mockito.when(objectUCC.getAObject(1)).thenReturn(object1);
    Mockito.when(object1.getIdOfferor()).thenReturn(1);
    Assertions.assertThrows(WebApplicationException.class,
        () -> {
          objectUCC.addAInterrest(1, 1, "plageHorraire",
              true, "251514814", 0);
        });
  }

  @Test
  void addAInterrest() {
    object1 = Mockito.mock(ObjectDTO.class);
    Mockito.when(objectUCC.getAObject(5)).thenReturn(object1);
    Mockito.when(object1.getIdOfferor()).thenReturn(2);
    Mockito.when(objectDAO.checkIfHeIsAlreadyInterrest(5, 1)).thenReturn(false);
    assertFalse(objectUCC.addAInterrest(5, 1, "plageHorraire",
        true, "251514814", 0));
  }

  @Test
  void addAInterrestReturnThrow() {
    object1 = Mockito.mock(ObjectDTO.class);
    Mockito.when(objectUCC.getAObject(6)).thenReturn(object1);
    Mockito.when(object1.getIdOfferor()).thenReturn(2);
    Mockito.when(objectDAO.checkIfHeIsAlreadyInterrest(6, 1)).thenReturn(true);
    Mockito.when(userDAO.addPhoneNumber(1, "251514814")).thenReturn(true);
    Mockito.when(objectDAO.getVersionObject(6)).thenReturn(1);
    Mockito.when(objectDAO.addAInterrest(6, 1,
        "plageHorraire", true)).thenReturn(true);
    Mockito.when(objectDAO.getNumberOfInterrest(6)).thenReturn(58152525);
    Mockito.doNothing().when(objectDAO).addAInterrestInObject(6, 58152525);
    Assertions.assertThrows(WebApplicationException.class,
        () -> {
          objectUCC.addAInterrest(6, 1, "plageHorraire",
              true, "251514814", 0);
        });
  }

  /*
  getObjectsReceived correct
   */

  /*
  getMyObjectsRecipient correct
   */
  @Test
  void getMyObjectsRecipientReturnTrue() {
    List<ObjectDTO> listObject = new ArrayList<>();
    Mockito.when(objectDAO.getMyObjectsRecipient(1, true)).thenReturn(listObject);
    Mockito.when(objectDAO.getTypes(1)).thenReturn("Meuble");
    assertEquals(listObject, objectUCC.getMyObjectsRecipient(1, true));
  }


}
