new Vue({
    el: '#section_output',
    data: {
        sezione: "",
        elenco: false,

        professori: null,
        corsi: null,
        ripetizioni: null,

        titolo_corso: "",
        id_professore: "",
        giorno: "",

        button: false,
        select_prof: false,
        select_giorno: false,

        email_login: null,
        email_register: null,
        password_login: null,
        password_register: null,
        nome: null,
        cognome: null,

        message: null,
        message_login: null,
        message_register: null
    },
    methods: {
        show_corsi: function () {
            var self = this;
            $.post("../servlet_server", {servlet: "index", operazione: "get_all_corsi"},
                function (data) {
                    self.corsi = JSON.parse(data);
                    self.sezione = "corsi";
                });
            self.message_login = null;
        },
        show_ripetizioni: function () {
            var self = this;
            self.button = false;
            self.select_prof = false;
            self.select_giorno = false;
            self.elenco = false;
            self.titolo_corso = "";
            self.id_professore = "";
            self.giorno = "";
            self.message = null;
            self.message_login = null;
            $.post("../servlet_server", {servlet: "index", operazione: "get_all_corsi"},
                function (data) {
                    self.corsi = JSON.parse(data);
                    self.sezione = "ripetizioni";
                });
        },
        show_register: function () {
            var self = this;
            self.sezione = "register";
            self.message_register = null;
            self.message_login = null;
            self.nome = "";
            self.cognome = "";
            self.email_register = "";
            self.password_register = "";
        },
        filter_prof: function () {
            var self = this;
            $.post("../servlet_server", {
                    servlet: "index",
                    operazione: "get_professore_from_corso",
                    titolo_corso: self.titolo_corso
                },
                function (data) {
                    if (data === "[]") {
                        self.message = "Al momento non ci sono professori che insegnano questo corso.";
                        self.button = false;
                        self.elenco = false;
                        self.select_prof = false;
                        self.select_giorno = false;
                        self.id_professore = "";
                        self.giorno = "";
                    } else {
                        self.professori = JSON.parse(data);
                        self.select_prof = true;
                        self.id_professore = "";
                        self.giorno = "";
                        self.message = null;
                    }
                }
            );
        },
        show_giorno: function () {
            var self = this;
            self.giorno = "";
            self.select_giorno = true;
        },
        show_button: function () {
            var self = this;
            self.button = true;
        },
        get_elenco: function () {
            var self = this;
            if (self.id_professore === "" || self.titolo_corso === "" || self.giorno === "") {
                self.message = "Selezionare tutti i campi";
                self.elenco = false;
            } else {
                $.post("../servlet_server", {
                        servlet: "index",
                        operazione: "get_ripetizioni_from_giorno",
                        giorno: self.giorno
                    },
                    function (data) {
                        if (data === "[]") {
                            self.message = "Al momento non sono disponibili ripetizioni";
                            self.elenco = false;
                        } else {
                            self.ripetizioni = JSON.parse(data);
                            self.elenco = true;
                            self.message = null;
                        }
                    }
                );
            }
        },
        login: function (e) {
            var self = this;
            if (self.email_login && self.password_login) {
                return true;
            } else {
                self.message_login = "Riempire tutti i campi";
                self.message_register = null;
            }
            e.preventDefault();
        },
        register: function (e) {
            var self = this;
            if (self.email_register && self.password_register && self.nome && self.cognome) {
                return true;
            } else {
                self.message_register = "Riempire tutti i campi";
                self.message_login = null;
            }
            e.preventDefault();
        }
    },
    mounted() {
        this.show_corsi();
    }
});