const { createApp } = Vue

createApp({
    data() {
        return {
            accounts: [],
            checked: false,
            datos: [],
            botonEditar: false,
            upperC: false,
            clientes: [],
            formLogin: {
                email: "",
                password: "",
            },
            formRegister: {
                name: "",
                lastName: "",
                email: "",
                password: "",
            },
            status: 0,
            url: "",
            errorEmail: false,
            error: "",
            emailIncompleto: false,
            errorLogin: false,
        }
    },
    created() {

    },
    methods: {
        login() {
            console.log(this.formLogin)
            axios.post('/api/login', "email=" + this.formLogin.email + "&password=" + this.formLogin.password, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    axios.get('/rest/clients')
                        .then(() => window.location.href = '/admin/manager.html')
                        .catch(() => window.location.href = '/web/accounts.html')
                })
                .catch(err => {
                    console.log(err)
                    this.errorLogin = true
                })
        },
        register() {
            const Toast = Swal.mixin({
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true,
                didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer)
                    toast.addEventListener('mouseleave', Swal.resumeTimer)
                }
            })
            console.log(this.formRegister)
            if (
                this.formRegister.email.includes('@hotmail.com') ||
                this.formRegister.email.includes('@gmail.com') ||
                this.formRegister.email.includes('@yahoo.com')
            ) {
                axios.post('/api/clients', `name=${this.formRegister.name}&lastName=${this.formRegister.lastName}&email=${this.formRegister.email}&password=${this.formRegister.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                    .then(response => {
                        this.formLogin.email = this.formRegister.email
                        this.formLogin.password = this.formRegister.password
                        this.login()
                    })
                    .catch((error) => {
                        this.error = error.response.data
                        console.log(this.error)
                        if (this.error == "Missing data") {
                            Toast.fire({
                                icon: 'error',
                                title: 'please fill in the designated information'
                            })
                        }
                        if (this.error == "email already in use") {
                            Toast.fire({
                                icon: 'error',
                                title: this.error
                            })
                        }
                    })
            } else {
                Toast.fire({
                    icon: 'error',
                    title: 'invalid Email'
                })
            }
        }
    }
}).mount('#app')