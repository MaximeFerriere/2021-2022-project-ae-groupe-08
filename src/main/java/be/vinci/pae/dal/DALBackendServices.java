package be.vinci.pae.dal;

import java.sql.PreparedStatement;

public interface DALBackendServices {

  /**
   * get the necessary data in the db with the query.
   *
   * @param query String
   * @return The preparedStatement of the query
   */
  PreparedStatement getPS(String query);
}
