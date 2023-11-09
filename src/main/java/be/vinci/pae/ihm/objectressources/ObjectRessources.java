package be.vinci.pae.ihm.objectressources;

import be.vinci.pae.biz.object.ObjectDTO;
import be.vinci.pae.biz.object.ObjectUseCaseController;
import be.vinci.pae.biz.searchobject.SearchObjectDTO;
import be.vinci.pae.biz.user.User;
import be.vinci.pae.biz.user.UserDTO;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.ihm.filters.AuthorizeAdmin;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.MyLogger;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.ContainerRequest;

@Singleton
@Path("/objects")

public class ObjectRessources {

  @Inject
  private ObjectUseCaseController oucc;

  /**
   * Get the 10 last offer from the DB.
   */
  @GET
  @Path("lastoffers")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjectDTO> getLastOffer() {
    MyLogger.addLogger("getting last Offers", Level.INFO);
    List<ObjectDTO> objects = oucc.getLastObjet();
    return objects;
  }

  /**
   * Get all the offer from the DB.
   */
  @GET
  @Path("offers")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<ObjectDTO> getOffer() {
    MyLogger.addLogger("Getting offers", Level.INFO);
    List<ObjectDTO> objects = oucc.getObjet("Offert");
    return objects;
  }

  /**
   * Get all reserved offer from the DB.
   */
  @GET
  @Path("reservedOffers")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public List<ObjectDTO> getOfferReserved() {
    MyLogger.addLogger("Getting reserved offers", Level.INFO);
    List<ObjectDTO> objects = oucc.getObjet("Réservé");
    return objects;
  }

  /**
   * Get all Given offer from the DB.
   */
  @GET
  @Path("givenOffers")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public List<ObjectDTO> getOfferGiven() {
    MyLogger.addLogger("Getting reserved offers", Level.INFO);
    List<ObjectDTO> objects = oucc.getObjet("Donné");
    return objects;
  }

  /**
   * Get all cancelled offer from the DB.
   */
  @GET
  @Path("canceledOffers")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public List<ObjectDTO> getOfferCanceled() {
    MyLogger.addLogger("Getting canceled offers", Level.INFO);
    List<ObjectDTO> objects = oucc.getObjet("Annulé");
    return objects;
  }

  /**
   * Get a specific offer from the DB with his id.
   *
   * @param idObject int
   * @return the specific Offer
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectDTO getAOffer(@PathParam("id") int idObject) {
    MyLogger.addLogger("Getting a specific offer", Level.INFO);
    ObjectDTO object = oucc.getAObject(idObject);
    if (object == null) {
      throw new WebApplicationException("Resource not found", Response.Status.NOT_FOUND);
    }
    return object;
  }

  /**
   * add a interrest to a object, check if the user is not the owner of the object.
   *
   * @param json JsonNode
   * @return a boolean if the methode work or not
   */
  @POST
  @Path("interrested")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public boolean addAInterrest(JsonNode json, @Context ContainerRequest request) {
    MyLogger.addLogger("Adding an interest ton an offer", Level.INFO);
    if (!json.hasNonNull("idUsername") || !json.hasNonNull("idObject")
        || !json.hasNonNull(
        "plageHorraire") || !json.hasNonNull("versionObject")) {
      throw new WebApplicationException("id member or id object or plage horaire required",
          Response.Status.BAD_REQUEST);
    }
    UserDTO user = (User) request.getProperty("user");
    int idUser = user.getId();
    int idObject = json.get("idObject").asInt();
    String plageHoraire = json.get("plageHorraire").asText();
    boolean conversationNeeded = json.get("boolConversation").asBoolean();
    String phoneNumber = json.get("phoneNumber").asText();
    int versionObject = json.get("versionObject").asInt();

    if (plageHoraire.length() > 240) {
      throw new WebApplicationException("plage horraire is too long",
          Response.Status.BAD_REQUEST);
    }
    if (phoneNumber.length() > 15) {
      throw new WebApplicationException("call number is too long",
          Response.Status.BAD_REQUEST);
    }
    return oucc.addAInterrest(idObject, idUser, plageHoraire,
        conversationNeeded, phoneNumber, versionObject);
  }

