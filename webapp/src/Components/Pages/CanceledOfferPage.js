/**
 * Render the CanceledOfferPage
 */
 import {Redirect} from "../Router/Router";
 import {getSessionObject} from "../../utils/session";
 
 const CanceledOfferPage = async () => {
   const pagemain = document.querySelector("#page");
   pagemain.innerHTML = "";
   // pageDiv will contain all the visuel of the page
   const pageDiv = document.querySelector("#content");
   pageDiv.innerHTML = ``;
   // add a filter button to the page
   pageDiv.innerHTML = `<h1>Offres</h1>
 
   <div class=boutonEtc> 
   </div>
   `;
 
   const navButton = document.querySelector(".boutonEtc");
   const newregister = document.createElement("input");
   newregister.type = "button";
   newregister.value = "Retour";
   newregister.dataset.uri = "/homePageMember   ";
   navButton.appendChild(newregister);
   newregister.className = "button_top";
 
   const cardWrapper2 = document.createElement("div");
   cardWrapper2.className = "card-deck1";
 
   try {
     let user = getSessionObject("user"); //get the user from the session
     const options = {
       method: "GET", // *GET, POST, PUT, DELETE, etc.
       headers: {
         "Authorization": user.token,
       },
     };
     const response = await fetch("/api/objects/canceledOffers", options); // fetch return a promise => we wait for the response
 
     if (!response.ok) {
       // status code was not 200, error status code
       throw new Error(
           "fetch error : " + response.status + " : " + response.statusText
       );
     }
 
     const objects = await response.json(); // json() returns a promise => we wait for the data
     // create a wrapper to provide a responsive table
 
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
 
       //start
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
       href.dataset.uri = "/OfferPage";
       href.dataset.id = objects[index].idObjet;
       //set the object in the uri 
       href.dataset.uri += "?id=" + objects[index].idObjet;
       href.className = "button-more-details";
 
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
     pageDiv.appendChild(cardWrapper2);
     pageDiv.innerHTML += "<br></br>";
 
   } catch (error) {
     console.error("HomePageView::error: ", error);
   }
 
   const detail = document.querySelector("#clickable");
 
 };
 
 export default CanceledOfferPage;
 