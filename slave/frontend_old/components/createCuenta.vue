<template>
  <v-dialog v-model="value" max-width="500px" transition="dialog-transition" persistent>
    <v-card>
      <v-toolbar color="primary" elevation="0">
        <v-toolbar-title>
          Agregar una cuenta nueva
        </v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn icon @click="$emit('input', false)">
          <v-icon>close</v-icon>
        </v-btn>
      </v-toolbar>
      <v-card-text>
        <h3 class="pt-2 pb-2">Servicios disponibles</h3>
        <v-row class="mb-2">
          <template v-if="cuentaSeleccionada==null">
            <v-col class="col-2 col-md-3 col-sm-3" v-for="cuenta in cuentas.results">
              <v-avatar @click="cuentaSeleccionada = cuenta">
                <img src="https://lh3.googleusercontent.com/e9aYgGqyIgaTVKwE31IJFvnTyIyYNmblppnKPP7cpuUPuvlPWt5tQNsdmciDZNFUDujz=w300" alt="John">
              </v-avatar>
              <b>{{cuenta.nombre}}</b>
            </v-col>

          </template>
          <template v-else>
            <v-col class="col-12 col-md-2">
              <v-avatar>
                <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John">
              </v-avatar>
              <p>{{cuentaSeleccionada.nombre}}</p>
            </v-col>
          </template>
        </v-row>
        <v-row v-if="cuentaSeleccionada!=null">
          <v-col class="col-12 col-md-12" v-for="campo in cuentaSeleccionada.campos">
            <v-text-field :label="campo.nombre" v-model="cuenta.campos[campo.nombre]"></v-text-field>
          </v-col>
          <input type="hidden" v-model="cuenta.cuenta = cuentaSeleccionada.pk">
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn @click="createCuenta()" color="success">Crear cuenta</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>


<script>
  export default {
    props: {
      value: Boolean,
    },
    data() {
      return {
        cuentas: {},
        cuentaSeleccionada: null,
        cuenta: {
          campos: {}
        },

      }
    },
    created() {
        this.getCuentas()
    },
    methods: {
      getCuentas() {
        this.$axios.get('/api/cuentas/')
          .then((data) => {
            this.cuentas = data.data
          })
      },
      createCuenta() {
        this.$axios.post('/api/usuarios/cuentas/', this.cuenta)
          .then(() => {
            this.cuenta = {}
            this.$emit('input', false)
            this.$emit('accountCreated', true)
          })
      },
    },
  }

</script>
