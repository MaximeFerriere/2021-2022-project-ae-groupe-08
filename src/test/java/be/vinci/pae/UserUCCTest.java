package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.object.Object;
import be.vinci.pae.biz.object.ObjectDTO;
import be.vinci.pae.biz.user.AddressDTO;
import be.vinci.pae.biz.user.InterestDTO;
import be.vinci.pae.biz.user.User;
import be.vinci.pae.biz.user.UserCaseController;
import be.vinci.pae.biz.user.UserDTO;
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
import org.mockito.Mock;
import org.mockito.Mockito;


public class UserUCCTest {


  private static UserCaseController userUCC;
  private static ServiceLocator locator;

  static {
    Config.load("ex.properties");
  }

  private User user1;
  private User user2;
  private User user3;
  private AddressDTO address;
  private UserDAO userDAO;
  private ObjectDTO object1;
  private ObjectDTO object2;
  private ObjectDAO objectDAO;
  private DalServices dalService;
  private InterestDTO interestDTO1;
  private InterestDTO interestDTO2;
  private Factory factory;

  /**
   * initialize ApplicationBinder and UserCasController before all test.
   */
  @BeforeAll
  public static void init() {
    locator = ServiceLocatorUtilities.bind(new ApplicationBinder());

    userUCC = locator.getService(UserCaseController.class);

  }

  @BeforeEach
  void setUp() {
    userDAO = locator.getService(UserDAO.class);
    Mockito.clearInvocations(userDAO);
    objectDAO = locator.getService(ObjectDAO.class);
    Mockito.clearInvocations(objectDAO);
    Mockito.reset(objectDAO);
    dalService = locator.getService(DalServices.class);
    Mockito.clearInvocations(dalService);

  }

  @Test
  void getOneWithExistentIdShouldReturnUserDto() {
    user1 = Mockito.mock(User.class);
    Mockito.when(user1.getUsername()).thenReturn("test");
    Mockito.when(user1.getPassword()).thenReturn("test");
    Mockito.when(user1.getId()).thenReturn(1);
    Mockito.when(user1.checkPassword("test")).thenReturn(true);

    Mockito.when(userDAO.getUserWithId(1)).thenReturn(user1);
    assertAll(
        () -> assertEquals(user1.getId(), userUCC.getOne(1).getId()),
        () -> assertEquals(user1.getUsername(), userUCC.getOne(1).getUsername()),
        () -> assertEquals(user1.getPassword(), userUCC.getOne(1).getPassword())
    );
  }

