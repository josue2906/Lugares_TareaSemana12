package com.lugares.ui.lugar

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lugares.R
import com.lugares.databinding.FragmentUpdateLugarBinding
import com.lugares.model.Lugar
import com.lugares.viewmodel.LugarViewModel

class UpdateLugarFragment : Fragment() {

    //Se describen parÃ¡metros pasados por argumento
    private val args by navArgs<UpdateLugarFragmentArgs>()
    private var _binding: FragmentUpdateLugarBinding? = null
    private val binding get() = _binding!!

    private lateinit var lugarViewModel : LugarViewModel
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =
            ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentUpdateLugarBinding.inflate(inflater, container, false)

        //Coloco info de lugar en campos del fragmento para modificar
        binding.etNombre.setText(args.lugar.nombre)
        binding.etCorreo.setText(args.lugar.correo)
        binding.etTelefono.setText(args.lugar.telefono)
        binding.etWeb.setText(args.lugar.web)
        binding.tvLongitud.text=args.lugar.longitud.toString()
        binding.tvLatitud.text=args.lugar.latitud.toString()
        binding.tvAltura.text=args.lugar.altura.toString()



        binding.btActualizar.setOnClickListener{ updateLugar() }
        binding.btEmail.setOnClickListener{ escribirCorreo() }
        binding.btPhone.setOnClickListener{ llamarLugar() }
        binding.btWhatsapp.setOnClickListener{ enviarWhatsapp() }
        binding.btWeb.setOnClickListener{ verWeb() }
        binding.btLocation.setOnClickListener{ verMapa() }

        if(args.lugar.rutaAudio?.isNotEmpty()==true){
            mediaPlayer=MediaPlayer()
            mediaPlayer.setDataSource(args.lugar.rutaAudio)
            mediaPlayer.prepare()
            binding.btPlay.isEnabled=true
        }else{
            binding.btPlay.isEnabled=false
        }
        if(args.lugar.rutaImagen?.isNotEmpty()==true){
            Glide.with(requireContext())
                .load(args.lugar.rutaImagen)
                .fitCenter()
                .into(binding.imagen)
        }
        binding.btPlay.setOnClickListener{mediaPlayer.start()}
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun escribirCorreo() {
        val para=binding.etCorreo.text.toString()
        if(para.isNotEmpty()){
            val intent=Intent(Intent.ACTION_SEND)
            intent.type="message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(para))
            intent.putExtra(Intent.EXTRA_SUBJECT,
            getString(R.string.msg_saludos)+" "+binding.etNombre.text)
            intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.msg_mensaje_correo))
            startActivity(intent)
        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }
    private fun llamarLugar() {
        val telefono=binding.etTelefono.text.toString()
        if(telefono.isNotEmpty()){
            val intent=Intent(Intent.ACTION_CALL)
            intent.data= Uri.parse("tel:$telefono")
            if(requireActivity().checkCallingOrSelfPermission(Manifest.permission.CALL_PHONE)!=
                    PackageManager.PERMISSION_GRANTED){
                        requireActivity().requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),105)
            }else{
                requireActivity().startActivity(intent)
            }
        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }
    private fun enviarWhatsapp() {
        val telefono=binding.etTelefono.text.toString()
        if(telefono.isNotEmpty()){
            val intent=Intent(Intent.ACTION_VIEW)
            val uri="whatsapp://send?phone=506$telefono&text="+
                    getString(R.string.msg_saludos)
            intent.setPackage("com.whatsapp")
            intent.data=Uri.parse(uri)
            startActivity(intent)
        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }
    private fun verWeb() {
        val sitio=binding.etWeb.toString()
        if(sitio.isNotEmpty()){
            val uri=Uri.parse("http://$sitio")
            val intent=Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }
    private fun verMapa() {
        val latitud=binding.tvLatitud.text.toString().toDouble()
        val longitud=binding.tvLongitud.text.toString().toDouble()
        if(latitud.isFinite() && longitud.isFinite()){
            val location=Uri.parse("geo:$latitud,$longitud?z=18")
            val intent=Intent(Intent.ACTION_VIEW,location)
            startActivity(intent)
        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //si es el borrado
        if(item.itemId==R.id.menu_delete){
            deleteLugar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateLugar() {
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etCorreo.text.toString()
        val telefono = binding.etTelefono.text.toString()
        val web = binding.etWeb.text.toString()
        if (nombre.isNotEmpty()) { //si se guarda
            val lugar = Lugar(args.lugar.id, nombre, correo, telefono, web,
                0.0,0.0,0.0,"","")
            lugarViewModel.updateLugar(lugar)
            Toast.makeText(requireContext(),getString(R.string.msg_lugar_update), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar)
        }else {  //msg error
            Toast.makeText(requireContext(),getString(R.string.msg_data), Toast.LENGTH_SHORT).show()
        }
    }
    private fun deleteLugar() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.si)) { _, _ ->
            lugarViewModel.deleteLugar(args.lugar)
            Toast.makeText(
                requireContext(),
                getString(R.string.deleted) + " ${args.lugar.nombre}!",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar)
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.setTitle(R.string.deleted)
        builder.setMessage(getString(R.string.seguroBorrar) + "${args.lugar.nombre}?")
        builder.create().show()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}