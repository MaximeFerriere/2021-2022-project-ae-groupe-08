package be.vinci.pae.biz;

import be.vinci.pae.biz.object.ObjectDTO;
import be.vinci.pae.biz.object.ObjectImpl;
import be.vinci.pae.biz.searchobject.SearchObjectDTO;
import be.vinci.pae.biz.searchobject.SearchObjectImpl;
import be.vinci.pae.biz.user.AddressDTO;
import be.vinci.pae.biz.user.AddressImpl;
import be.vinci.pae.biz.user.InterestDTO;
import be.vinci.pae.biz.user.InterestImpl;
import be.vinci.pae.biz.user.UserDTO;
import be.vinci.pae.biz.user.UserImpl;

public class FactoryImpl implements Factory {

  @Override
  public UserDTO getUserDTO() {
    return new UserImpl();
  }

  @Override
  public ObjectDTO getObjetDTO() {
    return new ObjectImpl();
  }

  @Override
  public AddressDTO getAddressDTO() {
    return new AddressImpl();
  }

  @Override
  public InterestDTO getInterestDTO() {
    return new InterestImpl();
  }

  @Override
  public SearchObjectDTO getSearchObjectDTO() {
    return new SearchObjectImpl();
  }
}
