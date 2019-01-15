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
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class BigEditText : RecyclerView {

    companion object {
        const val DEFAULT_SP_TEXT_SIZE : Float = 18.5f
    }

    private lateinit var adapter : BigTextAdapter

    /**
     * Length to which text will be divided in to improve performance
     */
    var divisionLength = 2000

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

        layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
        adapter = BigTextAdapter(context)
        isFocusable = false // Fixes recyclerView autoscrolling issue
        itemAnimator = null
        setText(attributes.getText(R.styleable.BigEditText_text) as String?)
        setHint(attributes.getText(R.styleable.BigEditText_hint) as String)
        setTextColor(attributes.getColor(R.styleable.BigEditText_textColor, Color.BLACK))
        setGravity(attributes.getInt(R.styleable.BigEditText_gravity,Gravity.TOP))
        isEnabled = attributes.getBoolean(R.styleable.BigEditText_enabled,true)
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
        adapter.enabled = enabled
    }

    fun setText(text: String?) {
        adapter.setItems(text?.splitInParts(divisionLength,subdivisionLength) ?: arrayOf(""))
    }

    fun setTextArray(text: Array<String>){
        adapter.textArray = text
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

    fun setHighlight(start: Int, end: Int){
        val pos = adapter.setHighlight(start,end)
        scrollToPosition(pos)
    }

    fun setSelectedItem(item: Int){
        adapter.selectionEnabled = true
        adapter.selectedItem = item
        adapter.notifyItemChanged(item)
        scrollToPosition(item)
    }

    fun setGravity(gravity: Int){
        adapter.gravity = gravity
    }

    fun getText() : String = adapter.textArray.joinToString("")

    fun getTextArray() : Array<String> = adapter.textArray

    fun getTextColor() : Int = adapter.textColor

    fun getTextSize(): Float = TypedValue.applyDimension(adapter.textSizeUnit,adapter.textSize,context.resources.displayMetrics)

    fun getHint() : String = adapter.hint

    fun getTypeface() : Typeface = adapter.typeface

    fun getSelectionStart() : Int = adapter.highlightStart

    fun getGravity() : Int = adapter.gravity

    fun addTextChangedListener(watcher: TextWatcher){
        adapter.textWatcher = watcher
    }

    fun removeTextChangedListener(watcher: TextWatcher){
        adapter.textWatcher = null
    }

}

