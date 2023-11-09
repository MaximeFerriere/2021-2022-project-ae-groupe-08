import {API_URL} from "../../utils/server";
//import userIcon from "../../img/user.png";

//register html page
let register = `<head xmlns="http://www.w3.org/1999/html">
<div class = "background-img">
<title>Register Page</title>
</head>
<body>
<div class="containerForRegister">
  <div class="tqt">
    <h3>Bienvenue chez Donamis</h3>
  </div> 
  <div class="container">
    <div class="title">
      <h3>Inscription</h3>
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
                <input id="phone_number" type="text" class="form-control" placeholder="Numéro de téléphone (optionnel)">
            </div>

            <div class="input-box" id="group_input">
                <input id="password" type="password" class="form-control" placeholder="mot-de-passe" required=true>
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
                <input id="building_number" type="text" class="form-control" placeholder="Numéro" required="true">
            </div>

            <div class="input-box" id="group_input">
                <input id="unit_number" type="text" class="form-control" placeholder="Numéro de boite">
            </div>
          </div>
            <div class="buttonOffer">
                <input type="submit" value="Inscription">
              <span id="span"></span> 
            </div>
        </form>
    </div>
  </div>
</div>
</div>
</body>`

//get all the user data and send it to the backend and into the database
function InscriptionPage() {
  //the main page of the register page
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = register;
  //get the reason why it is refused
  const txtRefusal = document.querySelector("#span");

  addEventListener("submit", onSubmit);

  //get all the input and send it to the backend
  async function onSubmit(e) {
    e.preventDefault();
    const username = document.getElementById("username");
    const firstname = document.getElementById("firstname");
    const lastname = document.getElementById("lastname");
    const callNumber = document.getElementById("phone_number");
    const password = document.getElementById("password");
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

    let unitN;
    if (unit_number.value === undefined) {
      unitN = null;
    } else {
      unitN = unit_number.value;
    }

    try {
      //send the data to the backend
      const options = {
        method: "PUT", // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify({
          callNumber: callN,
          username: username.value,
          password: password.value,
          firstname: firstname.value,
          lastname: lastname.value,
          street: street.value,
          building_number: building_number.value,
          unit_number: unitN,
          postcode: post_number.value,
          commune: commune.value,
        }), // body data type must match "Content-Type" header
        headers: {
          "Content-Type": "application/json",
        },
      };

      const response = await fetch(API_URL + "users/register", options);// fetch return a promise => we wait for the response

      if (!response.ok) {

        txtRefusal.innerHTML = `<br> Message d'erreur : <br>`;
        txtRefusal.innerHTML += "Pseudo déjà utilisé";

        throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
        );
      }
      txtRefusal.innerHTML = "Vous êtes bien inscrit, en attente d'une confirmation d'un administrateur";

    } catch (error) {
      console.error("InscriptionPage::error: ", error);
    }

  }
}

export default InscriptionPage;