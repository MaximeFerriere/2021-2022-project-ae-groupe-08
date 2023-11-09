import {getSessionObject} from "../../utils/session";
import {Redirect} from "../Router/Router";

/**
 * Render the myObjects
 */

const MyObjects = async () => {
  const pageDiv = document.querySelector("#content");
  pageDiv.innerHTML = ``;

  pageDiv.innerHTML = `<h1>Mes Offres</h1>
        <form autocomplete="off" class="d-flex" >
          <div class="autocomplete">
            <input class="form-control" name="search" id="myInput" type="txt" placeholder="Search" aria-label="Search">
          </div>
            <button class="btn btn-outline-success" id="searchSubmit" type="button">Search</button>
        </form>
  `;
  let listOfOBject;
  try {

    let user = getSessionObject("user"); //get the user from the session
    const options = {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      headers: {
        "Authorization": user.token,
      },
    };
    const response = await fetch("/api/objects/myObjects", options); // fetch return a promise => we wait for the response

    if (!response.ok) {
      // status code was not 200, error status code
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }

    const objects = await response.json(); // json() returns a promise => we wait for the data
    listOfOBject = objects;
    const cardWrapper2 = document.createElement("div");
    cardWrapper2.className = "card-deck1";

    for (let index = 0; index < objects.length; index++) {

      const cardWrapper1 = document.createElement("div");
      cardWrapper1.className = "card-deck";
      cardWrapper2.appendChild(cardWrapper1);

      //cardWrapper1.appendChild(href);
      const cardWrapper = document.createElement("div");
      cardWrapper.className = "card";
      cardWrapper.style = "width: 20rem";
      //cardWrapper1.appendChild(href);
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

      nameCell.innerHTML = `Nom de l'objet : `;
      nameCell.innerHTML += objects[index].name;
      nameCell.innerHTML += "<br >";
      nameCell.innerHTML += "\n Type de l'objet : ";
      nameCell.innerHTML += objects[index].typeStr;
      nameCell.innerHTML += "<br >";
      nameCell.innerHTML += "\n Nombre d'intéressés : ";
      nameCell.innerHTML += objects[index].numberOfPeopleInterested;
      nameCell.className = "text-body";
      newDiv.appendChild(nameCell);

      const href = document.createElement("input");
      href.type = "button";
      href.id = "clickable";
      href.value = "Plus de details";
      href.dataset.uri = "/MyObjectDetails";
      href.dataset.id = objects[index].idObjet;
      href.className = "button-more-details";
      //
      href.dataset.uri += "?id=" + objects[index].idObjet;

      href.addEventListener("click", () => {
        Redirect("/");
      });

      newDiv.appendChild(href);
      if (objects[index].state == "Receveur désactivé") {
        const messageRed = document.createElement("p");
        messageRed.id = "messageRouge";
        messageRed.innerHTML += "Attention nouveau receveur à choisir!"
        newDiv.appendChild(messageRed);
      }
      newDiv.dataset.ObjectId = objects[index].idObjet;
      tbody.appendChild(newDiv);
      cardWrapper.appendChild(tbody);
      cardWrapper1.appendChild(cardWrapper);
    }
    ;

    // add the HTMLTableElement to the main, within the #page div
    pageDiv.appendChild(cardWrapper2);
    pageDiv.innerHTML += "<br></br>";

  } catch (error) {
    console.error("MyObjectView::error: ", error);
  }

  //search bar

  autocomplete(document.getElementById("myInput"), listOfOBject);
  const inpu = document.querySelector("#myInput");

  function autocomplete(inp, arr) {
    /*the autocomplete function takes two arguments,
    the text field element and an array of possible autocompleted values:*/
    var currentFocus;
    /*execute a function when someone writes in the text field:*/
    inp.addEventListener("input", function (e) {
      var a, b, i, val = this.value;
      /*close any already open lists of autocompleted values*/
      closeAllLists();
      if (!val) {
        return false;
      }
      currentFocus = -1;
      /*create a DIV element that will contain the items (values):*/
      a = document.createElement("DIV");
      a.setAttribute("id", this.id + "autocomplete-list");
      a.setAttribute("class", "autocomplete-items");
      /*append the DIV element as a child of the autocomplete container:*/
      this.parentNode.appendChild(a);
      /*for each item in the array...*/
      for (i = 0; i < arr.length; i++) {
        /*check if the item starts with the same letters as the text field value:*/
        if (arr[i].typeStr.substr(0, val.length).toUpperCase()
            == val.toUpperCase()) {
          /*create a DIV element for each matching element:*/
          b = document.createElement("DIV");
          /*make the matching letters bold:*/
          b.innerHTML = "<strong>" + arr[i].typeStr.substr(0, val.length)
              + "</strong>";
          b.innerHTML += arr[i].typeStr.substr(val.length);
          /*insert a input field that will hold the current array item's value:*/
          b.innerHTML += "<input type='hidden' value='" + arr[i].typeStr
              + "'>";
          /*execute a function when someone clicks on the item value (DIV element):*/
          b.addEventListener("click", function (e) {
            /*insert the value for the autocomplete text field:*/
            inp.value = this.getElementsByTagName("input")[0].value;
            /*close the list of autocompleted values,
            (or any other open lists of autocompleted values:*/
            closeAllLists();
          });
          a.appendChild(b);
        }

      }
    });
    /*execute a function presses a key on the keyboard:*/
    inp.addEventListener("keydown", function (e) {
      var x = document.getElementById(this.id + "autocomplete-list");
      if (x) {
        x = x.getElementsByTagName("div");
      }
      if (e.keyCode == 40) {
        /*If the arrow DOWN key is pressed,
        increase the currentFocus variable:*/
        currentFocus++;
        /*and and make the current item more visible:*/
        addActive(x);
      } else if (e.keyCode == 38) { //up
        /*If the arrow UP key is pressed,
        decrease the currentFocus variable:*/
        currentFocus--;
        /*and and make the current item more visible:*/
        addActive(x);
      } else if (e.keyCode == 13) {
        /*If the ENTER key is pressed, prevent the form from being submitted,*/
        e.preventDefault();
        if (currentFocus > -1) {
          /*and simulate a click on the "active" item:*/
          if (x) {
            x[currentFocus].click();
          }
        }
      }
    });

    function addActive(x) {
      /*a function to classify an item as "active":*/
      if (!x) {
        return false;
      }
      /*start by removing the "active" class on all items:*/
      removeActive(x);
      if (currentFocus >= x.length) {
        currentFocus = 0;
      }
      if (currentFocus < 0) {
        currentFocus = (x.length - 1);
      }
      /*add class "autocomplete-active":*/
      x[currentFocus].classList.add("autocomplete-active");
    }

    function removeActive(x) {
      /*a function to remove the "active" class from all autocomplete items:*/
      for (var i = 0; i < x.length; i++) {
        x[i].classList.remove("autocomplete-active");
      }
    }

    function closeAllLists(elmnt) {
      /*close all autocomplete lists in the document,
      except the one passed as an argument:*/
      var x = document.getElementsByClassName("autocomplete-items");
      for (var i = 0; i < x.length; i++) {
        if (elmnt != x[i] && elmnt != inp) {
          x[i].parentNode.removeChild(x[i]);
        }
      }
    }

    /*execute a function when someone clicks in the document:*/
    document.addEventListener("click", function (e) {
      closeAllLists(e.target);
    });
  }

  const sbton = document.querySelector("#searchSubmit");

  sbton.addEventListener("click", async function (e) {
    pageDiv.innerHTML = "";
    pageDiv.innerHTML = `<h1>Mes Offres</h1>`

    try {
      let user = getSessionObject("user");
      const options = {
        method: "GET",
        headers: {
          Authorization: user.token,
        },
      };
      const response = await fetch(
          "/api/objects/searchObject?search=" + inpu.value, options); // fetch return a promise => we wait for the response

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
        if(objects[index].usernameOfferor==user.login){
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
              "/api/objects/picture/" + objects[index].idObject); // fetch return a promise => we wait for the response

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
          //urlPt

          //end
          cardWrapper.appendChild(img);
          const newDiv = document.createElement("div");
          const nameCell = document.createElement("p");
          nameCell.innerHTML = "Nom de l'objet : ";
          nameCell.innerHTML += objects[index].nomObject;
          nameCell.innerHTML += "<br >";
          nameCell.innerHTML += "Type de l'objet : ";
          nameCell.innerHTML += objects[index].wordingType;
          nameCell.innerHTML += "<br >";
          nameCell.innerHTML += "Nombre d'interresé : ";
          nameCell.innerHTML += objects[index].numberOInterest;
          newDiv.appendChild(nameCell);

          const href = document.createElement("input");
          href.type = "button";
          href.id = "clickable";
          href.value = "Plus de details";
          href.dataset.uri = "/MyObjectDetails";
          href.dataset.id = objects[index].idObject;
          //
          href.dataset.uri += "?id=" + objects[index].idObject;

          href.addEventListener("click", () => {
            Redirect("/");
          });

          newDiv.appendChild(href);
          newDiv.dataset.ObjectId = objects[index].idObject;
          tbody.appendChild(newDiv);
          cardWrapper.appendChild(tbody);
          cardWrapper1.appendChild(cardWrapper);
        }
      }
      ;

      // add the HTMLTableElement to the main, within the #page div
      pageDiv.appendChild(cardWrapper2);
      pageDiv.innerHTML += "<br></br>";

    } catch (error) {
      console.error("HomePageView::error: ", error);
    }
  });
};

export default MyObjects;
