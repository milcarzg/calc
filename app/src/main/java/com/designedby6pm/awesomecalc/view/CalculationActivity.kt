package com.designedby6pm.awesomecalc.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.designedby6pm.awesomecalc.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class CalculationActivity : AppCompatActivity() {

    companion object {
        private val TAG = this::class.java.simpleName
    }

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()

    //textView for input/output
    lateinit var input: TextView

    //textView for server response
    lateinit var response: TextView

    //flag if dot was already used
    var flagDot: Boolean = false

    //flag for number usage
    var flagNumber = false

    // temporary storage
    var currentCalc = Calculation("",null,false, Date())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        input = findViewById(R.id.textView_Input)
        response = findViewById(R.id.textView_response)
    }


    /**
     * Input number
     */
    fun onNumeric(view: View) {
        input.append((view as Button).text)
        flagNumber = true
    }

    /**
     * Input dot
     */
    fun onDot(view: View) {
        if(flagNumber && !flagDot) {
            input.append((view as Button).text)
            flagNumber = false
            flagDot = true
        } else {
            Toast.makeText(this, R.string.dot_error, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Input operation
     */
    fun onOperator(view: View) {
        if(flagNumber) {
            input.append((view as Button).text)
            flagNumber = false
            flagDot = false
        }
    }

    /**
     * Press Clear
     */
    fun onClear(view: View) {
        if (input.text.length >= 2) {
            input.text = input.text.dropLast(1)
        } else {
            onDelete(view)
        }
    }

    /**
     * Press Delete
     */
    fun onDelete(view: View) {
        input.text = ""
        flagNumber = false
        flagDot = false
        currentCalc.reset()
    }

    /**
     * Press Equals
     */
    fun onEquals (view: View) {
        if (flagNumber) {
            currentCalc.calculation = input.text.toString()
            // Create an Expression (A class from exp4j library)
            try {
                // Calculate the result and display
                currentCalc.calculate()
                input.text = currentCalc.result.toString()
                flagDot = true // Result contains a dot
                flagNumber = true
            } catch (ex: Exception) {
                // Display an error message
                input.text = "Error"
                flagNumber = false
            }
        }
    }


    /**
     * Press Save
     */
    fun onSave(view : View) {
        if (currentCalc.calculation.isNotEmpty() && currentCalc.result != null && !currentCalc.saved) {
            // Add a new document with a generated ID
            currentCalc.saved = true
            db.collection("calculations")
                .add(currentCalc)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    response.setText(R.string.saved)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    response.setText(R.string.save_error)
                    currentCalc.saved = false
                }
        } else {
            response.setText(R.string.save_not)
            Toast.makeText(this, "complete calculation - nothing to save", Toast.LENGTH_SHORT).show()
        }
    }
}
