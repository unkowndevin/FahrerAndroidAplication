package mx.com.ipn.finalproject

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import kotlinx.android.synthetic.main.activity_diagnostic.*

class DiagnosticActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnostic)

        super.setSupportActionBar(diagnostic_toolbar as Toolbar);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);

    }

    fun startActivity(view : View){

        val chatIntent : Intent =
                Intent(this,ChatActivity::class.java);
        super.startActivity(chatIntent);

    }

}
