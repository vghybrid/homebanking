const app = Vue.createApp({
    data() {
        return {
            url: "/api/clients/current",
            urlAccount: "/api/accounts",
            clients: [],
            cuentas: [],
            accounts: [],
            cards: [],
            transactions: [],
            loans: [],
            credit: [],
            debit: [],
            typeCard: '',
            typeColor: '',
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
                    this.loans = this.clients.loans;
                    this.cards = this.clients.cards;
                    this.credit = this.clients.cards.filter(item => item.type == "CREDIT");
                    this.debit = this.clients.cards.filter(item => item.type == "DEBIT");
                    this.accounts = this.clients.accounts.sort((a, b) => { return a.id - b.id; })
                    this.transactionsTable();
                })
        },
        transactionsTable() {
            let arrayTransactions = this.accounts.map(item => item.transactions.forEach(trans => this.transactions.push(trans)));
        },
        transformNumbers(num) {
            return numberTransform = new Intl.NumberFormat('es-AR', { style: 'currency', currency: 'ARS' }).format(num);
        },
        dateFormat(date) {
            let year = date.slice(8, 10);
            let month = date.slice(3, 5);
            return month + "/" + year;
        },
        logout() {
            axios.post('/api/logout').then((res) => window.location.href = '/web/index.html');
        },
        createAccount() {
            axios.post('/api/clients/current/accounts', `type=CURRENT`).then(() => this.loadData(this.url));
        },
        createCard() {
            axios.post('/api/clients/current/cards', `cardType=${this.typeCard}&cardColor=${this.typeColor}`).then(() => window.location.href = "/web/cards.html");
        },
        deleteAccount(number) {
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire(
                        'Deleted!',
                        'Your account has been deleted.',
                        'success',
                        axios.patch("/api/clients/current/accounts", `number=${number}`)
                            .then(() => this.loadData(this.url))
                    )
                }
            })

        },
        deleteCard(number){
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire(
                        'Deleted!',
                        'Your card has been deleted.',
                        'success',
                        axios.patch("/api/clients/current/cards", `number=${number}`)
                            .then(() => this.loadData(this.url))
                    )
                }
            })
        }
    }
}).mount('#app');