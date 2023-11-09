import {Redirect} from "../Router/Router";
import {getSessionObject} from "../../utils/session";

const deniedList = async () => {
  const pagemain = document.querySelector("#page");
  pagemain.innerHTML = "";
  //the pageDiv is where all the content will be displayed
  const pageDiv = document.querySelector("#content");
  pageDiv.innerHTML = ``;

  pageDiv.innerHTML = `<h3>Liste des refusés</h3>
  <hr>
 
  `;

  //new register button
  const newregister = document.createElement("input");
  newregister.type = "button";
  newregister.value = "Nouveau inscrit";
  newregister.dataset.uri = "/waitingList";
  newregister.className = "button_top";
  /*newregister.addEventListener("onclick", () => {
    waitingList({refused: false});
  });*/

  pageDiv.appendChild(newregister);

  //denied list button
  const refusedList = document.createElement("input");
  refusedList.type = "button";
  refusedList.value = "Liste des refus";
  refusedList.dataset.uri = "/deniedList";
  refusedList.className = "button_top";
  /*refusedList.addEventListener("onclick", () => {
    waitingList({refused: true});
  });*/

  pageDiv.appendChild(refusedList);
  try {
    let user = getSessionObject("user"); //get the user from the session
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        "Authorization": user.token,
      },
    };
    const response = await fetch("/api/users/waitingList?type-list=denied",
        options); // fetch return a promise => we wait for the response

    if (!response.ok) {
      // status code was not 200, error status code
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }

    const objects = await response.json(); // json() returns a promise => we wait for the data
    const tableWrapper = document.createElement("div");
    tableWrapper.className = "table-responsive pt-5";

    // create an HTMLTableElement dynamically, based on the pizzas data (Array of Objects)
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
    header3.innerText = "Pseudo";

    const header4 = document.createElement("th");
    header4.innerText = "Numero";

    const header5 = document.createElement("th");
    header5.innerText = "Adresse";

    const header6 = document.createElement("th");
    header6.innerText = "Admin";

    const header7 = document.createElement("th");
    header7.innerText = "Valider";

    header.appendChild(header1);
    header.appendChild(header2);
    header.appendChild(header3);
    header.appendChild(header4);
    header.appendChild(header5);
    header.appendChild(header6);
    header.appendChild(header7);
    table.appendChild(thead);

    //body
    const tbody = document.createElement("tbody");
    objects.forEach((object) => {
      //user
      const line = document.createElement("tr");

      //firstName
      const titleCell = document.createElement("td");
      titleCell.innerText = object.firstName;
      line.appendChild(titleCell);

      //lastName
      const descriptionCell = document.createElement("td");
      descriptionCell.innerText = object.lastName;
      line.appendChild(descriptionCell);

      //userName
      const EtatCell = document.createElement("td");
      EtatCell.innerText = object.username;
      line.appendChild(EtatCell);

      //callNumber
      const OffreurCell = document.createElement("td");
      if (!(object.callNumber == null)) {
        OffreurCell.innerText = object.callNumber;
      } else {
        OffreurCell.innerText = "non renseigné";
      }
      line.appendChild(OffreurCell);

      //address-------------------------------------
      const DateCell = document.createElement("td");

      //street
      DateCell.innerText = object.address.street;
      DateCell.innerText += " N°";

      //building number (building or house)
      DateCell.innerText += object.address.buildingNumber;

      //unitNumber (if this is a building)
      if (!(object.address.unitNumber == null)) {
        DateCell.innerText += " Boite ";
        DateCell.innerText += object.address.unitNumber;
      }
      DateCell.innerText += ", ";

      //postcode
      DateCell.innerText += object.address.postcode;
      DateCell.innerText += " ";

      //commune
      if (!(object.address.commune == null)) {
        DateCell.innerText += object.address.commune;
      } else {
        DateCell.innerText += "Bruxelles";
      }

      line.appendChild(DateCell);
      //end address---------------------------------------

      const form = document.createElement("form");
      form.name = object.username;

      //Admin
      const adminbox = document.createElement("td");
      line.appendChild(adminbox);
      const input = document.createElement("input");
      input.type = "checkbox";
      input.id = "isAdmin";
      input.name = "isAdmin";
      adminbox.appendChild(input);

      //confirm
      const confirm = document.createElement("td");

      const inputconf = document.createElement("input");
      inputconf.value = "confirm";
      inputconf.type = "submit";
      inputconf.className = "btn btn-success";
      inputconf.innerText += "Confirmer";
      confirm.appendChild(inputconf);
      line.appendChild(confirm);

      //confirm fonction --------------------------------------
      inputconf.addEventListener("click", async (e) => {
        e.preventDefault();

        const username = object.username;
        let user = getSessionObject("user"); //get the user from the session

        const option = {
          method: "POST", // *GET, POST, PUT, DELETE, etc.
          body: JSON.stringify({
            username: username,
          }), // body data type must match "Content-Type" header
          headers: {
            "Content-Type": "application/json",
            "Authorization": user.token,
          },
        };

        const reponse = await fetch("/api/users/accept", option);
        if (!response.ok) {
          throw new Error(
              "fetch error : " + response.status + " : " + response.statusText
          );
        }

        const cb = document.querySelector("#isAdmin");

        if (input.checked) {
          const response2 = await fetch("/api/users/AddAnAdmin", option);

          if (!response2.ok) {
            throw new Error(
                "fetch error : " + response.status + " : " + response.statusText
            );
          }
        }
        Redirect("/deniedList");
      });

      // hide info within each row, the pizza id
      line.dataset.pizzaId = object.id;
      tbody.appendChild(line);
    });

    table.appendChild(tbody);
    // add the HTMLTableElement to the main, within the #page div
    pageDiv.appendChild(tableWrapper);
  } catch (error) {
    console.error("HomePageView::error: ", error);
  }
};

export default deniedList;
