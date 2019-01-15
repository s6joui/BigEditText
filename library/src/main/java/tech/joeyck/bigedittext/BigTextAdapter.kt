package tech.joeyck.bigedittext

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

internal class BigTextAdapter(private val context : Context) : RecyclerView.Adapter<BigTextAdapter.ViewHolder>() {

    var textArray : Array<String> = arrayOf("")
    var textColor : Int = Color.BLACK
    var textSize : Float = BigEditText.DEFAULT_SP_TEXT_SIZE
    var textSizeUnit : Int = TypedValue.COMPLEX_UNIT_SP
    var hint : String = ""
    var typeface: Typeface = Typeface.DEFAULT
    var textWatcher : TextWatcher? = null
    var gravity: Int = Gravity.TOP
    var enabled: Boolean = true

    //Selection
    internal var selectionEnabled = false
    var selectedItem: Int = -1

    internal var highlightEnabled = false
    private var highlightStartEditText : Int = 0
    private var highlightEndEditText : Int = 0
    private var highlightEnd : Int = 0
    var highlightStart : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val editText = EditText(context,null,android.R.attr.editTextStyle);
        return ViewHolder(editText)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val editText = holder.editText
        if(position == 0 && textArray.size == 1){
            editText.hint = hint
        }
        editText.setTextColor(textColor)
        editText.setTextSize(textSizeUnit,textSize)
        editText.setText(textArray[position])
        editText.gravity = gravity
        editText.typeface = typeface
        editText.isEnabled = enabled

        // Handle selection - NOT WORKING AS EXPECTED
        if(selectionEnabled){
            if(selectedItem == position){
                editText.selectAll()
                editText.requestFocus()
            }
        }

        if(highlightEnabled){
            if(highlightStartEditText == position && highlightEndEditText == position){
                editText.setHighlight(highlightStart,highlightEnd)
            }else if(highlightStartEditText == position) {
                editText.setHighlight(highlightStart, editText.text.length - 1)
            }else if(position > highlightStartEditText && position < highlightEndEditText){
                editText.highlightAll()
            }else if(highlightEndEditText == position){
                editText.setHighlight(0,highlightEnd)
            }
        }

        editText.setOnFocusChangeListener { view, hasFocus -> hideKeyboard(view,hasFocus) }

        // Handle text changes
        val watcher = object : TextWatcher{
            override fun afterTextChanged(text: Editable?) {
                textWatcher?.afterTextChanged(text)
            }
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textWatcher?.beforeTextChanged(text,p1,p2,p3)
            }
            override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textArray[position] = newText.toString()
                textWatcher?.onTextChanged(newText,p1,p2,p3)
            }
        }
        editText.addTextChangedListener(watcher)
        editText.tag = watcher
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.recycle()
    }

    override fun getItemCount(): Int = textArray.size

    class ViewHolder(val editText: EditText) : RecyclerView.ViewHolder(editText){

        init{
            editText.movementMethod = null
            editText.background = null
            editText.setPadding(0,0,0,0)
            editText.setTextIsSelectable(true)
        }

        fun recycle(){
            editText.removeTextChangedListener(editText.tag as TextWatcher)
            editText.onFocusChangeListener = null
        }

    }

    fun setItems(items : Array<String>){
        textArray = items
        notifyDataSetChanged()
    }

    fun setHighlight(start: Int, end: Int): Int {
        val lengths = textArray.lengths()
        highlightEnabled = true
        highlightStartEditText = 0
        highlightEndEditText = 0

        while(start > lengths[highlightStartEditText] && highlightStartEditText < lengths.size){
            highlightStartEditText++
        }

        while(end > lengths[highlightEndEditText] && highlightEndEditText < lengths.size){
            highlightEndEditText++
        }

        highlightStart = start - if (highlightStartEditText > 0) lengths[highlightStartEditText-1] else 0
        highlightEnd = end - if (highlightEndEditText > 0) lengths[highlightEndEditText-1] else 0

        notifyItemRangeChanged(highlightStartEditText,highlightEndEditText-highlightStartEditText+1)

        return highlightStartEditText
    }

    fun clearSelection(){
        selectionEnabled = false
        selectedItem = -1
    }

    fun clearHighlight(){
        highlightEnabled = false
    }

    private fun hideKeyboard(view: View,hasFocus: Boolean) {
        if(!hasFocus){
            val imm : InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }
    }


}