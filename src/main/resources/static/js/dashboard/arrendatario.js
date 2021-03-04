
import {
    sendJSONData, deleteObject, showNotification, getFormValues
} from "../utils.js";

import Paginator from "../Paginator.js";

let paginador = {};

let addTenant = document.querySelector("#addTenant");
let addTenantBtn = document.querySelector("#showAddTenant");
let cardInfoBody = document.querySelector("#card-info-body");
let paginatorNext = document.querySelector("#paginator-next");
let registerTenant = document.querySelector("#registerTenant");
let tbodyTenants = document.querySelector("#body-table-tenants");
let paginatorPrevious = document.querySelector("#paginator-previous");
let tenantDeleteBtn = document.querySelector("#tenant-delete");

const nuevoArrendatario = async (event) => {
    
    event.preventDefault();
    let target = event.target;
    let form = target.form;
    
    if( form.checkValidity() ) {

        let formValues = getFormValues(form);
        
        let arrendatarioValues = {
        
            "usuario": {

                "usuario": formValues.usuario,
                "cedula": formValues.cedula,
                "nombres": formValues.nombres,
                "apellidos": formValues.apellidos,
                "correo": formValues.correo,
                "contrasena": formValues.contrasena

            },
            "inmuebles": [{"id": formValues.idInmueble}]
        
        };
        
        let response = await sendJSONData(
            "arrendatario", "post", arrendatarioValues);
        
        if(response.ok) {

            ++paginador.totalElements;
            let tenant = await response.json();
            
            if(tbodyTenants.children.length < 5) {
            
                tbodyTenants.innerHTML += newTenantFromTemplate(tenant);
                addTenantListeners(tbodyTenants.lastElementChild);
            
            } else {
                addTenants( await paginador.lastPage() ); }
            
            showNotification("Se registro el arrendatario exitosamente", "success");
            [...form].forEach( i => i.value = "");
            document.querySelectorAll(
                `#inmueble-id > option[value="${formValues.idInmueble}"]`)[0]
                .style.display="none";
        
        } else {
            
            let errors = await response.json();
            errors.map( error => {
                showNotification(error.defaultMessage, "danger") }); 
        
        }
        
    } else {
        showNotification("Algunos campos del formulario son incorrectos\
             o están vacios", "danger"); }
    
}

const addTenants = (tenants) => {

    if(tenants.length > 0) {
        
        let htmlTenants = tenants.map( (tenant) => {
            return newTenantFromTemplate(tenant); });
    
        tbodyTenants.innerHTML = htmlTenants.join("");
        [...tbodyTenants.children].map( tr => addTenantListeners(tr) );
    
    }

}

const toggleAddArrendatario = (event) => {

    let target = event.target;
    
    if(addTenant.style.display != "none") {
    
        addTenant.style.display = "none";
        target.innerHTML = "<i class='material-icons'>add</i> Añadir arrendatario";
    
    } else {

        addTenant.style.display = "block";
        target.innerHTML = "<i class='material-icons'>remove</i> Ocultar panel";

    }

}

const nextTenantsPage = async (event) => {

    event.preventDefault();

    let target = event.target;

    if(target.nodeName == "A") { 
        target = target.parentElement; }
    
    if(!target.classList.contains("disabled")) {
        addTenants( await paginador.nextPage() ); }

}

const previousTenantsPage = async (event) => {

    event.preventDefault();
    
    let target = event.target;

    if(target.nodeName == "A") { 
        target = target.parentElement; }

    if(!target.classList.contains("disabled")) {
        addTenants( await paginador.previousPage() ); }

}

const addTenantListeners = (tenant) => {

    let deleteBtn = tenant.querySelector(".delete-tenant-btn");
    let infoBtn = tenant.querySelector(".tenant-aditional-info-btn");

    deleteBtn.addEventListener("click", setTenantDeleteId);
    infoBtn.addEventListener("click", setTenantAditionalInfo);

}

const setTenantAditionalInfo = (event) => {

    let target = event.target;
    
    if(target.nodeName == "I") {
        target = target.parentElement; }
    
    let cardInfo = cardInfoBody.children;
    let inputsInfo = target.parentElement.parentElement.getElementsByTagName("input");
    
    cardInfo[0].textContent = `CI: ${inputsInfo[0].value}`;
    cardInfo[1].textContent = `Username: ${inputsInfo[1].value}`;
    cardInfo[2].innerHTML = `Descripcion: <br />${inputsInfo[2].value}`;
    cardInfoBody.previousElementSibling.querySelector("img").src = inputsInfo[3].value;
    
}


const setTenantDeleteId = (event) => {

    let target = event.target;
    
    if(target.nodeName == "I") {
        target = target.parentElement; }
    
    tenantDeleteBtn.value = target.parentElement.parentElement.firstElementChild.innerText;
    
}

const deleteTenant = async (event) => {

    let target = event.target;
    
    deleteObject("arrendatario/".concat(tenantDeleteBtn.value)).then( (res, rej) => {

        if(res.ok) { 
        
            document.querySelector(`#tenant-${tenantDeleteBtn.value}`).style.display = "none";
            showNotification("Usuario eliminado correctamente", "success");
        
        }

        return paginador.getPage(paginador.currentPageId);

    }).then( (res, rej) => {

        addTenants(res);

    });

    target.previousElementSibling.click();

}

const newTenantFromTemplate = (tenant) => {

    return `
        <tr id="tenant-${tenant.id}">
            <td class="text-center">${tenant.id}</td>\
            <td>${tenant.usuario.nombres} ${tenant.usuario.apellidos}</td>
            <td>${tenant.usuario.correo}</td>
            <td>${tenant.usuario.estado ? 'Activo' : 'Inactivo'}</td>
            <td class="text-right">${tenant.empleado ? 
                tenant.empleado.usuario.nombres.concat(' ').concat(tenant.empleado.usuario.apellidos) 
                : 'No asignado'}
            </td>
            <input type="hidden" value="${tenant.usuario.cedula}">
            <input type="hidden" value="${tenant.usuario.usuario}">
            <input type="hidden" value="${tenant.usuario.descripcion}">
            <input type="hidden" value="${tenant.usuario.urlImagenPerfil ? tenant.usuario.urlImagenPerfil 
                : 'http://style.anu.edu.au/_anu/4/images/placeholders/person_8x10.png'} ">
            <td class="td-actions text-right">
                <button type="button" rel="tooltip"
                    class="btn btn-info tenant-aditional-info-btn" data-toggle="modal"
                    data-target="#userDetailsModal">
                    <i class="material-icons">person</i>
                </button>
                <button type="button" rel="tooltip" class="btn btn-success">
                    <a href="/arrendatario/${tenant.id}"
                        style="text-decoration: none; color: white;">
                        <i class="material-icons">edit</i>
                    </a>
                </button>
                <button type="button" rel="tooltip" data-toggle="modal"
                    data-target="#modal-confirm-delete"
                    class="btn btn-danger delete-tenant-btn">
                    <i class="material-icons">delete_forever</i>
                </button>
            </td>
        </tr>`;

}

window.addEventListener("load", (event) => {

    tbodyTenants.querySelectorAll("tr")
        .forEach( (tr) => { addTenantListeners(tr) } );
    paginador = new Paginator("arrendatario", document.querySelector(".pagination"));

});

tenantDeleteBtn.addEventListener("click", deleteTenant);
paginatorNext.addEventListener("click", nextTenantsPage);
registerTenant.addEventListener("click", nuevoArrendatario);
addTenantBtn.addEventListener("click", toggleAddArrendatario);
paginatorPrevious.addEventListener("click", previousTenantsPage);
