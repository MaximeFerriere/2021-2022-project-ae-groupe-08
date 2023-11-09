import {Redirect} from "../Router/Router";
import {API_URL} from "../../utils/server";
import Navbar from "../Navbar/Navbar";
import {setSessionData, setSessionObject} from "../../utils/session";
import {setAdmin} from "../../utils/user";
//the html login page 
let login = `<head>
<title>Login Page</title>
</head>
<body>
<div class="containerForLogin">
  <div class="container">
    <div class="title">
      <h3>Connexion</h3>
    </div>
      <div class="contentLogin">
        <form id ="formlogin" class="p-5">
          <div class="offer-details-l">
            <div class="input-box" id="group_input">
                      
                <input id="username" type="text" class="form-control" placeholder="Pseudo" required=true>
            </div>

            <div class="input-box" id="group_input">
                <input id="password" type="password" class="form-control" placeholder="mot-de-passe" required=true>
            </div>

            <input id="rememberme" type="checkbox"  > Se souvenir de moi
            
          </div>
            <div class="buttonLogin">
                <input type="submit" value="Connexion">
              <span id="span"></span> 
            </div>
        </form>
    </div>
    <div class="card-footer">
      <div class="d-flex justify-content-center links">
          Pas de compte? <a href="sign_in" data-uri="/sign_in">S'inscrire</a>
      </div>
    </div>

  </div>
</div>
</div>
</body>`
//the login function to get all the user information and set it in the session
function LoginPage() {
  // reset #page div
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = login;
  const txtRefusal = document.querySelector("#span");

  addEventListener("submit", onSubmit);

  async function onSubmit(e) {
    e.preventDefault();
    const username = document.getElementById("username");//get the username
    const password = document.getElementById("password");//get the password
    const check = document.getElementById("rememberme");//get the rememberme checkbox
    try {
      //send the data to the backend 
      const options = {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify({
          username: username.value,
          password: password.value,
        }), // body data type must match "Content-Type" header
        headers: {
          "Content-Type": "application/json",
        },
      };

      const response = await fetch(API_URL + "users/login", options); // fetch return a promise => we wait for the response

      if (!response.ok) {

        txtRefusal.innerHTML = `<br> Message d'erreur : <br>`;
        txtRefusal.innerHTML += "Pseudo ou mot de passe incorrect";
        throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
        );
      }
      const user = await response.json(); // json() returns a promise => we wait for the data
      // save the user into the localStorage
      if (user.textRefusal == null) {

        if (check.checked == 1) {
          setSessionObject("user", user);
        }
        // save the user into the session
        else {
          setSessionData("user", user);
        }
        //refresh the navBar

        try {
          const options = {
            method: "POST", // *GET, POST, PUT, DELETE, etc.
            body: JSON.stringify({
              token: user.token,
            }), // body data type must match "Content-Type" header
            headers: {
              "Content-Type": "application/json",
            },
          };

          const response = await fetch("/api/users/isAdmin", options); // fetch return a promise => we wait for the response

          if (!response.ok) {
            // status code was not 200, error status code
            throw new Error(
                "fetch error : " + response.status + " : " + response.statusText
            );
          }
          const objects = await response.json(); // json() returns a promise => we wait for the data
          if (objects == true) {
            setAdmin();
          }
        } catch (error) {
          console.error("HomePageView::error: ", error);
        }

        Navbar({isAuthenticated: true});
        // call the HomePage via the Router
        Redirect("/homePageMember");
      } else {
        txtRefusal.innerHTML = `<br>`;
        txtRefusal.innerHTML += user.textRefusal; //the RefusalText form the User
      }

    } catch (error) {
      console.error("LoginPage::error: ", error);
    }
  }

};
export default LoginPage;





