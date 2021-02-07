
window.addEventListener("load", async => {

    let pdfBtn = document.querySelector("#generatePDF");

    pdfBtn.addEventListener("click", (e) => {

        fetch("/pdf").then((res) => {
            
            return res.json();

        })

    });

});
