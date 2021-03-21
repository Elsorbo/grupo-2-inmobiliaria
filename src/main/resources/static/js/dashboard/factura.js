
import { getData } from "../utils.js";

let selectTenant = document.querySelector("#select-tenant");
let receiptCardContainer = document.querySelector("#recibo-card-container");

const getTenantReceipts = async (event) => {

    let target = event.target;
 
    let request = await getData(`recibo?tenantid=${target.value}`);

    if(request.ok) {

        let receipts = await request.json();
        let htmlReceipts = receipts.map( receipt => receiptFromTemplate(receipt) );
        receiptCardContainer.innerHTML = htmlReceipts.join("");

    }

}

const receiptFromTemplate = (receipt) => {

    return `
    <div class="card" style="width: 20rem; margin: 12px;">
        <img class="card-img-top" src="${receipt.urlImagen}" 
            rel="nofollow" alt="Card image cap" style="height: 260px;">
        <div class="card-body">
            <p class="card-text">
                <strong>Arrendatario: </strong>
                ${receipt.arrendatario.usuario.nombres}
                ${receipt.arrendatario.usuario.apellidos}
            </p>
            <p class="card-text">
                <strong>Periodo: </strong>${receipt.periodoPago}
            </p>
            <a href="/recibo/${receipt.id}" class="card-link" target="_blank">Generar factura</a>
        </div>
    </div>`;

}

if( selectTenant ) {
    
    selectTenant.selectedIndex = -1;
    selectTenant.addEventListener("change", getTenantReceipts);

}
