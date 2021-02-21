
import {getData} from "./utils.js";

let zona = "";

let locacionLabel = document.querySelector("#locacion-label")
let checkBoxsLabels = document.querySelectorAll("input[type=radio]+label");

const getImueblesByZone = (event) => {

    let target = event.target;
    let checkbox;

    [...checkBoxsLabels].forEach( label => {
        
        checkbox = label.previousElementSibling;
        if(checkbox != target) { 
            checkbox.checked = false; }
    
    });

    locacionLabel.textContent = `Locación: ${target.nextElementSibling.textContent}`;

}

window.addEventListener("load", (event) => {

    let index = location.href.indexOf("?")
    
    if(index > 0) {
        
        zona = location.href.substr(index + 6).replaceAll("+", " ");
        history.pushState("", "", location.href.substr(0, index));
        
        let label = [...checkBoxsLabels].filter(label => label.innerText == zona)[0];
        label.previousElementSibling.checked = true;
    
    }

    checkBoxsLabels.forEach( label => {
        label.previousElementSibling.addEventListener("click", getImueblesByZone)});

});
