package mx.com.ipn.finalproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_activity_content.*
import mx.com.ipn.proyectologin.RequestHandler
import org.json.JSONObject

//https://stackoverflow.com/questions/24564470/unity3d-and-android-studio-integration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.init();



    }

    private fun init(){

        val toolbar : Toolbar =
                main_activity_toolbar as Toolbar;

        super.setSupportActionBar(toolbar);

        val drawerLayout : DrawerLayout =
                main_activity_navigation_drawer as DrawerLayout;

        val navigationView : NavigationView =
                main_activity_navigation_view as NavigationView;

        val toggleActionBar : ActionBarDrawerToggle =
                ActionBarDrawerToggle(
                        this,
                        drawerLayout,
                        toolbar,
                        R.string.main_activity_navigation_drawer_open,
                        R.string.main_activity_navigation_drawer_close
                );

        drawerLayout.addDrawerListener(toggleActionBar);
        toggleActionBar.syncState();

        navigationView.setNavigationItemSelectedListener {
            this.onNavigationItemSelectedListener(it, drawerLayout);
        }

    }

    private fun onNavigationItemSelectedListener(
            item : MenuItem, drawer : DrawerLayout) : Boolean{

        when(item.itemId){

            R.id.main_navigation_item_diagnostic -> {

                menuNavigationActivity(false, DiagnosticActivity::class.java);

            }

            R.id.main_navigation_item_log_out -> {

                RequestHandler.requestGET(
                        "logout", this,
                        { response ->
                            val sharedPreferencesHandler :SharedPreferencesHandler =
                                    SharedPreferencesHandler(this);
                            sharedPreferencesHandler.setLogIn(false);
                            menuNavigationActivity(true, LogInActivity::class.java);
                        },
                        { error ->

                        }
                )

            }

            R.id.main_navigation_item_exit -> {

                super.finish();

            }

            else -> {

                Toast.makeText(this,"Funciona!", Toast.LENGTH_SHORT).show()

            }

        }


        drawer.closeDrawer(GravityCompat.START);

        return true;

    }

    private fun menuNavigationActivity(flag : Boolean, `class` : Class<*>){

        val intent : Intent =
                Intent(this, `class`);
        super.startActivity(intent);

        if(flag){

            super.finish();

        }

    }

























}
