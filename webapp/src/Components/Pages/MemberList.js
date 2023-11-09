import {getSessionObject} from "../../utils/session";

const MemberList = async () => {
  const pageDiv = document.querySelector("#content");

  let listOfMember;
  let refused = false;
  pageDiv.innerHTML = "";
  pageDiv.innerHTML = `<h3>Liste des membres</h3>
    <hr>
<form autocomplete="off" class="d-flex" >
          <div class="autocomplete">
            <input class="form-control" name="search" id="myInput" type="txt" placeholder="Search" aria-label="Search">
          </div>
            <button class="btn btn-outline-success" id="searchSubmit" type="button">Search</button>
        </form>
`;

  try {
    let user = getSessionObject("user"); //get the user from the session
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        "Authorization": user.token,
      },
    };
    const response = await fetch("/api/users/waitingList?type-list=valid",
        options);
    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + ":" + response.statusText
      );
    }

    const objects = await response.json();
    listOfMember = objects;
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
    header5.innerText = "Offert";

    const header6 = document.createElement("th");
    header6.innerText = "Donné";

    const header7 = document.createElement("th");
    header7.innerText = "recu";

    const header8 = document.createElement("th");
    header8.innerText = "abandonné";

    const header9 = document.createElement("th");
    header9.innerText = "Plus d'infos";

    header.appendChild(header1);
    header.appendChild(header2);
    header.appendChild(header3);
    header.appendChild(header4);
    header.appendChild(header5);
    header.appendChild(header6);
    header.appendChild(header7);
    header.appendChild(header8);
    header.appendChild(header9);
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

      //offert
      const offert = document.createElement("td");
      offert.innerText = object.nbrOfferOffert
      line.appendChild(offert)

      //offert
      const given = document.createElement("td");
      given.innerText = object.nbrOfferGiven
      line.appendChild(given)

      //offert
      const received = document.createElement("td");
      received.innerText = object.nbrOffertReceived
      line.appendChild(received)

      //abandonné
      const giveUp = document.createElement("td");
      giveUp.innerText = object.numberOfferNotPickedUp
      line.appendChild(giveUp)

      const href = document.createElement("input");
      href.type = "button";
      href.id = "clickable";
      href.value = "Plus de details";
      href.dataset.uri = "/memberPage";
      href.dataset.id = object.id;
      //set the object in the uri
      href.dataset.uri += "?username=" + object.username;

      line.appendChild(href);

      const form = document.createElement("form");
      form.name = object.username;

      //--------------------------------------------------------------

      tbody.appendChild(line);
    });

    table.appendChild(tbody);
    // add the HTMLTableElement to the main, within the #page div
    pageDiv.appendChild(tableWrapper);
  } catch (error) {
    console.error("waitingList::error: ", error);
  }
  const buttonSubmit = document.querySelector("#searchSubmit");
  autocomplete(document.getElementById("myInput"), listOfMember);
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
        if (arr[i].lastName.substr(0, val.length).toUpperCase()
            == val.toUpperCase()) {
          /*create a DIV element for each matching element:*/
          b = document.createElement("DIV");
          /*make the matching letters bold:*/
          b.innerHTML = "<strong>" + arr[i].lastName.substr(0, val.length)
              + "</strong>";
          b.innerHTML += arr[i].lastName.substr(val.length);
          /*insert a input field that will hold the current array item's value:*/
          b.innerHTML += "<input type='hidden' value='" + arr[i].lastName
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
        if (arr[i].address.commune.substr(0, val.length).toUpperCase()
            == val.toUpperCase()) {
          /*create a DIV element for each matching element:*/
          b = document.createElement("DIV");
          /*make the matching letters bold:*/
          b.innerHTML = "<strong>" + arr[i].address.commune.substr(0,
                  val.length)
              + "</strong>";
          b.innerHTML += arr[i].address.commune.substr(val.length);
          /*insert a input field that will hold the current array item's value:*/
          b.innerHTML += "<input type='hidden' value='" + arr[i].address.commune
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

        if (arr[i].address.postcode.toString().substr(0,
                val.length).toUpperCase()
            == val.toUpperCase()) {
          /*create a DIV element for each matching element:*/
          b = document.createElement("DIV");
          /*make the matching letters bold:*/
          b.innerHTML = "<strong>" + arr[i].address.postcode.toString().substr(
                  0,
                  val.length)
              + "</strong>";
          b.innerHTML += arr[i].address.postcode.toString().substr(val.length);
          /*insert a input field that will hold the current array item's value:*/
          b.innerHTML += "<input type='hidden' value='"
              + arr[i].address.postcode
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
    pageDiv.innerHTML = `<h3>Liste des membres</h3>
    <hr>`
    try {
      let user = getSessionObject("user"); //get the user from the session
      const options = {
        method: "GET", // *GET, POST, PUT, DELETE, etc.
        headers: {
          "Authorization": user.token,
        },
      };
      const response = await fetch(
          "/api/users/searchMember?search=" + inpu.value,
          options);
      if (!response.ok) {
        throw new Error(
            "fetch error : " + response.status + ":" + response.statusText
        );
      }

      const objects = await response.json();
      listOfMember = objects;
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
      header5.innerText = "Offert";

      const header6 = document.createElement("th");
      header6.innerText = "Donné";

      const header7 = document.createElement("th");
      header7.innerText = "recu";

      const header8 = document.createElement("th");
      header8.innerText = "abandonné";

      const header9 = document.createElement("th");
      header9.innerText = "Plus d'infos";

      header.appendChild(header1);
      header.appendChild(header2);
      header.appendChild(header3);
      header.appendChild(header4);
      header.appendChild(header5);
      header.appendChild(header6);
      header.appendChild(header7);
      header.appendChild(header8);
      header.appendChild(header9);
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

        //offert
        const offert = document.createElement("td");
        offert.innerText = object.nbrOfferOffert
        line.appendChild(offert)

        //offert
        const given = document.createElement("td");
        given.innerText = object.nbrOfferGiven
        line.appendChild(given)

        //offert
        const received = document.createElement("td");
        received.innerText = object.nbrOffertReceived
        line.appendChild(received)

        //abandonné
        const giveUp = document.createElement("td");
        giveUp.innerText = object.numberOfferNotPickedUp
        line.appendChild(giveUp)

        const href = document.createElement("input");
        href.type = "button";
        href.id = "clickable";
        href.value = "Plus de details";
        href.dataset.uri = "/memberPage";
        href.dataset.id = object.id;
        //set the object in the uri
        href.dataset.uri += "?username=" + object.username;

        line.appendChild(href);

        const form = document.createElement("form");
        form.name = object.username;

        //--------------------------------------------------------------

        tbody.appendChild(line);
      });

      table.appendChild(tbody);
      // add the HTMLTableElement to the main, within the #page div
      pageDiv.appendChild(tableWrapper);
    } catch (error) {
      console.error("waitingList::error: ", error);
    }
  });

};

export default MemberList;