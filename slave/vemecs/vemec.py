import sys
import random
import socketio
import time
from datetime import datetime, timezone
vemec = {
    "data_historial": {}
}

import socketio
time.sleep(5)

print(sys.argv)

sio = socketio.Client()

@sio.on('connect')
def on_connect():
    print('connection established')

sio.connect('http://middleware-slave:8088')

async def message(vemec):
    await sio.emit('messages', vemec)


pulsacionesCardiacas = 100
segundos = 0
pulsacionesAumentar = True

def aumentoONormalizacionDePulsaciones():
    global pulsacionesCardiacas
    global pulsacionesAumentar

    if pulsacionesAumentar:
        if pulsacionesCardiacas > 100:
            pulsacionesAumentar = False
        pulsacionesCardiacas += 1
    else:
        if pulsacionesCardiacas < 60:
            pulsacionesAumentar = True
        pulsacionesCardiacas -= 1

    if (segundos % 90) == 0:
        if random.randrange(0,5) < 4:
            pulsacionesCardiacas = 100
    

        



while True:

    vemec["vemec_id"] = 1 if len(sys.argv) == 1 else sys.argv[1]
    vemec["data_historial"]["presionMin"] = random.randrange(0,50)
    vemec["data_historial"]["presionMax"] = random.randrange(0,50)
    vemec["data_historial"]["volGas"] = random.randrange(0,50)
    vemec["data_historial"]["freqAporte"] = random.randrange(0,50)
    vemec["data_historial"]["porcenMezclaO2"] = random.randrange(0,50)
    vemec["data_historial"]["humedadAire"] = random.randrange(0,50)
    vemec["data_historial"]["tempSalidaAire"] = random.randrange(0,50)
    vemec["data_historial"]["tempEntradaAire"] = random.randrange(0,50)
    vemec["data_historial"]["presionEntradaAire"] = random.randrange(0,50)
    vemec["data_historial"]["presionSalidaAire"] = random.randrange(0,50)
    vemec["data_historial"]["timestamp"] = str(datetime.now().today().strftime('%Y-%m-%d %H:%M:%S')) 
    vemec["data_historial"]["pulsaciones"] = random.randrange(65,120) if (segundos % 90 == 0) else random.randrange(40,80)
    vemec["vemec_energia"] = {
        "tipo":"red",
        "porcentaje":100
    }
    sio.emit('messages', vemec)    
    segundos += 1
    time.sleep(1)


 
