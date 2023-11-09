package be.vinci.pae;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.FactoryImpl;
import be.vinci.pae.biz.object.ObjectUseCaseController;
import be.vinci.pae.biz.object.ObjectUseCaseControllerImpl;
import be.vinci.pae.biz.user.UserCaseController;
import be.vinci.pae.biz.user.UserCaseControllerImpl;
import be.vinci.pae.dal.DALServicesImpl;
import be.vinci.pae.dal.DalServices;
import be.vinci.pae.dal.objectdao.ObjectDAO;
import be.vinci.pae.dal.objectdao.ObjectDAOImpl;
import be.vinci.pae.dal.userdao.UserDAO;
import be.vinci.pae.dal.userdao.UserDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(Mockito.mock(UserDAOImpl.class)).to(UserDAO.class);
    bind(UserCaseControllerImpl.class).to(UserCaseController.class).in(Singleton.class);
    bind(FactoryImpl.class).to(Factory.class).in(Singleton.class);
    bind(Mockito.mock(DALServicesImpl.class)).to(DalServices.class);
    //object
    bind(Mockito.mock(ObjectDAOImpl.class)).to(ObjectDAO.class);
    bind(ObjectUseCaseControllerImpl.class).to(ObjectUseCaseController.class).in(Singleton.class);
  }
}
