// When using Bootstrap to style components, the CSS is imported in index.js
// However, the JS has still to be loaded for each Bootstrap's component that needs it.
// Here, because our JS component 'Navbar' has the same name as Navbar Bootstrap's component
// we change the name of the imported Bootstrap's 'Navbar' component
import {getSessionObject} from "../../utils/session";
import {getIsAdmin} from "../../utils/user";

/**
 * Render the Navbar which is styled by using Bootstrap
 * Each item in the Navbar is tightly coupled with the Router configuration :
 * - the URI associated to a page shall be given in the attribute "data-uri" of the Navbar
 * - the router will show the Page associated to this URI when the user click on a nav-link
 */

const Navbar = () => {

  let admin = getIsAdmin();

  let user = getSessionObject("user");

  let loginToggle;
  //let inscription = getSessionObject("inscriptionPage");
  const navbarWrapper = document.querySelector("#navbarWrapper");
  let navbar;

  if (!user) {
    navbar = `
    <nav class="navbar navbar-expand-lg navbar-light" id="navbar" style="background-color: #dbdad6;">
      <div class="container-fluid">
      <a class="navbar-brand" href="#" data-uri="/">Donnamis</a>
    <button
      class="navbar-toggler"
      type="button"
      data-bs-toggle="collapse"
      data-bs-target="#navbarSupportedContent"
      aria-controls="navbarSupportedContent"
      aria-expanded="false"
      aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
        <div class="collapse navbar-collapse" id="navbarText">
        
        </div>
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item" id="loginVar">
            <li class="nav-item">
            <a id="inscription" href="/sign_in"><button href= ""type="button" class="button-59" >Inscription</button>
          </li>
          <li class="nav-item">
          <button type="button" class="button-59" data-uri="/login">Connexion</button>
          </ul>
      </div>
    </nav>`;
  } else if (user && !admin) {
    navbar = `<nav class="navbar navbar-expand-lg navbar-light" id="navbar" style="background-color: #dbdad6;">
    <div class="container-fluid">
    <a class="navbar-brand" href="#" data-uri="/homePageMember">Donnamis</a>
  <button
    class="navbar-toggler"
    type="button"
    data-bs-toggle="collapse"
    data-bs-target="#navbarSupportedContent"
    aria-controls="navbarSupportedContent"
    aria-expanded="false"
    aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
  </button>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item" id="loginVar">
          <li class="nav-item">
          <a class="nav-link" aria-current="page" href="/logout" >Deconnexion</a>
        </li>

        </ul>

          <a class="nav-link">
            <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
            <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
            <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"/>
            </svg>
          </a>
              <a class="nav-item nav-link disabled" href="#">${user.login}</a>
      </div>
    </div>
  </nav>
  <div class="container1">
  <aside class="sidebar" data-sidebar>
  <div class="top-sidebar" class="header">
    <button class="menu-icon-btn" data-menu-icon-btn>
      <svg xmlns="http://www.w3.org/2000/svg" width="46" height="46" fill="currentColor" class="bi bi-arrow-left-short" viewBox="0 0 16 16"> <!-- ARROW-->
        <path fill-rule="evenodd" d="M12 8a.5.5 0 0 1-.5.5H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5H11.5a.5.5 0 0 1 .5.5z"/>
      </svg>
    </button>
  </div>
  <div class="middle-sidebar">
    <ul class="sidebar-list">
    
      <li class="sidebar-list-item active">
        <a href=# data-uri="/homePageMember" class="sidebar-link">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="sidebar-icon" viewBox="0 0 16 16" data-uri="/homePageMember">
        <path data-uri="/homePageMember" d="M3 2.5a2.5 2.5 0 0 1 5 0 2.5 2.5 0 0 1 5 0v.006c0 .07 0 .27-.038.494H15a1 1 0 0 1 1 1v1a1 1 0 0 1-1 1H1a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1h2.038A2.968 2.968 0 0 1 3 2.506V2.5zm1.068.5H7v-.5a1.5 1.5 0 1 0-3 0c0 .085.002.274.045.43a.522.522 0 0 0 .023.07zM9 3h2.932a.56.56 0 0 0 .023-.07c.043-.156.045-.345.045-.43a1.5 1.5 0 0 0-3 0V3zm6 4v7.5a1.5 1.5 0 0 1-1.5 1.5H9V7h6zM2.5 16A1.5 1.5 0 0 1 1 14.5V7h6v9H2.5z"/>
        </svg>
        <div class="hidden-sidebar" data-uri="/homePageMember">Voir offres</div>
        </a>
      </li>
      
      <li class="sidebar-list-item">
        <a href=# data-uri="/profile" class="sidebar-link">
        <svg xmlns="http://www.w3.org/2000/svg"  width="36" height="36" fill="currentColor" class="sidebar-icon" viewBox="0 0 16 16" data-uri="/profile">
        <path data-uri="/profile" d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
        <path data-uri="/profile" fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"/>
        </svg>
          <div class="hidden-sidebar" data-uri="/profile">Voir profil</div>
        </a>
      </li>
      
      <li class="sidebar-list-item">
        <a href="#" data-uri="/MyObjects" class="sidebar-link">
            <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" fill="currentColor" class="sidebar-icon" viewBox="0 0 16 16" data-uri="/MyObjects">
                <path data-uri="/MyObjects" d="M0 2a1 1 0 0 1 1-1h14a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1v7.5a2.5 2.5 0 0 1-2.5 2.5h-9A2.5 2.5 0 0 1 1 12.5V5a1 1 0 0 1-1-1V2zm2 3v7.5A1.5 1.5 0 0 0 3.5 14h9a1.5 1.5 0 0 0 1.5-1.5V5H2zm13-3H1v2h14V2zM5 7.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5z"/>
            </svg>
            <div class="hidden-sidebar" data-uri="/MyObjects">Mes offres</div>
        </a>
      </li>
      
      <li class="sidebar-list-item">
      <a href="#" data-uri="/MyObjectsReceived" class="sidebar-link">
            <svg xmlns="http://www.w3.org/2000/svg"  width="36" height="36" fill="currentColor" class="sidebar-icon" viewBox="0 0 16 16" data-uri="/MyObjectsReceived">
              <path data-uri="/MyObjectsReceived"  d="M5.071 1.243a.5.5 0 0 1 .858.514L3.383 6h9.234L10.07 1.757a.5.5 0 1 1 .858-.514L13.783 6H15.5a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.5.5H15v5a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V9H.5a.5.5 0 0 1-.5-.5v-2A.5.5 0 0 1 .5 6h1.717L5.07 1.243zM3.5 10.5a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0v-3zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0v-3zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0v-3zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0v-3zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0v-3z"/>
            </svg>
          <div class="hidden-sidebar" data-uri="/MyObjectsReceived">Cadeaux reçus</div>
      </a>
      </li>
      
      <br><br><br><br>
      <li class="sidebar-list-item">
        <a href="/createObject"    class="sidebar-link">
        <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" fill="currentColor" class="sidebar-icon" viewBox="0 0 16 16">
        <path d="M8 7.982C9.664 6.309 13.825 9.236 8 13 2.175 9.236 6.336 6.31 8 7.982Z"/>
        <path d="M3.75 0a1 1 0 0 0-.8.4L.1 4.2a.5.5 0 0 0-.1.3V15a1 1 0 0 0 1 1h14a1 1 0 0 0 1-1V4.5a.5.5 0 0 0-.1-.3L13.05.4a1 1 0 0 0-.8-.4h-8.5Zm0 1H7.5v3h-6l2.25-3ZM8.5 4V1h3.75l2.25 3h-6ZM15 5v10H1V5h14Z"/>
        </svg>
      <div class="hidden-sidebar">Offrir</div>
        </a>
      </li>
    </ul>
  </div>
</aside>
    <main class="content" id="content"></main>
  </div>`;
  } else {
    navbar = `<nav class="navbar navbar-expand-lg navbar-light" id="navbar" style="background-color: #dbdad6;">
    <div class="container-fluid">
    <a class="navbar-brand" href="#" data-uri="/homePageMember">Donnamis</a>
  <button
    class="navbar-toggler"
    type="button"
    data-bs-toggle="collapse"
    data-bs-target="#navbarSupportedContent"
    aria-controls="navbarSupportedContent"
    aria-expanded="false"
    aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
  </button>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item" id="loginVar">
          <li class="nav-item">
          <a class="nav-link" aria-current="page" href="#" data-uri="/logout">Deconnexion</a>
        </li>
          </li>
        </ul>

          <a class="nav-link">
            <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
            <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
            <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"/>
            </svg>
          </a>
              <a class="nav-item nav-link disabled" href="#">${user.login} (ADMIN)</a>
      </div>
    </div>
  </nav>
  <div class="container1">
  <aside class="sidebar" data-sidebar>
  <div class="top-sidebar" class="header">
    <button class="menu-icon-btn" data-menu-icon-btn>
      <svg xmlns="http://www.w3.org/2000/svg" width="46" height="46" fill="currentColor" class="bi bi-arrow-left-short" viewBox="0 0 16 16"> <!-- ARROW-->
        <path fill-rule="evenodd" d="M12 8a.5.5 0 0 1-.5.5H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5H11.5a.5.5 0 0 1 .5.5z"/>
      </svg>
    </button>
  </div>
  <div class="middle-sidebar">
    <ul class="sidebar-list">
    
       <li class="sidebar-list-item active" data-uri="/homePageMember" >
          <a href=# data-uri="/homePageMember" class="sidebar-link">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="sidebar-icon" viewBox="0 0 16 16" data-uri="/homePageMember">
          <path data-uri="/homePageMember" d="M3 2.5a2.5 2.5 0 0 1 5 0 2.5 2.5 0 0 1 5 0v.006c0 .07 0 .27-.038.494H15a1 1 0 0 1 1 1v1a1 1 0 0 1-1 1H1a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1h2.038A2.968 2.968 0 0 1 3 2.506V2.5zm1.068.5H7v-.5a1.5 1.5 0 1 0-3 0c0 .085.002.274.045.43a.522.522 0 0 0 .023.07zM9 3h2.932a.56.56 0 0 0 .023-.07c.043-.156.045-.345.045-.43a1.5 1.5 0 0 0-3 0V3zm6 4v7.5a1.5 1.5 0 0 1-1.5 1.5H9V7h6zM2.5 16A1.5 1.5 0 0 1 1 14.5V7h6v9H2.5z"/>
          </svg>
          <div class="hidden-sidebar" data-uri="/homePageMember">Voir offres</div>
          </a>
       </li>
       
      <li class="sidebar-list-item">
      <a href=# data-uri="/profile" class="sidebar-link">
        <svg xmlns="http://www.w3.org/2000/svg"  width="36" height="36" fill="currentColor" class="sidebar-icon" viewBox="0 0 16 16" data-uri="/profile">
        <path data-uri="/profile" d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
        <path  data-uri="/profile" fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"/>
        </svg>
          <div class="hidden-sidebar" data-uri="/profile">Voir profil</div>
        </a>
      </li>
      
      <li class="sidebar-list-item">
        <a href="#" data-uri="/MyObjects"  class="sidebar-link">
            <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" fill="currentColor" class="sidebar-icon" viewBox="0 0 16 16" data-uri="/MyObjects">
                <path data-uri="/MyObjects" d="M0 2a1 1 0 0 1 1-1h14a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1v7.5a2.5 2.5 0 0 1-2.5 2.5h-9A2.5 2.5 0 0 1 1 12.5V5a1 1 0 0 1-1-1V2zm2 3v7.5A1.5 1.5 0 0 0 3.5 14h9a1.5 1.5 0 0 0 1.5-1.5V5H2zm13-3H1v2h14V2zM5 7.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5z"/>
            </svg>
            <div class="hidden-sidebar" data-uri="/MyObjects">Mes offres</div>
        </a>
      </li>
      
      <li class="sidebar-list-item">
        <a href="#" data-uri="/MyObjectsReceived" class="sidebar-link">
          <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" fill="currentColor" class="sidebar-icon" viewBox="0 0 16 16" data-uri="/MyObjectsReceived">
            <path  data-uri="/MyObjectsReceived" d="M5.071 1.243a.5.5 0 0 1 .858.514L3.383 6h9.234L10.07 1.757a.5.5 0 1 1 .858-.514L13.783 6H15.5a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.5.5H15v5a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V9H.5a.5.5 0 0 1-.5-.5v-2A.5.5 0 0 1 .5 6h1.717L5.07 1.243zM3.5 10.5a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0v-3zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0v-3zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0v-3zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0v-3zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0v-3z"/>
          </svg>
            <div class="hidden-sidebar" data-uri="/MyObjectsReceived">Cadeaux reçus</div>
        </a>
      </li>
      
      <li class="sidebar-list-item">
        <a href="#" data-uri="/waitingList" class="sidebar-link">
            <svg xmlns="http://www.w3.org/2000/svg"  width="36" height="36" fill="currentColor" class="sidebar-icon" viewBox="0 0 16 16" data-uri="/waitingList">
              <path data-uri="/waitingList" fill-rule="evenodd" d="M15.854 5.146a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.708 0l-1.5-1.5a.5.5 0 0 1 .708-.708L12.5 7.793l2.646-2.647a.5.5 0 0 1 .708 0z"/>
              <path data-uri="/waitingList" d="M1 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6z"/>
            </svg>
            <div class="hidden-sidebar" data-uri="/waitingList">Validation<br> Inscription</div>
        </a>
      </li>
      
      <li class="sidebar-list-item">
        <a href="#"  data-uri="/memberList" class="sidebar-link">
            <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" fill="currentColor" class="sidebar-icon" viewBox="0 0 16 16" data-uri="/memberList">
              <path data-uri="/memberList" d="M6 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm-5 6s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H1zM11 3.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1h-4a.5.5 0 0 1-.5-.5zm.5 2.5a.5.5 0 0 0 0 1h4a.5.5 0 0 0 0-1h-4zm2 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1h-2zm0 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1h-2z"/>
            </svg>
            <div class="hidden-sidebar" data-uri="/memberList">Liste Membre</div>
        </a>
      </li>
      
      <br><br><br><br><br>
      <li class="sidebar-list-item">
        <a href="/createObject"  class="sidebar-link">
        <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" fill="currentColor" class="sidebar-icon" viewBox="0 0 16 16">
        <path d="M8 7.982C9.664 6.309 13.825 9.236 8 13 2.175 9.236 6.336 6.31 8 7.982Z"/>
        <path d="M3.75 0a1 1 0 0 0-.8.4L.1 4.2a.5.5 0 0 0-.1.3V15a1 1 0 0 0 1 1h14a1 1 0 0 0 1-1V4.5a.5.5 0 0 0-.1-.3L13.05.4a1 1 0 0 0-.8-.4h-8.5Zm0 1H7.5v3h-6l2.25-3ZM8.5 4V1h3.75l2.25 3h-6ZM15 5v10H1V5h14Z"/>
        </svg>
      <div class="hidden-sidebar">Offrir</div>
        </a>
      </li>
    </ul>
  </div>
</aside>
    <main class="content" id="content"></main>
  </div>
  `;
  }
  navbarWrapper.innerHTML = navbar;
  if (user) {
    const menuIconButton = document.querySelector("[data-menu-icon-btn]");
    const sidebar = document.querySelector("[data-sidebar]");
    

    //a demander au prof quand on se deco posse probleme
    menuIconButton.addEventListener("click", () => {
      sidebar.classList.toggle("open");
    });

  }

};

export default Navbar;
