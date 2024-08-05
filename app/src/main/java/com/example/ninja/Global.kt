package com.example.ninja

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Parcel
import android.os.Parcelable

class Global() : Application(), Parcelable {
    private var power: Long = 1
    private var money: Long = 0
    private var levels: Array<Int> = arrayOf(1, 0, 0, 0, 0, 0)
    private var spinnerSel = 0

    constructor(parcel: Parcel) : this() {
        power = parcel.readLong()
        money = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(power)
        parcel.writeLong(money)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Global> {
        override fun createFromParcel(parcel: Parcel): Global {
            return Global(parcel)
        }

        override fun newArray(size: Int): Array<Global?> {
            return arrayOfNulls(size)
        }
    }

//    fun setPower(int: Int){
//        power = int
//    }

    fun getSpinSel(): Int{
        return spinnerSel
    }

    fun setSpinSel(int: Int){
        spinnerSel = int
    }

    fun getPower(): Long {
        calcPower()
        return power
    }

    fun setMoney(int: Long){
        money = int
    }

    fun getMoney(): Long {
        return money
    }

    fun setLevels(array: Array<Int>) {
        levels = array
    }

    fun getLevels(): Array<Int> {
        return levels
    }

    private fun calcPower() {
        power = (levels[0]+levels[1]*5+levels[2]*10+levels[3]*25+levels[4]*50+levels[5]*100).toLong()
    }

    fun chekSettings(prefs:SharedPreferences) {
        if (prefs.contains("APP_MONEY")) {
            money = prefs.getLong("APP_MONEY", 0)
        }
        if (prefs.contains("APP_LEVELS")) {
            val temp = prefs.getString("APP_LEVELS", "")?.split(",")!!
            for ( i in temp.indices) {
                levels[i] = temp[i].toInt()
            }
        }
        if (prefs.contains("APP_SPIN")) {
            spinnerSel = prefs.getInt("APP_SPIN", 0)
        }
    }

    fun setSettings(prefs:SharedPreferences) {
        prefs.edit().putLong("APP_MONEY", money).apply()
        prefs.edit().putString("APP_LEVELS", levels.joinToString(separator = ",")).apply()
        prefs.edit().putInt("APP_SPIN", spinnerSel).apply()
    }
}

var global = Global()