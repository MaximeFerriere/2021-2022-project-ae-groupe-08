package be.vinci.pae.dal;


import be.vinci.pae.utils.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

public class DALServicesImpl implements DALBackendServices, DalServices {

  private static BasicDataSource ds = new BasicDataSource();

  static {
    ds.setUrl(Config.getProperty("URLDB"));
    ds.setUsername(Config.getProperty("USERNAMEDB"));
    ds.setPassword(Config.getProperty("PASSWORDDB"));
    ds.setMaxTotal(1);
  }

  private ThreadLocal<Connection> tl = new ThreadLocal<>();

  /**
   * constructor. allows connection to the database
   */
  public DALServicesImpl() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Driver PostgreSQL manquant !");
      System.exit(1);
    }
  }


  @Override
  public PreparedStatement getPS(String query) {
    PreparedStatement result = null;
    Connection con = tl.get();
    try {
      result = con.prepareStatement(query);
    } catch (SQLException e) {
      System.out.println("Erreur avec les requêtes SQL !");
      System.exit(1);
    }
    return result;
  }

  @Override
  public void start() {
    try {
      Connection co = ds.getConnection();
      tl.set(co);
      co.setAutoCommit(false);
    } catch (SQLException e) {
      System.out.println("Erreur avec les requêtes SQL !"); //fuite de connexion a corriger
      close();
      System.exit(1);// a changer le rappele plus se qu'il a dit
    }
  }

  @Override
  public void commit() {
    Connection co = tl.get();
    try {
      co.commit();
    } catch (SQLException e) {
      System.out.println("Erreur avec les requêtes SQL !");
      System.exit(1);// a changer le rappele plus se qu'il a dit
    } finally {
      close();
    }
  }

  @Override
  public void rollback() {
    Connection co = tl.get();
    try {
      co.rollback();
    } catch (SQLException e) {
      System.out.println("Erreur avec les requêtes SQL !");
      System.exit(1); // a changer le rappele plus se qu'il a dit
    } finally {
      close();
    }
  }

  /**
   * close the connection.
   */
  public void close() {
    try {
      Connection co = tl.get();
      co.close();
      tl.remove();
    } catch (SQLException se) {
      se.printStackTrace();
    }
  }
}
