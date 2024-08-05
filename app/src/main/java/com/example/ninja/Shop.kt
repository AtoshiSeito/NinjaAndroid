package com.example.ninja

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Shop : AppCompatActivity() {
    private lateinit var backShop: Button
    private lateinit var buy1: Button
    private lateinit var buy2: Button
    private lateinit var buy3: Button
    private lateinit var buy4: Button
    private lateinit var buy5: Button
    private lateinit var buy6: Button
    private lateinit var levels: Array<Int>
    private var money: Long = 0
    private var antars = ""
    private var moneyToWtch: Long = money
    private lateinit var prefs: SharedPreferences
    private lateinit var spinner: Spinner
    private var spinCounter = 1
    private val maxlevel = 1000000

    @SuppressLint("MissingInflatedId", "SetTextI18n", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)

        backShop = findViewById(R.id.backShop)

        buy1 = findViewById(R.id.buy1)
        buy2 = findViewById(R.id.buy2)
        buy3 = findViewById(R.id.buy3)
        buy4 = findViewById(R.id.buy4)
        buy5 = findViewById(R.id.buy5)
        buy6 = findViewById(R.id.buy6)
        spinner = findViewById(R.id.spinner)

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> spinCounter = 1
                    1 -> spinCounter = 10
                    2 -> spinCounter = 0
                    else -> spinCounter = 1
                }
                updateActivityText()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        spinner.setSelection(global.getSpinSel())

        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        levels = global.getLevels()
        money = global.getMoney()
        calcAntars()

        updateActivityText()

        updateMoneyTxt()
        println(findViewById<TextView>(R.id.money).text.toString())
        buy1.setOnClickListener {
            if (spinCounter==0){
                while (money>=findViewById<TextView>(R.id.cost1).text.toString().toLong() && levels[0]<maxlevel){
                    money -= findViewById<TextView>(R.id.cost1).text.toString().toLong()
                    levels[0] += 1
                    updateActivityText()
                }
            }else if (money>=findViewById<TextView>(R.id.cost1).text.toString().toLong() && levels[0]<maxlevel) {
                money -= findViewById<TextView>(R.id.cost1).text.toString().toLong()
                levels[0] += spinCounter
                updateActivityText()
            }
        }
        buy2.setOnClickListener {
            if (spinCounter==0){
                while (money>=findViewById<TextView>(R.id.cost2).text.toString().toLong() && levels[1]<maxlevel){
                    money -= findViewById<TextView>(R.id.cost2).text.toString().toLong()
                    levels[1] += 1
                    updateActivityText()
                }
            }else if (money>=findViewById<TextView>(R.id.cost2).text.toString().toLong() && levels[1]<maxlevel) {
                money -= findViewById<TextView>(R.id.cost2).text.toString().toLong()
                levels[1] += spinCounter
                updateActivityText()
            }
        }
        buy3.setOnClickListener {
            if (spinCounter==0){
                while (money>=findViewById<TextView>(R.id.cost3).text.toString().toLong() && levels[2]<maxlevel){
                    money -= findViewById<TextView>(R.id.cost3).text.toString().toLong()
                    levels[2] += 1
                    updateActivityText()
                }
            }else if (money>=findViewById<TextView>(R.id.cost3).text.toString().toLong() && levels[2]<maxlevel) {
                money -= findViewById<TextView>(R.id.cost3).text.toString().toLong()
                levels[2] += spinCounter
                updateActivityText()
            }
        }
        buy4.setOnClickListener {
            if (spinCounter==0){
                while (money>=findViewById<TextView>(R.id.cost4).text.toString().toLong() && levels[3]<maxlevel){
                    money -= findViewById<TextView>(R.id.cost4).text.toString().toLong()
                    levels[3] += 1
                    updateActivityText()
                }
            }else if (money>=findViewById<TextView>(R.id.cost4).text.toString().toLong() && levels[3]<maxlevel) {
                money -= findViewById<TextView>(R.id.cost4).text.toString().toLong()
                levels[3] += spinCounter
                updateActivityText()
            }
        }
        buy5.setOnClickListener {
            if (spinCounter==0){
                while (money>=findViewById<TextView>(R.id.cost5).text.toString().toLong() && levels[4]<maxlevel){
                    money -= findViewById<TextView>(R.id.cost5).text.toString().toLong()
                    levels[4] += 1
                    updateActivityText()
                }
            }else if (money>=findViewById<TextView>(R.id.cost5).text.toString().toLong() && levels[4]<maxlevel) {
                money -= findViewById<TextView>(R.id.cost5).text.toString().toLong()
                levels[4] += spinCounter
                updateActivityText()
            }
        }
        buy6.setOnClickListener {
            if (spinCounter==0){
                while (money>=findViewById<TextView>(R.id.cost6).text.toString().toLong() && levels[5]<maxlevel){
                    money -= findViewById<TextView>(R.id.cost6).text.toString().toLong()
                    levels[5] += 1
                    updateActivityText()
                }
            }else if (money>=findViewById<TextView>(R.id.cost6).text.toString().toLong() && levels[5]<maxlevel) {
                money -= findViewById<TextView>(R.id.cost6).text.toString().toLong()
                levels[5] += spinCounter
                updateActivityText()
            }
        }

        backShop.setOnClickListener {
            finish()
        }
    }

    override fun onStop() {
        global.setMoney(money)
        global.setLevels(levels)
        global.setSpinSel(spinner.selectedItemPosition)
        global.setSettings(prefs)
        super.onStop()
    }

    override fun onDestroy() {
        global.setMoney(money)
        global.setLevels(levels)
        global.setSpinSel(spinner.selectedItemPosition)
        global.setSettings(prefs)
        setResult(RESULT_OK)
        super.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    fun updateMoneyTxt (){
        if (moneyToWtch%10 <2 || moneyToWtch%10 >= 5) {
            findViewById<TextView>(R.id.money).text = "$moneyToWtch $antars антар"
        } else {
            findViewById<TextView>(R.id.money).text = "$moneyToWtch $antars антары"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateActivityText(){
        calcAntars()
        updateMoneyTxt()
        findViewById<TextView>(R.id.level1).text = levels[0].toString()
        findViewById<TextView>(R.id.level2).text = levels[1].toString()
        findViewById<TextView>(R.id.level3).text = levels[2].toString()
        findViewById<TextView>(R.id.level4).text = levels[3].toString()
        findViewById<TextView>(R.id.level5).text = levels[4].toString()
        findViewById<TextView>(R.id.level6).text = levels[5].toString()
        if (spinCounter!=0) {
            findViewById<TextView>(R.id.cost1).text =
                (calcCost(levels[0], levels[0] + spinCounter) * 10).toString()
            findViewById<TextView>(R.id.cost2).text =
                (calcCost(levels[1], levels[1] + spinCounter) * 100).toString()
            findViewById<TextView>(R.id.cost3).text =
                (calcCost(levels[2], levels[2] + spinCounter) * 1000).toString()
            findViewById<TextView>(R.id.cost4).text =
                (calcCost(levels[3], levels[3] + spinCounter) * 10000).toString()
            findViewById<TextView>(R.id.cost5).text =
                (calcCost(levels[4], levels[4] + spinCounter) * 100000).toString()
            findViewById<TextView>(R.id.cost6).text =
                (calcCost(levels[5], levels[5] + spinCounter) * 1000000).toString()
        } else {
            findViewById<TextView>(R.id.cost1).text =
                (calcCost(levels[0], levels[0] + 1) * 10).toString()
            findViewById<TextView>(R.id.cost2).text =
                (calcCost(levels[1], levels[1] + 1) * 100).toString()
            findViewById<TextView>(R.id.cost3).text =
                (calcCost(levels[2], levels[2] + 1) * 1000).toString()
            findViewById<TextView>(R.id.cost4).text =
                (calcCost(levels[3], levels[3] + 1) * 10000).toString()
            findViewById<TextView>(R.id.cost5).text =
                (calcCost(levels[4], levels[4] + 1) * 100000).toString()
            findViewById<TextView>(R.id.cost6).text =
                (calcCost(levels[5], levels[5] + 1) * 1000000).toString()
        }
    }
    private fun calcAntars(){
        if (money/10000000000>0) {
            antars = "млрд."
            moneyToWtch = money/1000000000
        } else if (money/10000000>0) {
            antars = "млн."
            moneyToWtch = money/1000000
        } else if (money/10000>0) {
            antars = "тыс."
            moneyToWtch = money/1000
        } else {
            antars = ""
            moneyToWtch = money
        }
    }

    override fun onPause() {
        super.onPause()
        global.setMoney(money)
        global.setLevels(levels)
    }

    private fun calcCost(l1: Int, l2: Int): Long {
        return ((l2*(l2+1)-l1*(l1+1))/2).toLong()
    }
}