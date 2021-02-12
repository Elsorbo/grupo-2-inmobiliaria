
import {
    getFormValues, showNotification, sendJSONData, uploadFiles} 
from "../utils.js";

let profilePic = document.querySelector("#accountImage");

const updatePerfil = async (event) => {

    event.preventDefault();
    
    let target = event.target;
    let userValues = getFormValues(target.parentElement);    
    let details = document.querySelector("#perfilDetails");
    
    let l = await sendJSONData("perfil", "put", userValues);
    console.log(l)  
    details.previousElementSibling.textContent = `
        ${userValues.nombres} ${userValues.apellidos}`
    details.textContent = userValues.descripcion;
    
    showNotification("El perfil se ha actualizado correctamente", "success");

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
    
        let res = await uploadFiles("updatepic", "put", "picture", inputFile.files);
        console.log(res);
        profilePic.setAttribute("src", res.url);
    
    }

}

profilePic.addEventListener("load", (e) => {
            
    profilePic.previousElementSibling.style.visibility = "hidden";
    profilePic.style.visibility = "visible";
    
});

document.querySelector("#inputImage").addEventListener("change", updateImage);
document.querySelector("#updatePerfil").addEventListener("click", updatePerfil);
document.querySelector("#updateImage").addEventListener("click", 
    (e) => e.target.nextElementSibling.click());
