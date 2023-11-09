package be.vinci.pae.utils;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.FactoryImpl;
import be.vinci.pae.biz.object.ObjectUseCaseController;
import be.vinci.pae.biz.object.ObjectUseCaseControllerImpl;
import be.vinci.pae.biz.user.UserCaseController;
import be.vinci.pae.biz.user.UserCaseControllerImpl;
import be.vinci.pae.dal.DALBackendServices;
import be.vinci.pae.dal.DALServicesImpl;
import be.vinci.pae.dal.DalServices;
import be.vinci.pae.dal.objectdao.ObjectDAO;
import be.vinci.pae.dal.objectdao.ObjectDAOImpl;
import be.vinci.pae.dal.userdao.UserDAO;
import be.vinci.pae.dal.userdao.UserDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    //Factory Injection
    bind(FactoryImpl.class).to(Factory.class).in(Singleton.class);
    //DALService Injection
    bind(DALServicesImpl.class).to(DALBackendServices.class).to(DalServices.class)
        .in(Singleton.class);
    //User Injection
    bind(UserCaseControllerImpl.class).to(UserCaseController.class).in(Singleton.class);
    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);
    //object Injection
    bind(ObjectDAOImpl.class).to(ObjectDAO.class).in(Singleton.class);
    bind(ObjectUseCaseControllerImpl.class).to(ObjectUseCaseController.class).in(Singleton.class);
  }
}