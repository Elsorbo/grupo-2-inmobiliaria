

let notificationTemplate = "";

const getFormValues = (form) => {

    let formValues = {};
    let inputs = [ ...form ].slice(0, -1);

    inputs.map( i => formValues[i.name] = i.value );

    return formValues;

}

const getData = async (resource) => {

    let response = await fetch(`/${resource}`);

    return response.json();

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

const sendJSONData = async (resource, method, data) => {

    let headers = new Headers();
    headers.append("Content-Type", "application/json");
    
    let response = await fetch(`/${resource}`, {
    
        "method": `${method}`,
        "headers": headers,
        "body": JSON.stringify(data)
    
    });
    
    return response.json();

}

const uploadFiles = async (resource, method, name, files) => {

    let formData = new FormData();
    
    for(let x = files.length - 1; x >= 0; x--) {
        formData.append(name, files[x]); }

    let response = await fetch(`/${resource}`, {
    
        "method": `${method}`,
        "body": formData
    
    });
    
    return response.json();

}

export { 

    uploadFiles,
    getData,
    getFormValues, 
    showNotification, 
    sendJSONData,

};
