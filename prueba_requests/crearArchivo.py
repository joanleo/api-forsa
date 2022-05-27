from random import randint, random, sample, choice
import string

def createFile():
    with open("archivo.txt", "w") as file:
        lista = string.ascii_uppercase #+ "[!@#$%&*()_+=|<>?{}\\[\\]~-]"
        for row in range(100):
            codigopieza = randint(10000,99999)

            nom = ''.join((str(letra) for letra in sample(lista, 8)))
            nombre = f"Producto prueba {nom}  {row+1}"

            area = round(random(), 2)

            
            orden = ''.join((str(letra) for letra in sample(lista, 2))) + "  " + str(row+1)

            familias = [1,2,3,4]
            familia = choice(familias)

            fabricantes = [11111, 12345]
            fabricante = choice(fabricantes)

            empresas = [1111, 23456, 34567]
            empresa = choice(empresas)


            # fila = str(codigopieza)+ '|' + nombre + '|' + str(area) + '|' + orden + '|' + familia + '|' + fabricante + '|' 
            fila = f"{codigopieza}|{nombre}|{area}|{orden}|{familia}|{fabricante}|{empresa}"
            file.write(fila+'\n')

createFile()