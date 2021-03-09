
let zona = "";

let locacionLabel = document.querySelector("#locacion-label")
let inmueblesContainer = document.querySelector("#inmuebles-container");
let checkBoxsLabels = document.querySelectorAll("input[type=radio]+label");

const getImueblesByZone = async (event) => {

    let target = event.target;

    target.parentElement.lastElementChild.click();

}

let index = location.href.indexOf("?")

if (index > 0) {

    zona = location.href.substr(index + 6).replaceAll("+", " ");

    let label = [...checkBoxsLabels].filter(l => l.innerText == zona)[0];
    label ? label.previousElementSibling.checked = true : '';

}

checkBoxsLabels.forEach(label => {
    label.previousElementSibling.addEventListener("click", getImueblesByZone)
});
