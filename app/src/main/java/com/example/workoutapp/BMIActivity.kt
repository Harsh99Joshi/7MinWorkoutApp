package com.example.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.workoutapp.databinding.ActivityBMIBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    val metrics_unit_view = "METRIC_UNIT_VIEW"
    val us_unit_view = "US_UNIT_VIEW"
    var currentVisibleView: String = metrics_unit_view
    private lateinit var binding: ActivityBMIBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBMIBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarBmiActivity)

        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.title = "CALCULATE BMI"
        }
        binding.toolbarBmiActivity.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnCalculateUnits.setOnClickListener {
            if (currentVisibleView == metrics_unit_view) {
                if (validateMetricUnits()) {
                    val heightValue: Float =
                        binding.etMetricUnitHeight.text.toString().toFloat() / 100
                    val weightValue: Float = binding.etMetricUnitWeight.text.toString().toFloat()

                    val bmi = weightValue / (heightValue * heightValue)
                    displayBMI(bmi)
                } else {
                    Toast.makeText(
                        this@BMIActivity,
                        "Please enter valid values",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                if (validateUsUnits()) {
                    val usUnitHeightFeet: String = binding.etUsUnitHeightFeet.text.toString()
                    val usUnitHeightInch: String = binding.etUsUnitHeightInch.text.toString()
                    val usUnitWeight: Float = binding.etUsUnitWeight.text.toString().toFloat()

                    val heightValue = usUnitHeightInch.toFloat() + (usUnitHeightFeet.toFloat() * 12)

                    val bmi = 703 * (usUnitWeight / (heightValue * heightValue)).toFloat()
                    displayBMI(bmi)
                } else {
                    Toast.makeText(
                        this@BMIActivity,
                        "Please enter valid values",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


    makeVisibleMetricUnitsView()
    binding.rgUnits.setOnCheckedChangeListener {
        group, checkedId ->
        if (checkedId == R.id.rbMetricUnits) {
            makeVisibleMetricUnitsView()
        } else if (checkedId == R.id.rbUsUnits) {
            makeVisibleUSUnitsView()

        }
    }
}




    private fun validateMetricUnits(): Boolean{
        var isValid = true

        if(binding.etMetricUnitWeight.text.toString().isEmpty()){
            isValid = false
        }
        else if(binding.etMetricUnitHeight.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }


    private fun validateUsUnits(): Boolean{
        var isValid = true

        when {
            binding.etUsUnitWeight.text.toString().isEmpty() -> {
                isValid = false
            }
            binding.etUsUnitHeightFeet.text.toString().isEmpty() -> {
                isValid = false
            }
            binding.etUsUnitHeightInch.text.toString().isEmpty() -> {
                isValid = false
            }
        }
        return isValid
    }


    private fun displayBMI(bmi : Float){


        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(bmi,30f) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class 1 (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class 2 (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class 3 (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        binding.llDiplayBMIResult.visibility=View.VISIBLE


        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding.tvBMIValue.text = bmiValue // Value is set to TextView
        binding.tvBMIType.text = bmiLabel // Label is set to TextView
        binding.tvBMIDescription.text = bmiDescription // Description is set to TextView
    }



    private fun makeVisibleMetricUnitsView(){
        binding.etUsUnitHeightFeet.text!!.clear()
        binding.etUsUnitHeightInch.text!!.clear()
        binding.etUsUnitWeight.text!!.clear()
        binding.etMetricUnitHeight.text!!.clear()
        binding.etMetricUnitWeight.text!!.clear()


        currentVisibleView = metrics_unit_view
        binding.tilMetricUnitHeight.visibility=View.VISIBLE
        binding.tilMetricUnitWeight.visibility=View.VISIBLE

        binding.tilUsUnitWeight.visibility=View.GONE
        binding.llUsUnitsHeight.visibility=View.GONE

        binding.llDiplayBMIResult.visibility=View.GONE


    }



    private fun makeVisibleUSUnitsView(){

        currentVisibleView = us_unit_view
        binding.etUsUnitHeightFeet.text!!.clear()
        binding.etUsUnitHeightInch.text!!.clear()
        binding.etUsUnitWeight.text!!.clear()
        binding.etMetricUnitHeight.text!!.clear()
        binding.etMetricUnitWeight.text!!.clear()



        binding.tilMetricUnitHeight.visibility=View.GONE
        binding.tilMetricUnitWeight.visibility=View.GONE

        binding.llUsUnitsView.visibility=View.VISIBLE
        binding.tilUsUnitWeight.visibility=View.VISIBLE
        binding.llUsUnitsHeight.visibility=View.VISIBLE

        binding.llDiplayBMIResult.visibility=View.GONE
    }
}

