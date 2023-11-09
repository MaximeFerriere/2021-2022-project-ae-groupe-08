package be.vinci.pae.dal.userdao;

import be.vinci.pae.biz.user.AddressDTO;
import be.vinci.pae.biz.user.InterestDTO;
import be.vinci.pae.biz.user.UserDTO;
import java.util.List;

public interface UserDAO {

  //--------------------------USER GET AND CREATE-----------------------------------------

  /**
   * return a Member with all the attributes of a member int the db.
   *
   * @param idUser int
   * @return all the User information
   */
  UserDTO getUserWithId(int idUser);

  /**
   * return a Member with all the attributes of a member in the DB.
   *
   * @param username String
   * @return all the User information
   */
  UserDTO getUser(String username);

  /**
   * create a new member with the user in parameter.
   *
   * @param user UserDTO
   * @return a boolean if the create succes or not
   */
  boolean createUser(UserDTO user, int idAdress);

  /**
   * add a phone number to a member.
   *
   * @param idMember    int
   * @param phoneNumber String
   * @return a boolean if adding number succeeded or not
   */
  boolean addPhoneNumber(int idMember, String phoneNumber);

  /**
   * change the password to a member.
   *
   * @param idMember int
   * @param pwd      String
   * @return a boolean if changing password succeeded or not
   */
  boolean changePwd(int idMember, String pwd);

  //--------------------------------CHECK USER EXIST OR ADMIN-----------------------------

  /**
   * check if a member is present in the db with his username.
   *
   * @param username String
   * @return a boolean if the user is present or not
   */
  boolean usernameExist(String username);

  /**
   * check if the member is a admin.
   *
   * @param idMember int
   * @return true if the user is a admin
   */
  boolean getIfUserIsAdmin(int idMember);

  //-------------------------------MEMBER METHODE -----------------------------------------

  /**
   * get all people interested in my offer.
   *
   * @param idObject int
   * @return list of InterestDTO interested in my offer
   */
  List<InterestDTO> getIdMemberInterested(int idObject);


  /**
   * get more info about person interested in my offer.
   *
   * @param interest InterestDTO
   * @return InterestDTO with more complete info
   */
  InterestDTO getMemberInterestedCompleteInfo(InterestDTO interest);

  /**
   * increment the number of offer not picked up.
   *
   * @param idMember int
   * @return InterestDTO with more complete info
   */
  boolean incrementNumberOfferNotPickedUp(int idMember);

  //-------------------------------ADMIN METHODE INSCRIPTION-------------------------------

  /**
   * change the condition of the member to valid.
   *
   * @param username String
   * @return true if the user condition is correctely change
   */
  boolean confirmRegister(String username);

  /**
   * set the admin value of the member to true.
   *
   * @param username String
   * @return true if the member is become a admin
   */
  boolean becomeAdmin(String username);

  /**
   * change the condition of the member to refused. set the reason of refusal text.
   *
   * @param username String
   * @param justifie String
   * @return true if the member is refused
   */
  boolean refuseRegister(String username, String justifie);

  //------------------------------ADDRESS METHODE---------------------------------------------

  /**
   * create a new addres in the db with the parameters.
   *
   * @param street         String
   * @param buildingNumber int
   * @param unitNumber     String
   * @param postcode       int
   * @param commune        String
   * @return true if the address is create
   */
  boolean createAddres(String street, int buildingNumber, String unitNumber, int postcode,
      String commune);

  /**
   * get the id of a address with these parameters.
   *
   * @param street         String
   * @param buildingNumber int
   * @param unitNumber     String
   * @param postcode       int
   * @param commune        String
   * @return the id of the address
   */
  int findAdress(String street, int buildingNumber, String unitNumber, int postcode,
      String commune);

  /**
   * create a address with all the attributes of a address in the DB.
   *
   * @param idAddress int
   * @return all the address information
   */
  AddressDTO getAddress(int idAddress);

  //------------------------------LIST WITH STATUS--------------------------------------------

  /**
   * create a list with all member with the good status and add the address.
   *
   * @param status String
   * @return a hasmap with a member in key and his address in cintent
   */
  List<UserDTO> getListWithStatus(String status);

  boolean editAProfile(int id, String username, String lastName, String firstname,
      String callNumber);

  boolean editAAdress(int id, String street, int building, int postcode,
      String unit, String commune);

  int getVersionMember(String username,int idMember);

  int getVersionAddress(int idAddress);

  List<UserDTO> getSearchListWithStatus(String search);

  Boolean changeStatusToDisable(int idUser);

}
