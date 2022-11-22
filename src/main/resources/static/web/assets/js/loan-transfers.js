const app = Vue.createApp({
    data() {
        return {
            url: "/api/clients/current",
            amount: "",
            payment: "",
            getLoans: "",
            getAccounts: "",
            numberAccount: '',
            interest: 0,
            getAccount: [],
            destinyAccount: [],
            payments: {},
            loans: [],
            loan: [],
            clients: [],
            maxAmount: "",
        }
    },
    created() {
        this.loadLoans();
        this.loadAccount();
        this.loadData(this.url);
    },
    methods: {
        loadData(url) {
            axios.get(url)
                .then((res) => {
                    this.clients = res.data;
                })
        },
        loadLoans() {
            axios.get('/api/loans')
                .then(res => {
                    this.loans = res.data;
                    this.payments = this.loans.filter(loan => loan.id == this.getLoans);
                    this.payments = this.payments[0].payments;
                    this.maxAmount = this.loans.filter(loan => loan.id == this.getLoans);
                    this.maxAmount = this.maxAmount[0].amount;
                })
        },
        loadAccount() {
            axios.get("/api/clients/current")
                .then(response => {
                    this.destinyAccount = response.data.accounts
                })
        },
        transformNumbers(num) {
            return numberTransform = new Intl.NumberFormat('es-AR', { style: 'currency', currency: 'ARS' }).format(num);
        },
        createLoan() {
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, transfer'
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire(
                        'Send!',
                        'Your loan has been created.',
                        'success'
                    )
                    axios.post('/api/loans', {
                        idLoan: this.getLoans,
                        amount: this.amount,
                        payments: this.payment,
                        numberAccount: this.numberAccount,
                    })
                        .then( () => window.location.href = '/web/accounts.html')
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
        calculateInteres(loan) {
            if (loan == 1) {
                this.interest = this.amount * 1.20;
                return this.transformNumbers(this.interest);
            } else if (loan == 3) {
                this.interest = this.amount * 1.15;
                return this.transformNumbers(this.interest);
            } else if (loan == 2) {
                this.interest = this.amount * 1.10;
                return this.transformNumbers(this.interest);
            }
        },
    },
    computed: {

    },
}).mount('#app');