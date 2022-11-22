const app = Vue.createApp({
    data() {
        return {
            url: "/api/clients/current",
            accounts: [],
            clients: [],
            amount: '',
            description: "",
            numberDestiny: "",
            numberOrigin: "",
            error: "",
            accountSelect: ""
        }
    },
    created() {
        this.loadData(this.url);
    },
    methods: {
        loadData(url) {
            axios.get(url)
                .then((res) => {
                    this.clients = res.data;
                    this.accounts = this.clients.accounts.sort((a, b) => { return a.id - b.id; });
                })
        },
        transformNumbers(num) {
            return numberTransform = new Intl.NumberFormat('es-AR', { style: 'currency', currency: 'ARS' }).format(num);
        },
        sendTransfer() {
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this transaction",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, transfer'
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire(
                        'Send!',
                        'Your transfer has been sent.',
                        'success'
                    )
                    axios.post('/api/transactions', `amount=${this.amount}&description=${this.description}&numberDestiny=${this.numberDestiny}&numberOrigin=${this.numberOrigin}`)
                        .then(() => {
                            setTimeout(() => {
                                window.location.reload()
                            }, 1000);
                        })
                        .catch(err => {
                            this.error = err.response.status;
                            if (this.error == 400) {
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
                                    title: 'Missing Amount data'
                                })
                            } else {
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

                }
            })
        },
        selectAccount(value) {
            this.accountSelect = value;
        }
    },

    computed: {

    },
}).mount('#app');