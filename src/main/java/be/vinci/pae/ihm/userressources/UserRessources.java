package be.vinci.pae.ihm.userressources;


import be.vinci.pae.biz.user.InterestDTO;
import be.vinci.pae.biz.user.User;
import be.vinci.pae.biz.user.UserCaseController;
import be.vinci.pae.biz.user.UserDTO;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.ihm.filters.AuthorizeAdmin;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.MyLogger;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.glassfish.jersey.server.ContainerRequest;


@Singleton
@Path("/users")
public class UserRessources {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private UserCaseController ucc;

  /**
   * check the size of the parameters and send a request to the UCC.
   *
   * @param json JsonNode
   * @return the objectNode of the user token, id, username, and texte refusal
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode login(JsonNode json) {
    // Get and check credentials
    if (!json.hasNonNull("username") || !json.hasNonNull("password")) {
      throw new WebApplicationException("userName or password required",
          Response.Status.BAD_REQUEST);
    }
    String username = json.get("username").asText();
    String password = json.get("password").asText();
    if (username.length() > 240) {
      throw new WebApplicationException("userName is too long",
          Response.Status.BAD_REQUEST);
    }
    if (password.length() > 240) {
      throw new WebApplicationException("password is too long",
          Response.Status.BAD_REQUEST);
    }
    // Try to login
    UserDTO myUser = ucc.login(username, password);
    if (myUser == null) {
      throw new WebApplicationException("Login or password incorrect",
          Response.Status.UNAUTHORIZED);
    } else {
      String token;
      Calendar c = Calendar.getInstance();
      c.add(Calendar.MINUTE, 1440);
      Date expirationDate = c.getTime();
      try {
        if (!myUser.getCondition().equals("valid") && !myUser.getCondition().equals("disabled")) {

          ObjectNode publicUser = jsonMapper.createObjectNode()
              .put("textRefusal", myUser.getReasonForConnectionRefusal());
          return publicUser;
        }

        token = JWT.create().withIssuer("auth0").withExpiresAt(expirationDate)
            .withClaim("user", myUser.getId()).sign(this.jwtAlgorithm);

        ObjectNode publicUser = jsonMapper.createObjectNode()
            .put("token", token)
            .put("id", myUser.getId())
            .put("login", myUser.getUsername())
            .put("callNumber", myUser.getCallNumber())
            .put("textRefusal", (String) null);
        MyLogger.addLogger("Login", Level.INFO);
        return publicUser;

      } catch (Exception e) {
        throw new WebApplicationException(e.getMessage(),
            Response.Status.INTERNAL_SERVER_ERROR);
      }
    }
  }


  /**
   * check the size of the parameters and send a request to the UCC.
   *
   * @param json JsonNode
   * @return the objectNode of the user token, id, username, and texte refusal
   */
  @POST
  @Path("accept")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public boolean confirmRegister(JsonNode json) {
    // Get and check credentials
    if (!json.hasNonNull("username") || !json.hasNonNull("versionMember")) {
      throw new WebApplicationException("userName and version required",
          Response.Status.BAD_REQUEST);
    }
    String username = json.get("username").asText();
    int versionMember = json.get("versionMember").asInt();

    // Try to login
    MyLogger.addLogger("registration confirmation", Level.INFO);
    return ucc.registerConfirm(username, versionMember);

  }

  /**
   * check the size of the parameters and send a request to the UCC.
   *
   * @param json JsonNode
   * @return the objectNode of the user token, id, username, and texte refusal
   */
  @POST
  @Path("AddAnAdmin")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public boolean becomeAdmin(JsonNode json) {
    if (!json.hasNonNull("username")) {
      throw new WebApplicationException("userName required", Response.Status.BAD_REQUEST);
    }
    // Get and check credentials
    String username = json.get("username").asText();

    MyLogger.addLogger("Make someone become admin", Level.INFO);
    // Try to become Admin
    return ucc.becomeAdmin(username);

  }


