package tech.joeyck.bigedittext

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.floor

internal class BigTextAdapter(private val context : Context) : RecyclerView.Adapter<BigTextAdapter.ViewHolder>() {

    var list : Array<String?> = emptyArray()
    var textColor : Int = Color.BLACK
    var textSize : Float = BigEditText.DEFAULT_SP_TEXT_SIZE
    var textSizeUnit : Int = TypedValue.COMPLEX_UNIT_SP
    var hint : String = ""
    var typeface: Typeface = Typeface.DEFAULT
    var textWatcher : TextWatcher? = null
    var gravity: Int = Gravity.TOP

    //Selection
    var selectionEnabled = false
    var selectionStartEditText : Int = 0
    var selectionEndEditText : Int = 0
    var selectionStart : Int = 0
    var selectionEnd : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val editText = EditText(context,null,android.R.attr.editTextStyle);
        return ViewHolder(editText)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val editText = holder.editText
        if(position == 0 && list.size == 1){
            editText.setHint(hint)
        }
        editText.setTextColor(textColor)
        editText.setTextSize(textSizeUnit,textSize)
        editText.setText(list[position])
        editText.gravity = gravity
        editText.typeface = typeface

        // Handle selection - NOT WORKING AS EXPECTED
        /*if(selectionEnabled){
            if(selectionStartEditText == position && selectionEndEditText == position){
                editText.setSelection(selectionStart,selectionEnd)
            }else if(selectionStartEditText == position){
                editText.setSelection(selectionStart,editText.text.length-1)
            }else if(selectionEndEditText == position){
                editText.setSelection(0,selectionEnd)
            }
        } else{
            editText.clearFocus()
        }*/

        // Handle text changes
        val watcher = object : TextWatcher{
            override fun afterTextChanged(text: Editable?) {
                textWatcher?.afterTextChanged(text)
            }
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textWatcher?.beforeTextChanged(text,p1,p2,p3)
            }
            override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                list[position] = newText.toString()
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

    override fun getItemCount(): Int = list.size

    class ViewHolder(val editText: EditText) : RecyclerView.ViewHolder(editText){

        init{
            editText.movementMethod = null
            editText.background = null
            editText.setTextIsSelectable(true)
        }

        fun recycle(){
            editText.removeTextChangedListener(editText.tag as TextWatcher)
        }

    }

    fun setItems(items : Array<String?>){
        list = items
        notifyDataSetChanged()
    }

    fun setTextSelection(start: Int, end: Int, divisionLength: Int) : Int{
        selectionEnabled = true
        selectionStartEditText = floor((start/divisionLength).toDouble()).toInt()
        selectionEndEditText = floor((end/divisionLength).toDouble()).toInt()
        selectionStart = start - (selectionStartEditText*divisionLength)
        selectionEnd = end - (selectionEndEditText*divisionLength)
        notifyItemChanged(selectionStartEditText)
        if (selectionStartEditText != selectionEndEditText) notifyItemChanged(selectionEndEditText)
        return selectionStartEditText
    }

    fun clearSelection(){
        selectionEnabled = false
    }

}