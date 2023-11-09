import {getSessionObject} from "../../utils/session";
import Pun from "../../img/caffe.png";
import {Redirect} from "../Router/Router";

const MemberPage = async () => {
  const pageDiv = document.querySelector("#content");
  pageDiv.innerHTML = `<div id="information">
                        <div><h2><p id=userNameUser></p></h2></div?>
                        <div class=card id=UserCard  >
                          <p class=ObjectTitleCard> nom :</p>
                          <p id=nameUser></p>
                          <p class=ObjectTitleCard> prenom : </p>
                          <p id=firstNameUser></p>
                          <p class=ObjectTitleCard> adresse :</p>
                          <p id=adresseUser></p>
                          <p class=ObjectTitleCard> gsm : </p>
                          <p id=gsmUser></p>
                          <input type="button" value="Désactiver membre"  id ="buttonCondition">
                          <p id="ErrorMessage"></p>
                        </div>
                        <p class=ObjectOfferCard><h3> Objets du membre : </h3></p>
                        <p id=ObjectOffer></p>
                        <p class=ObjectReceivedCard><h3> Objets reçus : </h3></p>
                        <p id=ObjectReceived></p>
                      </div>
`;

  var url = new URL(window.location.href);
  var search_params = new URLSearchParams(url.search);
  if (search_params.has('username')) {
    var username = search_params.get('username');
  }
  let idMember;

  try {
    let user = getSessionObject("user"); //get the user from the session
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.

      headers: {
        "Authorization": user.token,
      },
    };
    const response = await fetch("/api/users/profile?username="+username, options);
    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + ":" + response.statusText
      );
    }
    const objects = await response.json();

    const usernameUser = document.querySelector("#userNameUser");
    usernameUser.innerHTML = username;
    const nameUser = document.querySelector("#nameUser")
    nameUser.innerHTML = objects.lastName;
    const firstNameUser = document.querySelector("#firstNameUser")
    firstNameUser.innerHTML = objects.firstName;
    const adresseUser = document.querySelector("#adresseUser")
    adresseUser.innerHTML = objects.address.street
    adresseUser.innerHTML += " N°"
    adresseUser.innerHTML += objects.address.buildingNumber
    if (!(objects.address.unitNumber == null)) {
      adresseUser.innerHTML += " Boite ";
      adresseUser.innerHTML += objects.address.unitNumber;
    }
    adresseUser.innerHTML += ", ";
    adresseUser.innerHTML += objects.address.postcode;
    adresseUser.innerHTML += "  ";
    if (!(objects.address.commune == null)) {
      adresseUser.innerHTML += objects.address.commune;
    } else {
      adresseUser.innerHTML += " Bruxelles";
    }
    idMember = objects.id;
    const gsmUser = document.querySelector("#gsmUser")
    if (!(objects.callNumber == null)) {
      gsmUser.innerHTML = objects.callNumber;
    } else {
      gsmUser.innerHTML = "non renseigné";
    }
    const buttonCondition = document.querySelector("#buttonCondition");
    buttonCondition.className = "btn btn-secondary";
    if (objects.condition == "disabled") {
      buttonCondition.disabled = true;
    }

    buttonCondition.addEventListener("click", async (e) => {
      e.preventDefault();
      let user = getSessionObject("user"); //get the user from the session
      const idMember = objects.id; //get the id of the object
      const options = {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify({
          id: idMember,
          version: objects.versionMember,
        }), // body data type must match "Content-Type" header
        headers: {
          "Content-Type": "application/json",
          "Authorization": user.token,
        },
      };
      const reponse = await fetch("/api/users/disableMember", options);// fetch return a promise => we wait for the response
      if (!reponse.ok) {
        throw new Error(
            "fetch error : " + reponse.status + " : " + reponse.statusText
        );
      }
      const ErrorMessage = document.querySelector("#ErrorMessage")
      ErrorMessage.innerHTML = "Le membre est bien désactivé"
      
    });

  } catch (error) {
    console.error("OfferPageView::error: ", error);
  }
  try {
    let user = getSessionObject("user"); //get the user from the session
    const options = {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      body: JSON.stringify({
        id: idMember,
      }), // body data type must match "Content-Type" header
      headers: {
        "Content-Type": "application/json",
        "Authorization": user.token,
      },
    };
    const response = await fetch("/api/objects/memberObjects", options); // fetch return a promise => we wait for the response

    if (!response.ok) {
      // status code was not 200, error status code
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }

    const objects = await response.json(); // json() returns a promise => we wait for the data
    // create a wrapper to provide a responsive table

    const divObjectOffer = document.querySelector("#ObjectOffer")
    const cardWrapper2 = document.createElement("div");
    cardWrapper2.className = "card-deck1";
    //get all the objects from objects
    for (let index = 0; index < objects.length; index++)  {
      const cardWrapper1 = document.createElement("div");
      cardWrapper1.className = "card-deck";
      cardWrapper2.appendChild(cardWrapper1);

      const cardWrapper = document.createElement("div");
      cardWrapper.className = "card";
      cardWrapper.style = "width: 20rem";

      const tbody = document.createElement("tbody");
      const img = document.createElement("img");
      const responseImage = await fetch("/api/objects/picture/"+objects[index].idObjet); // fetch return a promise => we wait for the response
  
      if (!responseImage.ok) {
      // status code was not 200, error status code
        throw new Error(
          "fetch error : " + responseImage.status + " : " + responseImage.statusText
        );
      }

      const imageObjectURL = await responseImage.blob();
      const image = URL.createObjectURL(imageObjectURL);
      img.className = "card-img";
      img.src = image; //object.urlPhoto;
      cardWrapper.appendChild(img);
      const newDiv = document.createElement("div");
      const nameCell = document.createElement("p");
      nameCell.innerHTML = "Nom de l'objet : ";
      nameCell.innerText += objects[index].name;
      nameCell.innerHTML += "\nType de l'objet : ";
      nameCell.innerText += objects[index].typeStr;
      nameCell.className = "text-body";
      newDiv.appendChild(nameCell);

      const href = document.createElement("input");
      href.type = "button";
      href.id = "clickable";
      href.value = "Plus de details";
      href.dataset.uri = "/OfferPage";
      href.dataset.id = objects[index].idObjet;
      href.className = "button-more-details";
      //set the object in the uri
      href.dataset.uri += "?id=" + objects[index].idObjet;

      href.addEventListener("click", () => {
        Redirect("/");
      });

      newDiv.appendChild(href);
      newDiv.dataset.ObjectId = objects[index].idObjet;
      tbody.appendChild(newDiv);
      cardWrapper.appendChild(tbody);
      cardWrapper1.appendChild(cardWrapper);
    };

    // add the HTMLTableElement to the main, within the #page div
    divObjectOffer.appendChild(cardWrapper2);
    divObjectOffer.innerHTML += "<br></br>";

  } catch (error) {
    console.error("HomePageView::error: ", error);
  }
  //Object Received
  try {
    let user = getSessionObject("user"); //get the user from the session
    const options = {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      body: JSON.stringify({
        id: idMember,
      }), // body data type must match "Content-Type" header
      headers: {
        "Content-Type": "application/json",
        "Authorization": user.token,
      },
    };
    const response = await fetch("/api/objects/memberObjectsReceived", options); // fetch return a promise => we wait for the response

    if (!response.ok) {
      // status code was not 200, error status code
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }

    const objects = await response.json(); // json() returns a promise => we wait for the data
    // create a wrapper to provide a responsive table

    const divObjectOffer = document.querySelector("#ObjectReceived")
    const cardWrapper2 = document.createElement("div");
    cardWrapper2.className = "card-deck1";
    //get all the objects from objects
    for (let index = 0; index < objects.length; index++) {
      const cardWrapper1 = document.createElement("div");
      cardWrapper1.className = "card-deck";
      cardWrapper2.appendChild(cardWrapper1);

      const cardWrapper = document.createElement("div");
      cardWrapper.className = "card";
      cardWrapper.style = "width: 20rem";

      const tbody = document.createElement("tbody");
      const img = document.createElement("img");

      const responseImage = await fetch("/api/objects/picture/"+objects[index].idObjet); // fetch return a promise => we wait for the response
  
      if (!responseImage.ok) {
      // status code was not 200, error status code
        throw new Error(
          "fetch error : " + responseImage.status + " : " + responseImage.statusText
        );
      }

      const imageObjectURL = await responseImage.blob();
      const image = URL.createObjectURL(imageObjectURL);
      img.className = "card-img";
      img.src = image; //object.urlPhoto;
      cardWrapper.appendChild(img);
      const newDiv = document.createElement("div");
      const nameCell = document.createElement("p");
      nameCell.innerHTML = "Nom de l'objet : ";
      nameCell.innerText += objects[index].name;
      nameCell.innerHTML += "\nType de l'objet : ";
      nameCell.innerText += objects[index].typeStr;
      nameCell.className = "text-body";
      newDiv.appendChild(nameCell);

      const href = document.createElement("input");
      href.type = "button";
      href.id = "clickable";
      href.value = "Plus de details";
      href.dataset.uri = "/OfferPage";
      href.dataset.id = objects[index].idObjet;
      href.className = "button-more-details";
      //set the object in the uri
      href.dataset.uri += "?id=" + objects[index].idObjet;

      href.addEventListener("click", () => {
        Redirect("/");
      });

      newDiv.appendChild(href);
      newDiv.dataset.ObjectId = objects[index].idObjet;
      tbody.appendChild(newDiv);
      cardWrapper.appendChild(tbody);
      cardWrapper1.appendChild(cardWrapper);
    };

    // add the HTMLTableElement to the main, within the #page div
    divObjectOffer.appendChild(cardWrapper2);
    divObjectOffer.innerHTML += "<br></br>";

  } catch (error) {
    console.error("HomePageView::error: ", error);
  }

};

export default MemberPage;
