package com.nnt.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.nnt.myapplication.databinding.ActivityMainBinding
import com.nnt.myapplication.model.Calculo

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.botaoCalcular.setOnClickListener { calcular() }
    }

    private fun calcular() {
        val peso = binding.campoPeso.text.toString().replace(",", ".").toDoubleOrNull()
        val altura = binding.campoAltura.text.toString().replace(",", ".").toDoubleOrNull()
        val imc: Double

        if(peso == null || altura == null) {
            Toast.makeText(this, "Por favor preencha todos os campos.", Toast.LENGTH_SHORT).show()
        } else {
            imc = peso / (altura * altura) // IMC = peso (kg) / altura (m) x altura (m)

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("imc", imc.toString())

            //binding.textViewTeste.text = "peso: $peso\naltura: $altura\nimc: $imc"

            Thread{
                val app = application as App
                val dao = app.db.calculoDao()

                dao.inserir(Calculo(tipo = "imc", resultado = imc))

                runOnUiThread{
                    //Toast.makeText(this@TMBActivity, "Medição salva com sucesso!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@MainActivity, ListaCalculoActivity::class.java)
                    intent.putExtra("tipo", "imc")
                    startActivity(intent)
                }
            }.start()

            startActivity(intent)
        }
    }
}