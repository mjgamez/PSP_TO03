package ejercicio3.data


import com.mariajosegamez.psp_to03.ejercicio3.model.BiziResponse
import retrofit2.Response
import retrofit2.http.GET


interface RetrofitService {
    @GET("servicio/urbanismo-infraestructuras/estacion-bicicleta.json")
    suspend fun listadoEstacionesBici(): Response<BiziResponse>
}