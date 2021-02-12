
import {
    getData, getFormValues, sendJSONData, showNotification
} from "../utils.js";

let addEmployeeBtn = document.querySelector("#addEmployee");
let showEmployee = document.querySelector("#showAddEmployee");
let registerEmployee = document.querySelector("#registerEmployee");
let tbodyEmployees = document.querySelector("#body-table-employees");

const getEmployees = async () => {

    let employees = await getData("empleado");
    
    let htmlEmployees = employees.map( (employee) => {
        
        return newEmployeeFromTemplate(employee);

    });

    tbodyEmployees.innerHTML = htmlEmployees.reduce( (a, b) => a.concat(b));

}

const addEmployee = async (event) => {
    
    event.preventDefault();

    let target = event.target;
    let form = target.parentElement.parentElement;
    
    if(form.checkValidity()) {

        let formValues = {"usuario": {}};
        formValues.usuario['usuario'] = form['usuario.usuario'].value;
        formValues.usuario['cedula'] = form['usuario.cedula'].value;
        formValues.usuario['nombres'] = form['usuario.nombres'].value;
        formValues.usuario['apellidos'] = form['usuario.apellidos'].value;
        formValues.usuario['correo'] = form['usuario.correo'].value;
        formValues.usuario['contrasena'] = form['usuario.contrasena'].value;
        formValues['telefono'] = form.telefono.value;

        let response = await sendJSONData("empleado", "post", formValues);

        tbodyEmployees.innerHTML += newEmployeeFromTemplate(response);
        showNotification("Se registro el empleado exitosamente", "success");
        [...form].forEach( i => i.value = "");
    
    } else {

        showNotification(
            "Algunos campos del formulario son incorrectos o están vacios", "danger");

    }

    
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

const newEmployeeFromTemplate = (employee) => {

    return (
    `<tr>\
        <td class="text-center">\
            ${employee.id}
        </td>\
        <td>${employee.usuario.nombres} ${employee.usuario.apellidos}\
        </td>\
        <td>${employee.usuario.correo}</td>\
        <td>${employee.usuario.estado ? "Activo" : "Inactivo"}</td>\
        <td class="text-right">${employee.telefono}</td>\
        <input type="text" hidden value="${employee.usuario.cedula}">
        <input type="text" hidden value="${employee.usuario.usuario}">
        <input type="text" hidden value="${employee.usuario.descripcion}">
        <input type="text" hidden value="${employee.usuario.urlImagenPerfil}">
        <input type="date" hidden value="${new Date(employee.usuario.fechaActualizacion)}">
        <td class="td-actions text-right">\
            <button id="userDetails" type="button" rel="tooltip" class="btn btn-info"\
                data-toggle="modal" data-target="#userDetailsModal">\
                <i class="material-icons">person</i>\
            </button>\
            <button type="button" rel="tooltip" class="btn btn-success">\
                <a href="/empleado/${employee.id}" style="text-decoration: none; color: white;">
                    <i class="material-icons">edit</i>\
                </a>
            </button>\
            <button type="button" rel="tooltip" class="btn btn-danger">\
                <i class="material-icons">close</i>\
            </button>\
        </td>\
    </tr>`);

}

window.addEventListener("load", getEmployees);
registerEmployee.addEventListener("click", addEmployee);
showEmployee.addEventListener("click", toggleAddEmployee);


const editEmployee = (event) => {

	

}

export {editEmployee};