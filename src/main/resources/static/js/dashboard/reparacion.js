
import {
    sendJSONData, deleteObject, showNotification, getFormValues
} from "../utils.js";

const REPAIR_STATE = {
	"SOLICITADA": "warning",
	"ACEPTADA": "success"
}

let noRepairs = document.querySelector("#no-repairs");
let repairForm = document.querySelector("#repair-form");
let showFormBtn = document.querySelector("#show-repair-form");
let registerBtn = document.querySelector("#register-request");
let repairCardContainer = document.querySelector("#reparacion-card-container");

const acceptRepair = (event) => {

	event.preventDefault();
	
	let target = event.target;
	
	let id = target.nextElementSibling.value

	sendJSONData(`reparacion/${id}`, "put", {}).then( (res, rej) => {
		
		if(res.ok) { 
			
			showNotification("Se cambio el estado de la reparación", "success"); 
			target.parentElement.parentElement.remove();
			
			if( repairCardContainer.children.length == 1 ) { 
				noRepairs.style.display = "block"; }
			
		} else { 
			showNotification("No se puedo cambiar el estado de la reparación", "danger"); }
		
	});
	
}

const toggleRepairForm = (event) => {

    let target = event.target;
    
    if(repairForm.style.display != "none") {
    
        repairForm.style.display = "none";
        target.innerHTML = "<i class='material-icons'>add</i> Solicitar reparación";
    
    } else {

        repairForm.style.display = "block";
        target.innerHTML = "<i class='material-icons'>remove</i> Ocultar panel";

    }

}

const sendRepairRequest = (event) => {

	event.preventDefault();

	let target = event.target;
	
	sendJSONData("reparacion", "post", getFormValues(target.form)).then( (res, rej) => { 
		
		if( res.ok ) {
			return res.json(); }
		else {
			showNotification("No se pudo registrar la solicitud"); }
	
		return new Promise();

	}).then( (res, rej) =>{ 
		
		let previousContent = repairCardContainer.innerHTML;
		repairCardContainer.innerHTML = addReparacionFromTemplate(res.reparacion)
			.concat(previousContent);
		[...target.form].forEach( i => i.value = "");

	 });

}

const addReparacionFromTemplate = (repair) => {

	return `
	<div class="card" style="width: 20rem;">
		<div class="card-body">
		  <h4 class="card-title" style="margin-bottom: 10px;">
		  	${repair.arrendatario.usuario.nombres} ${repair.arrendatario.usuario.apellidos}
		  </h4>
		  <h6 class="card-subtitle mb-2 text-muted">Monto estimado: $${repair.montoEstimado}</h6>
		  <p class="card-text">${repair.descripcion}</p>
		  <p>Estado:
			<span class="badge badge-${REPAIR_STATE[repair.estado]}">${repair.estado.toLowerCase()}</span>
		  </p>
		</div>
  	</div>`;

}

window.addEventListener("load", (event) => {

	document.querySelectorAll(".accept-repair").forEach( (a) => { 
		a.addEventListener("click", acceptRepair); });
	
	if( repairCardContainer.children.length == 1 ) { 
		noRepairs.style.display = "block"; }

});

showFormBtn ? showFormBtn.addEventListener("click", toggleRepairForm) : '';
registerBtn ? registerBtn.addEventListener("click", sendRepairRequest) : '';
