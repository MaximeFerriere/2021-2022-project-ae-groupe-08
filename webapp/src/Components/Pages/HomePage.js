/**
 * Render the HomePage
 */

const HomePage = async () => {

  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = ``;
  pageDiv.innerHTML = `
  <div class = "container-backgroundimage">


  <div class="container-title">
    <h2 class="title">
      <span class="title-word title-word-1">Bienvenue</span>
      <span class="title-word title-word-2">chez</span>
      <span class="title-word title-word-3">Donamis</span>
      <span class="title-word title-word-4">!</span>
    </h2>
  </div>

  <div class="container-title-2">
    <h2 class="title-2">
      <span class="title-word title-word-1">Dernière</span>
      <span class="title-word title-word-2">offres</span>
      <span class="title-word title-word-3">du</span>
      <span class="title-word title-word-4">moment :</span>
    </h2>
  </div>
  <div class ="small-div">
  </div>
  </div>
  `;
  try {

    const response = await fetch("/api/objects/lastoffers"); // fetch return a promise => we wait for the response

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
    const cardWrapper3 = document.createElement("div");
    cardWrapper3.className = "card-deck1";
    var i = 0;
    for (let index = 0; index < objects.length; index++) {
      //Show five objects on one row and then the next line is in the else
      if (i < 20) {
        i++;
        const cardWrapper1 = document.createElement("div");
        cardWrapper1.className = "card-deck";
        cardWrapper2.appendChild(cardWrapper1);
        const cardWrapper = document.createElement("div");
        cardWrapper.className = "card";
        cardWrapper.style = "width: 22rem";
        cardWrapper1.appendChild(cardWrapper);
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
        nameCell.innerHTML = "Nom de l'objet : "
        nameCell.innerText += objects[index].name;
        nameCell.innerHTML += "\nType de l'objet : "
        nameCell.innerText += objects[index].typeStr;
        nameCell.className = "text-body";
        newDiv.appendChild(nameCell);
        newDiv.dataset.objectId = objects[index].id;
        tbody.appendChild(newDiv);
        cardWrapper.appendChild(tbody);
      } else {
        const cardWrapper1 = document.createElement("div");
        cardWrapper1.className = "card-deck";
        cardWrapper3.appendChild(cardWrapper1);
        const cardWrapper = document.createElement("div");
        cardWrapper.className = "card";
        cardWrapper.style = "width: 22rem";
        cardWrapper1.appendChild(cardWrapper);
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
        img.className = "card-img-top";
        img.src = image; //object.urlPhoto;
        cardWrapper.appendChild(img);
        const newDiv = document.createElement("div");
        const nameCell = document.createElement("p");
        nameCell.innerHTML = "Nom de l'objet : "
        nameCell.innerText += objects[index].name;
        nameCell.innerHTML += "\nType de l'objet : "
        nameCell.innerText += objects[index].typeStr;
        newDiv.appendChild(nameCell);
        // hide info within each row, the object id
        newDiv.dataset.objectId = objects[index].id;
        tbody.appendChild(newDiv);
        cardWrapper.appendChild(tbody);
      }
    }
    ;
    // add the HTMLTableElement to the main, within the #page div
    const test123 = document.querySelector(".small-div")
    test123.appendChild(cardWrapper2);
    //test123.innerHTML+="<br></br>";
    test123.appendChild(cardWrapper3);
  } catch (error) {
    console.error("HomePage::error: ", error);
  }
};

export default HomePage;
