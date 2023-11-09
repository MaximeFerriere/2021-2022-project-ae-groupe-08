import Pun from "../../img/caffe.png";
import {getSessionObject} from "../../utils/session";
import {Redirect} from "../Router/Router";


const GiftReceived = async () => {
    const pageDiv = document.querySelector("#content");
    pageDiv.innerHTML = ``;
  
    pageDiv.innerHTML = `<h1>Mes Offres recues</h1>    
    <p class=ObjectNotReceivedCard><h3> Objets assignés : </h3></p>
    <p id=ObjectNotReceived></p>
    <p class=ObjectReceivedCard><h3> Objets reçus : </h3></p>
    <p id=ObjectReceived></p>
    `;
  
    try {
  
      let user = getSessionObject("user"); //get the user from the session
      const options = {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify({
            received: true,
        }), // body data type must match "Content-Type" header
        headers: {
          "Content-Type": "application/json",
          "Authorization": user.token,
        },
      };
      const response = await fetch("/api/objects/myObjectsRecipient", options); // fetch return a promise => we wait for the response
  
      if (!response.ok) {
        // status code was not 200, error status code
        throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
        );
      }
  
      const objects = await response.json(); // json() returns a promise => we wait for the data

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
  
        const responseImage = await fetch(
          "/api/objects/picture/" + objects[index].idObjet); // fetch return a promise => we wait for the response

      if (!responseImage.ok) {
        // status code was not 200, error status code
        throw new Error(
            "fetch error : " + responseImage.status + " : "
            + responseImage.statusText
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
        href.dataset.uri = "/MyObjectsReceivedDetails";
        href.dataset.id = objects[index].idObjet;
        href.className = "button-more-details";
        //set the object in the uri
        href.dataset.uri += "?id=" + objects[index].idObjet;
  
        newDiv.appendChild(href);
        newDiv.dataset.ObjectId = objects[index].idObjet;
        tbody.appendChild(newDiv);
        cardWrapper.appendChild(tbody);
        cardWrapper1.appendChild(cardWrapper);
  
      };
      divObjectOffer.appendChild(cardWrapper2);
      divObjectOffer.innerHTML += "<br></br>";
  
  
    } catch (error) {
      console.error("MyObjectReceivedView::error: ", error);
    }


    try {
  
        let user = getSessionObject("user"); //get the user from the session
        const options = {
          method: "POST", // *GET, POST, PUT, DELETE, etc.
          body: JSON.stringify({
              received: false,
          }), // body data type must match "Content-Type" header
          headers: {
            "Content-Type": "application/json",
            "Authorization": user.token,
          },
        };
        const response = await fetch("/api/objects/myObjectsRecipient", options); // fetch return a promise => we wait for the response
    
        if (!response.ok) {
          // status code was not 200, error status code
          throw new Error(
              "fetch error : " + response.status + " : " + response.statusText
          );
        }
    
        const objects = await response.json(); // json() returns a promise => we wait for the data
  
        const divObjectOffer = document.querySelector("#ObjectNotReceived")
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
    
          const responseImage = await fetch(
            "/api/objects/picture/" + objects[index].idObjet); // fetch return a promise => we wait for the response
  
        if (!responseImage.ok) {
          // status code was not 200, error status code
          throw new Error(
              "fetch error : " + responseImage.status + " : "
              + responseImage.statusText
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
          href.dataset.uri = "/MyObjectsReceivedDetails";
          href.dataset.id = objects[index].idObjet;
          href.className = "button-more-details";
          //set the object in the uri
          href.dataset.uri += "?id=" + objects[index].idObjet;
    
          newDiv.appendChild(href);
          if (objects[index].state == "Annulé") {
            const notificationMessage = document.createElement("p");
            notificationMessage.innerHTML = "L'offre a été annulée";
            notificationMessage.style.color = "red";
            newDiv.appendChild(notificationMessage);
          }
          newDiv.dataset.ObjectId = objects[index].idObjet;
          tbody.appendChild(newDiv);
          cardWrapper.appendChild(tbody);
          cardWrapper1.appendChild(cardWrapper);
    
        };
        divObjectOffer.appendChild(cardWrapper2);
        divObjectOffer.innerHTML += "<br></br>";
    
    
      } catch (error) {
        console.error("MyObjectNotReceivedView::error: ", error);
      }
  };
  
  export default GiftReceived;