  /**
   * check the size of the parameters and send a request to the UCC.
   *
   * @param json JsonNode
   * @return the objectNode of the user token, id, username, and texte refusal
   */
  @POST
  @Path("refuse")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public boolean refuseRegister(JsonNode json) {
    // Get and check credentials
    if (!json.hasNonNull("username") || !json.hasNonNull("versionMember")) {
      throw new WebApplicationException("userName or version required",
          Response.Status.BAD_REQUEST);
    }
    String username = json.get("username").asText();
    String justifie = json.get("reason_for_connection_refusal").asText();
    int versionMember = json.get("versionMember").asInt();

    MyLogger.addLogger("registration refused", Level.INFO);
    // Try to become Admin
    return ucc.refuseRegister(username, justifie, versionMember);

  }


  /**
   * check the size of the parameters and send a request to the UCC.
   *
   * @param json JsonNode
   * @return boolean true if the user is registered, false otherwise
   */
  @PUT
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public boolean register(JsonNode json) {
    // Get and check credentials
    if (!json.hasNonNull("username") || !json.hasNonNull("password") || !json.hasNonNull(
        "firstname") || !json.hasNonNull("lastname") || !json.hasNonNull("street")
        || !json.hasNonNull("building_number") || !json.hasNonNull("unit_number")
        || !json.hasNonNull("postcode") || !json.hasNonNull("commune") || !json.hasNonNull(
        "callNumber")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("fields are missing").type("text/plain").build());
    }
    String callN = json.get("callNumber").asText();
    if (callN.length() > 15) {
      throw new WebApplicationException("username is too long",
          Response.Status.BAD_REQUEST);
    }

