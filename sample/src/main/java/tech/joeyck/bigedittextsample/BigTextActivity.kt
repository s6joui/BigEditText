package tech.joeyck.bigedittextsample

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import tech.joeyck.bigedittext.BigEditText

class BigTextActivity : AppCompatActivity() {

    companion object {
        const val TEXT_PREFERENCE : String = "TEXT_PREF"
        const val TEXT_REPETITIONS : Int = 50
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_big_text)

        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val defaultString = getString(R.string.big_string).repeat(TEXT_REPETITIONS)
        val string = sp.getString(TEXT_PREFERENCE,defaultString)

        val et = findViewById<BigEditText>(R.id.editText)
        et.setText(string)
        et.setTypeface(Typeface.create("serif",Typeface.NORMAL))

        title = "BigEditText length: ${string.length} ${et.getTextSize()}px"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
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
