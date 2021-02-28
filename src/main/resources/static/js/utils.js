

let notificationTemplate = "";

const getFormValues = (form) => {

    let formValues = {};
    let inputs = [ ...form ].slice(0, -1);

    inputs.map( i => formValues[i.name] = i.value );

    return formValues;

}

const getData = async (resource, headers = null) => {
    
    let response = fetch(`/${resource}`);

    return response;

}

const showNotification = (message, type) => {

    notificationTemplate = `\
        <div class="alert alert-${type}\
            alert-with-icon" data-notify="container">\
            <i class="material-icons" data-notify="icon">add_alert</i>\
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">\
                <i class="material-icons">close</i>\
            </button><span data-notify="message">${message}</span>\
        </div>`;

    $.notify({
    },{
        template: notificationTemplate,
        timer: 100
    });

}

const sendJSONData = async (resource, method, data, headers = null) => {

    if(headers == null) {
        headers = new Headers(); }
    
    let tokenResponse = await getData("csrftoken");
    let token = await tokenResponse.json();

    headers.append("Content-Type", "application/json");
        headers.append("X-CSRF-TOKEN", token.key);
    
    let response = fetch(`/${resource}`, {
    
        "method": `${method}`,
        "headers": headers,
        "body": JSON.stringify(data)
    
    });
    
    return response;

}

const uploadFiles = async (resource, method, name, files, headers = null) => {

    let formData = new FormData();
    let tokenResponse = await getData("csrftoken");
    let token = await tokenResponse.json();
    
    if(headers == null) { 
        headers = new Headers(); }
    
    headers.append("X-CSRF-TOKEN", token.key);

    for(let x = files.length - 1; x >= 0; x--) {
        formData.append(name, files[x]); }
    
    let response = await fetch(`/${resource}`, {
    
        "method": `${method}`,
        "headers": headers,
        "body": formData
    
    });
    
    return response;

}

const deleteObject = async (resource, headers = null) => {

    let tokenResponse = await getData("csrftoken");
    let token = await tokenResponse.json();

    if(headers == null) {
        headers = new Headers(); }
    
    headers.append("Content-Type", "application/json");
    headers.append("X-CSRF-TOKEN", token.key);
    
    let response = fetch(`/${resource}`, {
        
        "method": "delete",
        "headers": headers
        
    });

    return response;
    
}

export { 

    uploadFiles,
    getData,
    getFormValues, 
    showNotification, 
    sendJSONData,
    deleteObject
    
};
