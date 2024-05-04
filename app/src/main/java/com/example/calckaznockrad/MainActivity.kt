package com.example.calckaznockrad

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.view.View
import java.util.Locale


class MainActivity : AppCompatActivity(){


    private lateinit var btnAdd: Button
    private lateinit var btnSub: Button
    private lateinit var btnMul: Button
    private lateinit var btnDiv: Button
    private lateinit var editTextN1 : EditText
    private lateinit var editTextN2 : EditText
    private lateinit var answer : TextView

    var num1: Int = 0
    var num2: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calculator = Calculator()

        val btn0: Button = findViewById(R.id.btn_0)
        val btn1: Button = findViewById(R.id.btn_1)
        val btn2: Button = findViewById(R.id.btn_2)
        val btn3: Button = findViewById(R.id.btn_3)
        val btn4: Button = findViewById(R.id.btn_4)
        val btn5: Button = findViewById(R.id.btn_5)
        val btn6: Button = findViewById(R.id.btn_6)
        val btn7: Button = findViewById(R.id.btn_7)
        val btn8: Button = findViewById(R.id.btn_8)
        val btn9: Button = findViewById(R.id.btn_9)
        val btnDot: Button = findViewById(R.id.btn_dot)
        val btnChangeSign: Button = findViewById(R.id.btn_change_sign)
        val btnAdd: Button = findViewById(R.id.btn_plus)
        val btnSub: Button = findViewById(R.id.btn_minus)
        val btnMul: Button = findViewById(R.id.btn_mul)
        val btnDiv: Button = findViewById(R.id.btn_div)
        val btnCalc: Button = findViewById(R.id.btn_calc)
        val btnClear: Button = findViewById(R.id.btn_clear)
        val btnBack: Button = findViewById(R.id.btn_back)




        val screen: TextView = findViewById(R.id.screen)

        btn0.setOnClickListener {
            calculator.press("0")
            screen.text = calculator.screenContent
        }
        btn1.setOnClickListener {
            calculator.press("1")
            screen.text = calculator.screenContent
        }
        btn2.setOnClickListener {
            calculator.press("2")
            screen.text = calculator.screenContent
        }
        btn3.setOnClickListener {
            calculator.press("3")
            screen.text = calculator.screenContent
        }
        btn4.setOnClickListener {
            calculator.press("4")
            screen.text = calculator.screenContent
        }
        btn5.setOnClickListener {
            calculator.press("5")
            screen.text = calculator.screenContent
        }
        btn6.setOnClickListener {
            calculator.press("6")
            screen.text = calculator.screenContent
        }
        btn7.setOnClickListener {
            calculator.press("7")
            screen.text = calculator.screenContent
        }
        btn8.setOnClickListener {
            calculator.press("8")
            screen.text = calculator.screenContent
        }
        btn9.setOnClickListener {
            calculator.press("9")
            screen.text = calculator.screenContent
        }
        btnDot.setOnClickListener {
            calculator.press(".")
            screen.text = calculator.screenContent
        }
        btnChangeSign.setOnClickListener {
            calculator.press("-/+")
            screen.text = calculator.screenContent
        }
        btnAdd.setOnClickListener {
            calculator.press("+")
            screen.text = calculator.screenContent
        }
        btnSub.setOnClickListener {
            calculator.press("-")
            screen.text = calculator.screenContent
        }
        btnMul.setOnClickListener {
            calculator.press("*")
            screen.text = calculator.screenContent
        }
        btnDiv.setOnClickListener {
            calculator.press("/")
            screen.text = calculator.screenContent
        }
        btnCalc.setOnClickListener {
            calculator.press("=")
            screen.text = calculator.screenContent
        }
        btnClear.setOnClickListener {
            calculator.press("C")
            screen.text = calculator.screenContent
        }
        btnBack.setOnClickListener {
            calculator.press("B")
            screen.text = calculator.screenContent
        }


    }

