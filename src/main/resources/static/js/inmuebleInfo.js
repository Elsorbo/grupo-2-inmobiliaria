
let zona = "";

let locacionLabel = document.querySelector("#locacion-label")
let inmueblesContainer = document.querySelector("#inmuebles-container");
let checkBoxsLabels = document.querySelectorAll("input[type=radio]+label");

const getImueblesByZone = async (event) => {

    let target = event.target;

    target.parentElement.lastElementChild.click();

}

const getInmuebleFromTemplate = (inmueble) => {

    return `
    <div class="w3-third w3-margin-bottom">
        <img src="${inmueble.fotos[0].url_foto}" alt="Norway" style="width:100%">
        <div class="w3-container w3-white card-container">
        <h3>${inmueble.titulo}</h3>
        <h6 class="w3-opacity">$${inmueble.precio}</h6>
        <p>${inmueble.localidad}</p>
        <p>${inmueble.area} m<sup>2</sup></p>
        <p class="w3-large"><i class="fa fa-bath"></i> <i class="fa fa-phone"></i> <i class="fa fa-wifi"></i></p>
        <button class="w3-button w3-block w3-black w3-margin-bottom">Ver detalles</button>
        </div>
    </div>`;

}

window.addEventListener("load", (event) => {

    let index = location.href.indexOf("?")

    if(index > 0) {
    
        zona = location.href.substr(index + 6).replaceAll("+", " ");
            
        let label = [...checkBoxsLabels].filter(l => l.innerText == zona)[0];
        label ? label.previousElementSibling.checked = true : '';

    }

    checkBoxsLabels.forEach( label => {
        label.previousElementSibling.addEventListener("click", getImueblesByZone)});

});
