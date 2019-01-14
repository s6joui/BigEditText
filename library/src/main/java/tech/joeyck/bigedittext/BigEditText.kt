package tech.joeyck.bigedittext

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BigEditText : RecyclerView {

    companion object {
        const val DEFAULT_SP_TEXT_SIZE : Float = 18.5f
    }

    private lateinit var adapter : BigTextAdapter

    /**
     * Length to which text will be divided in to improve performance
     */
    private var divisionLength = 3000

    /**
     * Length of search for dividable characters
     */
    private var subdivisionLength = 50

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?){
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.BigEditText)

        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = BigTextAdapter(context)
        setText(attributes.getText(R.styleable.BigEditText_text) as String?)
        setTextColor(attributes.getColor(R.styleable.BigEditText_textColor, Color.BLACK))
        val textSize = attributes.getDimensionPixelSize(R.styleable.BigEditText_textSize, -1).toFloat();
        if (textSize < 0) setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_SP_TEXT_SIZE) else setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize)
        setAdapter(adapter)

        attributes.recycle()
    }

    fun setText(text: String?) {
        adapter.setItems(text?.splitInParts(divisionLength,subdivisionLength) ?: emptyArray())
    }

    fun setTextColor(color: Int){
        adapter.textColor = color
    }

    fun setTextSize(unit: Int,size: Float){
        adapter.textSizeUnit = unit
        adapter.textSize = size
    }

    fun getText() : String{
        return adapter.list.joinToString("")
    }

    /**
     * Split String using substring, you'll have to tell where to split
     * @param len where to split
     * @return array of strings
     */
    private fun String.splitInParts(len: Int,m: Int): Array<String?> {
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
        return result
    }

    /**
     * Finds the first index of one of the characters passed as a parameter
     * @param anyOfTheseChars array of chars to look for
     * @return  the index of the first char that was found
     */
    private fun String.indexOf(anyOfTheseChars: ArrayList<Char>): Int{
        for (i in 0 until this.length) {
            val c = this[i]
            if (anyOfTheseChars.contains(c)) {
                return i
            }
        }
        return 0
    }

}

