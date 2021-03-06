
import {getData} from './utils.js';

class Paginator {

    constructor(resource, htmlPaginator){

        this.resource = resource;
        this.last = false;
        this.first = true;
        this.nextPageId = 2;
        this.totalElements = parseInt(htmlPaginator.lastElementChild.value);
        this.currentPageId = 1;
        this.previousPageId = 0;

        this.htmlPaginator = htmlPaginator;

    }
    
    async lastPage() {

        let response = await getData(
            `${this.resource}?pageNumber=${Math.ceil((this.totalElements + 1) / 5) - 1}`);
        
        if(response.ok) {

            let pageResponse = await response.json();
            
            this.last = true;
            this.first = false;
            this.nextPageId = pageResponse.totalPages + 1;
            this.currentPageId = pageResponse.totalPages;
            this.previousPageId = this.currentPageId - 1;
            this.totalElements = pageResponse.totalElements;

            this.htmlPaginator.children[4].classList.add("disabled");
            this.htmlPaginator.children[1].children[0].innerText = this.previousPageId;
            this.htmlPaginator.children[2].children[0].textContent = this.currentPageId;
            this.htmlPaginator.children[3].children[0].textContent = this.nextPageId;
            this.htmlPaginator.children[3].children[0].innerText = "-";

            if(this.currentPageId == 1) { 
                this.htmlPaginator.children[0].classList.add("disabled"); 
            } else { 
                this.htmlPaginator.children[0].classList.remove("disabled"); }

            return pageResponse.content;

        }

        return [];

    }

    async getPage(page) {

        let response = await getData(`${this.resource}?pageNumber=${page - 1}`);

        if(response.ok) { 

            let pageResponse = await response.json()

            return pageResponse.content;

        }

        return [];

    }
    
    async nextPage() {
        
        let response = await getData(`${this.resource}?pageNumber=${this.nextPageId - 1}`);
        
        if(response.ok) {
            
            let pageable = await response.json();
            
            if(!this.last) {
            
                this.previousPageId = this.currentPageId;
                this.currentPageId = this.nextPageId;
                this.nextPageId = this.currentPageId + 1;
                this.last = pageable.last;
                this.first = pageable.first;

                this.htmlPaginator.children[0].classList.remove("disabled");
                this.htmlPaginator.children[1].children[0].innerText = this.previousPageId;
                this.htmlPaginator.children[2].children[0].textContent = this.currentPageId;
                this.htmlPaginator.children[3].children[0].textContent = this.nextPageId;        
            
            } 
            
            if(this.last) {
                
                this.htmlPaginator.children[3].children[0].innerText = "-";
                this.htmlPaginator.children[4].classList.add("disabled");

            }

            return pageable.content;
            
        }

    }

    async previousPage() {

        let response = await getData(`${this.resource}?pageNumber=${this.previousPageId - 1}`);
        
        if(response.ok) {
            
            let pageable = await response.json();
            
            if(!this.first) {

                this.previousPageId = pageable.number;
                this.currentPageId = pageable.number + 1;
                this.nextPageId = pageable.number + 2;
                this.first = pageable.first;
                this.last = pageable.last;

                this.htmlPaginator.children[4].classList.remove("disabled");
                this.htmlPaginator.children[1].children[0].innerText = this.previousPageId;
                this.htmlPaginator.children[2].children[0].textContent = this.currentPageId;
                this.htmlPaginator.children[3].children[0].textContent = this.nextPageId;        
            
            }
            
            if(this.first) {
                
                this.htmlPaginator.children[0].classList.add("disabled");
                this.htmlPaginator.children[1].classList.add("disabled");
                this.htmlPaginator.children[1].children[0].innerText = "-";

            }

            return pageable.content;
            
        }

    }

}

export default Paginator;
