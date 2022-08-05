package com.lugares.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.*
import com.lugares.data.LugarDao
import com.lugares.model.Lugar
import com.lugares.repository.LugarRepository

class LugarViewModel(application: Application) : AndroidViewModel(application) {
    //Atributo para obtener la lista de lugares en un ArrayList especial
    val getAllData: MutableLiveData<List<Lugar>>
    //Atributo para acceder al repositorio Lugar
    private val repository: LugarRepository = LugarRepository(LugarDao())
    //Bloque de inicialización de los atributos
    init{
        getAllData=repository.getAllData
    }
    fun addLugar(lugar: Lugar){
            repository.addLugar(lugar)
    }
    fun updateLugar(lugar: Lugar){
            repository.updateLugar(lugar)
    }
    fun deleteLugar(lugar: Lugar){
            repository.deleteLugar(lugar)
    }
}