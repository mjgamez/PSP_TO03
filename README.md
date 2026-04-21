
## Ejercicio 1: Instalación Servidor VPS

Puedes ver el html sobre el proceso de instalación aquí: 

[Ver Ejercicio 1](https://mjgamez.github.io/PSP_TO03/Ejercicio1_Servidor/index.html)

## Ejercicio 2: Conversor de Moneda EUR/USD

Aplicación Android que descarga al iniciarse el tipo de cambio actual entre euro y dólar desde una API pública y lo utiliza para realizar conversiones en ambas direcciones. La interfaz cuenta con una imagen decorativa descargada de Internet con la librería Glide.

---

### Tecnologías utilizadas

El proyecto está desarrollado en **Kotlin**, el lenguaje oficial de Android.

- **OkHttp** gestiona la llamada HTTP a la API de tipo de cambio de forma asíncrona mediante `enqueue` y su interfaz `Callback`, sin bloquear el hilo principal.

- **JSONObject** de la librería estándar de Android parsea manualmente la respuesta JSON para extraer el valor del campo `USD` dentro del objeto `rates`.

- **Glide** descarga y renderiza la imagen decorativa del conversor directamente en un `ImageView`.

- **ViewBinding** elimina la necesidad de `findViewById`, generando una clase de binding tipada (`ActivityEjercicio2Binding`) para acceder a las vistas de forma segura.

- **runOnUiThread** permite actualizar la interfaz de usuario desde el callback de red de OkHttp, que se ejecuta en un hilo secundario.

---

### API utilizada

**Frankfurter — Tipos de cambio del Banco Central Europeo**

```
GET https://api.frankfurter.app/latest?from=EUR&to=USD
```

API pública y gratuita que muestra los tipos de cambio oficiales del Banco Central Europeo. No requiere API Key ni autenticación. 

Ejemplo de respuesta:

```json
{
  "amount": 1.0,
  "base": "EUR",
  "date": "2026-04-21",
  "rates": {
    "USD": 1.1767
  }
}
```

![Ejemplo de respuesta json](https://raw.githubusercontent.com/mjgamez/imagenes/main/respuesta_json.jpg)


**Ejemplo de respuesta json**

---

### Pantallas

#### Ejercicio2Activity

Pantalla única con todos los elementos de la conversión:

- Imagen decorativa del conversor descargada con Glide al arrancar.
- Campo de texto para introducir euros.
- Campo de texto para introducir dólares.
- Botón **Convertir** que opera en la dirección que tenga valor introducido.

![Ejemplo de respuesta json](https://raw.githubusercontent.com/mjgamez/imagenes/main/ejemplo_conversion.jpg)


**Ejemplo de conversión**

- TextView que muestra el tipo de cambio actual descargado o el error de red si la descarga falla.

Al ganar el foco un campo, el campo contrario se limpia automáticamente para evitar ambigüedad en la dirección de la conversión.

---

### Flujo de la aplicación

```
App arranca → Ejercicio2Activity.onCreate()
└─► ViewBinding inflado
└─► Glide.load(urlImagen) → ivConversor
└─► Se registra OnFocusChangeListener en ambos campos
    └─► Al enfocar euros   → borra dolares
    └─► Al enfocar dolares → borra euros
└─► descargarRatioCambio()
    └─► OkHttpClient.newCall(request).enqueue(callback)
        ├─► onFailure  → runOnUiThread → tvCambio = "Fallo: ..."
        └─► onResponse
            ├─► Error HTTP → tvCambio = "Error en servidor: código"
            └─► Éxito → JSONObject → rates.getDouble("USD") → ratio
                       → runOnUiThread → tvCambio = "Cambio actualizado: X.XX"

Usuario pulsa "Convertir"
└─► convertir()
    └─► ¿ratio <= 0? → Toast "No se pudo convertir..."
    └─► ¿euros no vacío? → dolares = euros * ratio
    └─► ¿dolares no vacío? → euros = dolares / ratio
    └─► ¿ambos vacíos? → Toast "Introduce una cantidad"
    └─► Formato inválido → Toast "Formato de número no válido"
```

---

### Dependencias (build.gradle)

```kotlin
// OkHttp
implementation("com.squareup.okhttp3:okhttp:4.10.0")

// Glide
implementation("com.github.bumptech.glide:glide:4.16.0")
```

---

### Permisos necesarios en AndroidManifest.xml

```xml
<uses-permission android:name="android.permission.INTERNET" />
```


## Ejercicio 3: Estaciones Bizi Zaragoza

Aplicación Android que consume la API REST del Ayuntamiento de Zaragoza para mostrar en tiempo real el estado de las estaciones del servicio de bicicletas públicas **Bizi Zaragoza**. Permite consultar la disponibilidad de bicicletas y anclajes de cada estación mediante una interfaz de lista que a su vez permite consultar el detalle que cada estación clicando sobre ella. 

---

### Tecnologías utilizadas

El proyecto está desarrollado en **Kotlin**, el lenguaje oficial de Android.

- **Retrofit 2** gestiona las llamadas a la API REST del Ayuntamiento de Zaragoza. Se configura con la base URL `https://www.zaragoza.es/sede/` y un convertidor Gson para deserializar el JSON de respuesta a objetos Kotlin.

- **Gson** actúa como convertidor JSON, mapeando los campos del JSON (`@SerializedName`) a las propiedades de los data classes `BiziResponse` y `EstacionBizi`.

- **Coroutines** (`lifecycleScope.launch`) permiten ejecutar la llamada de red de forma asíncrona sin bloquear el hilo principal, respetando el ciclo de vida del fragmento gracias a `viewLifecycleOwner`.

- **RecyclerView + Adapter propio** (`BiziAdapter`) renderiza la lista de estaciones con el patrón `ViewHolder`. Incluye indicadores visuales de color (verde/naranja/rojo) según la disponibilidad de bicicletas.

- **ViewBinding** genera automáticamente una clase de binding a partir del XML de layout, eliminando la necesidad de findViewById. Reduce el riesgo de errores en tiempo de ejecución por referencias incorrectas a vistas y hace el código más legible y seguro.

- **Navigation con FragmentManager** gestiona la transición entre `FragmentLista` y `FragmentDetalle` mediante `beginTransaction().replace()` con `addToBackStack`, permitiendo volver atrás con el botón de retroceso del sistema.

---

### Modelo de datos

La API devuelve un JSON cuya raíz contiene la clave `result` con el listado de estaciones:

```kotlin
data class BiziResponse(
    @SerializedName("result") val estaciones: List<EstacionBizi>
)

data class EstacionBizi(
    @SerializedName("id")                  val id: String,
    @SerializedName("title")               val title: String,
    @SerializedName("estado")              val estado: String,
    @SerializedName("bicisDisponibles")    val bicisDisponibles: Int,
    @SerializedName("anclajesDisponibles") val anclajesDisponibles: Int,
    @SerializedName("lastUpdated")         val lastUpdated: String
) : Serializable
```

`EstacionBizi` implementa `Serializable` para poder pasarse entre fragmentos mediante `Bundle`.

---

### Pantallas

#### FragmentLista

Lista principal con todas las estaciones Bizi. Cada ítem muestra el nombre de la estación y las bicicletas disponibles con un icono de color que varía según el número de bicicletas disponibles. Esto no era un requerimiento pero me ha parecido útil para el usuario ya que, de forma muy visial, se hace a la idea de la dispoonibilidad de bicicletas. El icono se ha incorporado desde app > res > drawable > New > Vector Asset y buscándolo en Clip Art.

![Ejemplo de listado de bicicletas disponibles](https://raw.githubusercontent.com/mjgamez/imagenes/main/listado_estaciones.jpg)

**Ejemplo de listado de bicicletas disponibles**

Como es un lista bastante larga, podemos hacer scroll sobre ella para ver todas las estaciones.

![Ejemplo de listado de bicicletas disponibles](https://raw.githubusercontent.com/mjgamez/imagenes/main/scroll.jpg)

**Listado de bicicletas disponibles en el scroll**

Al pulsar sobre una estación, navega al detalle de esta.

![Ejemplo de listado de bicicletas disponibles](https://raw.githubusercontent.com/mjgamez/imagenes/main/detalle.jpg)

**Ejemplo de detalle de una estación**

#### FragmentDetalle

Vista de detalle de la estación seleccionada. Muestra:

- Nombre de la estación
- Bicicletas disponibles
- Anclajes disponibles
- Estado actual
- Última actualización

---

### Estructura del proyecto

```
com.mariajosegamez.psp_to03
└── ejercicio3/
    ├── Ejercicio3Activity.kt       # Activity contenedora del fragmento
    ├── FragmentLista.kt            # Lista de estaciones + llamada a la API
    ├── FragmentDetalle.kt          # Detalle de una estación
    ├── model/
    │   └── EstacionBizi.kt         # Data classes: BiziResponse, EstacionBizi
    ├── data/
    │   ├── RetrofitService.kt      # Interfaz con el endpoint GET
    │   └── RetrofitClient.kt       # Singleton Retrofit 
    └── adapter/
        └── BiziAdapter.kt          # Adapter + ViewHolder para el RecyclerView
```

---

## Flujo de la aplicación

```
App arranca → Ejercicio3Activity
└─► Infla ActivityEjercicio3Binding
└─► Carga FragmentLista en el contenedor

FragmentLista.onViewCreated()
└─► Configura RecyclerView con LinearLayoutManager + DividerItemDecoration
└─► Instancia BiziAdapter con lista mutable vacía
└─► Llama a obtenerDatosBizi()
    └─► lifecycleScope.launch (coroutine)
        └─► RetrofitClient.service.listadoEstacionesBici()  ← GET suspend
            ├─► Éxito → estacionList.addAll() + notifyDataSetChanged()
            └─► Excepción → Toast "Error de red"

Usuario pulsa una estación
└─► onClickListener → verDetalle(estacion)
    └─► Crea FragmentDetalle
    └─► Serializa EstacionBizi en Bundle ("ESTACION")
    └─► replace() + addToBackStack()

FragmentDetalle.onViewCreated()
└─► Deserializa EstacionBizi del Bundle
└─► Rellena tvDetalleTitulo, tvDetalleBicis, tvDetalleAnclajes,
    tvDetalleEstado, tvDetalleActualizacion
```

---

## Dependencias (build.gradle)

```kotlin
// Retrofit + Gson
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.google.code.gson:gson:2.10.1")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// OkHttp 
implementation("com.squareup.okhttp3:okhttp:4.10.0")
```

---

## Permisos necesarios en AndroidManifest.xml

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## API utilizada

**Open Data Zaragoza — Estaciones Bicicleta Bizi**

```
GET https://www.zaragoza.es/sede/servicio/urbanismo-infraestructuras/estacion-bicicleta.json
```

Servicio público del Ayuntamiento de Zaragoza, sin necesidad de API Key ni autenticación.