  /**
   * get all the objects that you created.
   *
   * @param json JsonNode
   * @return a list of Objects that you created
   */
  @POST
  @Path("myObjects")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<ObjectDTO> getMyObjects(JsonNode json, @Context ContainerRequest request) {
    MyLogger.addLogger("Getting my objects", Level.INFO);

    UserDTO user = (User) request.getProperty("user");
    int idUser = user.getId();
    return oucc.getMyObjects(idUser);
  }

  /**
   * get all the objects where you are the recipient.
   *
   * @param json JsonNode
   * @return a list of Objects where you are the recipient
   */
  @POST
  @Path("myObjectsRecipient")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<ObjectDTO> getMyObjectsRecipient(JsonNode json, @Context ContainerRequest request) {
    MyLogger.addLogger("Getting my objects where I am the recipient", Level.INFO);
    if (!json.hasNonNull("received")) {
      throw new WebApplicationException("received argument required",
          Response.Status.BAD_REQUEST);
    }
    boolean received = json.get("received").asBoolean();
    UserDTO user = (User) request.getProperty("user");
    int idUser = user.getId();
    return oucc.getMyObjectsRecipient(idUser, received);
  }

  /**
   * cancel an offer that u made.
   *
   * @param json JsonNode
   * @return true if the offer is now canceled false otherwise
   */
  @POST
  @Path("cancelOffer")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public boolean cancelOffer(JsonNode json) {
    MyLogger.addLogger("Cancelling an offer", Level.INFO);
    if (!json.hasNonNull("idObject")) {
      throw new WebApplicationException("idObject required",
          Response.Status.BAD_REQUEST);
    }
    int idObject = json.get("idObject").asInt();
    return oucc.cancelOffer(idObject);
  }

  /**
   * create a new object in the db.
   *
   * @param json JsonNode
   * @return true if the object is create
   */
  @POST
  @Path("createObject")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public int createObject(JsonNode json, @Context ContainerRequest request) {
    // Get and check credentials
    if (!json.hasNonNull("offer_name") || !json.hasNonNull("description")
        || !json.hasNonNull("type") || !json.hasNonNull("date")
        || !json.hasNonNull("image")) {
      throw new WebApplicationException("fields are missing",
          Response.Status.BAD_REQUEST);
    }
    String offerName = json.get("offer_name").asText();
    if (offerName.length() > 50) {
      throw new WebApplicationException("the name of the offer is too long",
          Response.Status.BAD_REQUEST);
    }
    String description = json.get("description").asText();
    if (description.length() > 240) {
      throw new WebApplicationException("the description is too long",
          Response.Status.BAD_REQUEST);
    }
    String timeSlotsAvailable = json.get("date").asText();
    if (timeSlotsAvailable.length() > 240) {
      throw new WebApplicationException("time slots available is too long",
          Response.Status.BAD_REQUEST);
    }
    String urlPhoto = json.get("image").asText();
    if (urlPhoto.length() > 50) {
      throw new WebApplicationException("Url of the photo is too long",
          Response.Status.BAD_REQUEST);
    }
    String type = json.get("type").asText();
    if (type.length() > 50) {
      throw new WebApplicationException("type is too long",
          Response.Status.BAD_REQUEST);
    }
    UserDTO user = (User) request.getProperty("user");
    int idOfferor = user.getId();

    return oucc.createObject(offerName, description, urlPhoto, timeSlotsAvailable, type, idOfferor);
  }

  /**
   * get all types in the db.
   *
   * @return a List of string of wording_type of the types
   */
  @GET
  @Path("/getAllTypes")
  @Produces(MediaType.APPLICATION_JSON)
  public List<String> getAllTypes() {
    MyLogger.addLogger("getting all types", Level.INFO);
    List<String> types = oucc.getAllTypes();
    return types;
  }

