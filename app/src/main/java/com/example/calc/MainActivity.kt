package com.example.calc

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    val actions = listOf("*", "+", "/", "-")
    var text = "0"
    var dotAvailable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttons = listOf(
            findViewById<Button>(R.id.button_one),
            findViewById<Button>(R.id.button_two),
            findViewById<Button>(R.id.button_three),
            findViewById<Button>(R.id.button_four),
            findViewById<Button>(R.id.button_five),
            findViewById<Button>(R.id.button_six),
            findViewById<Button>(R.id.button_seven),
            findViewById<Button>(R.id.button_eight),
            findViewById<Button>(R.id.button_nine),
            findViewById<Button>(R.id.button_zero),
            findViewById<Button>(R.id.button_equals),
            findViewById<Button>(R.id.button_plus),
            findViewById<Button>(R.id.button_minus),
            findViewById<Button>(R.id.button_multiply),
            findViewById<Button>(R.id.button_divide),
            findViewById<Button>(R.id.dot),
            findViewById<Button>(R.id.button_clear_symbol),
            findViewById<Button>(R.id.button_clear)
        )
        buttons.forEach { button ->
            button.setOnClickListener(this)
        }
    }
    fun addDigit(digit: String){
        if (text == "-0") text = "-" + digit
        else if (text != "0") text += digit
        else text = digit
        setText()
    }
    fun setText(){
        findViewById<TextView>(R.id.field).text = text
    }
    fun removeText(){
        text = "0"
        setText()
    }
    fun evaluate(): Boolean{
        if (actions.any { text.contains(it) }) {
            var operation = ""
            var result = ""
            var first = 0.0
            var second = 0.0
            for (item in text.toCharArray()) {
                if (actions.contains(item.toString())) {
                    operation = item.toString()
                }
            }
            var e = text.split(operation)
            if (e.count() == 3){
                first = e[1].toDouble() * -1
                second = e[2].toDouble()
            }
            else{
                first = e[0].toDouble()
                second = e[1].toDouble()
            }

            when{
                operation == "+" -> result = (first + second).toString()
                operation == "-" -> result = (first - second).toString()
                operation == "*" -> result = (first * second).toString()
                operation == "/" && e[1] == "0" -> result = "NAN"
                operation == "/" -> result = (first / second).toString()

            }
            text = result
            setText()
            return true
        }
        else {
            return false
        }
    }

    override fun onClick(view: View?) {
        val button = view as? Button
        button ?: return
        val buttonValue = button.text.toString()
        when {

            //removes last symbol
            button.id == R.id.button_clear_symbol -> {
                if (text.length != 1){
                    if (text.last() == '.'){
                        dotAvailable = true
                        removeText()
                    }
                    else text = text.substring(0, text.length - 1)
                }
                else removeText()
                setText()
            }

            //removes all text
            button.id == R.id.button_clear -> {
                dotAvailable = true
                removeText()
            }

            //if last symbol is dot, we can add only digits
            text.lastOrNull() != null && text.last() == '.' -> {
                if (buttonValue.first().isDigit()) {
                    addDigit(buttonValue.first().toString())
                    setText()
                }
                else return
            }
            //adding digit
            buttonValue.first().isDigit() -> {
                addDigit(buttonValue.first().toString())
            }

            actions.contains(buttonValue) -> {
                if (actions.contains(text.last().toString())) return
                else {
                    if (actions.any { text.contains(it) }) {
                        dotAvailable = true
                        if (text.first() == '-') text += buttonValue
                        else if (evaluate()) text += buttonValue
                        setText()
                    }
                    else if (text == "0") text = "-0"
                    else text += buttonValue
                    setText()
                }
            }

            button.id == R.id.button_equals -> {
                try {
                    evaluate()
                }
                catch (e: Exception){
                }
            }

            buttonValue == "." -> {
                if (dotAvailable){
                    text += "."
                    dotAvailable = false
                }
                setText()
            }
            else -> { /* And more more actions ;) */ }
        }
    }

}