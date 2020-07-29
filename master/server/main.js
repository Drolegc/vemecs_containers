var express = require('express')
var bodyParser = require('body-parser')
var axios = require('axios')
const { request } = require('express')
var cors = require('cors')
var app = express()
app.use(cors({ origin: '*' }))
var server = require('http').Server(app)
var io = require('socket.io')(server)

var url = "http://picacode.ddns.net:8080"

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
        axios.post(url + '/historiales/', data)
            .then((res) => console.log("Datos del historial guardados"))
            .catch((error) => console.log("Problemas guardando historial"))
    })
})

app.get('/', (req, res) => res.send("Middleware del master"))

app.get('/medicos/', async function(req, res) {
    console.log("Request medicos")
    var medicos = await axios.get(url + '/medicos/', {
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    })
    res.send(medicos.data)
})

app.get('/vemecs/', async function(req, res) {
    console.log("Request /vemecs/")
    var vemecs = await axios.get(url + '/vemecs/', {
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    })
    res.send(vemecs.data)
})

app.get('/vemecs/:id', async function(req, res) {
    console.log("Request /vemecs/:id")
    var vemec = await axios.get(url + '/vemecs/' + req.params.id, {
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    })
    res.send(vemec.data)
})

app.get('/pacientes/vemec/:id', bodyParser.json(), async function(req, res) {
    console.log("Request /pacientes/vemecs/" + req.params.id)
    var response = await axios.get(url + '/pacientes/vemec/' + req.params.id, req.body, {
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    }).catch("No existe asociacion paciente - vemec con ese id")
    res.send(response.data)
})

app.post('/fichas/', bodyParser.json(), async function(req, res) {
    console.log("Request POST /fichas/")
    console.log(req.body)
    var response = await axios.post(url + '/fichas/', req.body, {
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    }).catch((error) => console.error("ERROR " + error))
    res.send(response.data)
})

app.post('/VeMecApi/vemecs/', bodyParser.json(), async function(req, res) {
    console.log("Request POST /vemecs/")
    console.log(res.body)
    var response = await axios.post(url + '/vemecs/', req.body, {
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    }).catch((error) => console.error("ERROR " + error))
    console.log(response.data)
    res.send(response.data)
    io.emit("nuevoVemec", "Nuevo vemec a sido registrado desde un slave")
})

app.post('/VeMecApi/pacientes/', bodyParser.json(), async function(req, res) {
    console.log("Request POST /pacientes/")
    console.log(req.body)
    var response = await axios.post(url + '/pacientes/', req.body, {
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    }).catch((error) => console.log("Error " + error))
    res.send(response.data)
    io.emit("nuevoPaciente", "Nuevo paciente a sido registrado desde un slave")
})