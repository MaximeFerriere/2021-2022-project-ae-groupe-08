/**
 * Render the HomePage
 */
import {getSessionObject} from "../../utils/session";

const GiftReceivedDetails = async () => {
  const pageDiv = document.querySelector("#content");

  pageDiv.innerHTML = ` 
  <div class"all-object-details-container">
    <div class="object-details-container">
      <h2 id=ObjectTitle></h2><hr>
      <div class="img-container-details-2">
       <img id=ObjectImage></img> 
      </div>

      <div id="eval">
      </div>

    </div>
  </div>
  <div id=ObjectCard style=width:43rem >
    <h2><p id=ObjectName></p></h2>
    <p class=ObjectTitleCard> Description de l'offre :</p>
    <p id=ObjectDescription></p>
    <p class=ObjectTitleCard> Type : </p>
    <p id=ObjectType></p>
    <p class=ObjectTitleCard> Etat :</p>
    <p id=ObjectState></p>
    <p class=ObjectTitleCard> Offreur : </p>
    <p id=ObjectOfferer></p>
    <p class=ObjectTitleCard> Disponibilités : </p>
    <p id=ObjectTimeSlotsAvailable></p>
    <p class=ObjectTitleCard> Nombre d'interesé : </p>
    <p id=NumberOfInterest></p>
  </div>
</div>`;

  var url = new URL(window.location.href);
  var search_params = new URLSearchParams(url.search);
  if (search_params.has("id")) {
    var id = search_params.get("id");
  }
  let user = getSessionObject("user"); //get the user from the session
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: user.token,
      },
    };
    const response = await fetch("/api/objects/" + id, options); // fetch return a promise => we wait for the response
    if (!response.ok) {
      // status code was not 200, error status code
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const objects = await response.json(); // json() returns a promise => we wait for the data
    // create a wrapper to provide a responsive table

    const title = document.querySelector("#ObjectTitle");
    title.innerHTML = objects.name;
    const ObjectDescription = document.querySelector("#ObjectDescription");
    ObjectDescription.innerHTML = objects.description;
    const ObjectType = document.querySelector("#ObjectType");
    ObjectType.innerHTML = objects.typeStr;
    const ObjectState = document.querySelector("#ObjectState");
    ObjectState.innerHTML = objects.state;
    const ObjectOfferer = document.querySelector("#ObjectOfferer");
    ObjectOfferer.innerHTML = objects.offerorStr;
    const ObjectTimeSlotsAvailable = document.querySelector(
        "#ObjectTimeSlotsAvailable"
    );
    ObjectTimeSlotsAvailable.innerHTML = objects.timeSlotAvailable;
    const information = document.querySelector("#information");

    const NumberOfInterest = document.querySelector("#NumberOfInterest");
    NumberOfInterest.innerHTML = objects.numberOfPeopleInterested;

    const responseImage = await fetch(
        "/api/objects/picture/" + objects.idObjet); // fetch return a promise => we wait for the response
    if (!responseImage.ok) {
      // status code was not 200, error status code
      throw new Error(
          "fetch error : " + responseImage.status + " : "
          + responseImage.statusText
      );
    }
    const imageObjectURL = await responseImage.blob();
    const image = URL.createObjectURL(imageObjectURL);
    const imageS = document.querySelector("#ObjectImage");
    imageS.src = image; //object.urlPhoto;

    if (objects.state == "Donné") {
      const evalu = document.querySelector("#eval");
      evalu.innerHTML = `<h3> Evaluer l'objet :</h3><hr>
      <form id="evaluation">
        <div class="starRating">
          <input type="radio" name="starRate" value="5" id="fifth">
          <label for="fifth"></label>
          <input type="radio" name="starRate" value="4" id="fourth">
          <label for="fourth"></label>
          <input type="radio" name="starRate" value="3" id="thirth">
          <label for="thirth"></label>
          <input type="radio" name="starRate" value="2" id="second">
          <label for="second"></label>
          <input type="radio" name="starRate" value="1" id="first">
          <label for="first"></label>
          <span class="result"></span>
        </div>
        <label for="Remark"><h4>Remarque :</h4></label>
        <div class="container-remark">
          <textarea id="Remark" type="text" value=""></textarea><br>
          <input id=ButtonSubmit type=submit value="Valider" >
        </div>
      </form>
      <span id=ErrorEvaluation></span>`;

      const form = document.querySelector("#evaluation");
      form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const errorText = document.querySelector("#ErrorEvaluation");

        const idObject = objects.idObjet;
        const starRateSele = document.querySelector(
            'input[name="starRate"]:checked'
        );
        const remarkSele = document.querySelector('#Remark');
        //remarkSele.style = "width: 400px; height: 300px;"
        if (remarkSele.value == "" || starRateSele == null) {
          errorText.innerHTML = " Veuillez remplir tous les champs !"
        } else {
          const remark = remarkSele.value;
          const starRate = starRateSele.value;
          const options = {
            method: "POST", // *GET, POST, PUT, DELETE, etc.
            body: JSON.stringify({
              idObject: idObject,
              starRate: starRate,
              remark: remark,
            }), // body data type must match "Content-Type" header
            headers: {
              "Content-Type": "application/json",
              Authorization: user.token,
            },
          };

          const response = await fetch("/api/objects/evaluateObject", options);
          if (!response.ok) {
            errorText.innerHTML = "Vous avez déjà évalué cet objet";
            throw new Error(
                "fetch error : " + response.status + " : " + response.statusText
            );
          }
          const result = await response.json();

          if (!result) {
            errorText.innerHTML = "Vous avez déjà évalué cet objet";
          } else {
            errorText.innerHTML = "Vous avez bien évalué l'objet !";
          }
        }

      });
    }
  } catch (error) {
    console.error("OfferPageView::error: ", error);
  }
};

export default GiftReceivedDetails;
