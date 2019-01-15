package tech.joeyck.bigedittext

import android.graphics.Color
import android.widget.EditText
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.text.style.BackgroundColorSpan

// =========================================== //
// =============== EXTENSIONS ================ //
// =========================================== //

/**
 * Split String using substring, you'll have to tell where to split
 * @param len where to split
 * @return array of strings
 */
internal fun String.splitInParts(len: Int,m: Int): Array<String> {
    val result = arrayOfNulls<String>(Math.ceil(this.length.toDouble() / len.toDouble()).toInt())
    var cutStart = 0
    for (i in result.indices){
        var cutEnd = Math.min(this.length,(i+1)*len)
        if(cutEnd != this.length && (cutEnd + m) < this.length){
            val section = this.substring(cutEnd - m,cutEnd + m)
            val index = section.indexOf(arrayListOf('.','\n',';',':','?','!'))
            cutEnd = cutEnd - m + index + 1
        }
        result[i] = this.substring(cutStart, cutEnd)
        cutStart = cutEnd
    }
    return result.requireNoNulls()
}

/**
 * Finds the first index of one of the characters passed as a parameter
 * @param anyOfTheseChars array of chars to look for
 * @return  the index of the first char that was found
 */
internal fun String.indexOf(anyOfTheseChars: ArrayList<Char>): Int{
    for (i in 0 until this.length) {
        val c = this[i]
        if (anyOfTheseChars.contains(c)) {
            return i
        }
    }
    return 0
}

/**
 * Highlits text on EditText view
 * @param start setHighlight start index
 * @param end setHighlight end index
 */
internal fun EditText.setHighlight(start: Int, end: Int){
    val spannable = SpannableString(this.text)
    val startSelection = if (start < this.text.length) start else 0
    val endSelection = if (end < this.text.length) end else this.text.length
    spannable.setSpan(BackgroundColorSpan(highlightColor), startSelection, endSelection, 0)
    setText(spannable)
}

/**
 * Highlits all the text in an EditText
 */
internal fun EditText.highlightAll(){
    val spannable = SpannableString(this.text)
    spannable.setSpan(BackgroundColorSpan(highlightColor), 0, this.text.length, 0)
    setText(spannable)
}

internal fun Array<String>.lengths() : Array<Int>{
    val result = arrayOfNulls<Int>(this.size)
    var i = 0
    var sum = 0
    for (s in this){
        sum += s.length
        result[i] = sum
        i++
    }
    return result.requireNoNulls()
}