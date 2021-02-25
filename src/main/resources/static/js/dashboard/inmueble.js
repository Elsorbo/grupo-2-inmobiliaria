
import {
    getData, getFormValues, sendJSONData, deleteObject, showNotification, uploadFiles
} from "../utils.js";

import Paginator from "../Paginator.js";

let paginador = {};

let paginatorNext = document.querySelector("#paginator-next");
let imgPickerBtn = document.querySelector("#img-picker-btn");
let imgPicker = document.querySelector("#inmueble-img-picker");
let inmuebleLoader = document.querySelector("#inmueble-loader");
let tbodyInmuebles = document.querySelector("#body-table-inmuebles");
let registrarInmueble = document.querySelector("#registrar-inmueble");
let paginatorPrevious = document.querySelector("#paginator-previous");
let inmuebleIdBorrado = document.querySelector("#inmueble-delete-id");
let inmuebleBorrarBtn = document.querySelector("#inmueble-borrar-btn");
let mostrarAnadirInmueble = document.querySelector("#mostrar-inmueble");
let formularioInmueble = document.querySelector("#formulario-inmueble");

const nuevoInmueble = async (event) => {
    
    event.preventDefault();

    let target = event.target;
    let form = target.parentElement.parentElement;
    
    if(form.checkValidity()) {
        
        inmuebleLoader.style.display = "block";
        showNotification(
            "Subiendo las imagenes del inmueble, esto puede tardar unos segundos", "warning");
        debugger;
        let upladedPictures = await uploadFiles(
            "inmueblepics", "post", "inmueblepics", imgPicker.files);
        let picturesData = await upladedPictures.json();
        let idServicios = getServicios();

        let formValues = getFormValues(form);
        let inmuebleValues = {
        
            "titulo": formValues.titulo,
            "descripcion": formValues.descripcion,
            "localidad": formValues.localidad,
            "precio": formValues.precio,
            "comercializado": document.querySelector("#radio-si").checked,
            "area": formValues.area,
            "fotos": picturesData,
            "servicios": idServicios,
            "empleados": [{"id": formValues.idEmpleado}]
        
        };

        let response = await sendJSONData("inmueble", "post", inmuebleValues);
        
        if(response.ok) {

            ++paginador.totalElements;
            let inmueble = await response.json();
            
            if(tbodyInmuebles.children.length < 5) {
            
                tbodyInmuebles.innerHTML += nuevoInmuebleDesdePlantilla(inmueble);
                agregarInmuebleListener(tbodyInmuebles.lastElementChild);
            
            } else {
                agregarInmuebles( await paginador.lastPage() ); }
            
            showNotification("Se registro el inmueble exitosamente", "success");
            [...form].forEach( i => i.value = "");
        
        } else {
            
            let errors = await response.json();
            errors.map( error => {
                showNotification(error.defaultMessage, "danger") }); 
        
        }
        
        inmuebleLoader.style.display = "none";

    } else {

        showNotification(
            "Algunos campos del formulario son incorrectos o están vacios", "danger");

    }

}

const getServicios = () => {

    let inputsServicios = document.querySelectorAll(".input-servicio-id");
    let servicios = [...inputsServicios].filter( c => c.checked);

    return servicios.map(e => { return {"id": e.value} });

}

const pickImages = (event) => {

    let target = event.target;
    
    let fileNames = [...target.files].map( f => f.name);
    document.querySelector("#img-picker-label").value = fileNames.join("/");

}

const agregarInmuebles = (inmuebles) => {

    if(inmuebles.length > 0) {
    
        let htmlinmuebles = inmuebles.map( (inmueble) => {
            return nuevoInmuebleDesdePlantilla(inmueble); });
        
        tbodyInmuebles.innerHTML = htmlinmuebles.join("");
        [...tbodyInmuebles.children].map( tr => agregarInmuebleListener(tr) );
    
    }

}

const previousInmueblePage = async (event) => {
   
    event.preventDefault();

    let target = event.target;

    if(target.nodeName == "A") { 
        target = target.parentElement; }
    
    if(!target.classList.contains("disabled")) {
        agregarInmuebles( await paginador.previousPage() ); }

}

const nextInmueblePage = async (event) => {

    event.preventDefault();
    
    let target = event.target;

    if(target.nodeName == "A") { 
        target = target.parentElement; }

    if(!target.classList.contains("disabled")) { 
        agregarInmuebles( await paginador.nextPage() ); }
    

}

const toggleAgregarInmueble = (event) => {

    let target = event.target;
    
    if(formularioInmueble.style.display != "none") {
    
        formularioInmueble.style.display = "none";
        target.innerHTML = "<i class='material-icons'>add</i> Añadir Inmueble";
    
    } else {

        formularioInmueble.style.display = "block";
        target.innerHTML = "<i class='material-icons'>remove</i> Ocultar panel";

    }

}

const definirIdBorrado = (event) => {

    let target = event.target;
    
    if(target.nodeName == "I") {
        target = target.parentElement; }
    
    inmuebleIdBorrado.value = target.parentElement.parentElement.firstElementChild.innerText;

}

const borrarInmueble = async (event) => {

    let target = event.target;
    
    let response = await deleteObject(
        "inmueble/".concat(inmuebleIdBorrado.value));

    if(response.ok) { 
    
        document.querySelector(`#inmueble-${inmuebleIdBorrado.value}`)
            .style.display = "none";
        showNotification("Inmueble eliminado correctamente", "success"); }
        
    target.previousElementSibling.click();

}

const nuevoInmuebleDesdePlantilla = (inmueble) => {
    
    return (
    ` <tr id="inmueble-+${inmueble.id}">
        <td class="text-center">${inmueble.id}</td>
        <td>${inmueble.titulo}</td>
        <td>${inmueble.area} m2</td>
        <td>${inmueble.precio}</td>
        <td class="text-center">${inmueble.comercializado ? "Si" : "No"}</td>
        <td>${inmueble.empleados.length > 0 ? 
            inmueble.empleados[0].usuario.nombres + " " + inmueble.empleados[0].usuario.apellidos 
            : "No asignado" } </td>
        <input type="hidden" value="${inmueble.alquilado}">
        <input type="hidden" value="${inmueble.descripcion}">
        <td class="td-actions text-right">
            <button type="button" rel="tooltip" class="btn btn-success">
                <a href="/inmueble/${inmueble.id}"
                    style="text-decoration: none; color: white;">
                    <i class="material-icons">edit</i>
                </a>
            </button>
            <button type="button" rel="tooltip" data-toggle="modal"
                data-target="#modal-confirm-delete"
                class="btn btn-danger" id="borrar-inmueble-btn">
                <i class="material-icons">delete_forever</i>
            </button>
        </td>
    </tr>`);
    
}

const agregarInmuebleListener = (inmueble) => {
    
    let deleteBtn = inmueble.querySelector("#borrar-inmueble-btn");
    
    deleteBtn.addEventListener("click", definirIdBorrado);

}

window.addEventListener("load", (event) => {

    tbodyInmuebles.querySelectorAll("tr")
        .forEach( tr => agregarInmuebleListener(tr));

    paginador = new Paginator("inmueble", document.querySelector(".pagination"));


});

imgPicker.addEventListener("change", pickImages);
registrarInmueble.addEventListener("click", nuevoInmueble);
paginatorNext.addEventListener("click", nextInmueblePage)
inmuebleBorrarBtn.addEventListener("click", borrarInmueble);
paginatorPrevious.addEventListener("click", previousInmueblePage);
imgPickerBtn.addEventListener("click", event => imgPicker.click() );
mostrarAnadirInmueble.addEventListener("click", toggleAgregarInmueble);
