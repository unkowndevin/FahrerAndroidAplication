package mx.com.ipn.finalproject

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.text.Html
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_welcome.*
import java.io.Console

@Suppress("DEPRECATION")
class WelcomeActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {


    private var viewPager : ViewPager? = null;
    private var dotsContainer : LinearLayout? = null;
    private var panelLayouts : Array<Int>? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        //Revisando cosas===================================
        this.checkingFirstTimeLaunch();
        //final===============================================
        setContentView(R.layout.activity_welcome);

        //Se inicializan las variables================
        this.init();
        //final========================================
        this.addDots(0);

        //Inicializando el view pager===========================
        this.viewPager!!.adapter =
                CustomViewPagerAdapter(this, this.panelLayouts!!)

        this.viewPager!!.addOnPageChangeListener(this);
        //final====================================================

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {

        //Poniendo la app inmersiva
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){

            super.getWindow().decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        }

    }

    //Inicializa las propiedades

    private fun init(){

        this.viewPager =
                super.findViewById<ViewPager>(R.id.welcome_slider_view_pager) as ViewPager;

        this.dotsContainer = welcome_slider_dots_container;

        this.panelLayouts = arrayOf(
                R.layout.welcome_slider_panels_first,
                R.layout.welcome_slider_panels_second,
                R.layout.welcome_slider_panels_third,
                R.layout.welcome_slider_panels_fourth
        )

    }

    //Agregar los puntitos
    private fun addDots(current : Int){
        val activeColors : IntArray =
            super.getResources().getIntArray(R.array.welcomeSliderArrayActiveDotBackgrounds);
        val inactiveColors : IntArray =
                super.getResources().getIntArray(R.array.welcomeSliderArrayInactiveDotBackgrounds);
        val dots : Array<TextView> =
                Array<TextView>(this.panelLayouts!!.size,
                    { i ->
                        TextView(this);
                    });

        this.dotsContainer!!.removeAllViews();

        for(i in dots.indices){
            dots[i].text = "•";
            dots[i].textSize = super.getResources()
                    .getDimension(R.dimen.welcome_slider_dots_size);
            dots[i].setTextColor(inactiveColors[current]);
            this.dotsContainer!!.addView(dots[i]);
        }

        if(dots.isNotEmpty()){
            dots[current].setTextColor(activeColors[current]);
        }
    }

    //Implementación para el listener del cambio de página

    override fun onPageSelected(position: Int) {

        addDots(position);

        if(position == this.panelLayouts!!.size-1){
            welcome_slider_button_next.text =
                    super.getString(R.string.welcome_slider_got_it_button)
            welcome_slider_button_skip.visibility = View.GONE;
        }else{
            welcome_slider_button_next.text =
                    super.getString(R.string.welcome_slider_next_button)
            welcome_slider_button_skip.visibility = View.VISIBLE;
        }

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

    private fun getItem(i : Int) : Int{
        return this.viewPager!!.currentItem + i;
    }

    //Revisando cosas para el correcto funcionamiento

    private fun checkingFirstTimeLaunch(){

        //Revisa en la preferencias compartidas si ya se ha iniciado por primer
        // ves la aplicación
        val sharedPreferencesHandler : SharedPreferencesHandler
            = SharedPreferencesHandler(this);

        if(!sharedPreferencesHandler.getIsFirstTimeLaunch()){

            //Mandamos a la activity de login
            this.launchLogInActivity(null);
            this.finish();

        }

    }

    //Acciones de botones e inciar una actividad

    fun launchLogInActivity(view : View?){

        val sharedPreferencesHandler : SharedPreferencesHandler
                = SharedPreferencesHandler(this);
        sharedPreferencesHandler.setFirstTimeLaunch(false);
        val logInIntent : Intent =
                Intent(this,LogInActivity::class.java);
        super.startActivity(logInIntent)
        super.finish();

    }

    fun nextButtonAction(view : View?){
        val current : Int = this.getItem(+1);

        if (current<this.panelLayouts!!.size){
            this.viewPager!!.currentItem = current
        }else{
            this.launchLogInActivity(null);
        }

    }








}

