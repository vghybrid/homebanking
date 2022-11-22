const app = Vue.createApp({
    data() {
        return {
            account: {},
            transaction: [],
            id: (new URLSearchParams(location.search)).get("id"),
            url: "http://localhost:8080/api/accounts/",
        }
    },
    created() {
        this.getData(this.url + this.id);
    },
    methods: {
        getData(url) {
            axios.get(url)
                .then((res) => {
                    this.account = res.data;
                    this.transaction = this.account.transactions.map(item => item).sort((a, b) => { return a.id - b.id; });
                })
        },
        logout() {
            axios.post('/api/logout').then((res) => window.location.href = '/web/index.html');
        },
        transformNumbers(num) {
            return numberTransform = new Intl.NumberFormat('es-AR', { style: 'currency', currency: 'ARS' }).format(num);
        }
    },
}).mount('#app');