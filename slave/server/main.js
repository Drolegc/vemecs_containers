var express = require('express')
var bodyParser = require('body-parser')
var axios = require('axios')
const { request } = require('express')
var cors = require('cors')
var app = express()
app.use(cors({ origin: '*' }))
var server = require('http').Server(app)
var io = require('socket.io')(server)
const io_client = require('socket.io-client')

// Servidor a la escucha
server.listen(8088, function() {
    console.log('Servidor corriendo en http://localhost:8088')
});


var socketMaster = io_client.connect('http://middleware-master:8099/')
axios.get('http://middleware-master:8099/').then((res) => console.log(res.data))


socketMaster.on('connect', () => {
    console.log("conectado middleware con middleware pa")
})

var sendData = []
var urlMaster = ""

/*

Data

{
    vemec_id:1,
    data_historial:{
        presionMin:1,
        presionMax:2,
        volGas:3,
        freqAporte:4,
        porcenMezclaO2:20,
        humedadAire:23,
        tempSalidaAire:12,
        presionEntradaAire:23,
        presionSalidaAire:34,
        timestamp:'23:40',
        pulsaciones:23,
    }
    vemec_energia:{
        "tipo":"red",
        "porcentaje":100
    },
}
*/
var vemecs = {}

io.on('connection', (socket) => {
    console.log('Un cliente se ha conectado')
    console.log(socket.id)

    // Cuando se conecte el frontend se va a generar un objeto vacio asociado con su socket.id
    vemecs[`${socket.id}`] = {
        datos: [],
        promedio: {}
    }

    // Recibo datos de cierto vemec (identificado por socket.id)
    socket.on('messages', (data) => {

        datosParaElMaster(data, socket.id)

        // sendData.push(data)
        // if (sendData.length == 5) {
        //     axios.post('http://api:8080/VeMecApi/historiales/varios_registros', sendData, { headers: { 'Access-Control-Allow-Origin': '*' } })
        //         .then(() => {}).catch((e) => {})
        //     sendData = []
        // }

        // Se envian los datos y el frontend del slave hace un control
        // para saber si es el vemec que le corresponde
        io.emit('messages', data);
    });
})

