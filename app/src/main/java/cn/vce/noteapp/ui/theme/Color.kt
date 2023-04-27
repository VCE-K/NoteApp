package cn.vce.noteapp.ui.theme

import android.graphics.Color
import cn.vce.noteapp.feature_note.domain.model.Note


val DarkGray = Color.parseColor("#FF202020")
val LightBlue = Color.parseColor("#FFD7E8DE")

val RedOrange = Color.parseColor("#ffffab91")
val RedPink = Color.parseColor("#fff48fb1")
val BabyBlue = Color.parseColor("#ff81deea")
val Violet = Color.parseColor("#ffcf94da")
val LightGreen = Color.parseColor("#ffe7ed9b")

fun main() {
    val list = Note.noteColors
    println(list)
}
