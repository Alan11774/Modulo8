package mx.com.yourlawyer.ireaderqr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import mx.com.yourlawyer.ireaderqr.databinding.FragmentScannerBinding
import java.net.MalformedURLException
import java.net.URL

class ScannerFragment : Fragment() {
    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScannerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cbvScanner.decodeContinuous{ result ->
            Toast.makeText(requireContext(), "Codigo Leido: ${result.text}" , Toast.LENGTH_SHORT).show()
            Log.d("APPLOGS", "Codigo Creado: ${result.text}")

            try {
                URL(result.text)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(result.text)
                startActivity(intent)
            }catch (e: MalformedURLException){
                AlertDialog.Builder(requireContext())
                    .setTitle("Error")
                    .setMessage("El codigo no es valido para la app")
                    .setNeutralButton("Aceptar"){ dialog, _ ->
                        dialog.dismiss()

                    }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .create()
                    .show()
            }

            binding.cbvScanner.pause()
            findNavController().navigate(R.id.action_scannerFragment_to_mainFragment)


        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

    override fun onResume() {
        super.onResume()
        binding.cbvScanner.resume()

    }

    override fun onPause() {
        super.onPause()
        binding.cbvScanner.pause()
    }
}