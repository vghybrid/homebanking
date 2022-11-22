const app = Vue.createApp({
    data() {
        return {
            formLogin: {
                email: "",
                password: ""
            },
            formRegister: {
                firstName: "",
                lastName: "",
                email: "",
                password: ""
            },
            error: "",
        }
    },
    created() {

    },
    methods: {
        login() {
            axios.post('/api/login', "email=" + this.formLogin.email + "&password=" + this.formLogin.password)
                .then(() => window.location.href = '/web/accounts.html')
        },
        register() {
            axios.post('/api/clients', `firstName=${this.formRegister.firstName}&lastName=${this.formRegister.lastName}&email=${this.formRegister.email}&password=${this.formRegister.password}`)
                .then(() => {
                    this.formLogin.email = this.formRegister.email
                    this.formLogin.password = this.formRegister.password
                    this.login()
                })
                .catch(err => {
                    if (this.error == "403") {
                        console.log(err);
                        const Toast = Swal.mixin({
                            toast: true,
                            position: 'top-end',
                            showConfirmButton: false,
                            timer: 3000,
                            timerProgressBar: true,
                            background: '#19191a',
                            color: '#c2c2c2',
                            didOpen: (toast) => {
                                toast.addEventListener('mouseenter', Swal.stopTimer)
                                toast.addEventListener('mouseleave', Swal.resumeTimer)
                            }
                        })
                        Toast.fire({
                            icon: 'error',
                            title: err.response.data
                        })
                    }
                })
        },
        showPassword(idPass, idSpan) {
            let pswrd = document.getElementById(idPass);
            let toggleBtn = document.getElementById(idSpan);
            if (pswrd.type === 'password') {
                pswrd.setAttribute('type', 'text');
                toggleBtn.classList.add('hidden');
            } else {
                pswrd.setAttribute('type', 'password');
                toggleBtn.classList.remove('hidden');
            }
        }
    },

    computed: {

    },
}).mount('#app');