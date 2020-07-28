<template>
  <v-app>
    <v-content>
      <v-app-bar color="green" elevation="1" fixed height="45">
        <v-toolbar color="transparent" class="pa-0 header" elevation="0">
          <v-spacer></v-spacer>
          <v-btn to="/" text exact class="white--text">Inicio</v-btn>
          <v-btn @click="modalVemec = true" text exact class="white--text">Agregar veMec</v-btn>
          <v-btn @click="modalPaciente = true" text exact class="white--text">Pacientes</v-btn>
        </v-toolbar>
      </v-app-bar>
      <nuxt class="content" />
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
            <v-text-field outlined label="Id de vemec" color="black" v-model="vemec.id">
            </v-text-field>
            <v-text-field outlined label="Marca" color="black" v-model="vemec.marca"></v-text-field>
            <v-text-field outlined label="Modelo" color="black" v-model="vemec.modelo"></v-text-field>
            <v-text-field outlined label="Sector hospitalario" color="black" v-model="vemec.sectorHospitalario">
            </v-text-field>
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
            <v-text-field outlined label="Documento" color="black" v-model="paciente.data_paciente.documento_id">
            </v-text-field>
            <v-text-field outlined label="Nombre" color="black" v-model="paciente.data_paciente.nombre"></v-text-field>
            <v-text-field outlined label="Apellido" color="black" v-model="paciente.data_paciente.apellido">
            </v-text-field>
            <v-text-field type="number" label="Edad" color="black" outlined v-model="paciente.data_paciente.edad">
            </v-text-field>
            <v-select :items="['Masculino','Femenino']" outlined label="Sexo"  color="black" class="black--text" 
              v-model="paciente.sexo"></v-select>

            <v-select :items="vemecs" outlined label="Vemec a usar" item-value="id" item-text="marca"  color="black" class="black--text" 
              v-model="paciente.vemec_id"></v-select>
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

    </v-content>
  </v-app>
</template>

<script>
    export default {
        props: {
            source: String,
        },
        data() {
            return {
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
                medicos: []
            }
        },
        created() {},
        methods: {
            createVemec() {
                this.$axios.post('http://localhost:8080/vemecs/', this.vemec, {
                        headers: {
                            'Access-Control-Allow-Origin': '*'
                        }
                    })
                    .then(() => {
                        this.$root.$emit('vemecAdded', 1)
                        this.modalVemec = false
                        this.vemecAgregado = true
                        this.vemec = {}
                        setTimeout(() => {
                            this.vemecAgregado = false
                        }, 5000)
                    })
            },
            createPaciente() {
                this.$axios.post('http://localhost:8080/pacientes/', this.paciente, {
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
                        setTimeout(() => {
                            this.pacienteAgregado = false
                        }, 5000)
                    })
            },
        },
        computed: {
            vemecs() {
                return this.$store.getters.vemecs
            },
        }
    }
</script>
<style>
    .content {
        margin-top: 45px;
        margin-bottom: 45px;
    }
</style>