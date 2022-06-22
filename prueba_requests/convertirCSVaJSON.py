import csv
from operator import itemgetter

def crearJson(archivoCSV):

    ListaDict = {}
    lista = []
    with open(archivoCSV) as csvf:
        lectorCSV = csv.reader(csvf)
        ListaDict = list(lectorCSV)

        with open('prueba.txt', 'w') as txt:
            for kv in ListaDict:
                kv =str(kv).replace('[','')
                kv =(kv).replace(']','')
                kv =(kv).replace(',','.')
                kv =(kv).replace("'",'')#+'\n'
                linea = kv.split(';')
                insertar = linea[9]+'|'+linea[5]+'|'+linea[7].replace(" ","")+'|'+linea[0]+'|'+linea[1]+'|'+'11111'+'|'+'23456'+'\n'
                print(insertar)
                #lista.append(kv)

                #kv =str(kv).replace(';','|')

                txt.write(insertar)
                #lista.append(kv)
        
        

crearJson('Muestra_Datos_Qr.csv')