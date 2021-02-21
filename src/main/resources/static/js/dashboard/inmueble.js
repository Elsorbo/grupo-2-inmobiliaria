
import {
    getData, getFormValues, sendJSONData, deleteObject, showNotification, uploadFiles
} from "../utils.js";

import Paginator from "../Paginator.js";

let paginador = {};

let paginatorNext = document.querySelector("#paginator-next");
let imgPickerBtn = document.querySelector("#img-picker-btn");
let imgPicker = document.querySelector("#inmueble-img-picker");
let registrarInmueble = document.querySelector("#registrar-inmueble");
let tbodyEmployees = document.querySelector("#body-table-inmuebles");
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

            
            let inmueble = await response.json();
            console.log(inmueble);
            
            // if(tbodyEmployees.children.length < 5) {
            
            //     tbodyEmployees.innerHTML += nuevoInmuebleDesdePlantilla(employee);
            //     addEmployeeListeners(tbodyEmployees.lastElementChild);
            
            // }
            
            showNotification("Se registro el inmueble exitosamente", "success");
            [...form].forEach( i => i.value = "");
        
        } else {
            
            let errors = await response.json();
            errors.map( error => {
                showNotification(error.defaultMessage, "danger") }); 
        
        }
        
    
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

const addEmployees = (employees) => {

    if(employees.length > 0) {
    
        let htmlEmployees = employees.map( (employee) => {
        return nuevoInmuebleDesdePlantilla(employee); });
    
        tbodyEmployees.innerHTML = htmlEmployees.reduce( (a, b) => a.concat(b));
        [...tbodyEmployees.children].map( tr => addEmployeeListeners(tr) );
    
    }

}

const previousEmployeePage = async (event) => {
   
    event.preventDefault();

    let target = event.target;

    if(target.nodeName == "A") { 
        target = target.parentElement; }
    
    if(!target.classList.contains("disabled")) {
        addEmployees( await paginador.previousPage() ); }

}

const nextEmployeePage = async (event) => {

    event.preventDefault();
    
    let target = event.target;

    if(target.nodeName == "A") { 
        target = target.parentElement; }

    if(!target.classList.contains("disabled")) {
        addEmployees( await paginador.nextPage() ); }

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
        "empleado/".concat(inmuebleIdBorrado.value));

    if(response.ok) { 
    
        document.querySelector(`#inmueble-${inmuebleIdBorrado.value}`)
            .style.display = "none";
        showNotification("Inmueble eliminado correctamente", "success"); }
        
    target.previousElementSibling.click();

}

const nuevoInmuebleDesdePlantilla = (inmueble) => {

    return (
    `<tr id="inmueble-${inmueble.id}">
        <td class="text-center">${inmueble.id}</td>
        <td>${inmueble.usuario.nombres} ${inmueble.usuario.apellidos}</td>
        <td>${inmueble.usuario.correo}</td>
        <td>${inmueble.usuario.estado ? "Activo" : "Inactivo"}</td>
        <td class="text-right">${inmueble.telefono}</td>
        <input type="hidden" value="${inmueble.usuario.cedula}">
        <input type="hidden" value="${inmueble.usuario.usuario}">
        <input type="hidden" value="${inmueble.usuario.descripcion}">
        <input type="hidden" value="${inmueble.usuario.urlImagenPerfil ? 
            inmueble.usuario.urlImagenPerfil : 
            'http://style.anu.edu.au/_anu/4/images/placeholders/person_8x10.png'}">
        <input type="hidden" 
            value="${new Date(inmueble.usuario.fechaActualizacion)}">
        <td class="td-actions text-right">
            <button type="button" rel="tooltip" 
                class="btn btn-info inmueble-aditional-info-btn" 
                data-toggle="modal" data-target="#userDetailsModal">
                <i class="material-icons">person</i>
            </button>
            <button type="button" rel="tooltip" class="btn btn-success">
                <a href="/empleado/${inmueble.id}" 
                    style="text-decoration: none; color: white;">
                    <i class="material-icons">edit</i>
                </a>
            </button>
            <button type="button" rel="tooltip" data-toggle="modal" 
                data-target="#modal-confirm-delete" 
                class="btn btn-danger delete-inmueble-btn">
                <i class="material-icons">close</i>
            </button>
        </td>
    </tr>`);

}

const addEmployeeListeners = (employee) => {
    
    let deleteBtn = employee.querySelector("#borrar-inmueble-btn");
    
    deleteBtn.addEventListener("click", definirIdBorrado);

}

window.addEventListener("load", (event) => {

    tbodyEmployees.querySelectorAll("tr")
        .forEach( tr => addEmployeeListeners(tr));

    paginador = new Paginator("empleado", document.querySelector(".pagination"));


});

imgPicker.addEventListener("change", pickImages);
registrarInmueble.addEventListener("click", nuevoInmueble);
paginatorNext.addEventListener("click", nextEmployeePage)
inmuebleBorrarBtn.addEventListener("click", borrarInmueble);
paginatorPrevious.addEventListener("click", previousEmployeePage);
imgPickerBtn.addEventListener("click", event => imgPicker.click() );
mostrarAnadirInmueble.addEventListener("click", toggleAgregarInmueble);
