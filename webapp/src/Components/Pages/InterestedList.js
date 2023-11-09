import {getSessionObject} from "../../utils/session";

const interestedList = async () => {
  const pagemain = document.querySelector("#page");
  pagemain.innerHTML = "";
  const pageDiv = document.querySelector("#content");
  pageDiv.innerHTML = ``;

  pageDiv.innerHTML = `<h3>Liste des intéressés</h3>
    <hr>
    <div id="tableaux"></div>
    <span id=ErrorRecipient></span>
    `;
  //get the url of the current page
  var url = new URL(window.location.href);
  var search_params = new URLSearchParams(url.search); //get the object of the url
  var id = search_params.get('id'); // get the id from the object in the url

  try {
    //send the data to the backend
    let user = getSessionObject("user"); //get the user from the session
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        "Authorization": user.token,
      },
    };
    const response = await fetch("/api/users/interestedMembers?idObject="+id, options); // fetch return a promise => we wait for the response

    if (!response.ok) {
      // status code was not 200, error status code
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }

    const objects = await response.json(); // json() returns a promise => we wait for the data
    //create a div in the html
    const tableWrapper = document.createElement("div");
    tableWrapper.className = "table-responsive pt-5";
    //create a table in the div
    const table = document.createElement("table");
    table.className = "table table";
    tableWrapper.appendChild(table);

    // deal with header
    const thead = document.createElement("thead");
    const header = document.createElement("tr");
    thead.appendChild(header);

    //titles
    const header1 = document.createElement("th");
    header1.innerText = "Nom";

    const header2 = document.createElement("th");
    header2.innerText = "Prenom";

    const header3 = document.createElement("th");
    header3.innerText = "Nécessité d'une discussion pour les modalités pratiques";

    const header4 = document.createElement("th");
    header4.innerText = "Numero";

    const header5 = document.createElement("th");
    header5.innerText = "Disponibilités";

    const header6 = document.createElement("th");
    header6.innerText = "Actions";

    header.appendChild(header1);
    header.appendChild(header2);
    header.appendChild(header3);
    header.appendChild(header4);
    header.appendChild(header5);
    header.appendChild(header6);
    table.appendChild(thead);

    //body
    const tbody = document.createElement("tbody");
    objects.forEach((object) => {
      //user
      const line = document.createElement("tr");

      //firstName
      const firstnameCell = document.createElement("td");
      firstnameCell.innerText = object.userDTO.firstName;
      line.appendChild(firstnameCell);

      //lastName
      const lastnameCell = document.createElement("td");
      lastnameCell.innerText = object.userDTO.lastName;
      line.appendChild(lastnameCell);

      //userName
      const EtatCell = document.createElement("td");
      if (object.conversationNeeded === true) {
        EtatCell.innerText = "Oui";
      } else {
        EtatCell.innerText = "Non";
      }
      line.appendChild(EtatCell);

      //callNumber
      const callNumberCell = document.createElement("td");
      callNumberCell.innerText = object.userDTO.callNumber;
      line.appendChild(callNumberCell);

      //availability
      const availability = document.createElement("td");
      availability.innerText = object.timeSlots;
      line.appendChild(availability);

      const choseRecipientbox = document.createElement("td");
      line.appendChild(choseRecipientbox);
      const choseRecipientInput = document.createElement("input");
      choseRecipientInput.type = "button";
      choseRecipientInput.id = "choseRecipientButton";
      choseRecipientInput.value = "Choisir comme receveur";
      choseRecipientbox.appendChild(choseRecipientInput);

      line.dataset.userId = object.userDTO.id;
      line.dataset.objectId = id;
      tbody.appendChild(line);

      choseRecipientInput.addEventListener("click", async (e) => {
        e.preventDefault();
        const errorText = document.querySelector("#ErrorRecipient");
        const options = {
          method: "POST",
          body: JSON.stringify({
            idObject: line.dataset.objectId,
            idRecipient: line.dataset.userId,
          }),
          headers: {
            "Content-Type": "application/json",
            "Authorization": user.token,
          },
        };

        const response = await fetch("/api/objects/addRecipient", options);
        if (!response.ok) {
          errorText.innerHTML = "Vous avez déjà choisi un receveur pour cette offre";
          throw new Error(
              "fetch error : " + response.status + " : " + response.statusText
          );
        }

        const result = await response.json();
        if (!result) {
          errorText.innerHTML = "Vous avez déjà choisi un receveur pour cette offre";
        } else {
          errorText.innerHTML = "Le receveur à bien été enregistré !";
        }
      });
    });

    table.appendChild(tbody);
    const tableau = document.querySelector("#tableaux");
    tableau.appendChild(tableWrapper);
    //pageDiv.appendChild(tableWrapper);
    //pageDiv.innerHTML = `<span id=ErrorRecipient></span>`
  } catch (error) {
    console.error("InterestedList::error: ", error);
  }

};

export default interestedList;