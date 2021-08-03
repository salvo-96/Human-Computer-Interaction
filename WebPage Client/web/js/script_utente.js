new Vue({
    el: '#section_output',
    data: {
        jsessionid: null,
        info_utente: "Caricamento..",
        sezione: "",
        elenco: false,

        message: "",

        button_disdici: "",
        button_completata: "",

        corsi: null,
        professori: null,
        ripetizioni: null,

        titolo_corso: "",
        id_professore: "",
        giorno: "",
        ora: "",

        button: false,
        button_prenota: false,
        select_prof: false,
        select_giorno: false,

        in_corso: null,
        annullate: null,
        completate: null
    },
    methods: {
        get_info_utente: function () {
            var self = this;
            var vet = window.location.href.split(";jsessionid=");
            if (vet.length === 1) {
                alert("Non puoi visualizzare la pagina se non effettui correttamente il login");
                $.post("../servlet_server", {servlet: "render_index"},
                    function (data) {
                        window.location.href = data;
                    }
                );
            } else {
                self.jsessionid = vet[1];
                $.post("../servlet_server;jsessionid=" + self.jsessionid, {servlet: "session", submit: "info"},
                    function (data) {
                        self.info_utente = JSON.parse(data);
                        self.show_corsi();
                    }
                );
            }
        },
        show_corsi: function () {
            var self = this;
            $.post("../servlet_server", {servlet: "index", operazione: "get_all_corsi"},
                function (data) {
                    self.corsi = JSON.parse(data);
                    self.sezione = "corsi";
                    self.button_prenota = false;
                }
            );
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
            self.message = "";
            self.button_prenota = false;
            $.post("../servlet_server", {servlet: "index", operazione: "get_all_corsi"},
                function (data) {
                    self.corsi = JSON.parse(data);
                    self.sezione = "ripetizioni";
                }
            );
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
                        self.message = "";
                        self.elenco = false;
                    }
                }
            );
        },
        show_giorno: function () {
            var self = this;
            self.giorno = "";
            self.select_giorno = true;
            self.elenco = false;
        },
        show_button: function () {
            var self = this;
            self.button = true;
            self.button_prenota = false;
            self.elenco = false;
        },
        get_elenco: function () {
            var self = this;
            if (self.id_professore === "" || self.titolo_corso === "") {
                self.message = "Selezionare entrambi i campi";
            }
            $.post("../servlet_server", {
                    servlet: "index",
                    operazione: "get_ripetizioni_from_giorno",
                    giorno: self.giorno
                },
                function (data) {
                    if (data === "[]") {
                        self.message = "Al momento non sono disponibili ripetizioni";
                        self.ripetizioni = null;
                        self.elenco = false;
                    } else {
                        self.ripetizioni = JSON.parse(data);
                        self.elenco = true;
                        self.message = "";
                    }
                }
            );
        },
        prenota: function () {
            var self = this;
            if (self.ora === "") {
                self.message = "Selezionare un'orario";
            } else {
                $.post("../servlet_server;jsessionid=" + self.jsessionid, {
                        servlet: "user", operazione: "prenota",
                        id_professore: self.id_professore,
                        titolo_corso: self.titolo_corso,
                        data: self.giorno,
                        ora: self.ora,
                    },
                    function (data) {
                        if (data === "true") {
                            self.message = "Prenotata";
                        } else {
                            self.message = "Non prenotata. Non più disponibile";
                        }
                    }
                );
            }
        },
        disdici: function (data, ora) {
            var self = this;
            $.post("../servlet_server", {servlet: "user", operazione: "disdici", data: data, ora: ora},
                function (data) {
                    if (data === "true") {
                        self.message = "Prenotazione annullata";
                        $.post("../servlet_server;jsessionid=" + self.jsessionid, {
                                servlet: "user",
                                operazione: "get_ripetizioni_annullate"
                            },
                            function (data) {
                                self.annullate = JSON.parse(data);
                            });
                        $.post("../servlet_server;jsessionid=" + self.jsessionid, {
                                servlet: "user",
                                operazione: "get_ripetizioni_in_corso"
                            },
                            function (data) {
                                self.in_corso = JSON.parse(data);
                            });
                    } else {
                        self.message = "Operazione non riuscita";
                    }
                }
            );
        },
        completata: function (data, ora) {
            var self = this;
            $.post("../servlet_server", {servlet: "user", operazione: "completata", data: data, ora: ora},
                function (data) {
                    if (data === "true") {
                        self.message = "Prenotazione completata";
                        $.post("../servlet_server;jsessionid=" + self.jsessionid, {
                                servlet: "user",
                                operazione: "get_ripetizioni_completate"
                            },
                            function (data) {
                                self.completate = JSON.parse(data);
                            });
                        $.post("../servlet_server;jsessionid=" + self.jsessionid, {
                                servlet: "user",
                                operazione: "get_ripetizioni_in_corso"
                            },
                            function (data) {
                                self.in_corso = JSON.parse(data);
                            });
                    } else {
                        self.message = "Operazione non riuscita";
                    }
                }
            );
        },
        show_elenco: function () {
            var self = this;
            self.message = "";
            self.button_prenota = false;
            $.post("../servlet_server;jsessionid=" + self.jsessionid, {
                    servlet: "user",
                    operazione: "get_ripetizioni_in_corso"
                },
                function (data) {
                    self.in_corso = JSON.parse(data);
                });
            $.post("../servlet_server;jsessionid=" + self.jsessionid, {
                    servlet: "user",
                    operazione: "get_ripetizioni_completate"
                },
                function (data) {
                    self.completate = JSON.parse(data);
                });
            $.post("../servlet_server;jsessionid=" + self.jsessionid, {
                    servlet: "user",
                    operazione: "get_ripetizioni_annullate"
                },
                function (data) {
                    self.annullate = JSON.parse(data);
                });
            self.sezione = "elenco";
        },
        logout: function () {
            var self = this;
            $.post("../servlet_server;jsessionid=" + self.jsessionid, {servlet: "session", submit: "logout"});
        },
        show_prenota: function () {
            var self = this;
            self.button_prenota = true;
        }
    },
    mounted() {
        this.get_info_utente();
        this.show_corsi();
    }
});