new Vue({
    el: '#section_output',
    data: {
        jsessionid: null,
        info_utente: "Caricamento..",

        select_prof: true,

        titolo_corso: null,

        corsi: null,
        professori: null,
        ripetizioni: null,

        id_prof: null,
        nome_prof: null,
        cognome_prof: null,

        message: null,
        sezione: "",

        button: false,
        button_prenota: false,
        select_prof: false,
        select_giorno: false,
        elenco: false,

        id_professore: "",
        giorno: "",
        ora: "",

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
            $.post("../servlet_server;jsessionid=" + self.jsessionid, {servlet: "index", operazione: "get_all_corsi"},
                function (data) {
                    self.corsi = JSON.parse(data);
                    self.sezione = "corsi";
                })
        },
        show_ripetizioni: function () {
            var self = this;
            self.button = false;
            self.select_prof = false;
            self.select_giorno = false;
            self.elenco = false;
            self.titolo_corso = null;
            self.id_professore = "";
            self.giorno = "";
            self.message = null;
            $.post("../servlet_server", {servlet: "index", operazione: "get_all_corsi"},
                function (data) {
                    self.corsi = JSON.parse(data);
                    self.sezione = "ripetizioni";
                });
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
                        self.select_prof = false;
                    } else {
                        self.professori = JSON.parse(data);
                        self.select_prof = true;
                        self.id_professore = "";
                        self.giorno = "";
                        self.message = null;
                        self.elenco = false;
                    }
                }
            );
            self.button_prenota = false;
        },
        show_giorno: function () {
            var self = this;
            self.giorno = "";
            self.select_giorno = true;
            self.elenco = false;
            self.button_prenota = false;
        },
        show_button: function () {
            var self = this;
            self.button = true;
            self.button_prenota = false;
            self.elenco = false;
        },
        get_elenco: function () {
            var self = this;
            if (self.id_professore === "" || self.titolo_corso === null) {
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
                    } else {
                        self.ripetizioni = JSON.parse(data);
                        self.elenco = true;
                        self.message = null;
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
                        ora: self.ora
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
            window.history.forward();
            function noBack() { window.history.forward();}
        },
        show_add_corso: function () {
            var self = this;
            self.sezione = "inserisci_corso";
            self.message = null;
        },
        show_remove_corso: function () {
            var self = this;
            $.post("../servlet_server", {
                    servlet: "index",
                    operazione: "get_all_corsi"
                },
                function (data) {
                    self.corsi = JSON.parse(data);
                }
            );
            self.sezione = "rimuovi_corso";
            self.message = null;
        },
        show_add_prof: function () {
            var self = this;
            self.sezione = "inserisci_prof";
            self.message = null;
        },
        show_remove_prof: function () {
            var self = this;
            $.post("../servlet_server", {
                    servlet: "index",
                    operazione: "get_all_professori"
                },
                function (data) {
                    self.professori = JSON.parse(data);
                }
            );
            self.sezione = "rimuovi_prof";
            self.message = null;
        },
        show_add_insegnamento: function () {
            var self = this;
            $.post("../servlet_server", {
                    servlet: "index",
                    operazione: "get_all_professori"
                },
                function (data) {
                    self.professori = JSON.parse(data);
                }
            );
            $.post("../servlet_server", {
                    servlet: "index",
                    operazione: "get_all_corsi"
                },
                function (data) {
                    self.corsi = JSON.parse(data);
                }
            );
            self.sezione = "add_insegnamento";
            self.message = null;
        },
        show_remove_insegnamento: function () {
            var self = this;
            $.post("../servlet_server", {
                    servlet: "index",
                    operazione: "get_all_professori"
                },
                function (data) {
                    self.professori = JSON.parse(data);
                }
            );
            self.corsi = "";
            self.id_prof = null;
            self.sezione = "remove_insegnamento";
            self.message = null;
        },
        get_corso_from_insegna: function () {
            var self = this;
            $.post("../servlet_server", {
                    servlet: "admin",
                    operazione: "get_corso_from_insegna",
                    id: self.id_prof
                },
                function (data) {
                    self.corsi = JSON.parse(data);
                }
            );
        },
        add_corso: function () {
            var self = this;
            if (self.titolo_corso === null) {
                self.message = "Riempire tutti i campi";
            } else {
                $.post("../servlet_server", {
                        servlet: "admin",
                        operazione: "add_corso",
                        titolo_corso: self.titolo_corso
                    },
                    function (data) {
                        var corso = JSON.parse(data);
                        console.log("MESS ", self.titolo_corso);
                        if (corso.titolo === self.titolo_corso) {
                            self.message = "Corso aggiunto";
                        } else {
                            self.message = "Corso gia' presente";
                        }
                        self.titolo_corso = null;
                    }
                );
            }
        },
        remove_corso: function () {
            var self = this;
            if (self.titolo_corso === null) {
                self.message = "Prego, selezionare un corso";
            } else {
                $.post("../servlet_server", {
                        servlet: "admin",
                        operazione: "remove_corso",
                        titolo: self.titolo_corso
                    },
                    function (data) {
                        if (data === "true") {
                            self.message = "Corso rimosso";
                            $.post("../servlet_server", {servlet: "index", operazione: "get_all_corsi"},
                                function (data) {
                                    self.corsi = JSON.parse(data);
                                    self.titolo_corso = null;
                                });
                        } else {
                            self.message = "Corso non rimosso: e' insegnato da qualche professore!";
                        }
                    }
                );
            }
        },
        add_prof: function () {
            var self = this;
            if (self.nome_prof === null || self.cognome_prof === null) {
                self.message = "Riempire tutti i campi";
            } else {
                $.post("../servlet_server", {
                        servlet: "admin",
                        operazione: "add_professore",
                        nome: self.nome_prof,
                        cognome: self.cognome_prof
                    },
                    function (data) {
                        var professore = JSON.parse(data);
                        if (professore.nome === self.nome_prof) {
                            self.message = "Professore aggiunto";
                        } else {
                            self.message = "Professore non inserito";
                        }
                        self.nome_prof = null;
                        self.cognome_prof = null;
                    }
                );
            }
        },
        remove_prof: function () {
            var self = this;
            if (self.id_prof === null) {
                self.message = "Selezionare un professore";
            } else {
                $.post("../servlet_server", {
                        servlet: "admin",
                        operazione: "remove_professore",
                        id: self.id_prof
                    },
                    function (data) {
                        if (data === "true") {
                            self.message = "Professore rimosso";
                            $.post("../servlet_server", {
                                    servlet: "index",
                                    operazione: "get_all_professori"
                                },
                                function (data) {
                                    self.professori = JSON.parse(data);
                                    self.id_prof = null;
                                }
                            );
                        } else {
                            self.message = "Professore non rimosso: insegna qualche corso!";
                        }
                    }
                );
            }
        },
        add_insegnamento: function () {
            var self = this;
            if (self.id_prof === null || self.titolo_corso === null) {
                self.message = "Riempire tutti i campi";
            } else {
                $.post("../servlet_server", {
                        servlet: "admin",
                        operazione: "add_insegnamento",
                        id: self.id_prof,
                        titolo: self.titolo_corso
                    },
                    function (data) {
                        if (data === "true") {
                            self.message = "Professore assegnato";
                        } else {
                            self.message = "Professore non assegnato";
                        }
                        self.id_prof = null;
                        self.titolo_corso = null;
                    }
                );
            }
        },
        remove_insegnamento: function () {
            var self = this;
            if (self.id_professore === null || self.titolo_corso === null) {
                self.message = "Selezionare tutti i campi";
            } else {
                $.post("../servlet_server", {
                        servlet: "admin",
                        operazione: "remove_insegnamento",
                        id: self.id_prof,
                        titolo: self.titolo_corso
                    },
                    function (data) {
                        if (data === "true") {
                            self.message = "Assegnamento rimosso";
                        } else {
                            self.message = "Assegnamento non rimosso";
                        }
                        $.post("../servlet_server", {
                                servlet: "admin",
                                operazione: "get_corso_from_insegna",
                                id: self.id_prof
                            },
                            function (data) {
                                self.corsi = JSON.parse(data);
                            }
                        );
                        self.id_prof = null;
                        self.titolo_corso = null;
                    }
                );
            }
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

