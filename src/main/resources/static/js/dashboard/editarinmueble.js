
import {
	getFormValues, 
	showNotification, 
	sendJSONData
} from "../utils.js";

let actualizarInmueble = document.querySelector("#actualizar-inmueble");

const updateInmueble = async (event) => {

	event.preventDefault();
	let target = event.target;

	if( target.form.checkValidity() ) { 
	
		let formValues = getFormValues(target.form);
		let inmuebleValues = {

			"id": formValues.inmuebleId,
			"titulo": formValues.titulo,
			"descripcion": formValues.descripcion,
			"localidad": formValues.localidad,
			"precio": formValues.precio,
			"comercializado": document.querySelector("#radio-si").checked,
			"area": formValues.area,
			"fotos": [{"id": -1}]

		}
				
		let response = await sendJSONData("inmueble", "put", inmuebleValues);

		if(response.ok) { 
			showNotification("El inmueble ha sido actualizado con exito", "success");
		} else { 
		
			let errors = await response.json();
			errors.forEach( e => { showNotification(error.defaultMessage, "danger") });
		
		}

	} else { alert(""); }

}

actualizarInmueble.addEventListener("click", updateInmueble);
