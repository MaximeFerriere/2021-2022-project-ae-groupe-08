import {API_URL} from "../../utils/server";
import {getSessionObject} from "../../utils/session";
import likeIcon from "../../img/like.png";

let offer = `
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
<head xmlns="http://www.w3.org/1999/html">
    <title>Offer Page</title>
</head>
<body>

<div class="container">
        <div class="title">
            <h3>Nouvelle offre</h3>
        </div>
        <div class="content">
            <form id ="formrOffer" class="p-5" >
            <div class="offer-details">
                <div class="input-box" id="group_input">
                    <div class="input-group-prepend">
                        <span class="details">Nom de l'offre</span>
                    </div>
                    <input id="offer_name" type="text" class="form-control" placeholder="Nom de l'offre" required=true>
                </div>

                <div class="input-box" id="group_input">
                    <div class="input-group-prepend">
                        <span class="details">Description de l'offre</span>
                    </div>
                    <input id="description" type="text" class="form-control" placeholder="Description de l'offre" required=true>
                </div>
                
                <div class="input-box" id="group_input">
                    <div class="input-group-prepend">
                        <span class="details">type de l'objet</span>
                    </div>
                    <div class="select">
                        <select id="typestypes" name="typestypes">
                        </select>
                    </div>
                    <! –– <input id="object_type" type="submit" class="form-control" placeholder="Type de l'objet"> 
                </div>
                
                <div class="input-box" id="group_input">
                    <div class="input-group-prepend">
                        <span class="details">Dates de disponibilites</span>
                    </div>
                    <input id="dates" type="text" class="form-control" placeholder="Dates de disponibilites" required=true>
                </div>


                <div class="input-box" id="group_input">
                    <div class="input-group-prepend">
                        <span class="details">Créer un nouveau type</span>
                    </div>
                    <input id="newType" type="text" class="form-control" placeholder="Créer un nouveau type" >
                </div>
                
                <div class="buttonCreateType" >
                    <div class="buttonOffer">
                    <input id="buttonCreateType" type="button" value="Créer Type" class="btn btn-dark">
                    </div>
                </div>
                <span id="span"></span>
              <div class = "select-button">
                <form>
                  <label>Select File</label>
                  <input name="file" type= "file" /> <br/><br/>
                </form>

                <div class="buttonOfferCont">
                    <div class="buttonOffer">
                        <input type="submit" value="Créer l'offre">
                    </div>
                    <span id="textRefus"></span>
                </div>
              </div>
                <span id="span"></span>
            </form>
        </div>
    </div>
</div>
</body>`

async function OfferCreatePage() {

  const pageDiv = document.querySelector("#content");
  pageDiv.innerHTML = offer;
  //const txtRefusal=document.querySelector("#span");

  const typeList = document.querySelector("#typestypes");

  const response = await fetch("/api/objects/getAllTypes"); // fetch return a promise => we wait for the response
  if (!response.ok) {
    // status code was not 200, error status code
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    );
  }
  const rsponse = await response.json(); // json() returns a promise => we wait for the data
  // create a wrapper to provide a responsive table
  let i = 0;
  //affichage des des types
  rsponse.forEach((type) => {
    const opt = document.createElement("option");
    opt.value = rsponse[i];
    opt.innerHTML = rsponse[i];
    i++;
    typeList.appendChild(opt);
  })
  const txtRefusal = document.querySelector("#textRefus");

  const buttonCreateType = document.querySelector("#buttonCreateType");

  buttonCreateType.addEventListener("click", async (e) => {
    e.preventDefault;
    const newType = document.getElementById("newType");
    let user = getSessionObject("user");

    try {
      const options = {
        method: "POST",
        body: JSON.stringify({
          typeName: newType.value,
        }), // body data type must match "Content-Type" header
        headers: {
          "Content-Type": "application/json",
          "Authorization": user.token,
        },
      };

      const response = await fetch("/api/objects/createType", options);

      if (!response.ok) {

        txtRefusal.innerHTML = `<br> Message d'erreur : <br>`;
        txtRefusal.innerHTML += "Echec de la création du nouveau type";

        throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
        );
      }
      txtRefusal.innerHTML = "Le nouveau type a bien été créé, veuillez rafraichire la page pour utiliser ce nouveau type";

    } catch (error) {
      console.error("OfferPage::error: ", error);
    }

  });

  addEventListener("submit", onSubmit);

  async function onSubmit(e) {
    e.preventDefault();
    const offer_name = document.getElementById("offer_name");
    const description = document.getElementById("description");
    const type = document.getElementById("typestypes");
    const date = document.getElementById("dates");
    //const image = document.getElementById("IMAGE");
    let user = getSessionObject("user");
    var strType = type.value;
    try {
      const options = {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify({
          offer_name: offer_name.value,
          description: description.value,
          type: strType,
          date: date.value,
          image: "R",
        }), // body data type must match "Content-Type" header
        headers: {
          "Content-Type": "application/json",
          "Authorization": user.token,
        },
      };

      const response = await fetch(API_URL + "objects/createObject", options);

      if (!response.ok) {

        txtRefusal.innerHTML = `<br> Message d'erreur : <br>`;
        txtRefusal.innerHTML += "Veuillez completer toutes les cases";

        throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
        );
      }

      const itemId = await response.text()
      const fileInput = document.querySelector('input[name=file]');
      const formData = new FormData();
      formData.append('file', fileInput.files[0]);
      formData.append('itemId',itemId);
      const optionsimg = {
        method: 'POST', 
        body: formData
      };
      const responseUpload = await fetch(API_URL + "objects/upload/"+itemId, optionsimg);
      if (!response.ok) {

        txtRefusal.innerHTML = `<br> Message d'erreur : <br>`;
        txtRefusal.innerHTML += "Echec de l'upload de l'image";

        throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
        );
      }

      
      txtRefusal.innerHTML = "Votre offre a bien ete créée";

    } catch (error) {
      console.error("OfferPage::error: ", error);
    }
  }

}

export default OfferCreatePage;