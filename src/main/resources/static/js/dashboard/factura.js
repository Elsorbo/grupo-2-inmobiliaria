
let selectTenant = document.querySelector("#select-tenant");

const getTenantReceipts = (event) => {

    // To Do...

}

if( selectTenant ) {

    selectTenant.selectedIndex = -1;
    selectTenant.addEventListener("change", getTenantReceipts);

}