  @Test
  void getOneWithNonexistentIdShouldReturnException() {
    Exception exception = Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.getOne(-1);
    });
    String expectedMessage = "get user with his id failed";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test()
  void loginWrongUsernameAndWrongPasswordShouldReturnException() {
    user1 = Mockito.mock(User.class);
    Mockito.when(user1.getUsername()).thenReturn(null);
    Mockito.when(userDAO.getUser("df")).thenReturn(user1);
    Exception exception = Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.login("df", "df");
    });
    String expectedMessage = "login failed wrong password or problem with DAL";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void loginFalseUsernameAndGoodPasswordShouldReturnException() {
    Exception exception = Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.login("df", "test");
    });
    String expectedMessage = "login failed wrong password or problem with DAL";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void loginGoodUsernameAndWrongPasswordShouldReturnException() {
    user1 = Mockito.mock(User.class);
    Mockito.when(user1.getUsername()).thenReturn("test");
    Mockito.when(user1.getPassword()).thenReturn("test");
    Mockito.when(user1.getId()).thenReturn(1);
    Mockito.when(user1.checkPassword("df")).thenReturn(false);
    Exception exception = Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.login("test", "df");
    });
    String expectedMessage = "login failed wrong password or problem with DAL";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }


  @Test
  void loginGoodUsernameAndPasswordShouldReturnUserDTO() {
    user1 = Mockito.mock(User.class);
    Mockito.when(user1.getUsername()).thenReturn("test");
    Mockito.when(user1.getPassword()).thenReturn("test");
    Mockito.when(user1.getId()).thenReturn(1);
    Mockito.when(user1.checkPassword("test")).thenReturn(true);
    Mockito.when(user1.getCondition()).thenReturn("valid");

    Mockito.when(userDAO.getUser("test")).thenReturn(user1);
    assertAll(
        () -> assertEquals(user1.getId(), userUCC.login("test", "test").getId()),
        () -> assertEquals(user1.getUsername(), userUCC.login("test",
            "test").getUsername()),
        () -> assertEquals(user1.getPassword(), userUCC.login("test",
            "test").getPassword())
    );
  }

  @Test
  void loginWithConditionDisabledShouldReturnUserDTO() {
    user1 = Mockito.mock(User.class);
    Mockito.when(user1.getUsername()).thenReturn("test");
    Mockito.when(user1.getPassword()).thenReturn("test");
    Mockito.when(user1.getId()).thenReturn(1);
    Mockito.when(user1.checkPassword("test")).thenReturn(true);
    Mockito.when(user1.getCondition()).thenReturn("disabled");
    Mockito.when(userDAO.confirmRegister(user1.getUsername())).thenReturn(true);

    Mockito.when(userDAO.getUser("test")).thenReturn(user1);
    assertAll(
        () -> assertEquals(user1.getId(), userUCC.login("test", "test").getId()),
        () -> assertEquals(user1.getUsername(), userUCC.login("test",
            "test").getUsername()),
        () -> assertEquals(user1.getPassword(), userUCC.login("test",
            "test").getPassword())
    );
  }

  @Test
  void registerConfirmShouldReturnTrue() {
    Mockito.when(userDAO.getVersionMember("test3", 0)).thenReturn(1);
    Mockito.when(userDAO.confirmRegister("test3")).thenReturn(true);
    assertTrue(userUCC.registerConfirm("test3", 1));
  }

  @Test
  void registerConfirmShouldReturnFalse() {
    Mockito.when(userDAO.getVersionMember("test3", 0)).thenReturn(1);
    Mockito.when(userDAO.confirmRegister("test3")).thenReturn(false);
    assertFalse(userUCC.registerConfirm("test3", 1));
  }

  @Test
  void registerConfirmShouldReturnException() {
    Mockito.when(userDAO.getVersionMember("test3", 0)).thenReturn(2);
    Exception exception = Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.registerConfirm("test3", 1);
    });
    String expectedMessage = "version actualisée";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void becomeAdminShouldReturnTrue() {
    Mockito.when(userDAO.becomeAdmin("test3")).thenReturn(true);
    assertTrue(userUCC.becomeAdmin("test3"));
  }

  @Test
  void becomeAdminShouldReturnException() {
    Mockito.when(userDAO.becomeAdmin("test5")).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.becomeAdmin("test5");
    });
  }

  @Test
  void refuseRegisterShouldReturnTrue() {
    Mockito.when(userDAO.getVersionMember("test3", 0)).thenReturn(1);
    Mockito.when(userDAO.refuseRegister("test3", "pas bien")).thenReturn(true);
    assertTrue(userUCC.refuseRegister("test3", "pas bien", 1));
  }

  @Test
  void refuseRegisterShouldReturnFalse() {
    Mockito.when(userDAO.getVersionMember("test3", 0)).thenReturn(1);
    Mockito.when(userDAO.refuseRegister("test3", "pas bien")).thenReturn(false);
    assertFalse(userUCC.refuseRegister("test3", "pas bien", 1));
  }

  @Test
  void refuseRegisterShouldReturnException() {
    Mockito.when(userDAO.getVersionMember("test5", 0)).thenReturn(2);
    Exception exception = Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.refuseRegister("test5", "pas bien", 1);
    });
    String expectedMessage = "version actualisée";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void registerShouldReturnTrue() {
    user1 = Mockito.mock(User.class);
    Mockito.when(userDAO.createAddres("street", 1, "1",
        5030, "commune")).thenReturn(true);
    Mockito.when(userDAO.findAdress("street", 1, "1",
        5030, "commune")).thenReturn(1);
    Mockito.when(user1.getUsername()).thenReturn("username");
    Mockito.when(user1.getPassword()).thenReturn("password");
    Mockito.when(user1.getFirstName()).thenReturn("firstname");
    Mockito.when(user1.getLastName()).thenReturn("lastname");
    Mockito.when(userDAO.createUser(user1, 1)).thenReturn(true);
    assertTrue(userUCC.register("username", "password",
        "firstname", "lastname", "0101",
        "street", 1, "1", 5030, "commune"));
  }

  @Test
  void registerShouldReturnException() {
    user2 = Mockito.mock(User.class);
    Mockito.when(userDAO.createAddres("street2", 12, "12",
        50302, "commune2")).thenThrow(WebApplicationException.class);
    Mockito.when(userDAO.findAdress("street2", 12, "12",
        50302, "commune2")).thenReturn(2);
    Mockito.when(user2.getUsername()).thenReturn("username2");
    Mockito.when(user2.getPassword()).thenReturn("password2");
    Mockito.when(user2.getFirstName()).thenReturn("firstname2");
    Mockito.when(user2.getLastName()).thenReturn("lastname2");
    Mockito.when(userDAO.createUser(user2, 2)).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.register("username2", "password2",
          "firstname2", "lastname2", "01012",
          "street2", 12, "12", 50302, "commune2");
    });
  }

  @Test
  void checkUsernameAlreadyExistShouldReturnFalse() {
    Mockito.when(userDAO.usernameExist("existepas")).thenReturn(false);
    assertFalse(userUCC.checkUsernameAlreadyExist("existepas"));
  }

  @Test
  void checkUsernameAlreadyExistShouldReturnTrue() {
    Mockito.when(userDAO.usernameExist("existe")).thenReturn(true);
    assertTrue(userUCC.checkUsernameAlreadyExist("existe"));
  }

  @Test
  void checkUsernameAlreadyExistShouldReturnException() {
    Mockito.when(userDAO.usernameExist("error")).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.checkUsernameAlreadyExist("error");
    });
  }

  @Test
  void checkUserIsAdminShouldReturnTrue() {
    Mockito.when(userDAO.getIfUserIsAdmin(2)).thenReturn(true);
    assertTrue(userUCC.checkUserIsAdmin(2));
  }

  @Test
  void checkUserIsAdminShouldReturnFalse() {
    Mockito.when(userDAO.getIfUserIsAdmin(3)).thenReturn(false);
    assertFalse(userUCC.checkUserIsAdmin(3));
  }

  @Test
  void checkUserIsAdminShouldReturnException() {
    Mockito.when(userDAO.getIfUserIsAdmin(4)).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.checkUserIsAdmin(4);
    });
  }

  @Test
  void listMemberWithStatusShouldReturnListOfUserDTO() {
    user2 = Mockito.mock(User.class);
    Mockito.when(user2.getUsername()).thenReturn("username2");
    Mockito.when(user2.getPassword()).thenReturn("password2");
    Mockito.when(user2.getFirstName()).thenReturn("firstname2");
    Mockito.when(user2.getLastName()).thenReturn("lastname2");
    user1 = Mockito.mock(User.class);
    Mockito.when(user1.getUsername()).thenReturn("username");
    Mockito.when(user1.getPassword()).thenReturn("password");
    Mockito.when(user1.getFirstName()).thenReturn("firstname");
    Mockito.when(user1.getLastName()).thenReturn("lastname");
    List<UserDTO> userDTOList = new ArrayList<>();
    userDTOList.add(user1);
    userDTOList.add(user2);
    Mockito.when(userDAO.getListWithStatus("waiting")).thenReturn(userDTOList);
    assertEquals(userUCC.listMemberWithStatus("waiting"), userDTOList);
  }

  @Test
  void listMemberWithStatusShouldReturnException() {
    Mockito.when(userDAO.getListWithStatus("denied")).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.listMemberWithStatus("denied");
    });
  }

  @Test
  void getAddressDTOShouldReturnAnAddress() {
    Mockito.when(userDAO.getAddress(1)).thenReturn(address);
    assertEquals(address, userUCC.getAddressDTO(1));
  }

  @Test
  void getAddressDTOShouldReturnException() {
    Mockito.when(userDAO.getAddress(2)).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.getAddressDTO(2);
    });
  }

  @Test
  void getListMemberInterestedShouldReturnListOfInterestDto() {
    interestDTO1 = Mockito.mock(InterestDTO.class);
    Mockito.when(interestDTO1.getTimeSlots()).thenReturn("Plage horaire");
    interestDTO2 = Mockito.mock(InterestDTO.class);
    Mockito.when(interestDTO1.getTimeSlots()).thenReturn("Plage horaire 2");
    List<InterestDTO> interestDTOList = new ArrayList<>();
    interestDTOList.add(interestDTO1);
    interestDTOList.add(interestDTO2);
    Mockito.when(userDAO.getIdMemberInterested(1)).thenReturn(interestDTOList);
    Mockito.when(userDAO.getMemberInterestedCompleteInfo(interestDTO1)).thenReturn(interestDTO1);
    Mockito.when(userDAO.getMemberInterestedCompleteInfo(interestDTO2)).thenReturn(interestDTO2);
    assertEquals(interestDTOList, userUCC.getListMemberInterested(1));
  }

  @Test
  void getListMemberInterestedShouldReturnException() {
    Mockito.when(userDAO.getIdMemberInterested(2)).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.getListMemberInterested(2);
    });
  }

  @Test
  void getUserProfileShouldReturnUserDTO() {
    user1 = Mockito.mock(User.class);
    Mockito.when(user1.getUsername()).thenReturn("username");
    Mockito.when(user1.getPassword()).thenReturn("password");
    Mockito.when(user1.getFirstName()).thenReturn("firstname");
    Mockito.when(user1.getLastName()).thenReturn("lastname");
    Mockito.when(userDAO.getUser("user")).thenReturn(user1);
    assertEquals(user1, userUCC.getUserProfile("user"));
  }

  @Test
  void getUserProfileShouldReturnException() {
    Mockito.when(userDAO.getUser("user2")).thenThrow(WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.getUserProfile("user2");
    });
  }

  @Test
  void editAMemberWithSameUsernameShouldReturnTrue() {
    user1 = Mockito.mock(User.class);
    Mockito.when(user1.getUsername()).thenReturn("username");
    Mockito.when(user1.getPassword()).thenReturn("password");
    Mockito.when(user1.getFirstName()).thenReturn("firstname");
    Mockito.when(user1.getLastName()).thenReturn("lastname");
    Mockito.when(userDAO.getUserWithId(1)).thenReturn(user1);
    Mockito.when(userDAO.editAProfile(1, "username",
            "newlastname", "newfirstname", "new123"))
        .thenReturn(true);
    Mockito.when(userDAO.editAAdress(1, "newstreet", 2, 2,
            "newunit", "newcommune"))
        .thenReturn(true);
    assertTrue(userUCC.editAMember(1, "username", "newlastname",
        "newfirstname",
        "new123", 1, "newstreet", 2,
        2, "newunit", "newcommune"));
  }

  @Test
  void editAMemberWithNewUsernameThatDoesntExistShouldReturnTrue() {
    user1 = Mockito.mock(User.class);
    Mockito.when(user1.getUsername()).thenReturn("username");
    Mockito.when(user1.getPassword()).thenReturn("password");
    Mockito.when(user1.getFirstName()).thenReturn("firstname");
    Mockito.when(user1.getLastName()).thenReturn("lastname");
    Mockito.when(userDAO.getUserWithId(1)).thenReturn(user1);
    Mockito.when(userDAO.usernameExist("newusername")).thenReturn(false);
    Mockito.when(userDAO.editAProfile(1, "newusername",
            "newlastname", "newfirstname", "new123"))
        .thenReturn(true);
    Mockito.when(userDAO.editAAdress(1, "newstreet",
            2, 2, "newunit", "newcommune"))
        .thenReturn(true);
    assertTrue(userUCC.editAMember(1, "newusername",
        "newlastname", "newfirstname",
        "new123", 1, "newstreet",
        2, 2, "newunit", "newcommune"));
  }

  @Test
  void editAMemberWithNewUsernameThatExistShouldReturnException() {
    user1 = Mockito.mock(User.class);
    Mockito.when(user1.getUsername()).thenReturn("username");
    Mockito.when(user1.getPassword()).thenReturn("password");
    Mockito.when(user1.getFirstName()).thenReturn("firstname");
    Mockito.when(user1.getLastName()).thenReturn("lastname");
    Mockito.when(userDAO.getUserWithId(1)).thenReturn(user1);
    Mockito.when(userDAO.usernameExist("newusernameexistant")).thenReturn(true);
    Exception exception = Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.editAMember(1, "newusernameexistant",
          "newlastname", "newfirstname",
          "new123", 1, "newstreet", 2,
          2, "newunit", "newcommune");
    });
    String expectedMessage = "Login already exist";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void changePwdShouldReturnTrue() {
    user1 = Mockito.mock(User.class);
    factory = Mockito.mock(Factory.class);
    Mockito.when(factory.getUserDTO()).thenReturn(user1);
    Mockito.when(user1.cryptPassword("pwd")).thenReturn("cryptedpwd");
    Mockito.when(userDAO.changePwd(1, "cryptedpwd")).thenReturn(true);
    assertFalse(userUCC.changePwd(1, "pwd"));
  }

  @Test
  void getSearchListWithStatusShouldReturnListUserDTO() {
    user2 = Mockito.mock(User.class);
    Mockito.when(user2.getUsername()).thenReturn("username2");
    Mockito.when(user2.getPassword()).thenReturn("password2");
    Mockito.when(user2.getFirstName()).thenReturn("firstname2");
    Mockito.when(user2.getLastName()).thenReturn("lastname2");
    user1 = Mockito.mock(User.class);
    Mockito.when(user1.getUsername()).thenReturn("username");
    Mockito.when(user1.getPassword()).thenReturn("password");
    Mockito.when(user1.getFirstName()).thenReturn("firstname");
    Mockito.when(user1.getLastName()).thenReturn("lastname");
    List<UserDTO> userDTOList = new ArrayList<>();
    userDTOList.add(user1);
    userDTOList.add(user2);
    Mockito.when(userDAO.getSearchListWithStatus("user")).thenReturn(userDTOList);
    assertEquals(userDTOList, userUCC.getSearchListWithStatus("user"));
  }

  @Test
  void getSearchListWithStatusShouldReturnException() {
    Mockito.when(userDAO.getSearchListWithStatus("uazezae")).thenThrow(
        WebApplicationException.class);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.getSearchListWithStatus("uazezae");
    });

  }

  @Test
  void changeStatusToDisableShouldReturnTrue() {
    object1 = Mockito.mock(ObjectDTO.class);
    object2 = Mockito.mock(ObjectDTO.class);
    ObjectDTO object3;
    object3 = Mockito.mock(ObjectDTO.class);
    List<ObjectDTO> objectMemberReceveid = new ArrayList<>();
    List<ObjectDTO> objectMemberD = new ArrayList<>();
    objectMemberD.add(object1);
    objectMemberD.add(object2);
    objectMemberReceveid.add(object3);
    Mockito.when(userDAO.getVersionMember(null, 2)).thenReturn(1);
    Mockito.when(userDAO.changeStatusToDisable(2)).thenReturn(true);
    Mockito.when(objectDAO.getMyObjects(2)).thenReturn(objectMemberD);
    Mockito.when(objectDAO.getMyObjectsRecipient(2, false)).thenReturn(objectMemberReceveid);
    Mockito.when(objectDAO.deleteAllInterets(2)).thenReturn(true);
    assertTrue(userUCC.changeStatusToDisable(2, 1));
  }

  @Test
  void changeStatusToDisableShouldReturnException() {
    object1 = Mockito.mock(ObjectDTO.class);
    object2 = Mockito.mock(ObjectDTO.class);
    ObjectDTO object3;
    object3 = Mockito.mock(ObjectDTO.class);
    List<ObjectDTO> objectMemberReceveid = new ArrayList<>();
    List<ObjectDTO> objectMemberD = new ArrayList<>();
    objectMemberD.add(object1);
    objectMemberD.add(object2);
    objectMemberReceveid.add(object3);
    Mockito.when(userDAO.getVersionMember(null, 2)).thenReturn(3);
    Mockito.when(userDAO.changeStatusToDisable(2)).thenReturn(true);
    Mockito.when(objectDAO.getMyObjects(2)).thenReturn(objectMemberD);
    Mockito.when(objectDAO.getMyObjectsRecipient(2, false)).thenReturn(objectMemberReceveid);
    Mockito.when(objectDAO.deleteAllInterets(2)).thenReturn(true);
    Assertions.assertThrows(WebApplicationException.class, () -> {
      userUCC.changeStatusToDisable(2, 1);
    });
  }
}
