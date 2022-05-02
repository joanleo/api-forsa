

async function search(){
    //event.preventDefault()
    let element = document.getElementById("prueba").value
    console.log(element)
    let fetching = await fetchprueba(element).catch(
        error=>error.message
    )
    console.log(fetching)
}

let search_event = document.getElementById("prueba").addEventListener('keydown', search)

async function fetchprueba(letras){

    let url = 'http://localhost:8091/prueba/empresas?letter='+letras
    let data = {letter:letras}
    let request = new Request(url,{ //+'?letter=${letras}'
        method:'GET',
        headers:{
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2FuMkBnbWFpbC5jb20iLCJpYXQiOjE2NTEwNzExNzcsImV4cCI6MTY1MTA3ODM3N30.aYeVNjvKJyo3lrj1eVDsbtcL7nAh4kQqJf-Kc2H8TxABx8Jm4EbpJgL3wQpIf0RIrPFo3x62D37Dj9xxkdztQg'
        }
    })
    const response = await fetch(request)

    if(!response.ok){
        const message = "Ocurrio un error: ${response.status}"
        throw new Error(message)
    }

    const res = await response.json()

    return res
}