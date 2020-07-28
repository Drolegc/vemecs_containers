<template>
  <v-container>
    <v-row>
      <v-col class="col-md-6 col-12">
        <h3 class="mb-0">MASTER: {{dataPaciente.nombre}} - CI: {{dataPaciente.documento_id}} - VEMEC: {{dataPaciente.vemec.id}} </h3>
      </v-col>
      <v-col class="col-md-3 col-12">
        <v-btn color="green" class="full-width white--text" @click="modalPaciente = true">Agregar paciente</v-btn>
      </v-col>
      <v-col class="col-md-3 col-12">
        <v-btn color="green" class="full-width white--text"  @click="modalVemec = true">Agregar vemec</v-btn>
      </v-col>
      <v-col class="col-12 col-md-6">
        <v-card class="mx-auto text-center" width="100%" height="100%" min-height="300px">
          <v-fade-transition>
            <div v-show="!loading1">
              <v-card-title class="pb-0 mb-3 card-header">
                <v-row no-gutters>
                  <v-col class="col-12 col-md-6">
                    <p class="title-card white--text">{{select1.text}}</p>
                  </v-col>
                  <v-col class="col-12 col-md-6">
                    <v-select outlined dense return-object :items="computedElementos1" v-model="select1"></v-select>
                  </v-col>
                </v-row>
              </v-card-title>
              <v-card-text>
                <v-sheet color="rgba(0, 0, 0, .12)">
                  <v-sparkline :value="values1" line-width="2" height="100%" padding="24" show-labels
                    stroke-linecap="round">
                    <template v-slot:text="item">
                      {{ item.value }}
                    </template>
</v-sparkline>
</v-sheet>
</v-card-text>
</div>
</v-fade-transition>
<v-fade-transition>
    <v-card-text v-show="loading1" style="height: 100%;">
        <div class="d-flex justify-center align-center" style="height: 100%;">
            <v-progress-circular :size="70" :width="7" color="green" indeterminate></v-progress-circular>
        </div>
    </v-card-text>
</v-fade-transition>
</v-card>
</v-col>
<v-col class="col-12 col-md-6">
    <v-card height="100%">
        <v-fade-transition>
            <v-row no-gutters v-show="!loading1">
                <v-col class="col-12 col-md-6" v-for="(elemento,index) in computedElementos1" :key="index">
                    <v-card-title class="title-card  body-1 font-weight-bold text-center">
                        {{elemento.text}}
                    </v-card-title>
                    <v-card-text class="d-flex justify-center">
                        <v-progress-circular :value="data[elemento.value]" size="75"><b>
                      {{data[elemento.value]}}
                    </b>
                        </v-progress-circular>
                    </v-card-text>
                </v-col>
            </v-row>
        </v-fade-transition>
        <v-fade-transition>
            <v-card-text v-show="loading1" style="height: 100%;">
                <div class="d-flex justify-center align-center" style="height: 100%;">
                    <v-progress-circular :size="70" :width="7" color="green" indeterminate></v-progress-circular>
                </div>
            </v-card-text>
        </v-fade-transition>
    </v-card>
</v-col>
<v-col class="col-12 col-md-6">
    <v-card class="mx-auto text-center" width="100%" height="100%" min-height="300px">
        <v-fade-transition>
            <div v-show="!loading2">
                <v-card-title class="pb-0 mb-3 card-header">
                    <v-row no-gutters>
                        <v-col class="col-12 col-md-6">
                            <p class="title-card white--text">{{select2.text}}</p>
                        </v-col>
                        <v-col class="col-12 col-md-6">
                            <v-select return-object outlined dense :items="computedElementos2" v-model="select2"></v-select>
                        </v-col>
                    </v-row>
                </v-card-title>
                <v-card-text v-if="!loading2">
                    <v-sheet color="rgba(0, 0, 0, .12)">
                        <v-sparkline :value="values2" line-width="2" color="black" height="100%" padding="24" show-labels stroke-linecap="round">
                        </v-sparkline>
                    </v-sheet>
                </v-card-text>
            </div>
        </v-fade-transition>
        <v-fade-transition>
            <v-card-text v-show="loading2" style="height: 100%;">
                <div class="d-flex justify-center align-center" style="height: 100%;">
                    <v-progress-circular :size="70" :width="7" color="green" indeterminate></v-progress-circular>
                </div>
            </v-card-text>
        </v-fade-transition>

    </v-card>
