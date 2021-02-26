
import {
    sendJSONData, deleteObject, showNotification
} from "../utils.js";

import Paginator from "../Paginator.js";


let paginador = {};

let addEmployeeBtn = document.querySelector("#addEmployee");
let cardInfoBody = document.querySelector("#card-info-body");
let paginatorNext = document.querySelector("#paginator-next");
let showEmployee = document.querySelector("#showAddEmployee");
let registerEmployee = document.querySelector("#registerEmployee");
let tbodyEmployees = document.querySelector("#body-table-employees");
let paginatorPrevious = document.querySelector("#paginator-previous");
let employeeDeleteInputId = document.querySelector("#employee-delete");

const newEmployee = async (event) => {
    
    event.preventDefault();

    let target = event.target;
    let form = target.parentElement.parentElement;
    
    if( form.checkValidity() ) {

        let formValues = {
        
            "telefono":  form.telefono.value,
            "usuario": {

                "usuario": form.usuario.value,
                "cedula": form.cedula.value,
                "nombres": form.nombres.value,
                "apellidos": form.apellidos.value,
                "correo": form.correo.value,
                "contrasena": form.contrasena.value

            }
        
        };

        let response = await sendJSONData("empleado", "post", formValues);
        
        if(response.ok) {

            ++paginador.totalElements;
            let employee = await response.json()
            
            if(tbodyEmployees.children.length < 5) {
            
                tbodyEmployees.innerHTML += newEmployeeFromTemplate(employee);
                addEmployeeListeners(tbodyEmployees.lastElementChild);
            
            } else {
                addEmployees( await paginador.lastPage() ); }
            
            showNotification("Se registro el empleado exitosamente", "success");
            [...form].forEach( i => i.value = "");
        
        } else {
            
            let errors = await response.json();
            errors.map( error => {
                showNotification(error.defaultMessage, "danger") }); 
        
        }
        
    } else {
        showNotification("Algunos campos del formulario son incorrectos\
             o están vacios", "danger"); }
    
}

const addEmployees = (employees) => {

    if(employees.length > 0) {
        
        let htmlEmployees = employees.map( (employee) => {
            return newEmployeeFromTemplate(employee); });
    
        tbodyEmployees.innerHTML = htmlEmployees.join("");
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

const toggleAddEmployee = (event) => {

    let target = event.target;
    
    if(addEmployeeBtn.style.display != "none") {
    
        addEmployeeBtn.style.display = "none";
        target.innerHTML = "<i class='material-icons'>add</i> Añadir empleado";
    
    } else {

        addEmployeeBtn.style.display = "block";
        target.innerHTML = "<i class='material-icons'>remove</i> Ocultar panel";

    }

}

const setEmployeeAditionalInfo = (event) => {

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

const setEmployeeDeleteId = (event) => {

    let target = event.target;
    
    if(target.nodeName == "I") {
        target = target.parentElement; }
    
    employeeDeleteInputId.value = target.parentElement.parentElement.firstElementChild.innerText;

}

const deleteEmployee = async (event) => {

    let target = event.target;
    
    deleteObject("empleado/".concat(employeeDeleteInputId.value)).then( (res, rej) => {

        if(res.ok) { 
        
            document.querySelector(`#employee-${employeeDeleteInputId.value}`)
                .style.display = "none";
            
            showNotification("Usuario eliminado correctamente", "success");
        
        }

        return paginador.getPage(paginador.currentPageId);

    }).then( (res, rej) => {

        addEmployees(res);

    });

    target.previousElementSibling.click();

}

const newEmployeeFromTemplate = (employee) => {

    return (
    `<tr id="employee-${employee.id}">
        <td class="text-center">${employee.id}</td>
        <td>${employee.usuario.nombres} ${employee.usuario.apellidos}</td>
        <td>${employee.usuario.correo}</td>
        <td>${employee.usuario.estado ? "Activo" : "Inactivo"}</td>
        <td class="text-right">${employee.telefono}</td>
        <input type="hidden" value="${employee.usuario.cedula}">
        <input type="hidden" value="${employee.usuario.usuario}">
        <input type="hidden" value="${employee.usuario.descripcion}">
        <input type="hidden" value="${employee.usuario.urlImagenPerfil ? 
            employee.usuario.urlImagenPerfil : 
            'http://style.anu.edu.au/_anu/4/images/placeholders/person_8x10.png'}">
        <input type="hidden" 
            value="${new Date(employee.usuario.fechaActualizacion)}">
        <td class="td-actions text-right">
            <button type="button" rel="tooltip" 
                class="btn btn-info employee-aditional-info-btn" 
                data-toggle="modal" data-target="#userDetailsModal">
                <i class="material-icons">person</i>
            </button>
            <button type="button" rel="tooltip" class="btn btn-success">
                <a href="/empleado/${employee.id}" 
                    style="text-decoration: none; color: white;">
                    <i class="material-icons">edit</i>
                </a>
            </button>
            <button type="button" rel="tooltip" data-toggle="modal" 
                data-target="#modal-confirm-delete" 
                class="btn btn-danger delete-employee-btn">
                <i class="material-icons">delete_forever</i>
            </button>
        </td>
    </tr>`);

}

const addEmployeeListeners = (employee) => {
    
    let deleteBtn = employee.querySelector(".delete-employee-btn");
    let infoBtn = employee.querySelector(".employee-aditional-info-btn");
    
    deleteBtn.addEventListener("click", setEmployeeDeleteId);
    infoBtn.addEventListener("click", setEmployeeAditionalInfo);

}

window.addEventListener("load", (event) => {

    tbodyEmployees.querySelectorAll("tr")
        .forEach( tr => addEmployeeListeners(tr));

    paginador = new Paginator("empleado", document.querySelector(".pagination"));
        
});

registerEmployee.addEventListener("click", newEmployee);
paginatorNext.addEventListener("click", nextEmployeePage)
showEmployee.addEventListener("click", toggleAddEmployee);
employeeDeleteInputId.addEventListener("click", deleteEmployee);
paginatorPrevious.addEventListener("click", previousEmployeePage);
