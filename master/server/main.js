var express = require('express')
var bodyParser = require('body-parser')
var axios = require('axios')
const { request } = require('express')
var cors = require('cors')
var app = express()
app.use(cors({ origin: '*' }))
var server = require('http').Server(app)
var io = require('socket.io')(server)

var url = "http://api-master:8080"

// Servidor a la escucha
server.listen(8099, function() {
    console.log('Servidor midleware master corriendo en http://localhost:8099')
});

io.on('connection', (socket) => {
    console.log("puede ser pa")
    console.log("puede ser que se llena la vecindad")

    socket.on('masterMiddleware', (data) => {
        console.log("Nuevos datos recibidos")
        io.emit("frontMaster", data)
            //guardarDatosHistorial(data)
    })
})

app.get('/', (req, res) => res.send("Middleware del master"))

app.get('/vemecs/', async function(req, res) {
    console.log("Request /vemecs/")
    var vemecs = await axios.get(url + '/vemecs/', {
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    })
    res.send(vemecs.data)
})

app.get('/pacientes/vemecs/:id', bodyParser.json(), async function(req, res) {
    console.log("Request /pacientes/vemecs/" + req.params.id)
    var response = await axios.get(url + '/pacientes/vemecs/' + req.params.id, req.body, {
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    })
    res.send(response.data)
})

app.post('/VeMecApi/vemecs/', bodyParser.json(), async function(req, res) {
    console.log("Request /VeMecApi/vemecs/")
    var response = await axios.post(url + '/VeMecApi/vemecs/', req.body, {
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    })
    res.send(response.data)
    io.emit("nuevoVemec", "Nuevo vemec a sido registrado desde un slave")
})

app.post('/VeMecApi/pacientes/', bodyParser.json(), async function(req, res) {
    console.log("Request /VeMecApi/pacientes/")
    var response = await axios.post(url + '/VeMecApi/pacientes/', req.body, {
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    })
    res.send(response.data)
    io.emit("nuevoPaciente", "Nuevo paciente a sido registrado desde un slave")
})

function guardarDatosHistorial(data) {
    axios.post('http://api:8080/VeMecApi/historiales/', sendData, { headers: { 'Access-Control-Allow-Origin': '*' } })
        .then((response) => { console.log(response) }).catch((e) => { console.log(e) })
}

function guardarDatosVemec(data) {
    axios.post('http://api:8080/VeMecApi/vemecs/', sendData, { headers: { 'Access-Control-Allow-Origin': '*' } })
        .then((response) => { console.log(repsonse) }).catch((e) => { console.log(e) })
}

function guardarDatosPaciente(data) {
    axios.post('http://api:8080/VeMecApi/pacientes/', sendData, { headers: { 'Access-Control-Allow-Origin': '*' } })
        .then((repsonse) => { console.log(response) }).catch((e) => { console.log(e) })
}

function guardarDatosFicha(data) {
    axios.post('http://api:8080/VeMecApi/fichas/', sendData, { headers: { 'Access-Control-Allow-Origin': '*' } })
        .then((response) => { console.log(response) }).catch((e) => { console.log(e) })
}