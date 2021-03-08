
import { 
	getFormValues, sendJSONData, showNotification } 
from "../utils.js";

let registerNotification = document.querySelector("#register-request");

const addNotification = async (event) => {

    event.preventDefault();

    let target = event.target;

	if( target.form.checkValidity() ) { 
		
		let notificationValue = getFormValues(target.form);
		notificationValue["arrendatario"] = {"id": notificationValue.arrendatario};
		
		let response = await sendJSONData("notificacion", "post", notificationValue);

		if( response.ok ) { 
		
			showNotification("La notificación se envio de forma correcta", "success"); 
			[...target.form].forEach( input => input.value = "");
		
		} else {
			response.json().then( (res, rej) => { 
				res.forEach( e => showNotification(e.defaultMessage, "danger") )
			});
		}
	
	} else { 
		showNotification("Algunos campos del formulario están vacios o son incorrectos", "danger"); }
    

}

registerNotification ? registerNotification.addEventListener("click", addNotification) : '';
