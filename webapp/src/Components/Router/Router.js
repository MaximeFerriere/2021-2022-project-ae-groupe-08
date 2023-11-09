import HomePage from "../Pages/HomePage";
import LoginPage from "../Pages/LoginPage";
import InscriptionPage from "../Pages/InscriptionPage";
import HomePageMember from "../Pages/HomePageMember";
import LogoutComponent from "../Pages/LogoutComponent";
import waitingList from "../Pages/WaitingList";
import deniedList from "../Pages/deniedList";
import OfferPage from "../Pages/OfferPage";
import MyObjects from "../Pages/MyObjects";
import MyObjectDetails from "../Pages/MyObjectDetails";
import OfferCreatePage from "../Pages/OfferCreatePage";
import InterestedList from "../Pages/InterestedList";
import CanceledOfferPage from "../Pages/CanceledOfferPage";
import MemberList from "../Pages/memberList";
import ProfilePage from "../Pages/Profile";
import MemberPage from "../Pages/MemberPage";
import GiftReceivedPage from "../Pages/GiftReceived";
import GiftReceivedDetailsPage from "../Pages/GiftReceivedDetails"
import ReservedOfferList from "../Pages/ReservedOfferList";
import GivenOfferList from "../Pages/GivenOfferList";

import {API_URL} from "../../utils/server";
import {getSessionObject, setSession} from "../../utils/session";
import {setAdmin} from "../../utils/user";
import Navbar from "../Navbar/Navbar";

// Configure your routes here
const routes = {
  "/": HomePage,
  "/login": LoginPage,
  "/sign_in": InscriptionPage,
  "/homePageMember": HomePageMember,
  "/logout": LogoutComponent,
  "/waitingList": waitingList,
  "/deniedList": deniedList,
  "/MyObjects": MyObjects,
  "/MyObjectDetails": MyObjectDetails,
  "/OfferPage": OfferPage,
  "/createObject": OfferCreatePage,
  "/interestedList": InterestedList,
  "/profile":ProfilePage,
  "/canceledOffer": CanceledOfferPage,
  "/memberList": MemberList,
  "/memberPage": MemberPage,
  "/MyObjectsReceived" : GiftReceivedPage,
  "/MyObjectsReceivedDetails" : GiftReceivedDetailsPage,
  "/reservedOffer" : ReservedOfferList,
  "/givenOffer" : GivenOfferList,
};

/**
 * Deal with call and auto-render of Functional Components following click events
 * on Navbar, Load / Refresh operations, Browser history operation (back or next) or redirections.
 * A Functional Component is responsible to auto-render itself : Pages, Header...
 */

const Router = () => {
  /* Manage click on the Navbar */
  let navbarWrapper = document.querySelector("#navbarWrapper");
  navbarWrapper.addEventListener("click", async (e) => {
    // To get a data attribute through the dataset object, get the property by the part of the attribute name after data- (note that dashes are converted to camelCase).
    let uri = e.target.dataset.uri;

    if (uri) {
      e.preventDefault();
      /* use Web History API to add current page URL to the user's navigation history 
       & set right URL in the browser (instead of "#") */
      window.history.pushState({}, uri, window.location.origin + uri);
      /* render the requested component
      NB : for the components that include JS, we want to assure that the JS included 
      is not runned when the JS file is charged by the browser
      therefore, those components have to be either a function or a class*/

      var String = window.location.pathname;
      //check if the user isAdmin

      const componentToRender = routes[String];
      if (routes[String]) {
        componentToRender();
      } else {
        throw Error("The " + String + " ressource does not exist");
      }
    }
  });

  /* Route the right component when the page is loaded / refreshed */
  window.addEventListener("load", async (e) => {

    var componentToRender = routes[window.location.pathname];
    //check if the user is connected and  if the token is available
    let me = getSessionObject("user");

    if (me) {
      try {
        const options = {
          method: "POST", // *GET, POST, PUT, DELETE, etc.
          body: JSON.stringify({
            token: me.token,
          }), // body data type must match "Content-Type" header
          headers: {
            "Content-Type": "application/json",
          },
        };

        const response = await fetch(API_URL + "users/checkToken", options); // fetch return a promise => we wait for the response

        if (!response.ok) {
          var componentToRender = routes["/logout"];
          throw new Error(
              "fetch error : " + response.status + " : " + response.statusText
          );
        }
        const result = await response.json();
        setSession("user", result);
        setAdmin();
      } catch (error) {
        console.error("Reload::error: ", error);
      }

      //check if the user is Admin
      try {
        const options = {
          method: "POST", // *GET, POST, PUT, DELETE, etc.
          body: JSON.stringify({
            token: me.token,
          }), // body data type must match "Content-Type" header
          headers: {
            "Content-Type": "application/json",
          },
        };
        const response2 = await fetch(API_URL + "users/isAdmin", options);// fetch return a promise => we wait for the response
        if (!response2.ok) {
          throw new Error(
              "fetch error : " + response2.status + " : " + response2.statusText
          );
        }
        const admin = await response2.json();
        if (admin == true) {
          setAdmin();
          Navbar({isAuthenticated: true});
        }
      } catch (error) {
        console.error("Reload::error: ", error);
      }

    }

    if (!componentToRender) {
      throw Error(
          "The " + window.location.pathname + " ressource does not exist."
      );
    }

    componentToRender();
  });

  // Route the right component when the user use the browsing history
  window.addEventListener("popstate", () => {
    const componentToRender = routes[window.location.pathname];
    componentToRender();
  });
};

/**
 * Call and auto-render of Functional Components associated to the given URL
 * @param {*} uri - Provides an URL that is associated to a functional component in the
 * routes array of the Router
 */

const Redirect = (uri) => {
  // use Web History API to add current page URL to the user's navigation history & set right URL in the browser (instead of "#")
  window.history.pushState({}, uri, window.location.origin + uri);
  // render the requested component
  var String = window.location.pathname;
  const componentToRender = routes[String];
  if (routes[String]) {
    componentToRender();
  } else {
    throw Error("The " + uri + " ressource does not exist");
  }
};

export {Router, Redirect};
