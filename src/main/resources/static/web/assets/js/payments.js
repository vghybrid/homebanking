const { createApp } = Vue

createApp({
    data() {
        return {
            accountNumber: "",
            amount: "",
            cardNumber: "",
            cvv: "",
            account: "",
        }
    },
    created() {

    },
    methods: {
        loadData(url) {
            axios.get(url)
                .then((res) => {
                    this.clients = res.data;
                    this.accounts = this.clients.accounts.sort((a, b) => { return a.id - b.id; })
                })
        },
    }
}).mount('#app')