    String username = json.get("username").asText();
    if (username.length() > 240) {
      throw new WebApplicationException("username is too long",
          Response.Status.BAD_REQUEST);
    }
    String password = json.get("password").asText();
    if (password.length() > 240) {
      throw new WebApplicationException("password is too long",
          Response.Status.BAD_REQUEST);
    }
    String firstname = json.get("firstname").asText();
    if (firstname.length() > 50) {
      throw new WebApplicationException("firstname is too long",
          Response.Status.BAD_REQUEST);
    }
    String lastname = json.get("lastname").asText();
    if (lastname.length() > 50) {
      throw new WebApplicationException("lastname is too long",
          Response.Status.BAD_REQUEST);
    }
    String street = json.get("street").asText();
    if (street.length() > 240) {
      throw new WebApplicationException("street is too long",
          Response.Status.BAD_REQUEST);
    }
    String commune = json.get("commune").asText();
    if (commune.length() > 240) {
      throw new WebApplicationException("street is too long",
          Response.Status.BAD_REQUEST);
    }
    int buildingNumber = json.get("building_number").asInt();
    String unitNumber = json.get("unit_number").asText();
    if (unitNumber.length() > 50) {
      throw new WebApplicationException("unit number is too long",
          Response.Status.BAD_REQUEST);
    }
    int postcode = json.get("postcode").asInt();
    boolean myUser;
    MyLogger.addLogger("registration", Level.INFO);
    myUser = ucc.register(username, password, firstname, lastname, callN,
        street, buildingNumber, unitNumber, postcode, commune);
    return myUser;
  }

  /**
   * get all user with condtion waiting.
   *
   * @return List of userDTO
   */
  @GET
  @Path("waitingList")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public List<UserDTO> listMemberWithStatus(@DefaultValue("-1") @QueryParam("type-list")
      String typeList) {
    //check autorize
    MyLogger.addLogger("display the list of pending registrations", Level.INFO);
    return ucc.listMemberWithStatus(typeList);
  }

  //on l'utilise dans quoi ?
  /*
  /**
   * get a address with the id in the parameter of the requeast.
   *
   * @return AddressDTO

  @POST
  @Path("address")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public AddressDTO getAddress(@DefaultValue("-1") @QueryParam("idAddress")
      int idAddress) {
    if (idAddress==-1) {
      throw new WebApplicationException("id address required",
          Response.Status.BAD_REQUEST);
    }
    MyLogger.addLogger("get Address", Level.INFO);
    return ucc.getAddressDTO(idAddress);

  }*/

  /**
   * check the size of the parameters and send a request to the UCC.
   *
   * @param json JsonNode
   * @return the objectNode of the user token, id, username, and texte refusal
   */

  @POST
  @Path("isAdmin")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public boolean checkUserIsAdmin(JsonNode json) {
    // Get and check credentials
    if (!json.hasNonNull("token")) {
      throw new WebApplicationException("token required",
          Response.Status.BAD_REQUEST);
    }
    JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0").build();
    String token = json.get("token").asText();
    DecodedJWT decodedToken = null;
    try {
      decodedToken = jwtVerifier.verify(token);
    } catch (Exception e) {
      throw new WebApplicationException("token unavailable",
          Response.Status.UNAUTHORIZED);
    }
    //check if the user is admin
    MyLogger.addLogger("Checking user is admin", Level.INFO);
    return ucc.checkUserIsAdmin(decodedToken.getClaim("user").asInt());
  }

  /**
   * check if the token is available.
   *
   * @param json JsonNode
   * @return true if token available
   */
  @POST
  @Path("checkToken")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode checkToken(JsonNode json) {
    if (!json.hasNonNull("token")) {
      throw new WebApplicationException("token required",
          Response.Status.BAD_REQUEST);
    }

    JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0").build();
    String token = json.get("token").asText();
    ObjectNode publicUser;
    DecodedJWT decodedToken = null;
    try {
      decodedToken = jwtVerifier.verify(token);
      UserDTO myUser = ucc.getOne(decodedToken.getClaim("user").asInt());
      publicUser = jsonMapper.createObjectNode()
          .put("token", token)
          .put("id", myUser.getId())
          .put("login", myUser.getUsername())
          .put("callNumber", myUser.getCallNumber())
          .put("textRefusal", (String) null);
    } catch (Exception e) {
      throw new WebApplicationException("token unavailable",
          Response.Status.UNAUTHORIZED);
    }
    MyLogger.addLogger("token checked", Level.INFO);
    return publicUser;
  }

  /**
   * get all user with and information about their interest in an object.
   *
   * @return List of Interest
   */
  @GET
  @Path("interestedMembers")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<InterestDTO> interestedList(@DefaultValue("-1") @QueryParam("idObject")
      int idObject) {
    // rajouter authorize pour bien verifier que c'est la personne
    // qui a créé l'objet qui veut la liste des intérressés
    if (idObject == -1) {
      throw new WebApplicationException("id object required",
          Response.Status.BAD_REQUEST);
    }
    MyLogger.addLogger("list of interested people", Level.INFO);
    return ucc.getListMemberInterested(idObject);
  }

  /**
   * get all member (user with status valid).
   *
   * @return list of user
   */
  @GET
  @Path("allMember")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public List<UserDTO> allUser() {
    MyLogger.addLogger("list of all member", Level.INFO);
    return ucc.listMemberWithStatus("valid");
  }

  /**
   * get the profile with the username in the parameter of the requeast.
   *
   * @return UserDTO
   */
  @GET
  @Path("profile")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public UserDTO getProfile(@DefaultValue("-1") @QueryParam("username")
      String username) {
    if (username == "-1") {
      throw new WebApplicationException("username required",
          Response.Status.BAD_REQUEST);
    }
    MyLogger.addLogger("get Profile", Level.INFO);
    return ucc.getUserProfile(username);

  }


  /**
   * get the profile with the username in the parameter of the requeast.
   *
   * @param json JsonNode
   * @return UserDTO
   */
  @POST
  @Path("editprofile")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public boolean editProfile(JsonNode json) {
    if (!json.hasNonNull("idAdress") || !json.hasNonNull("username")
        || !json.hasNonNull("call_number") || !json.hasNonNull(
        "firstname") || !json.hasNonNull("lastname") || !json.hasNonNull("street")
        || !json.hasNonNull("building_n umber") || !json.hasNonNull("unit_number")
        || !json.hasNonNull("postcode") || !json.hasNonNull("idMember") || !json.hasNonNull(
        "commune")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("fields are missing").type("text/plain").build());
    }
    String username = json.get("username").asText();
    if (username.length() > 240) {
      throw new WebApplicationException("username is too long",
          Response.Status.BAD_REQUEST);
    }

    String firstname = json.get("firstname").asText();
    if (firstname.length() > 50) {
      throw new WebApplicationException("firstname is too long",
          Response.Status.BAD_REQUEST);
    }

    String callNumber = json.get("call_number").asText();
    if (callNumber.length() > 15) {
      throw new WebApplicationException("call number is too long",
          Response.Status.BAD_REQUEST);
    }

    String street = json.get("street").asText();
    if (street.length() > 240) {
      throw new WebApplicationException("street is too long",
          Response.Status.BAD_REQUEST);
    }

    String lastname = json.get("lastname").asText();
    if (lastname.length() > 50) {
      throw new WebApplicationException("lastname is too long",
          Response.Status.BAD_REQUEST);
    }

    int buildingNumber = json.get("building_number").asInt();

    String unitNumber = json.get("unit_number").asText();
    if (unitNumber.length() > 50) {
      throw new WebApplicationException("unit number is too long",
          Response.Status.BAD_REQUEST);
    }

    String commune = json.get("commune").asText();
    if (commune.length() > 240) {
      throw new WebApplicationException("street is too long",
          Response.Status.BAD_REQUEST);
    }

    int idMember = json.get("idMember").asInt();
    int idAdress = json.get("idAdress").asInt();
    int postcode = json.get("postcode").asInt();
    return ucc.editAMember(idMember, username, lastname, firstname, callNumber, idAdress, street,
        buildingNumber, postcode, unitNumber, commune);

  }

  /**
   * get the profile with the username in the parameter of the requeast.
   *
   * @param json JsonNode
   * @return UserDTO
   */
  @POST
  @Path("changePwd")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public boolean changepwd(JsonNode json, @Context ContainerRequest request) {
    if (!json.hasNonNull("password")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("fields are missing").type("text/plain").build());
    }
    String pwd = json.get("password").asText();
    if (pwd.length() > 240) {
      throw new WebApplicationException("password is too long",
          Response.Status.BAD_REQUEST);
    }
    UserDTO user = (User) request.getProperty("user");
    int idUser = user.getId();
    MyLogger.addLogger("get Profile", Level.INFO);
    return ucc.changePwd(idUser, pwd);
  }


  /**
   * get all user with condtion waiting.
   *
   * @return List of userDTO
   */
  @GET
  @Path("searchMember")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public List<UserDTO> getSearchListWithStatus(@DefaultValue("-1") @QueryParam("search")
      String search) {
    //check autorize
    MyLogger.addLogger("display the list of pending registrations", Level.INFO);
    return ucc.getSearchListWithStatus(search);
  }

  /**
   * change condition of a member to disable.
   *
   * @param json JsonNode
   * @return True if the condition has been change
   */
  @POST
  @Path("disableMember")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public boolean changeStatusToDisable(JsonNode json) {
    if (!json.hasNonNull("id") || !json.hasNonNull("version")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("fields are missing").type("text/plain").build());
    }
    MyLogger.addLogger("change status of the user to disable", Level.INFO);
    int idUser = json.get("id").asInt();
    int versionMemberToCheck = json.get("version").asInt();
    return ucc.changeStatusToDisable(idUser,versionMemberToCheck);
  }

}
