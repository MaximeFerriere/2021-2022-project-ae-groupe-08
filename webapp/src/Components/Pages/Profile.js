import {API_URL} from "../../utils/server";
import {getSessionObject} from "../../utils/session";
//import userIcon from "../../img/user.png";

//register html page
let idAdress;
let idMember;
let register = `<head xmlns="http://www.w3.org/1999/html">
<title>Profile Page</title>
</head>
<body>
<div class="containerForRegister">
<div class = "space-b-c"></div>

  <div class="container">
    <div class="title">
      <h3>Profil</h3>
    </div>
      <div class="content">
        <form id ="formrOffer" class="p-5">
          <div class="offer-details">
            <div class="input-box" id="group_input">
                      
                <input id="firstname" type="text" class="form-control" placeholder="Prénom" required=true>
            </div>

            <div class="input-box" id="group_input">
                <input id="lastname" type="text" class="form-control" placeholder="Nom de famille" required=true>
            </div>

            <div class="input-box" id="group_input">
                <input id="username" type="text" class="form-control" placeholder="Pseudo" required=true>
            </div>

            <div class="input-box" id="group_input">        
                <input id="phone_number" type="text" class="form-control" placeholder="Numéro de téléphone">
            </div>

            <div class="input-box" id="group_input">
                <input id="commune" type="text" class="form-control" placeholder="Commune" required=true>
            </div>

            <div class="input-box" id="group_input">
                <input id="post_number" type="text" class="form-control" placeholder="Code postal" required=true>
            </div>

            <div class="input-box" id="group_input">
                <input id="street" type="text" class="form-control" placeholder="Rue" required=true>
            </div>

            <div class="input-box" id="group_input">
                <input id="building_number" type="text" class="form-control" placeholder="Numéro" required=true>
            </div>

            <div class="input-box" id="group_input">
                <input id="unit_number" type="text" class="form-control" placeholder="Numéro de boite" required=true>
            </div>
          </div>
            <div class="buttonOffer">
                <input type="button" id="Sauvegarder" value="Sauvegarder">
              <span id="span"></span> 
            </div>
        </form>
    </div>
  </div>

  <div class = "space-b-c"></div>

  <div class="container">
    <div class="title">
      <h3>Changer de mot-de-passe</h3>
    </div>
      <div class="content">
        <form id ="formrOffer2" class="p-5">
          <div class="offer-details">

            <div class="input-box" id="group_input">
                <input id="password1" type="password" class="form-control" placeholder="mot-de-passe" required=true>
            </div>
            <div class="input-box" id="group_input">
                <input id="password2" type="password" class="form-control" placeholder="mot-de-passe" required=true>
            </div>
            
          </div>
            <div class="buttonOffer">
                <input type="button" id="changePwd" value="Confirmer">
              <span id="span"></span> 
            </div>
        </form>
        <p id="errormsg"></p>
    </div>
  </div>
</div>

</body>`;

//get all the user data and send it to the backend and into the database
async function ProfilePage() {
  //the main page of the register page
  const pageDiv = document.querySelector("#content");
  pageDiv.innerHTML = register;
  //get the reason why it is refused
  const txtRefusal = document.querySelector("#span");

  try {
    let user = getSessionObject("user"); //get the user from the session
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        "Authorization": user.token,
      },
    };
    const response = await fetch("/api/users/profile?username=" + user.login,
        options);
    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + ":" + response.statusText
      );
    }
    const objects = await response.json();

    const username = document.getElementById("username");
    const firstname = document.getElementById("firstname");
    const lastname = document.getElementById("lastname");
    const callNumber = document.getElementById("phone_number");
    const commune = document.getElementById("commune");
    const post_number = document.getElementById("post_number");
    const building_number = document.getElementById("building_number");
    const unit_number = document.getElementById("unit_number");
    const street = document.getElementById("street");
    username.value = objects.username;
    firstname.value = objects.firstName;
    lastname.value = objects.lastName;
    callNumber.value = objects.callNumber;
    commune.value = objects.address.commune;
    post_number.value = objects.address.postcode;
    if (objects.address.buildingNumber == 0) {
      building_number.value = "";

    } else {
      building_number.value = objects.address.buildingNumber;
    }
    street.value = objects.address.street;
    unit_number.value = objects.address.unitNumber;
    idAdress = objects.address.id;
    idMember = objects.id;
  } catch (error) {
    console.error("Profile::error: ", error);
  }

  const save = document.getElementById("Sauvegarder");
  save.addEventListener("click", onSubmit);

  //get all the input and send it to the backend
  async function onSubmit(e) {
    e.preventDefault();

    const username = document.getElementById("username");
    const firstname = document.getElementById("firstname");
    const lastname = document.getElementById("lastname");
    const callNumber = document.getElementById("phone_number");

    const commune = document.getElementById("commune");
    const post_number = document.getElementById("post_number");
    const building_number = document.getElementById("building_number");
    const unit_number = document.getElementById("unit_number");
    const street = document.getElementById("street");
    let callN;
    if (callNumber.value === undefined) {
      callN = null;
    } else {
      callN = callNumber.value;
    }

    let buildN;
    if (callNumber.value === undefined) {
      buildN = null;
    } else {
      buildN = building_number.value;
    }
    let user = getSessionObject("user");
    try {
      //send the data to the backend
      const options = {
            method: "POST", // *GET, POST, PUT, DELETE, etc.
            body: JSON.stringify({
              idAdress: idAdress,
              idMember: idMember,
              username: username.value,
              call_number: callN,
              firstname: firstname.value,
              lastname: lastname.value,
              street: street.value,
              building_number: buildN,
              unit_number: unit_number.value,
              postcode: post_number.value,
              commune: commune.value,
            }), // body data type must match "Content-Type" header
            headers: {
              "Content-Type": "application/json",
              "Authorization": user.token,
            }
            ,
          }
      ;

      const response = await fetch(API_URL + "users/editprofile", options); // fetch return a promise => we wait for the response

      if (!response.ok) {
        txtRefusal.innerHTML = `<br> Message d'erreur : <br>`;
        txtRefusal.innerHTML += "Pseudo déjà utilisé";

        throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
        );
      }
      txtRefusal.innerHTML =
          "Modification effectuée";
    } catch (error) {
      console.error("ProfilePage::error: ", error);
    }
  }

  //Change Password

  const pwd1 = document.querySelector("#password1");
  const pwd2 = document.querySelector("#password2");
  const errormsg = document.querySelector("#errormsg")

  const change = document.getElementById("changePwd");
  change.addEventListener("click", onSubmit2);

  //get all the input and send it to the backend
  async function onSubmit2(e) {
    e.preventDefault();
    if (pwd1.value != pwd2.value) {
      errormsg.innerHTML = "Les deux mots de passe ne correspondent pas";
    } else {
      const password = document.getElementById("password1");
      let user = getSessionObject("user");
      try {
        //send the data to the backend
        const options = {
              method: "POST", // *GET, POST, PUT, DELETE, etc.
              body: JSON.stringify({
                password: password.value,
              }), // body data type must match "Content-Type" header
              headers: {
                "Content-Type": "application/json",
                "Authorization": user.token,
              }
              ,
            }
        ;

        const response = await fetch(API_URL + "users/changePwd", options); // fetch return a promise => we wait for the response

        if (!response.ok) {
          throw new Error(
              "fetch error : " + response.status + " : " + response.statusText
          );
        }
        errormsg.innerHTML =
            "Mot de passe modifié avec succès";
      } catch (error) {
        console.error("ProfilePage::error: ", error);
      }

    }
  }
}

export default ProfilePage;