</v-col>
<v-col class="col-12 col-md-6">
    <v-card height="100%">
        <v-row no-gutters v-if="!loading2">
            <v-col class="col-12 col-md-6" v-for="(elemento,index) in computedElementos2" :key="index">
                <v-card-title class="title-card body-1 font-weight-bold text-center">
                    {{elemento.text}}
                </v-card-title>
                <v-card-text class="d-flex justify-center">
                    <v-progress-circular :value="data[elemento.value]" size="75"><b>
                    {{data[elemento.value]}}
                  </b>
                    </v-progress-circular>
                </v-card-text>
            </v-col>
        </v-row>
        <v-card-text v-show="loading2" style="height: 100%;">
            <div class="d-flex justify-center align-center" style="height: 100%;">
                <v-progress-circular :size="70" :width="7" color="green" indeterminate></v-progress-circular>
            </div>
        </v-card-text>
    </v-card>
</v-col>
</v-row>
<v-dialog v-model="modalVemec" width="500">
    <v-card>
        <v-toolbar color="green" elevation="0">
            <v-toolbar-title class="white--text">Agregar Vemec</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn icon @click="modalVemec = false">
                <v-icon>close</v-icon>
            </v-btn>
        </v-toolbar>
        <v-card-text class="mt-3">
            <v-text-field outlined label="Id de vemec" :readonly="errorGetPaciente" color="black" v-model="vemec.id"></v-text-field>
            <v-text-field outlined label="Marca" color="black" v-model="vemec.marca"></v-text-field>
            <v-text-field outlined label="Modelo" color="black" v-model="vemec.modelo"></v-text-field>
            <v-text-field outlined label="Sector hospitalario" color="black" v-model="vemec.sectorHospitalario"></v-text-field>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="green" class="white--text" @click="createVemec()">Agregar vemec</v-btn>
        </v-card-actions>
    </v-card>
</v-dialog>
<v-dialog v-model="modalPaciente" width="500">
    <v-card>
        <v-toolbar color="green" elevation="0">
            <v-toolbar-title class="white--text">Agregar Vemec</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn icon @click="modalPaciente = false">
                <v-icon>close</v-icon>
            </v-btn>
        </v-toolbar>
        <v-card-text class="mt-3">
            <v-text-field outlined label="Documento" color="black" v-model="paciente.data_paciente.documento_id"></v-text-field>
            <v-text-field outlined label="Nombre" color="black" v-model="paciente.data_paciente.nombre"></v-text-field>
            <v-text-field outlined label="Apellido" color="black" v-model="paciente.data_paciente.apellido"></v-text-field>
            <v-text-field type="number" label="Edad" color="black" outlined v-model="paciente.data_paciente.edad"></v-text-field>
            <v-select :items="vemecs" outlined label="Vemec a usar" :readonly="errorGetPaciente" color="black" v-model="paciente.vemec_id"></v-select>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="green" @click="createPaciente()" class="white--text">Agregar paciente</v-btn>
        </v-card-actions>
    </v-card>
</v-dialog>
<v-snackbar color="green" v-model="pacienteAgregado">
    Paciente agregado
    <v-btn text @click="pacienteAgregado = false">Cerrar</v-btn>
</v-snackbar>
<v-snackbar color="green" v-model="vemecAgregado">
    Vemec agregado
    <v-btn text @click="vemecAgregado = false">Cerrar</v-btn>
</v-snackbar>
</v-container>
</template>

