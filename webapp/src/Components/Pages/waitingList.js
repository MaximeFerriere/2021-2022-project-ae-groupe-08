import {Redirect} from "../Router/Router";
import {getSessionObject} from "../../utils/session";

/**
 * Render the waitingList
 */

const waitingList = async () => {
  const pageDiv = document.querySelector("#content");

  let refused = false;
  pageDiv.innerHTML = "";
  pageDiv.innerHTML = `<h3>Validation d'inscription</h3>
  <hr>`;

  //new register button
  const newregister = document.createElement("input");
  newregister.type = "button";
  newregister.value = "Nouveau inscrit";
  newregister.dataset.uri = "/waitingList";
  newregister.className = "button_top";
  newregister.addEventListener("onclick", () => {
    refused = false;
  });

  pageDiv.appendChild(newregister);

  //denied list button
  const refusedList = document.createElement("input");
  refusedList.type = "button";
  refusedList.value = "Liste des refus";
  refusedList.dataset.uri = "/deniedList";
  refusedList.className = "button_top";
  refusedList.addEventListener("onclick", () => {
    refused = true;
  });

  pageDiv.appendChild(refusedList);

  try {
    let user = getSessionObject("user"); //get the user from the session
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        "Authorization": user.token,
      },
    };
    const response = await fetch("/api/users/waitingList?type-list=waiting",
        options);
    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + ":" + response.statusText
      );
    }

    const objects = await response.json();
    const tableWrapper = document.createElement("div");
    tableWrapper.className = "table-responsive pt-5";

    // create an HTMLTableElement dynamically, based on the pizzas data (Array of Objects)
    const table = document.createElement("table");
    table.className = "table";
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

    const header8 = document.createElement("th");
    header8.innerText = "Refuser";

    header.appendChild(header1);
    header.appendChild(header2);
    header.appendChild(header3);
    header.appendChild(header4);
    header.appendChild(header5);
    header.appendChild(header6);
    header.appendChild(header7);
    header.appendChild(header8);
    table.appendChild(thead);

    //body

    const tbody = document.createElement("tbody");
    objects.forEach((object) => {
      //user
      const line = document.createElement("tr");

      //lastname
      const lastNameCell = document.createElement("td");
      lastNameCell.innerText = object.lastName;
      line.appendChild(lastNameCell);

      //firstname
      const firstnameCell = document.createElement("td");
      firstnameCell.innerText = object.firstName;
      line.appendChild(firstnameCell);

      //username
      const userNameCell = document.createElement("td");
      userNameCell.innerText = object.username;
      line.appendChild(userNameCell);

      //callNumber
      const phoneCell = document.createElement("td");
      if (!(object.callNumber == null)) {
        phoneCell.innerText = object.callNumber;
      } else {
        phoneCell.innerText = "non renseigné";
      }
      line.appendChild(phoneCell);

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

      const input = document.createElement("input");
      input.type = "checkbox";
      input.id = "isAdmin";
      input.name = "isAdmin";
      adminbox.appendChild(input);
      line.appendChild(adminbox);

      //confirm
      const confirm = document.createElement("td");

      const inputconf = document.createElement("input");
      inputconf.value = "confirm";
      inputconf.type = "submit";
      inputconf.className = "btn btn-success";
      inputconf.innerText += "Confirmer";
      confirm.appendChild(inputconf);
      line.appendChild(confirm);

      //refuse
      const refuse = document.createElement("td");

      const inputrefused = document.createElement("span");
      inputrefused.value = "Refuser";
      inputrefused.dataset.id = object.username;
      //inputrefused.type = "button";
      //inputrefused.className = "btn btn-danger";
      //inputrefused.innerText += "Refuser";
      inputrefused.innerHTML = `
  <div class="modal_fade"  tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Raison du refus</h5>
      </div>
      <div class="modal-body">
        <form>
          <div class="mb-3">
            <label for="message-text" class="col-form-label">Message:</label>
            <textarea class="form-control" ></textarea>
          </div>
        </form>
      </div>
      <div class="modal-footer" id="modal-footer">
      </div>
    </div>
  </div>
</div>`;

      const text = inputrefused.querySelector("#exampleModalLabel");
      text.innerHTML += " " + object.username;
      const submitRefuse = inputrefused.querySelector("#modal-footer");
      submitRefuse.dataset.id = object.username;
      const buttonRefused = document.createElement("button");
      buttonRefused.type = "button";
      buttonRefused.className = "btn btn-danger";
      buttonRefused.id = "refusedUser";
      buttonRefused.dataset.version = object.versionMember;
      buttonRefused.innerText = "Refuser";
      buttonRefused.dataset.id = inputrefused.dataset.id;

      submitRefuse.appendChild(buttonRefused);

      const modalBody = inputrefused.querySelector(".modal_fade");
      modalBody.dataset.id = "var_div" + object.username;

      refuse.appendChild(inputrefused);

      //document.getElementById("var_div").setAttribute("id",object.username);

      line.appendChild(refuse);

      line.appendChild(form);

      //confirm fonction --------------------------------------
      inputconf.addEventListener("click", async (e) => {
        e.preventDefault();

        const username = object.username;
        const versionMember = object.versionMember

        const option = {
          method: "POST", // *GET, POST, PUT, DELETE, etc.
          body: JSON.stringify({
            username: username,
            versionMember: versionMember,
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
        Redirect("/waitingList");
      });
      //-------------------------------------------------------------------

      let RefusText = inputrefused.querySelector(".form-control");
      RefusText.id += "message-text" + object.username;
      //refuse fonction --------------------------------------------
      //let buttonRefused = inputrefused.querySelector("#refusedUser")

      buttonRefused.addEventListener("click", async (e) => {
        e.preventDefault();
        const username = buttonRefused.dataset.id;
        const reason_for_connection_refusal = document.querySelector(
            "#message-text" + object.username);
        const versionMemberRefused = buttonRefused.dataset.version;
        const optionss = {
          method: "POST", // *GET, POST, PUT, DELETE, etc.
          body: JSON.stringify({
            username: username,
            reason_for_connection_refusal: reason_for_connection_refusal.value,
            versionMember: versionMemberRefused
          }), // body data type must match "Content-Type" header
          headers: {
            "Content-Type": "application/json",
            "Authorization": user.token,
          },
        };

        const reponse = await fetch("/api/users/refuse", optionss);
        if (!response.ok) {
          throw new Error(
              "fetch error : " + response.status + " : " + response.statusText
          );
        }
        Redirect("/waitingList");
      });
      //--------------------------------------------------------------

      tbody.appendChild(line);
    });

    table.appendChild(tbody);
    // add the HTMLTableElement to the main, within the #page div
    pageDiv.appendChild(tableWrapper);
  } catch (error) {
    console.error("waitingList::error: ", error);
  }
};

export default waitingList;
