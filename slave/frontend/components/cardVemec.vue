<template>
  <v-card class="mx-auto text-center" width="100%" height="100%" min-height="300px" flat
    style="border:1px solid #cecece" @click="showPatient()">
    <v-toolbar :color="(data[data.length-1]>100)?'red':(value.vemec.porcentaje<20)?'yellow':'green'">
      <v-toolbar-title class="white--text">
        Pulsaciones
      </v-toolbar-title>
    </v-toolbar>
    <v-card-text>
      <v-sheet color="rgba(0, 0, 0, .12)">
        <v-sparkline :value="data" line-width="2" height="100%" padding="24" show-labels stroke-linecap="round">
          <template v-slot:text="item">
            {{ item.value }}
          </template>
        </v-sparkline>
      </v-sheet>
    </v-card-text>
    <v-card-text class="text-left black--text">
      <p><b>Nombre: {{paciente.nombre}}</b></p>
      <p><b>Apellido: {{paciente.apellido}}</b></p>
      <p><b>Sexo: {{paciente.sexo}}</b></p>
      <p><b>Energia:{{value.vemec.porcentaje}}%</b></p>
      <p><b></b></p>
    </v-card-text>
    <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue" class="black--text font-weight-light" :to="`/vemecs/${vm}`" outlined>Mas informacion</v-btn>
    </v-card-actions>
  </v-card>
</template>


<script>
  export default {
    props: {
      value: Object,
      vm: null,
      pacientes: Array
    },
    data() {
      return {
        paciente:{},
        data: [0,0,0,0,0,0],
      }
    },
    created() {
      this.getPaciente()
    },
    methods: {
      showPatient(){
          this.$emit('showPatient', this.paciente)
      },
      getPaciente() {
        this.$axios.get(`http://picacode.ddns.net:8080/pacientes/vemec/${this.vm}`, {
            headers: {
              'Access-Control-Allow-Origin': '*'
            }
          })
          .then((data) => {
            this.paciente = data.data
          })
      },

    },
    watch: {
      value: {
        handler(val) {
          if (parseInt(val.pulsaciones.vemec) == parseInt(this.vm))
            this.data.push(val.pulsaciones.valor)

          if (this.data.length > 10) {
            this.data.shift()
          }
        },
        deep: true,
      }
    },
  }

</script>
