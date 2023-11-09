import { Redirect, Router } from "../Router/Router";
import Navbar from "../Navbar/Navbar";
import { removeLocalData } from "../../utils/session";
import { setAdminFalse } from "../../utils/user";
//remove the local data and reset the navbar and location 
const Logout = () => {
  removeLocalData();
  Navbar();
  Redirect("/");
  Router();
  setAdminFalse();
  location.reload();
};


export default Logout;
