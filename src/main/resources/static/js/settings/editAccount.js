
import {
    getFormValues, showNotification, sendJSONData, uploadFiles} 
from "../utils.js";

let profilePic = document.querySelector("#accountImage");
let accountId = document.querySelector("#account-id");
let usernameInput = document.querySelector("#username-input");

const updateAccount = async (event) => {

    event.preventDefault();
    let target = event.target;
    
    let path = "perfil";
    let accountValues = getFormValues(target.parentElement);

    if(accountValues.telefono) { 
    
        path = `empleado/${accountId.value}`;
        accountValues = loadEmployeeData(accountValues);
    
    }

    let details = document.querySelector("#perfilDetails");
    
    let response = await sendJSONData(path, "put", accountValues);
    if(response.ok) {

        details.previousElementSibling.textContent = `
        ${accountValues.nombres ? accountValues.nombres : accountValues.usuario.nombres} \
        ${accountValues.apellidos ? accountValues.apellidos : accountValues.usuario.apellidos}`;
        details.textContent = accountValues.descripcion ? accountValues.descripcion : accountValues.usuario.descripcion;
        showNotification(
            "El perfil se ha actualizado correctamente", "success");
    
    } else {
        showNotification(
            "Ha ocurrido un error al intentar actualizar el perfil", "danger"); }
    

}

const updateImage = async (event) => {

    let inputFile = event.target;
    let fileSizeMB = inputFile.files[0].size / (1024 * 1024);
    
    if( fileSizeMB >  1) {
        showNotification(
            "Por favor elija un archivo de tamaño menor a 1MB", "danger"); }
    else {
        
        profilePic.style.visibility = "hidden";
        profilePic.previousElementSibling.style.visibility = "visible";
    
        let res = await uploadFiles(`updatepic?username=${usernameInput.value}`, 
            "put", "picture", 
            inputFile.files
        );
        let resData = await res.json();
        profilePic.setAttribute("src", resData.url);
    
    }

}

const loadEmployeeData = (formData) => {

    
    debugger;
    let employeeValues = {"usuario": {}, "telefono": "0000000"};
        employeeValues.usuario['usuario'] = formData.usuario;
        employeeValues.usuario['cedula'] = formData.cedula;
        employeeValues.usuario['nombres'] = formData.nombres;
        employeeValues.usuario['apellidos'] = formData.apellidos;
        employeeValues.usuario['correo'] = formData.correo;
        employeeValues.usuario['descripcion'] = formData.descripcion;
        employeeValues.usuario['estado'] = formData.estado == "on" ? true : false;
        // if(formData.contrasena1 === formData.contrasena2) {
        //     employeeValues.usuario['contrasena'] = formData.contrasena; }
        employeeValues.telefono = formData.telefono;

    return employeeValues;

}

profilePic.addEventListener("load", (e) => {
            
    profilePic.previousElementSibling.style.visibility = "hidden";
    profilePic.style.visibility = "visible";
    
});

document.querySelector("#inputImage").addEventListener("change", updateImage);
document.querySelector("#updateAccount").addEventListener("click", updateAccount);
document.querySelector("#updateImage").addEventListener("click", (e) => {
    
    let target = e.target;
    
    if(target.nodeName != "INPUT") { 
        target.nextElementSibling.click(); }

});
