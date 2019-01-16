package tech.joeyck.bigedittextsample

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.EditText

class EditTextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val defaultString = getString(R.string.big_string).repeat(BigTextActivity.TEXT_REPETITIONS);
        val string = sp.getString(BigTextActivity.TEXT_PREFERENCE,defaultString)

        val et = findViewById<EditText>(R.id.editText)
        et.setText(string)
        et.typeface = Typeface.create("serif", Typeface.NORMAL)

        title = "EditText length: ${string.length}"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.save -> {
                val et = findViewById<EditText>(R.id.editText)
                val text = et.getText().toString()
                val sp = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = sp.edit()
                editor.putString(BigTextActivity.TEXT_PREFERENCE,text)
                editor.apply()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
