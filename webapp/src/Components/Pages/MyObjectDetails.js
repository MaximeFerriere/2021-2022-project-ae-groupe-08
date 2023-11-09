import {Redirect} from "../Router/Router";
import {getSessionObject} from "../../utils/session";
import {API_URL} from "../../utils/server";
//show the object details
const MyObjectDetailsPage = async () => {
  const pageDiv = document.querySelector("#content");
  pageDiv.innerHTML = ` 
  <div class"all-object-details-container">
    <div class="object-details-container">
      <h2 id=ObjectTitle></h2><hr>
      <div class="img-container-details">
        <img id=ObjectImage></img> 
      </div>
      <div class="container-button-donate">
        <div  id="objectDonatedDiv"></div>
        <div  id="objectNotDonatedDiv"></div>

        <div  id="newRecipientDiv"></div>
        <div  id="reofferDiv"></div>
      </div>
    </div>
  </div>

  <div class="col">
    <div id="information">
      <div class=card-MOD id=ObjectCard>

        <div class="row">
          <h2 id=ObjectName> </h2>
        </div>

        <div class="row">
          <p class=ObjectTitleCard> Description de l'offre :</p>
        </div>

        <div class="row">
          <p id=ObjectDescription></p>
        </div>

        <div class="row">
          <div class="col-6 col-sm-3">
            <p class=ObjectTitleCard> Type : </p>
          </div>
          <div class="col-6 col-sm-3">
            <p id=ObjectType></p>
          </div>
        </div>

        <div class="row">
          <div class="col-6 col-sm-3">
            <p class=ObjectTitleCard> Etat :</p>
          </div>
          <div class="col-6 col-sm-3">
            <p id=ObjectState></p>
          </div>
        </div>
        <input type="button"   id ="buttonstate">

        <div class="row">
          <div class="col-6 col-sm-3">
            <p class=ObjectTitleCard> Offreur : </p>
          </div>
          <div class="col-6 col-sm-3">
            <p id=ObjectOfferer></p>
          </div>
        </div>

        <div class="row">
          <p class=ObjectTitleCard> Disponibilités : </p>
        </div>

        <div class="row">
          <p id=ObjectTimeSlotsAvailable></p>
        </div>

        <div class="row">
          <div class="col">
            <p class=ObjectTitleCard> Nombre d'interesé : </p>
          </div>
          <div class="col">
            <p id=NumberOfInterest></p>
          </div>
        </div>
        
        <div class="row">
          <p id="buttonInterrest"></p>
        </div>

        <div class="row">
          <input id="buttonSaveEdit"  class="btn btn-secondary" value ="Sauvegarder" type="button"></input>
        </div>
        <br>
           <form>
            <label class=ObjectTitleCard>Changer de Photo : </label>
            <input name="file" type= "file" /> <br/><br/>
     
          </form>
          <input type="button" class="btn btn-secondary" id="fileChange" value="Changer de photo">
          <p id="errorUpload"></p>
      </div>
    </div>
  </div>`;

  var url = new URL(window.location.href);//get the url of the current page 
  var search_params = new URLSearchParams(url.search);//get the object of the url
  if (search_params.has('id')) {
    var id = search_params.get('id');// get the id from the object in the url 
  }
  try {
    let user = getSessionObject("user"); //get the user from the session
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
    // Select the Title
    const title = document.querySelector("#ObjectTitle");
    title.innerHTML = objects.name;
    // Select the name
    const ObjectName = document.querySelector("#ObjectName");
    ObjectName.innerHTML = objects.name;
    // Select the Description
    const ObjectDescription = document.querySelector("#ObjectDescription");
    const inpDes = document.createElement("input");
    inpDes.type = "text";
    inpDes.className = "form-control";
    inpDes.value = objects.description;
    inpDes.style = "width : 400px";
    ObjectDescription.appendChild(inpDes);
    //ObjectDescription.innerHTML = objects.description;
    //Select the type
    const ObjectType = document.querySelector("#ObjectType");
    ObjectType.innerHTML = objects.typeStr;
    //Select the state
    const ObjectState = document.querySelector("#ObjectState");
    ObjectState.innerHTML = objects.state;
    const buttonState = document.querySelector("#buttonstate");
    buttonState.className = "btn btn-secondary";
    if (objects.state != "Annulé") {
      buttonState.value = "Annuler offre";
    } else {
      buttonState.value = "Offrir à nouveau";
    }
    if (objects.state == "Donné") {
      buttonState.disabled = true;
    }
    //Select Offerer
    const ObjectOfferer = document.querySelector("#ObjectOfferer");
    ObjectOfferer.innerHTML = objects.offerorStr;
    //Select time slots available
    const ObjectTimeSlotsAvailable = document.querySelector(
        "#ObjectTimeSlotsAvailable");
    //ObjectTimeSlotsAvailable.innerHTML = objects.timeSlotAvailable;
    const timeInput = document.createElement("input");
    timeInput.type = "text";
    timeInput.value = objects.timeSlotAvailable;
    timeInput.className = "form-control";
    timeInput.style = "width : 400px";
    ObjectTimeSlotsAvailable.appendChild(timeInput);

    //??
    const information = document.querySelector("#information");
    //Select the number of interest
    const NumberOfInterest = document.querySelector("#NumberOfInterest");
    NumberOfInterest.innerHTML = objects.numberOfPeopleInterested;
    //Select the image
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

    buttonState.addEventListener("click", async (e) => {
      e.preventDefault();
      let user = getSessionObject("user"); //get the user from the session
      const idObject = objects.idObjet; //get the id of the object
      const options = {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify({
          idObject: idObject,
        }), // body data type must match "Content-Type" header
        headers: {
          "Content-Type": "application/json",
          "Authorization": user.token,
        },
      };
      if (objects.state != "Annulé") {
        const reponse = await fetch("/api/objects/cancelOffer", options);// fetch return a promise => we wait for the response
        if (!reponse.ok) {
          throw new Error(
              "fetch error : " + reponse.status + " : " + reponse.statusText
          );
        }
        Redirect("/MyObjectDetails?id=" + id);
      } else {
        const reponse = await fetch("/api/objects/reOffer", options);// fetch return a promise => we wait for the response
        if (!reponse.ok) {
          throw new Error(
              "fetch error : " + reponse.status + " : " + reponse.statusText
          );
        }
        Redirect("/MyObjectDetails?id=" + id);
      }
    });

    const buttonInterrest = document.querySelector("#buttonInterrest");//get the html id 
    const href = document.createElement("input");//create a button in the p html 
    href.type = "button";
    href.id = "clickable";
    href.value = "Voir liste des intéressés";
    href.dataset.uri = "/interestedList";
    href.dataset.id = objects.idObjet;//get the id of the object
    //put the id of the object in the uri
    href.dataset.uri += "?id=" + objects.idObjet;
    if (objects.state == "Annulé") {
      href.disabled = true;
    }

    //---------------------------------------------------
    buttonInterrest.appendChild(href);
    //save the data and send it to the backend on click
    const buttonSaveEdit = document.querySelector("#buttonSaveEdit");
    if (objects.state == "Donné") {
      buttonSaveEdit.disabled = true;
    }
    buttonSaveEdit.addEventListener("click", async (e) => {
      e.preventDefault();
      const idObject = id;
      const description = inpDes.value;
      const timeSlot = timeInput.value;

      const options = {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify({
          idObject: idObject,
          date: timeSlot,
          description: description,
        }), // body data type must match "Content-Type" header
        headers: {
          "Content-Type": "application/json",
          "Authorization": user.token,
        },
      };
      const reponse = await fetch("/api/objects/editObject", options);
      if (!reponse.ok) {
        throw new Error(
            "fetch error : " + reponse.status + " : " + reponse.statusText
        );
      }
      //Redirect("/MyObjectDetails")
    });

    //things under are for buttons "indiquer objet donné" et "indiquer objet pas donné"
    if (objects.state === "Réservé") {
      const indicateObjectDonatedButton = document.createElement("input");
      indicateObjectDonatedButton.type = "button";
      indicateObjectDonatedButton.id = "objectDonated";
      indicateObjectDonatedButton.value = "L'objet a été donné";
      indicateObjectDonatedButton.className = "button_top";

      const indicateObjectNotDonatedButton = document.createElement("input");
      indicateObjectNotDonatedButton.type = "button";
      indicateObjectNotDonatedButton.id = "objectNotDonated";
      indicateObjectNotDonatedButton.value = "L'objet n'a pas été donné et ne le sera pas";
      indicateObjectNotDonatedButton.className = "button_top";

      const indicateObjectDonatedButtonDiv = document.querySelector(
          "#objectDonatedDiv");
      indicateObjectDonatedButtonDiv.appendChild(indicateObjectDonatedButton);

      const indicateObjectNotDonatedButtonDiv = document.querySelector(
          "#objectNotDonatedDiv");
      indicateObjectNotDonatedButtonDiv.appendChild(
          indicateObjectNotDonatedButton);

      const errorObjectDonated = document.createElement("span");
      errorObjectDonated.id = "errorObjectDonated";

      pageDiv.appendChild(errorObjectDonated);

      indicateObjectDonatedButton.addEventListener("click", async (e) => {
        e.preventDefault();
        const errorText = document.querySelector("#errorObjectDonated");
        const options = {
          method: "POST",
          body: JSON.stringify({
            idObject: objects.idObjet,
            donated: true,
          }),
          headers: {
            "Content-Type": "application/json",
            "Authorization": user.token,
          },
        };
        const response = await fetch("/api/objects/indicateObjectDonated",
            options);
        if (!response.ok) {
          errorText.innerHTML = "L'objet n'est pas dans l'état réservé, il a surement déjà été indiqué comme donné ou pas donné";
          throw new Error(
              "fetch error : " + response.status + " : " + response.statusText
          );
        }
        const result = await response.json();
        if (!result) {
          errorText.innerHTML = "L'objet n'est pas dans l'état réservé, il a surement déjà été indiqué comme donné ou pas donné";
        } else {
          Redirect("/MyObjectDetails?id=" + id);
          errorText.innerHTML = "Merci d'avoir indiqué que l'objet à été donné !";
        }
      });

      indicateObjectNotDonatedButton.addEventListener("click", async (e) => {
        e.preventDefault();
        const errorText = document.querySelector("#errorObjectDonated");
        const options = {
          method: "POST",
          body: JSON.stringify({
            idObject: objects.idObjet,
            donated: false,
          }),
          headers: {
            "Content-Type": "application/json",
            "Authorization": user.token,
          },
        };
        const response = await fetch("/api/objects/indicateObjectDonated",
            options);
        if (!response.ok) {
          errorText.innerHTML = "L'objet n'est pas dans l'état réservé, il a surement déjà été indiqué comme donné ou pas donné";
          throw new Error(
              "fetch error : " + response.status + " : " + response.statusText
          );
        }
        const result = await response.json();
        if (!result) {
          errorText.innerHTML = "L'objet n'est pas dans l'état réservé, il a surement déjà été indiqué comme donné ou pas donné";
        } else {
          Redirect("/MyObjectDetails?id=" + id);
          errorText.innerHTML = "Merci d'avoir indiqué que l'objet n'a pas pu être donné !";
        }
      });
    }

    if (objects.state === "Pas donné") {
      const newRecipientButton = document.createElement("input");
      newRecipientButton.type = "button";
      newRecipientButton.id = "newRecipient";
      newRecipientButton.value = "Choisir un nouveau receveur";
      newRecipientButton.dataset.uri = "/interestedList";
      newRecipientButton.dataset.id = objects.idObjet;
      newRecipientButton.dataset.uri += "?id=" + objects.idObjet;
      newRecipientButton.className = "button_top"

      const reofferButton = document.createElement("input");
      reofferButton.type = "button";
      reofferButton.id = "reoffer";
      reofferButton.value = "Offrir à nouveau l'objet";
      reofferButton.className = "button_top"

      reofferButton.addEventListener("click", async (e) => {
        e.preventDefault();
        let user = getSessionObject("user"); //get the user from the session
        const idObject = objects.idObjet; //get the id of the object
        const options = {
          method: "POST", // *GET, POST, PUT, DELETE, etc.
          body: JSON.stringify({
            idObject: idObject,
          }), // body data type must match "Content-Type" header
          headers: {
            "Content-Type": "application/json",
            "Authorization": user.token,
          },
        };

        const reponse = await fetch("/api/objects/reOffer", options);// fetch return a promise => we wait for the response
        if (!reponse.ok) {
          throw new Error(
              "fetch error : " + reponse.status + " : " + reponse.statusText
          );
        }
        Redirect("/MyObjectDetails?id=" + id);

      });

      const errorObjectDonated = document.createElement("span");
      errorObjectDonated.id = "errorObjectDonated";

      const newRecipientButtonDiv = document.querySelector("#newRecipientDiv");
      const reofferButtonDiv = document.querySelector("#reofferDiv");

      newRecipientButtonDiv.appendChild(newRecipientButton);
      reofferButtonDiv.appendChild(reofferButton);
      pageDiv.appendChild(errorObjectDonated);
    }

    //change photo
    const buttonChangePhto = document.querySelector("#fileChange");
    buttonChangePhto.addEventListener("click", changepto);

    async function changepto() {
      const errorText = document.querySelector("#errorUpload");
      const itemId = id;
      const fileInput = document.querySelector('input[name=file]');
      const formData = new FormData();
      formData.append('file', fileInput.files[0]);
      formData.append('itemId', itemId);
      const optionsimg = {
        method: 'POST',
        body: formData
      };
      const responseUpload = await fetch(API_URL + "objects/upload/" + itemId,
          optionsimg);
      if (!response.ok) {

        errorText.innerHTML = `<br> Message d'erreur : <br>`;
        errorText.innerHTML += "échec de l'upload de l'image";

        throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
        );
      } else {
        Redirect("/MyObjectDetails?id=" + id);
        errorText.innerHTML = "votre photo a bien ete changer";
      }
    }

  } catch (error) {
    console.error("OfferPageView::error: ", error);
  }

};

export default MyObjectDetailsPage;
