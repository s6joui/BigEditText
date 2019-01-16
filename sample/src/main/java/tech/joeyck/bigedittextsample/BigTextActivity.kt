package tech.joeyck.bigedittextsample

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import tech.joeyck.bigedittext.BigEditText

class BigTextActivity : AppCompatActivity() {

    companion object {
        const val TEXT_PREFERENCE : String = "TEXT_PREF"
        const val TEXT_REPETITIONS : Int = 50
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_big_text)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val defaultString = getString(R.string.medium_string)//.repeat(TEXT_REPETITIONS)
        val string = sp.getString(TEXT_PREFERENCE,defaultString)

        val et = findViewById<BigEditText>(R.id.editText)
        et.setTypeface(Typeface.create("serif",Typeface.NORMAL))
        et.setText(string)

        title = "BigEditText length: ${et.getText().length}"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.highlight -> {
                val et = findViewById<BigEditText>(R.id.editText)
                et.setHighlight(321,3422)
                return true
            }
            R.id.save -> {
                val et = findViewById<BigEditText>(R.id.editText)
                val text = et.getText()
                val sp = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = sp.edit()
                editor.putString(TEXT_PREFERENCE,text)
                editor.apply()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
