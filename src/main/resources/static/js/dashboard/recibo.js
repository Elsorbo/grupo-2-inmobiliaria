
import { 
    getFormValues, sendJSONData, showNotification, uploadFiles 
} from "../utils.js";

let noReceipts = document.querySelector("#no-receipts");
let reciboLoader = document.querySelector("#receipt-loader");
let imgPicker = document.querySelector("#receipt-input-file");
let formularioRecibo = document.querySelector("#receipt-form");
let showReciboBtn = document.querySelector("#show-receipt-form");
let agregarReciboBtn = document.querySelector("#register-receipt");
let imgPickerBtn = document.querySelector("#receipt-input-file-btn");
let reciboCardContainer = document.querySelector("#recibo-card-container");

const agregarReciboPago = async (event) => {

    event.preventDefault();

    let target = event.target;

    if( target.form.checkValidity() ) {
        
        reciboLoader.style.display = "block";
        let subirImagen = await uploadFiles(
            "receiptpics", "post", "receiptpic", imgPicker.files);
        let imagenSubida = await subirImagen.json();
        reciboLoader.style.display = "none";
        
        let reciboValues = {
            "periodoPago": target.form.periodoPago.value,
            "nombreImagen": imagenSubida.name,
            "urlImagen": imagenSubida.url
        }
        
        let request = await sendJSONData("recibo", "post", reciboValues);

        if( request.ok ) {

            let recibo = await request.json();
            noReceipts.style.display = "none";
            showNotification("Se registro el recibo de pago exitosamente", "success");
            
            let previousContent = reciboCardContainer.innerHTML;
            reciboCardContainer.innerHTML = reciboDesdePlantilla(recibo)
                .concat(previousContent);
            [...target.form].forEach( i => i.value = "");
        
        } else {
            
            let errors = await request.json();
            errors.forEach( error => showNotification(error.defaultMessage, "danger") );
        
        }

    } else { 
        showNotification("Algunos campos del formulario son incorrectos o están vacios", "danger"); }
    
}

const reciboDesdePlantilla = (recibo) => {

    return `
    <div class="card" style="width: 20rem; margin: 12px;">
        <img class="card-img-top" src="${recibo.urlImagen}" 
            rel="nofollow" alt="Card image cap" style="height: 260px;">
        <div class="card-body">
            <p class="card-text">
                <strong>Periodo: </strong>${recibo.periodoPago}
            </p>
            <a href="/recibos" class="card-link" target="_blank">Editar el recibo</a>
            <input type="hidden" name="repairid" value="${recibo.id}">
        </div>
    </div>`;

}

const toggleAgregarRecibo = (event) => {

    let target = event.target;
    
    if(formularioRecibo.style.display != "none") {
    
        formularioRecibo.style.display = "none";
        target.innerHTML = "<i class='material-icons'>add</i> Añadir un recibo de pago";
        toggleEmptyContainer();
        
    } else {

        formularioRecibo.style.display = "block";
        target.innerHTML = "<i class='material-icons'>remove</i> Ocultar panel";
        noReceipts.style.display = "none";

    }

}

const toggleEmptyContainer = () => {
    
    if( reciboCardContainer.children.length == 1 ) { 
		noReceipts.style.display = "block"; }

}
showReciboBtn.addEventListener("click", toggleAgregarRecibo);
agregarReciboBtn.addEventListener("click", agregarReciboPago);
imgPickerBtn.addEventListener("click", event => imgPicker.click() );
imgPicker.addEventListener("change", (event) => {

    let target = event.target;

    target.nextElementSibling.firstElementChild.placeholder = target.files[0].name;
    
});

window.addEventListener("load", (event) => {

	toggleEmptyContainer();

});
