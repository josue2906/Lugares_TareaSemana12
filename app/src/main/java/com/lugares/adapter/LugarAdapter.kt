import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lugares.databinding.LugarFilaBinding
import com.lugares.model.Lugar
import com.lugares.ui.lugar.LugarFragmentDirections

class LugarAdapter : RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {

    //una lista para gestionar la información de los lugares
    private var lista = emptyList<Lugar>()

    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding)
        : RecyclerView.ViewHolder (itemBinding.root){
        fun dibuja(lugar: Lugar) {
            itemBinding.tvNombre.text = lugar.nombre
            itemBinding.tvCorreo.text = lugar.correo
            itemBinding.tvTelefono.text = lugar.telefono
            itemBinding.tvWeb.text = lugar.web

            Glide.with(itemBinding.root.context)
                .load(lugar.rutaImagen)
                .circleCrop()
                .into(itemBinding.imagen)

            itemBinding.vistaFila.setOnClickListener{
                val action=LugarFragmentDirections
                    .actionNavLugarToUpdateLugarFragment(lugar)
                itemView.findNavController().navigate(action)
            }
        }
    }

    //Acá se va a crear una "cajita" del reciclador...  una fila... sólo la estructura
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val itemBinding =
            LugarFilaBinding.inflate(LayoutInflater.from(parent.context),
                parent,false)
        return LugarViewHolder(itemBinding)
    }

    //Acá se va a solicitar "dibujar" una cajita, según el elemento de la lista...
    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        val lugar = lista[position]
        holder.dibuja(lugar)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    fun setData(lugares: List<Lugar>) {
        lista = lugares
        notifyDataSetChanged()
    }

}