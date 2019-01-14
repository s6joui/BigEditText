package tech.joeyck.bigedittext

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

internal class BigTextAdapter(private val context : Context) : RecyclerView.Adapter<BigTextAdapter.ViewHolder>() {

    var list : Array<String?> = emptyArray()
    var textColor : Int = Color.BLACK
    var textPaddingTop : Int = 0
    var textPaddingBottom : Int = 0
    var textPaddingStart : Int = 0
    var textPaddingEnd : Int = 0
    var textSize : Float = BigEditText.DEFAULT_SP_TEXT_SIZE
    var textSizeUnit : Int = TypedValue.COMPLEX_UNIT_SP

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val editText = EditText(context,null,android.R.attr.editTextStyle);
        return ViewHolder(editText)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val editText = (holder.itemView as EditText)
        editText.setPadding(textPaddingStart,textPaddingTop,textPaddingEnd,textPaddingBottom)
        editText.setTextColor(textColor)
        editText.setTextSize(textSizeUnit,textSize)
        editText.setText(list[position])
        val watcher = object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                list[position] = newText.toString()
            }
        }
        editText.addTextChangedListener(watcher)
        editText.tag = watcher
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        val editText = (holder.itemView as EditText)
        editText.removeTextChangedListener(editText.tag as TextWatcher?)
    }

    fun setItems(items : Array<String?>){
        list = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(editText: EditText) : RecyclerView.ViewHolder(editText){

        init{
            editText.movementMethod = null
            editText.background = null
            editText.setTextIsSelectable(true)
        }

    }

}