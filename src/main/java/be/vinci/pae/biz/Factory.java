package be.vinci.pae.biz;

import be.vinci.pae.biz.object.ObjectDTO;
import be.vinci.pae.biz.searchobject.SearchObjectDTO;
import be.vinci.pae.biz.user.AddressDTO;
import be.vinci.pae.biz.user.InterestDTO;
import be.vinci.pae.biz.user.UserDTO;

public interface Factory {

  UserDTO getUserDTO();

  ObjectDTO getObjetDTO();

  AddressDTO getAddressDTO();

  InterestDTO getInterestDTO();

  SearchObjectDTO getSearchObjectDTO();
}
