package be.vinci.pae.biz.user;


import java.util.List;

public interface UserCaseController {

  UserDTO getOne(int idUser);

  UserDTO login(String username, String password);

  boolean registerConfirm(String username, int versionMember);

  boolean becomeAdmin(String username);

  boolean refuseRegister(String username, String justifie, int versionMember);


  boolean register(String username, String password, String firstname, String lastname,
      String callN,
      String street, int buildingNumber, String unitNumber, int postcode, String commune);

  boolean checkUsernameAlreadyExist(String username);

  boolean checkUserIsAdmin(int idMember);

  List<UserDTO> listMemberWithStatus(String typeList);

  AddressDTO getAddressDTO(int idAddress);

  List<InterestDTO> getListMemberInterested(int idObject);

  UserDTO getUserProfile(String username);

  boolean editAMember(int idMember, String username, String lastName, String firstname,
      String callNumber, int idAdress, String street, int building, int postcode,
      String unit, String commune);

  boolean changePwd(int idMember, String pwd);

  List<UserDTO> getSearchListWithStatus(String search);

  Boolean changeStatusToDisable(int idUser, int versionMemberToCheck);
}
