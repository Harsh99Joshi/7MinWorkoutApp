package com.example.workoutapp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutapp.databinding.ActivityExerciseBinding
import com.example.workoutapp.databinding.DialogCustomBackBinding
import com.sevenminuteworkout.Constants
import com.sevenminuteworkout.ExerciseModel


class ExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExerciseBinding

    private var restTimer: CountDownTimer?= null
    private var restProgress: Int = 0
    private var ExerciseTimer: CountDownTimer?=null
    private var ExerciseProgress: Int = 0

    private var exerciseList: ArrayList<ExerciseModel>?=null
    private var currentExercisePosition = -1

    private var exerciseAdapter: ExerciseStatusAdapter?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding= ActivityExerciseBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarExerciseActivity)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarExerciseActivity.setNavigationOnClickListener {
            customDialogForBackButton()

        }
        exerciseList = Constants.defaultExerciseList()
        setupRestView()

        setupExerciseStatusRecyclerView()
    }

    override fun onDestroy() {
        if(restTimer!=null){
            restTimer!!.cancel()
            restProgress = 0
        }
        if(ExerciseTimer!=null){
            ExerciseTimer!!.cancel()
            ExerciseProgress = 0

        }


        super.onDestroy()

    }

    private fun setRestProgressBar(){


        binding.progressBar.progress = restProgress
        restTimer = object : CountDownTimer(10000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding.progressBar.progress = 10 - restProgress
                binding.tvTimer.text = (10 - restProgress).toString()
            }




            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                launchExercise()
            }
        }.start()

    }

    private fun setupRestView(){



        if(restTimer!=null){
            restTimer!!.cancel()
            restProgress = 0

        }
        binding.llRestView.visibility=View.VISIBLE
        binding.llExerciseView.visibility=View.GONE
        binding.tvRestView.text = exerciseList!![currentExercisePosition + 1].getName()




        setRestProgressBar()

    }

    private fun launchExercise(){

        binding.llRestView.visibility= View.GONE
        binding.llExerciseView.visibility=View.VISIBLE
        setupExerciseView()

    }

    private fun setExerciseProgressBar(){
        binding.ExerciseprogressBar.progress = ExerciseProgress
        ExerciseTimer = object : CountDownTimer(30000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                ExerciseProgress++
                binding.ExerciseprogressBar.progress = 30 - ExerciseProgress
                binding.tvExerciseTimer.text = (30 - ExerciseProgress).toString()
            }


            override fun onFinish() {
              if(currentExercisePosition< exerciseList?.size!! - 1){
                  exerciseList!![currentExercisePosition].setIsSelected(false)
                  exerciseList!![currentExercisePosition].setIsCompleted(true)
                  exerciseAdapter!!.notifyDataSetChanged()
                  setupRestView()
              }else{
                  finish()
                  val intent=Intent(this@ExerciseActivity, FinishActivity::class.java)
                  startActivity(intent)
              }
            }
        }.start()
    }

    private fun setupExerciseView(){
        if(ExerciseTimer!=null){
            ExerciseTimer!!.cancel()
            ExerciseProgress = 0

        }

        setExerciseProgressBar()

        binding.ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding.tvExerciseName.text=exerciseList!![currentExercisePosition].getName()
    }

    private fun setupExerciseStatusRecyclerView(){
        binding.rvExerciseStatus.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this@ExerciseActivity)
        binding.rvExerciseStatus.adapter = exerciseAdapter
    }

    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        val binding: DialogCustomBackBinding = DialogCustomBackBinding.inflate(layoutInflater)
        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        customDialog.setContentView(binding.root)
        // customDialog.setContentView(R.layout.dialog_custom)

        binding.tvYes.setOnClickListener(View.OnClickListener {
            finish()
            customDialog.dismiss() // Dialog will be dismissed
        })
        binding.tvNo.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "clicked cancel", Toast.LENGTH_LONG).show()
            customDialog.dismiss()
        })
        //Start the dialog and display it on screen.
        customDialog.show()
    }
}