  /**
   * create a new type with the typeName as wording_type.
   *
   * @param json JsonNode
   * @return true if the type is create
   */
  @POST
  @Path("/createType")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public boolean createType(JsonNode json) {
    // Get and check credentials
    if (!json.hasNonNull("typeName")) {
      throw new WebApplicationException("type name or token are missing",
          Response.Status.BAD_REQUEST);
    }

    String typeName = json.get("typeName").asText();
    if (typeName.length() > 50) {
      throw new WebApplicationException("type is too long",
          Response.Status.BAD_REQUEST);
    }

    return oucc.createType(typeName);

  }

  /**
   * create a new object in the db.
   *
   * @param json JsonNode
   * @return true if the object is create
   */
  @POST
  @Path("editObject")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public boolean editObject(JsonNode json) {
    // Get and check credentials
    if (!json.hasNonNull("idObject") || !json.hasNonNull("description")
        || !json.hasNonNull("date")) {
      throw new WebApplicationException("fields are missing",
          Response.Status.BAD_REQUEST);
    }
    String description = json.get("description").asText();
    if (description.length() > 240) {
      throw new WebApplicationException("the description is too long",
          Response.Status.BAD_REQUEST);
    }
    String timeSlotsAvailable = json.get("date").asText();
    if (timeSlotsAvailable.length() > 240) {
      throw new WebApplicationException("time slots available is too long",
          Response.Status.BAD_REQUEST);
    }
    int idObject = json.get("idObject").asInt();
    return oucc.editAOffer(idObject, description, timeSlotsAvailable);
  }

  /**
   * add a recipient to an offer.
   *
   * @param json JsonNode
   * @return a boolean if the methode work or not
   */
  @POST
  @Path("addRecipient")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public boolean addRecipient(JsonNode json) {
    //add filter to verify if it is the owner of the object
    // that is adding a recipient
    MyLogger.addLogger("Adding an recipient to an offer", Level.INFO);
    if (!json.hasNonNull("idRecipient") || !json.hasNonNull("idObject")) {
      throw new WebApplicationException("fields are missing",
          Response.Status.BAD_REQUEST);
    }
    int idObject = json.get("idObject").asInt();
    int idRecipient = json.get("idRecipient").asInt();

    return oucc.addRecipient(idObject, idRecipient);
  }

  /**
   * cancel an offer that u made.
   *
   * @param json JsonNode
   * @return true if the offer is now canceled false otherwise
   */
  @POST
  @Path("reOffer")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public boolean reOfferObject(JsonNode json) {
    MyLogger.addLogger("Cancelling an offer", Level.INFO);
    if (!json.hasNonNull("idObject")) {
      throw new WebApplicationException("idObject required",
          Response.Status.BAD_REQUEST);
    }
    int idObject = json.get("idObject").asInt();
    return oucc.reOfferObject(idObject);
  }

  /**
   * indicate an object has been donated.
   *
   * @param json JsonNode
   * @return true if the offer is now indicated as donated or not donated
   */
  @POST
  @Path("indicateObjectDonated")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public boolean indicateObjectDonated(JsonNode json) {
    MyLogger.addLogger("Indicate an object is donated", Level.INFO);
    if (!json.hasNonNull("idObject") || !json.hasNonNull("donated")) {
      throw new WebApplicationException("idObject and state 'donated' required",
          Response.Status.BAD_REQUEST);
    }
    int idObject = json.get("idObject").asInt();
    boolean state = json.get("donated").asBoolean();
    return oucc.indicateObjectDonated(idObject, state);
  }

  /**
   * get all the objects that you created.
   *
   * @param json JsonNode
   * @return a list of Objects that you created
   */
  @POST
  @Path("memberObjects")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<ObjectDTO> getMemberObjects(JsonNode json) {
    MyLogger.addLogger("Getting my objects", Level.INFO);
    if (!json.hasNonNull("id")) {
      throw new WebApplicationException("token required",
          Response.Status.BAD_REQUEST);
    }
    int id = json.get("id").asInt();
    return oucc.getMyObjects(id);
  }

