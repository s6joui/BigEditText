package tech.joeyck.bigedittextsample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bigBtn.setOnClickListener {
            val i = Intent(this,BigTextActivity::class.java)
            startActivity(i)
        }

        etBtn.setOnClickListener {
            val i = Intent(this,EditTextActivity::class.java)
            startActivity(i)
        }
    }

}
