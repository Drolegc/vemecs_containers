<template>
  <v-container fluid>
    <v-card>
      <v-toolbar color="green" elevation="0">
        <v-toolbar-title class="white--text">
          Central
        </v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-row>
          <v-col class="col-12 col-md-4" v-for="vm in vemecs">
            <card-vemec v-model="values" :vm="vm.id" :pacientes="pacientes" @showPatient="openPatient($event)">
            </card-vemec>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
    <v-dialog scrollable persistent max-width="600px" v-model="modalPaciente" transition="dialog-transition">

      <v-card>
        <v-toolbar color="green" elevation="0">
          <v-toolbar-title class="white--text font-weight-light" v-if="accionMedica">Accion medica</v-toolbar-title>
          <v-toolbar-title class="white--text font-weight-light" v-else>Ficha de paciente</v-toolbar-title>
        </v-toolbar>
        <v-form ref="signup">
          <v-card-text class="pa-3" style="height: 400px;overflow-y:auto">
            <template v-if="accionMedica">
              <v-row>
                <v-col class="col-12 col-md-12">
                  <v-select label="Medico" v-model="accion.medico_id" :items="medicos" outlined color="primary">
                  </v-select>
                </v-col>
                <v-col class="col-12 col-md-12">
                  <v-textarea label="Antecedentes" v-model="accion.data_ficha.antecedentes" outlined color="primary">
                  </v-textarea>
                </v-col>
                <v-col class="col-12 col-md-12">
                  <v-text-field label="Datos familiares" v-model="accion.data_ficha.datosFamiliares" outlined color="primary">
                  </v-text-field>
                </v-col>
                <v-col class="col-12 col-md-12">
                  <v-select label="Nivel de riesgo" :items="[1,2,3]" v-model="accion.data_ficha.nivelRiesgo" outlined
                    color="primary">
                  </v-select>
                </v-col>
                <v-col class="col-12 col-md-12">
                  <v-text-field label="Medicacion" v-model="accion.data_ficha.medicacion" outlined
                    color="primary">
                  </v-text-field>
                </v-col>
              </v-row>
            </template>
            <template v-else>
              <v-row>
                <v-col class="col-12 col-md-12">
                  <v-text-field label="Documento" v-model="paciente.documento_id" readonly filled outlined
                    color="primary">
                  </v-text-field>
                </v-col>
                <v-col class="col-12 col-md-12">
                  <v-text-field label="Nombre" v-model="paciente.nombre" readonly filled outlined color="primary">
                  </v-text-field>
                </v-col>
                <v-col class="col-12 col-md-12">
                  <v-text-field label="Apellido" v-model="paciente.apellido" readonly filled outlined color="primary">
                  </v-text-field>
                </v-col>
                <v-col class="col-12 col-md-12">
                  <v-text-field label="Edad" v-model="paciente.edad" readonly filled outlined color="primary">
                  </v-text-field>
                </v-col>
                <v-col class="col-12 col-md-12">
                  <v-text-field label="Nacionalidad" v-model="paciente.nacionalidad" readonly filled outlined
                    color="primary">
                  </v-text-field>
                </v-col>
                <v-col class="col-12 col-md-12">
                  <v-text-field label="Historia clinica" v-model="paciente.historiaClinica" readonly filled outlined
                    color="primary">
                  </v-text-field>
                </v-col>
                <v-col class="col-12 col-md-12">
                  <v-text-field label="Fecha de internacion"
                    :value="(paciente.fechaInternacion!=null)?paciente.fechaInternacion:'Aun no dado de alta'" readonly
                    filled outlined color="primary">
                  </v-text-field>
                </v-col>
                <v-col class="col-12 col-md-12">
                  <v-text-field label="Fecha de alta" v-model="paciente.fechaAlta" readonly filled outlined
                    color="primary">
                  </v-text-field>
                </v-col>
                <v-col class="col-12 col-md-12">
                  <v-text-field label="Medico tratante" v-model="paciente.medicoTratante" readonly filled outlined
                    color="primary">
                  </v-text-field>
                </v-col>
                <v-col class="col-12 col-md-12">
                  <v-text-field label="Lugar de residencia" v-model="paciente.lugarResidencia" outlined color="primary">
                  </v-text-field>
                </v-col>
              </v-row>
            </template>

          </v-card-text>
          <v-divider></v-divider>
          <v-card-actions>
            <v-spacer></v-spacer>
            <template v-if="accionMedica">
              <v-btn color="red" @click="accionMedica = false; modalPaciente = false" class="black--text font-weight-light" outlined>
                Cerrar
              </v-btn>
              <v-btn color="blue" @click="addAccionMedica()" class="black--text font-weight-light" outlined>
                AGREGAR ACCION MEDICA
              </v-btn>
            </template>
            <template v-else>
              <v-btn color="red" @click="modalPaciente = false" class="black--text font-weight-light" outlined>
                Cerrar
              </v-btn>
              <v-btn color="blue" @click="accionMedica = true" class="black--text font-weight-light" outlined>
                Accion medica
              </v-btn>
            </template>
          </v-card-actions>
        </v-form>
        <v-snackbar
        color="success"
          v-model="accionAgregada"
        >
          Accion medica agregada
          <v-btn text @click.native="accionAgregada = false">Cerrar</v-btn>
        </v-snackbar>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
  import cardVemec from '~/components/cardVemec.vue'
  import io from 'socket.io-client';

  export default {
    components: {
      cardVemec
    },
    data() {
      return {
        modalVemec: false,
        modalPaciente: false,
        vemecAgregado: false,
        pacienteAgregado: false,
        accionMedica: false,
        accion:{
          data_ficha:{}
        },
        vemec: {},
        pacientes: [],
        paciente: {},
        modalPaciente: false,
        values: {
          pulsaciones: {},
          vemec: {}
        },
        medicos:[],
        errorGetPaciente: false,
        accionAgregada: false
      }
    },
    created() {
      this.getData()
      this.getVemecs()
      this.getPacientes()
      this.getMedicos()
    },
    mounted() {
      this.$root.$on('vemecAdded',()=>{
        this.getVemecs()
      })
    },
    methods: {
      getData() {
        var socket = io.connect('https://118d7363f085.ngrok.io', {
          'forceNew': true
        });
        socket.on('messages', (data) => {
          this.values.vemec = data.vemec_energia
          this.values.pulsaciones = {
            "vemec": data.vemec_id,
            "valor": data.data_historial.pulsaciones
          }

        })
      },
      openPatient($e) {
        this.modalPaciente = true
        this.paciente = $e
      },
      addAccionMedica(){
        this.accion.paciente_id = 1111
        this.$axios.post('http://picacode.ddns.net:8080/fichas/', this.accion, {
            headers: {
              'Access-Control-Allow-Origin': '*'
            }
          }).then(()=>{
            this.accionAgregada = true
            this.modalPaciente = false 
            this.accionMedica = false
          })

      },
      createPaciente() {
        this.$axios.post('http://picacode.ddns.net:8080/pacientes/', this.paciente, {
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
      getMedicos() {
        this.$axios.get('http://picacode.ddns.net:8080/medicos/')
          .then((data)=>{
            this.medicos = data.data.map((medico)=>{
              let data = {}
              data.value = medico.documento_id 
              data.text = `${medico.nombre} ${medico.apellido}`
              return data
            })
          })
      },
      getPacientes() {
        this.$axios.get(`http://picacode.ddns.net:8080/pacientes/`, {
            headers: {
              'Access-Control-Allow-Origin': '*'
            }
          })
          .then((data) => {
            this.pacientes = data.data
          })
      },
      getVemecs() {
        this.$axios.get('http://picacode.ddns.net:8080/vemecs/', {
            headers: {
              'Access-Control-Allow-Origin': '*'
            }
          })
          .then((data) => {
            this.$store.dispatch('setVemecs',data.data)
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
      vemecs() {
        return this.$store.getters.vemecs
      },
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
