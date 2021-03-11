
import { 
    sendJSONData, showNotification, getFormValues
} from "../utils.js";

let addDetail = document.querySelector("#add-detail");
let totalBilling = document.querySelector("#total-billing");
let detailsContent = document.querySelector("#details-content");
let generateBillingBtn = document.querySelector("#generate-billing");

const addDetailContent = (event) => {

    event.preventDefault();

    detailsContent.innerHTML += getDetailFromTemplate();

}

const removeDetail = (event) => {
    
    let target = event.target;

    if(target.nodeName == "I") { 
        
        event.stopPropagation();
        target.parentNode.remove();
    
    }

}

const addBilling = async (event) => {

    event.preventDefault();

    let target = event.target;

    if( target.form.checkValidity() ) {
        
        let facturaValues = getFacturaValues(target.form);
        let request = await sendJSONData(
            `factura?reciboID=${target.form.recibo.value}`, "post", facturaValues);

        if( request.ok ) { 
            
            showNotification("Se ha generado la factura exitosamente", "success");
            window.location.replace("/facturas");
            
        } else { 
            showNotification("No se pudo crear la factura", "danger"); }
    
    } else {
        showNotification("Algunos campos del formulario son incorrectos\
             o están vacios", "danger"); }
    

}

const getFacturaValues = (form) => {

    let montos = [...form.querySelectorAll("input[name=monto]")].map( m => m.value );
    let conceptos = [...form.querySelectorAll("input[name=concepto]")].map( c => c.value );
    let detalles = [];
    
    conceptos.forEach( (c, i) => {
        detalles.push({
            "concepto": c, 
            "monto": parseInt(montos[i])
        });
    });
    
    return {
        "arrendatario": {"id": form.arrendatario.value},
        "descripcion": form.descripcion.value,
        "detalles": detalles,
        "total": montos.reduce( (a, b) => parseInt(a) + parseInt(b), 0)
    };

}

const getDetailFromTemplate = () => {
    
    return `
    <div class="form-row" style="margin-bottom: 12px;">
        <i class="material-icons" style="position: absolute; left: -6px;font-size: 20px;">delete_forever</i>
        <div class="col-md-9">
            <input required type="text" name="concepto" class="form-control" placeholder="Concepto">
        </div>
        <div class="col-md-3">
            <input required type="text" name="monto" class="form-control" placeholder="$$ monto" pattern="^[1-9][0-9]{1,6}$">
        </div>
    </div>`;
    
}

addDetail.addEventListener("click", addDetailContent);
detailsContent.addEventListener("click", removeDetail);
generateBillingBtn.addEventListener("click", addBilling);