//    -----------------------------
enum class CalcState {
    Input1,
    Operation,
    Input2,
    Result,
    Error
}

    enum class CalcKey {
        Undefined,
        Digit,
        Dot,
        ChangeSign,
        Operation,
        Result,
        Clear,
        Back,
    }

    class Calculator {
        private var screen: String = "0"
        private var memory: String = ""
        private var op: String = ""
        private var temp: String = ""
        private var state: CalcState = CalcState.Input1

        val screenContent: String
            get() = screen

        fun press(key: String) {
            try {
                state = when (state) {
                    CalcState.Input1 -> processInput1(key)
                    CalcState.Input2 -> processInput2(key)
                    CalcState.Operation -> processOperation(key)
                    CalcState.Result -> processResult(key)
                    CalcState.Error -> processError(key)
                }
            } catch (e: Exception) {
                screen = "Error"
                state = CalcState.Error
            }
        }

        private fun processInput1(key: String): CalcState {
            return when (getKeyKind(key)) {
                CalcKey.Digit -> {
                    screen = addDigit(screen, key)
                    CalcState.Input1
                }
                CalcKey.Dot -> {
                    screen = addDot(screen)
                    CalcState.Input1
                }
                CalcKey.ChangeSign -> {
                    screen = changeSign(screen)
                    CalcState.Input1
                }
                CalcKey.Operation -> {
                    memory = screen
                    op = key
                    CalcState.Operation
                }
                CalcKey.Result -> CalcState.Input1
                CalcKey.Clear -> {
                    clear()
                    CalcState.Input1
                }
                CalcKey.Back -> {
                    screen = back(screen)
                    CalcState.Input1
                }
                else -> CalcState.Error
            }
        }

        private fun processInput2(key: String): CalcState {
            return when (getKeyKind(key)) {
                CalcKey.Digit -> {
                    screen = addDigit(screen, key)
                    CalcState.Input2
                }
                CalcKey.Dot -> {
                    screen = addDot(screen)
                    CalcState.Input2
                }
                CalcKey.ChangeSign -> {
                    screen = changeSign(screen)
                    CalcState.Input2
                }
                CalcKey.Operation -> {
                    screen = calculate(memory, screen, op)
                    memory = screen
                    op = key
                    CalcState.Operation
                }
                CalcKey.Result -> {
                    temp = memory
                    memory = screen
                    screen = temp
                    screen = if (op == "-" || op == "/") {
                        calculate(screen, memory, op)
                    } else {
                        calculate(memory, screen, op)
                    }
                    CalcState.Result
                }
                CalcKey.Clear -> {
                    clear()
                    CalcState.Input1
                }
                CalcKey.Back -> {
                    screen = back(screen)
                    CalcState.Input2
                }
                else -> CalcState.Error
            }
        }

        private fun processOperation(key: String): CalcState {
            return when (getKeyKind(key)) {
                CalcKey.Digit -> {
                    screen = key
                    CalcState.Input2
                }
                CalcKey.Dot -> {
                    screen = "0."
                    CalcState.Input2
                }
                CalcKey.ChangeSign -> CalcState.Operation
                CalcKey.Operation -> {
                    op = key
                    CalcState.Operation
                }
                CalcKey.Result -> {
                    screen = calculate(memory, screen, op)
                    CalcState.Result
                }
                CalcKey.Clear -> {
                    clear()
                    CalcState.Input1
                }
                CalcKey.Back -> CalcState.Operation
                else -> CalcState.Error
            }
        }

        private fun processResult(key: String): CalcState {
            return when (getKeyKind(key)) {
                CalcKey.Digit -> {
                    screen = key
                    CalcState.Input1
                }
                CalcKey.Dot -> {
                    screen = "0."
                    CalcState.Input1
                }
                CalcKey.ChangeSign -> {
                    screen = changeSign(screen)
                    CalcState.Result
                }
                CalcKey.Operation -> {
                    memory = screen
                    op = key
                    CalcState.Operation
                }
                CalcKey.Result -> {
                    screen = if (op == "-" || op == "/") {
                        calculate(screen, memory, op)
                    } else {
                        calculate(memory, screen, op)
                    }
                    CalcState.Result
                }
                CalcKey.Clear -> {
                    clear()
                    CalcState.Input1
                }
                CalcKey.Back -> CalcState.Result
                else -> CalcState.Error
            }
        }

        private fun processError(key: String): CalcState {
            return when (getKeyKind(key)) {
                CalcKey.Digit, CalcKey.Dot, CalcKey.ChangeSign, CalcKey.Operation, CalcKey.Result, CalcKey.Back -> CalcState.Error
                CalcKey.Clear -> {
                    clear()
                    CalcState.Input1
                }
                else -> CalcState.Error
            }
        }

        private fun calculate(arg1: String, arg2: String, op: String): String {
            val x = arg1.toDouble()
            val y = arg2.toDouble()
            val res = when (op) {
                "+" -> (x + y)*0.95
                "-" -> (x - y)*0.95
                "*" -> (x * y)*0.95
                "/" -> {
                    if (y == 0.0) throw Exception()
                    (x / y)*0.95
                }
                else -> throw Exception()
            }
            return String.format(Locale.US, "%.10f", res)
        }

        private fun getKeyKind(key: String): CalcKey {
            return when (key) {
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> CalcKey.Digit
                "." -> CalcKey.Dot
                "-/+" -> CalcKey.ChangeSign
                "-", "+", "*", "/" -> CalcKey.Operation
                "=" -> CalcKey.Result
                "C" -> CalcKey.Clear
                "B" -> CalcKey.Back
                else -> CalcKey.Undefined
            }
        }

        private fun addDigit(num: String, key: String): String {
            return if (num == "0" && key == "0") {
                "0"
            } else if (num == "0") {
                key
            } else {
                num + key
            }
        }

        private fun clear() {
            screen = "0"
            memory = ""
            op = ""
        }

        private fun addDot(num: String): String {
            return if (!num.contains(".")) {
                num + "."
            } else {
                num
            }
        }

        private fun changeSign(num: String): String {
            return if (num == "0") {
                "0"
            } else if (num.startsWith("-")) {
                num.substring(1)
            } else {
                "-$num"
            }
        }

        private fun back(num: String): String {
            return if (num.length == 1) {
                "0"
            } else if (num.length == 2 && num.startsWith("-")) {
                "0"
            } else {
                num.substring(0, num.length - 1)
            }
        }
    }

}