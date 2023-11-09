import { Footer as BootstrapNavbar} from "bootstrap";

const Footer = () => {
  const FooterWrapper = document.querySelector("#Footer");
  let html =`
  </div>
  </body>`
  FooterWrapper.innerHTML = html;
};

export default Footer;