
import {
    getFormValues, showNotification, sendJSONData, uploadFiles} 
from "../utils.js";

let accountId = document.querySelector("#account-id");
let profilePic = document.querySelector("#accountImage");
let perfilDetails = document.querySelector("#perfilDetails");
let usernameInput = document.querySelector("#username-input");

const updateAccount = async (event) => {

    event.preventDefault();
    let target = event.target;
    
    let path = "perfil";
    let accountValues = getFormValues(target.parentElement);
    
    if(accountValues.hasOwnProperty("estado")) {
        accountValues["estado"] = document.querySelector("#account-state").checked; }
    
    if(accountValues.hasOwnProperty("contrasena1")) {
		
        if(accountValues.contrasena1 == accountValues.contrasena2) {
            accountValues["contrasena"] = accountValues.contrasena2; }
        
    }
    
    if(accountValues.telefono) { 
        
        path = `empleado/${accountId.value}`;
        accountValues = loadEmployeeData(accountValues);
        
    }
    
    let response = await sendJSONData(path, "put", accountValues);

    if(response.ok) {

        perfilDetails.previousElementSibling.textContent = `
        ${accountValues.nombres ? accountValues.nombres : accountValues.usuario.nombres} \
        ${accountValues.apellidos ? accountValues.apellidos : accountValues.usuario.apellidos}`;
        perfilDetails.textContent = accountValues.descripcion ? accountValues.descripcion : accountValues.usuario.descripcion;
        showNotification("El perfil se ha actualizado correctamente", "success");
        
    } else {
        let errors = await response.json();
        errors.map( error => showNotification(error.defaultMessage, "danger") );
        showNotification("Ha ocurrido un error al intentar actualizar el perfil", "danger"); }
    
}

const updateImage = async (event) => {

    let inputFile = event.target;
    let fileSizeMB = inputFile.files[0].size / (2048 * 1024);
    
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

    let employeeValues = {
        
        "telefono":  formData.telefono,
        "usuario": {

            "usuario": formData.usuario,
            "cedula": formData.cedula,
            "nombres": formData.nombres,
            "apellidos": formData.apellidos,
            "correo": formData.correo,
            "descripcion": formData.descripcion,
            "estado": document.querySelector("#account-state").checked

        }
    
    };
    
    if(formData.contrasena1 == formData.contrasena2) {
        employeeValues.usuario["contrasena"] = formData.contrasena2; }
    
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
