
import { getFormValues, sendJSONData } from "../utils.js";

let registerNotification = document.querySelector("#register-request");

const addNotification = (event) => {

    event.preventDefault();

    let target = event.target;

    console.log( getFormValues(target.form) );

}

registerNotification.addEventListener("click", addNotification);
