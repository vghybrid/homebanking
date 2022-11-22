const app = Vue.createApp({
    data() {
        return {
            url: 'http://localhost:8080/rest/clients',
            info: null,
            clientes: [],
            firstName: "",
            lastName: "",
            email: "",
            editar: true,
            id: null,
        }
    },
    created() {
        this.loadData(this.url)
    },
    mounted() {
    },
    methods: {
        loadData(url) {
            axios.get(url)
                .then( res => {
                    this.info = res;
                    this.clientes = this.info.data._embedded.clients;
                })
                .catch(error => console.error(error.message));
        },
        agregarCliente(){
            if(this.firstName.length != 0 && this.lastName != 0 && this.email != 0){
                let cliente = {
                    firstName: this.firstName,
                    lastName: this.lastName,
                    email: this.email
                }
                this.postCliente(cliente)
                this.firstName = ""
                this.lastName = ""
                this.email = ""
            }
        },
        postCliente(cliente){
            axios.post(this.url, cliente).then(()=>{
                this.loadData(this.url)
            })
        },editarCliente(cliente){
            this.id = cliente._links.self.href
            this.firstName = cliente.firstName
            this.lastName = cliente.lastName
            this.email = cliente.email
            this.editar = false
        },actulizarCliente(){
            axios.put(this.id, {firstName: this.firstName, lastName: this.lastName, email: this.email}).then(()=>{
                this.loadData(this.url)
                this.firstName = ""
                this.lastName = ""
                this.email = ""
                this.editar = true
            })
        }
    }
}).mount('#app')