  /**
   * get all the objects that you received.
   *
   * @param json JsonNode
   * @return a list of Objects that you created
   */
  @POST
  @Path("memberObjectsReceived")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<ObjectDTO> getMemberObjectsReceived(JsonNode json) {
    MyLogger.addLogger("Getting my objects", Level.INFO);
    if (!json.hasNonNull("id")) {
      throw new WebApplicationException("token required",
          Response.Status.BAD_REQUEST);
    }
    int id = json.get("id").asInt();
    return oucc.getMyObjectsRecipient(id, true);
  }


  /**
   * get objects and users that are search by the user in the db.
   *
   * @return a List of objects and users that are in the db or an empty list
   */
  @GET
  @Path("searchObject")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<SearchObjectDTO> search(@DefaultValue("-1") @QueryParam("search")
      String search) {
    MyLogger.addLogger("Searching objects and users", Level.INFO);
    return oucc.search(search);
  }

  /**
   * get objects and users that are search by the user in the db with a date.
   *
   * @return a List of objects and users that are in the db or an empty list
   */
  @GET
  @Path("searchObjectDate")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<SearchObjectDTO> searchWithDate(@DefaultValue("-1") @QueryParam("search")
      String search) {
    MyLogger.addLogger("Searching objects and users", Level.INFO);
    System.out.println(search);
    return oucc.searchDate(search);
  }


  /**
   * upload a new photo in the db and the directory of pictures.
   *
   * @param file            the picture
   * @param fileDisposition the data of the picture
   * @param idObject        the id of the object
   * @return the response if the upload work or not
   */
  @POST
  @Path("/upload/{id}")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFile(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition,
      @PathParam("id") int idObject) {
    String fileName = "R";
    if (!(fileDisposition.getFileName() == null)) {
      fileName = fileDisposition.getFileName() + UUID.randomUUID().toString().replace("-",
          "") + ".png";

      String path = Config.getProperty("URLPIC");
      try {
        Files.copy(file, Paths.get(path, fileName));
      } catch (IOException e) {
        MyLogger.addLogger(e.getMessage(), e, Level.WARNING);
        throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
            .entity("no image uploaded").type("text/plain").build());
      }
      oucc.updateObjectPicture(idObject, fileName);
    }
    return Response.ok(fileName).header("Access-Control-Allow-Origin", "*").build();
  }

  /**
   * get the picture of a object.
   *
   * @param idObject the id of the object
   * @return the picture
   */
  @GET
  @Path("/picture/{id}")
  @Produces({"image/png", "image/jpg", "image/jpeg"})
  public Response getPicture(@PathParam("id") int idObject) {
    if (idObject <= 0) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("information is missing").type("text/plain").build());
    }
    ObjectDTO object = oucc.getAObject(idObject);

    try {
      String drivePath = Config.getProperty("URLPIC");
      String imagePath;
      if (object.getUrlPhoto() == null || object.getUrlPhoto().equals("R")) {
        imagePath = drivePath + "/caffe.png";
      } else {
        imagePath = drivePath + "/" + object.getUrlPhoto();
      }
      Image picture = ImageIO.read(new File(imagePath));
      return Response.ok(picture).build();
    } catch (IOException e) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("impossible de recuperer les images").type("text/plain").build());
    }

  }


  /**
   * evaluate an object.
   *
   * @param json JsonNode
   * @return true if the object is now evaluated
   */
  @POST
  @Path("evaluateObject")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public boolean evaluateObject(JsonNode json, @Context ContainerRequest request) {
    MyLogger.addLogger("Evaluating an object", Level.INFO);
    if (!json.hasNonNull("idObject") || !json.hasNonNull("starRate")
        || !json.hasNonNull("remark")) {
      throw new WebApplicationException("parameters idObject, starRate and remark required",
          Response.Status.BAD_REQUEST);
    }
    UserDTO user = (User) request.getProperty("user");
    int idMember = user.getId();
    int idObject = json.get("idObject").asInt();
    int starRate = json.get("starRate").asInt();
    String remark = json.get("remark").asText();
    return oucc.evaluateObject(idObject, starRate, remark, idMember);
  }

}
