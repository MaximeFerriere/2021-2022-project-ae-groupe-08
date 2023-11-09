/**
 * Render the HomePage
 */
import {getSessionObject} from "../../utils/session";

const OfferPage = async () => {
      const pageDiv = document.querySelector("#content");
      pageDiv.innerHTML = ` 
      <div class"all-object-details-container">
        <div class="object-details-container">
          <h2 id=ObjectTitle></h2><hr>
          <div class="img-container-details">
             <img id=ObjectImage></img> 
          </div>
          <form id =FormInterest>
            <p><label for="DateInterest" class=ObjectTitleCard >Plage horaire :</label>
              <input id=DateInterest type=text value=""><br>
            </p>
            <p>
              <label for="ConversationNeeded">
              Une conversation pour discuter des modalités pratiques est nécessaire:</label>
              <input id="ConversationNeeded" type="checkbox"><br>
            </p>
            <p>
              <label for="PhoneNumber" class=ObjectTitleCard >Numero de télephone :</label>
              <input id="PhoneNumber" type="text" value=""><br>
            </p>
            <button class="button-interest">
              Marquer son intérêt
              <input id=ButtonInterest type=submit >
            </button>
          </form>
          <span id=ErrorInterest></span>
          <div id="information"></div>
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
      if (search_params.has('id')) {
        var id = search_params.get('id');
      }
      let user = getSessionObject("user"); //get the user from the session
      const phoneN = document.querySelector("#PhoneNumber");
      if (user.callNumber != "") {
        phoneN.value = user.callNumber;
      }
      try {
        const options = {
          method: "GET", // *GET, POST, PUT, DELETE, etc.
          headers: {
            "Authorization": user.token,
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
        const form = document.querySelector("#FormInterest");

        const title = document.querySelector("#ObjectTitle");
        title.innerHTML = objects.name;
        const ObjectName = document.querySelector("#ObjectName");
        ObjectName.innerHTML = objects.name;
        const ObjectDescription = document.querySelector("#ObjectDescription");
        ObjectDescription.innerHTML = objects.description;
        const ObjectType = document.querySelector("#ObjectType");
        ObjectType.innerHTML = objects.typeStr;
        const ObjectState = document.querySelector("#ObjectState");
        ObjectState.innerHTML = objects.state;
        const ObjectOfferer = document.querySelector("#ObjectOfferer");
        ObjectOfferer.innerHTML = objects.offerorStr;
        const ObjectTimeSlotsAvailable = document.querySelector(
            "#ObjectTimeSlotsAvailable");
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
        imageS.src = image;

        if (objects.state == "Annulé" || objects.state == "Donné" || objects.state == "Réservé") {
          const buttonInterest = document.querySelector(".button-interest");
          buttonInterest.disabled = true;
        }
        form.addEventListener("submit", async (e) => {
          e.preventDefault();
          const errorText = document.querySelector("#ErrorInterest");
          const idObject = objects.idObjet;
          let user = getSessionObject("user");
          const idUsername = user.token;
          const date = document.querySelector("#DateInterest");
          const DateInterest = date.value;
          const conversationNeeded = document.querySelector(
              "#ConversationNeeded");
          const boolonversationNeeded = conversationNeeded.checked;
          const phoneNumber = document.querySelector("#PhoneNumber");
          const PhoneNumber = phoneNumber.value;
          const versionObjectI = objects.versionObject;

          const options = {
            method: "POST", // *GET, POST, PUT, DELETE, etc.
            body: JSON.stringify({
              idObject: idObject,
              idUsername: idUsername,
              plageHorraire: DateInterest,
              boolConversation: boolonversationNeeded,
              phoneNumber: PhoneNumber,
              versionObject: versionObjectI,
            }), // body data type must match "Content-Type" header
            headers: {
              "Content-Type": "application/json",
              "Authorization": user.token,
            },
          };

          const response = await fetch("/api/objects/interrested", options);
          if (!response.ok) {
            errorText.innerHTML = "Vous ne pouvez pas marquer votre intérêt pour votre propre offre";
            throw new Error(
                "fetch error : " + response.status + " : " + response.statusText
            );

          }
          const result = await response.json();

          if (!result) {
            errorText.innerHTML = "Vous avez déjà marqué votre intérêt pour cette offre !";
          } else {
            errorText.innerHTML = "Vous avez bien marqué votre intérêt !";
            NumberOfInterest.innerHTML = objects.numberOfPeopleInterested + 1;
          }
        });

      } catch (error) {
        console.error("OfferPageView::error: ", error);
      }

    }
;

export default OfferPage;
