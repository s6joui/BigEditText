package tech.joeyck.bigedittext

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
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
    var divisionLength = 3000

    /**
     * Length of search for dividable characters
     */
    var subdivisionLength = 50

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?){
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.BigEditText)

        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = BigTextAdapter(context)
        isFocusable = false // Fixes recyclerView autoscrolling issue
        setText(attributes.getText(R.styleable.BigEditText_text) as String?)
        setHint(attributes.getText(R.styleable.BigEditText_hint) as String)
        setTextColor(attributes.getColor(R.styleable.BigEditText_textColor, Color.BLACK))
        setGravity(attributes.getInt(R.styleable.BigEditText_gravity,Gravity.TOP))
        val textSize = attributes.getDimensionPixelSize(R.styleable.BigEditText_textSize, -1).toFloat();
        if (textSize < 0) setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_SP_TEXT_SIZE) else setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize)
        setAdapter(adapter)

        attributes.recycle()
    }

    override fun clearFocus(){
        super.clearFocus()
        adapter.clearSelection()
    }

    override fun setEnabled(enabled: Boolean) {
        adapter.enabled = false
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

    fun setHint(hint: String){
        adapter.hint = hint
    }

    fun setTypeface(typeface: Typeface){
        adapter.typeface = typeface
    }

    fun setSelection(start: Int,end: Int){
        val pos = adapter.setTextSelection(start,end,divisionLength)
        scrollToPosition(pos)
    }

    fun setGravity(gravity: Int){
        adapter.gravity = gravity
    }

    fun getText() : String = adapter.list.joinToString("")

    fun getTextColor() : Int = adapter.textColor

    fun getTextSize(): Float = TypedValue.applyDimension(adapter.textSizeUnit,adapter.textSize,context.resources.displayMetrics)

    fun getHint() : String = adapter.hint

    fun getTypeface() : Typeface = adapter.typeface

    fun getSelectionStart() : Int = adapter.selectionStart

    fun getGravity() : Int = adapter.gravity

    fun addTextChangedListener(watcher: TextWatcher){
        adapter.textWatcher = watcher
    }

    fun removeTextChangedListener(watcher: TextWatcher){
        adapter.textWatcher = null
    }

    // =========================================== //
    // =============== EXTENSIONS ================ //
    // =========================================== //

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

