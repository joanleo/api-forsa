from email import header
import requests
from requests.auth import HTTPBasicAuth
import pprint
import os

pp = pprint.PrettyPrinter(indent=4)


def prueba():
    URL = "https://forsa-demo.herokuapp.com/prueba/auth/login"

    body = {
        "usernameOrEmail":"joan2",
        "password":"joan2"
    }

    data = body

    headers = {"content-type": "application/json"}

    with requests.Session() as sess:
        #response = sess.post(URL, data=data, headers=headers)
        response = requests.post(URL, json=body)
        #print(response.content.decode())
    res = response.json()

    token = res["tokenDeAcceso"]

    #print(token)
    return token

token = prueba()


def productos(token):

    URL = "https://forsa-demo.herokuapp.com/prueba/productos/"

    headers = {"Authorization": f'Bearer {token}'}

    #print("este es el token ", token)
    response = requests.get(URL, headers=headers)

    pp.pprint(response.json())

productos(token)