<script>
    import Logo from '~/components/Logo.vue'
    import VuetifyLogo from '~/components/VuetifyLogo.vue'
    import io from 'socket.io-client';

    export default {
        components: {
            Logo,
            VuetifyLogo
        },
        data() {
            return {
                socket: {},
                modalVemec: false,
                modalPaciente: false,
                vemecAgregado: false,
                pacienteAgregado: false,
                vemec: {},
                paciente: {
                    data_paciente: {}
                },
                dataPaciente: {
                    vemec: {}
                },
                vemecs: [],
                data: {},
                values1: [10, 10, 10, 10],
                values2: [10, 10, 10, 10],
                select1: {
                    text: "Presion de entrada de aire",
                    value: "presionEntradaAire"
                },
                select2: {
                    text: "Presion de salida de aire",
                    value: "presionSalidaAire"
                },
                elementos1: [{
                    text: "Presion minima",
                    value: "presionMin"
                }, {
                    text: "Presion maxima",
                    value: "presionMax"
                }, {
                    text: "Volumen de gas",
                    value: "volGas"
                }, {
                    text: "Frecuencia de aporte",
                    value: "freqAporte"
                }, {
                    text: "Presion de entrada de aire",
                    value: "presionEntradaAire"
                }, ],
                elementos2: [{
                    text: "Porcentaje de mezcla de CO2",
                    value: "porcenMezclaO2"
                }, {
                    text: "Humedad de aire",
                    value: "humedadAire"
                }, {
                    text: "Temperatura de salida de aire",
                    value: "tempSalidaAire"
                }, {
                    text: "Temperatura de entrada de aire",
                    value: "tempEntradaAire"
                }, {
                    text: "Presion de salida de aire",
                    value: "presionSalidaAire"
                }, ],
                loading1: false,
                loading2: false,
                errorGetPaciente: false
            }
        },
        created() {
            this.socket = io.connect('http://localhost:8081', {
                'forceNew': true
            });
            this.socket.on('connect', () => {
                console.log("conectado al middleware master pa")
            })
            this.values1[this.select1.value] = []
            this.values2[this.select2.value] = []
            this.getData()
            this.getVemecs()
            this.getFichaNotificacion()
        },
        mounted() {},
        methods: {
            getFichaNotificacion() {
                console.log("getFichaNotifiaction")
                this.socket.on('ficha', (data) => {
                    console.log("Desde el frontend - ficha")
                    console.log(data)
                })
            },
            getData() {

                this.socket.on('frontMaster', (data) => {
                    console.log("Nuevo mensaje")
                    this.data = data.data_historial
                    if (this.dataPaciente.nombre == undefined && this.errorGetPaciente == false) {
                        this.dataPaciente.vemec_id = data.vemec_id
                        this.getPaciente()
                    }

                    this.values1.push(data.data_historial[this.select1.value])
                    this.values2.push(data.data_historial[this.select2.value])
                    if (this.values1.length > 10) {
                        this.values1.shift()
                    }
                    if (this.values2.length > 10) {
                        this.values2.shift()
                    }

                })
            },
            createPaciente() {
                this.$axios.post('http://localhost:30500/VeMecApi/pacientes/', this.paciente, {
                        headers: {
                            'Access-Control-Allow-Origin': '*'
                        }
                    })
                    .then(() => {
                        this.modalPaciente = false
                        this.pacienteAgregado = true
                        this.paciente = {
                            data_paciente: {}
                        }
                        this.errorGetPaciente = false
                        setTimeout(() => {
                            this.pacienteAgregado = false
                        }, 5000)
                    })
            },
            getPaciente() {
                this.$axios.get(`http://localhost:30500/VeMecApi/pacientes/${this.dataPaciente.vemec_id}`, {
                        headers: {
                            'Access-Control-Allow-Origin': '*'
                        }
                    })
                    .then((data) => {
                        this.dataPaciente = data.data
                    })
                    .catch(() => {
                        this.errorGetPaciente = true
                        this.vemec.id = this.dataPaciente.vemec_id
                        this.modalVemec = true
                    })
            },
            getVemecs() {
                this.$axios.get('http://localhost:30500/VeMecApi/vemecs/', {
                        headers: {
                            'Access-Control-Allow-Origin': '*'
                        }
                    })
                    .then((data) => {
                        this.vemecs = data.data.map((vemec) => {
                            let data = {}
                            data.text = `${vemec.marca} - ${vemec.modelo} - ${vemec.sectorHospitalario}`
                            data.value = vemec.id
                            return data
                        })
                    })
            },
            createVemec() {
                this.$axios.post('http://localhost:30500/VeMecApi/vemecs/', this.vemec, {
                        headers: {
                            'Access-Control-Allow-Origin': '*'
                        }
                    })
                    .then(() => {
                        this.getVemecs()
                        if (this.errorGetPaciente) {
                            this.modalPaciente = true
                            this.paciente.vemec_id = String(this.vemec.id)
                        }
                        this.modalVemec = false
                        this.vemecAgregado = true
                        this.vemec = {}
                        setTimeout(() => {
                            this.vemecAgregado = false
                        }, 5000)
                    })
            }
        },
        computed: {
            computedElementos1() {
                return this.elementos1.filter((elemento) => {
                    if (elemento.value != this.select1.value) {
                        return elemento
                    }
                })
            },
            computedElementos2() {
                return this.elementos2.filter((elemento) => {
                    if (elemento.value != this.select2.value) {
                        return elemento
                    }
                })
            }
        },
        watch: {
            select1() {
                this.values1 = [10, 10, 10, 10]
                this.loading1 = true
                setTimeout(() => {
                    this.loading1 = false
                }, 3000)
            },
            select2() {
                this.values2 = [10, 10, 10, 10]
                this.loading2 = true
                setTimeout(() => {
                    this.loading2 = false
                }, 3000)

            }
        }
    }
</script>

<style>
    .title-card {
        text-overflow: ellipsis !important;
        white-space: nowrap !important;
        overflow: hidden;
        text-overflow: ellipsis;
        display: block !important;
    }
    
    .card-header {
        background: #4CAF50 !important;
    }
    
    .full-width {
        width: 100%;
    }
</style>