function datosParaElMaster(data, socket_id) {

    /*
    Las condiciones de urgencia son: aumento o disminución de pulsaciones cardíacas fuera de los
    valores promedio. Sistema operando con batería y nivel de carga por debajo del 20%. Ritmo
    respiratorio alterado o pérdida de lectura en parámetros de la VeMec
    */
    var estadoNormal = (data['vemec_energia']['energia'] > 20) || (data['data_historial']['pulsaciones'] < 100)

    if (estadoNormal) {
        vemecs[`${socket_id}`].datos.push(data)
        var datosPromedio = {
            vemec_id: 0,
            data_historial: {
                presionMin: 0,
                presionMax: 0,
                volGas: 0,
                freqAporte: 0,
                porcenMezclaO2: 0,
                humedadAire: 0,
                tempSalidaAire: 0,
                presionEntradaAire: 0,
                presionSalidaAire: 0,
                timestamp: '',
                pulsaciones: 0,
            },
            vemec_energia: {
                tipo: "red",
                porcentaje: 100
            },
        }

        vemecs[`${socket_id}`].datos.forEach(datos => {

            datosPromedio.vemec_id = datos.vemec_id
            datosPromedio.data_historial.presionMin += datos.data_historial.presionMin
            datosPromedio.data_historial.presionMax += datos.data_historial.presionMax
            datosPromedio.data_historial.volGas += datos.data_historial.volGas
            datosPromedio.data_historial.freqAporte += datos.data_historial.freqAporte
            datosPromedio.data_historial.porcenMezclaO2 += datos.data_historial.porcenMezclaO2
            datosPromedio.data_historial.humedadAire += datos.data_historial.humedadAire
            datosPromedio.data_historial.tempSalidaAire += datos.data_historial.tempSalidaAire
            datosPromedio.data_historial.presionEntradaAire += datos.data_historial.presionEntradaAire
            datosPromedio.data_historial.presionSalidaAire += datos.data_historial.presionSalidaAire
            datosPromedio.data_historial.timestamp = datos.data_historial.timestamp
            datosPromedio.data_historial.pulsaciones += datos.data_historial.pulsaciones
            datosPromedio.vemec_energia.tipo = datos.vemec_energia.tipo
            datosPromedio.vemec_energia.porcentaje = datos.vemec_energia.porcentaje

        });

        var cantidadDeDatos = vemecs[`${socket_id}`].datos.length
        datosPromedio.data_historial.presionMin /= cantidadDeDatos
        datosPromedio.data_historial.presionMax /= cantidadDeDatos
        datosPromedio.data_historial.volGas /= cantidadDeDatos
        datosPromedio.data_historial.freqAporte /= cantidadDeDatos
        datosPromedio.data_historial.porcenMezclaO2 /= cantidadDeDatos
        datosPromedio.data_historial.humedadAire /= cantidadDeDatos
        datosPromedio.data_historial.tempSalidaAire /= cantidadDeDatos
        datosPromedio.data_historial.presionEntradaAire /= cantidadDeDatos
        datosPromedio.data_historial.presionSalidaAire /= cantidadDeDatos
        datosPromedio.data_historial.pulsaciones /= cantidadDeDatos

        vemecs[`${socket_id}`].promedio = datosPromedio

        //Dado que los datos vienen por segundo, el largo del array == tiempo transcurrido
        if (vemecs[`${socket_id}`].datos.length == 60) {
            console.log("Enviando promedio al master")
            console.log(vemecs[`${socket_id}`].promedio)
            socketMaster.emit('masterMiddleware', vemecs[`${socket_id}`].promedio)
            vemecs[`${socket_id}`] = {
                datos: [],
                promedio: {}
            }
        }

    } else {
        console.log("ESTADO ALERTA")
        console.log(data['data_historial']['pulsaciones'])
        vemecs[`${socket_id}`] = {
                datos: [],
                promedio: {}
            }
            //io.emit('master', data)
        socketMaster.emit('masterMiddleware', data)
    }

}




// Endpoint para las acciones medicas

app.post("/ficha", bodyParser.json(), function(request, response) {
    console.log("Post en ficha")
    if (!request.body) return response.sendStatus(400)
    console.log(request.body)

    // Enviar datos al frontend del slave para notificar que se tiene una nueva ficha
    io.sockets.emit('ficha', request.body)

    response.send({
        "hello": "world"
    })


})

app.get('/vemecs/:id', async function(req, res) {
    console.log("Request /vemecs/:id")
    var vemec = await axios.get('http://middleware-master:8099/vemecs/' + req.params.id, {
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    })
    res.send(vemec.data)
})

app.post("/vemecs/", bodyParser.json(), function(request, response) {
    console.log("Nuevo vemec a registrar")
    axios.post('http://middleware-master:8099/VeMecApi/vemecs/', request.body, { headers: { 'Access-Control-Allow-Origin': '*' } })
        .then((res) => { response.send(res.data) }).catch((e) => { console.log("ERROR" + e) })
})

app.post("/pacientes/", bodyParser.json(), function(request, response) {
    console.log("Nuevo paciente a registrar")
    axios.post('http://middleware-master:8099/VeMecApi/pacientes/', request.body, { headers: { 'Access-Control-Allow-Origin': '*' } })
        .then((res) => { response.send(res.data) }).catch((e) => {})
})

app.get("/pacientes/vemec/:id", bodyParser.json(), async function(req, res) {
    var response = await axios.get("http://middleware-master:8099/pacientes/vemec/" + req.params.id, { headers: { 'Access-Control-Allow-Origin': '*' } })
    res.send(response.data)
})

app.get("/", function(req, res) {
    res.send("Soy el middleware!")
})