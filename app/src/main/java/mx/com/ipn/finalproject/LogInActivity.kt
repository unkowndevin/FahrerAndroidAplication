package mx.com.ipn.finalproject

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.transition.TransitionManager
import android.view.*
import android.widget.*
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.sign_up_dialog.*
import mx.com.ipn.proyectologin.RequestHandler
import org.json.JSONArray
import org.json.JSONObject
import java.security.Key
import java.util.*

class LogInActivity : AppCompatActivity() {

    private var container : ViewGroup? = null;
    private var placesArray : JSONArray? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.checkSession();
        setContentView(R.layout.activity_log_in)

        //Inicializando propiedades
        this.init();

    }

    private fun init(){

        this.container = log_in_container as ViewGroup;
        this.getPlaces();

    }

    fun showSignUpDialog(view : View?){

        //Creando el cuadro de dialogo
        val signUpDialogView : View = View.inflate(
                this, R.layout.sign_up_dialog, null
        );

        val signUpDialog : Dialog = Dialog(
                this, R.style.SignUpDialogStyle
        );
        signUpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        signUpDialog.setContentView(signUpDialogView);

        //Boton de cerrar
        val closeButton : ImageView = signUpDialog.findViewById(R.id.sign_up_dialog_close_icon) as ImageView;
        closeButton.setOnClickListener {
            setSignUpDialogAnimation(signUpDialogView, false, signUpDialog);
        }


        //Agregandor el dialogo para el datepicker
        val datePicker : TextView =
                signUpDialog.findViewById(R.id.sign_up_dialog_text_date) as TextView;
        datePicker.setOnClickListener {

            datePicker(datePicker);

        }

        //Agregando spinner para paises
        val spinner : Spinner =
                signUpDialog.findViewById(R.id.sign_up_dialog_text_place) as Spinner;

        val placesLabels : Array<String> =
                Array<String>(this.placesArray!!.length(),
                        { i->
                            this.placesArray!!.getJSONObject(i).getString("name")
                        })

        val adapter : ArrayAdapter<CharSequence> =
                ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        placesLabels

                        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = adapter;



        //Agregando el clicklistener para el boton
        val signUpButton : Button =
                signUpDialog.findViewById(R.id.sign_up_dialog_button_submit) as Button;
        signUpButton.setOnClickListener {

            signUp(signUpDialog);

        }

        //Eventos del cuadro de dialogo
        signUpDialog.setOnShowListener {
            setSignUpDialogAnimation(signUpDialogView, true, null);
        };

        signUpDialog.setOnKeyListener { dialog, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_BACK){
                setSignUpDialogAnimation(
                        signUpDialogView, false, signUpDialog
                );
                true

            }else{
                false
            }
        }

        //Terminando el cuadro de dialogo
        signUpDialog.window.setBackgroundDrawable(
                ColorDrawable(android.graphics.Color.TRANSPARENT)
        );
        signUpDialog.show();


    }

    private fun setSignUpDialogAnimation(v : View, flag : Boolean, dialog : Dialog?){

        val view : View = v.findViewById(R.id.sign_up_dialog);

        //Obteniendo el radio final y las posiciones del centro de la
        //animacion de cicular reveal

        val radiusEnd : Float = Math.hypot(
                view.width.toDouble(), view.height.toDouble()
        ).toFloat();


        val positionX : Int =
                (view.width/2).toInt();
        val positionY : Int =
                (view.height/2).toInt();


        if(flag){

            val circularRevealAnimationIn : Animator =
                ViewAnimationUtils.createCircularReveal(
                        v, positionX, positionY, 0.toFloat(), radiusEnd
                );
            v.visibility = View.VISIBLE;
            circularRevealAnimationIn.duration = 500;
            circularRevealAnimationIn.start();

        }else{

            val circularRevealAnimationOut : Animator =
                    ViewAnimationUtils.createCircularReveal(
                            v, positionX, positionY, radiusEnd, 0.toFloat()
                            );
            v.visibility = View.VISIBLE;

            circularRevealAnimationOut.addListener(
                    object : AnimatorListenerAdapter(){
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            dialog!!.dismiss();
                            v.visibility = View.INVISIBLE;
                        }
                    }
            )

            circularRevealAnimationOut.duration = 500;
            circularRevealAnimationOut.start();

        }




    }

    fun logIn(view : View){

        val email : String = log_in_input_email.text.toString()
        val password : String = log_in_input_password.text.toString()

        val data : JSONObject = JSONObject();
        data.put("email", email);
        data.put("password", password);
        //Authenticate user data
        RequestHandler.requestPOST(
                "login",
                this,
                data,
                { response ->
                    this.logInCallback(response);
                },
                { error ->
                    this.logInCallbackError(error);
                }
        );
    }

    private fun logInCallback(response : JSONObject){

        if(response.getInt("status")==200){
            //this.getProfile(null)
            this.startMainActivity();
        }else{
            this.showLogInRequestCallbackDialog(
                    super.getString(R.string.log_in_dialog_callback_title_wrong),
                    response.getString("message")
            );
        }

    }

    private fun logInCallbackError(error : VolleyError){

        this.showLogInRequestCallbackDialog(
                super.getString(R.string.log_in_dialog_callback_title_error),
                error.message.toString()
        );

    }

    private fun showLogInRequestCallbackDialog(title : String, content : String){
        val logInRequestCallbackDialogView : View =
                View.inflate(this, R.layout.log_in_request_callback_dialog, null);

        val logInRequestCallbackDialog : Dialog =
                Dialog( this, R.style.LogInRequestCallback);

        logInRequestCallbackDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logInRequestCallbackDialog.setContentView(logInRequestCallbackDialogView);

        val dismissButton : TextView =
                logInRequestCallbackDialog.findViewById(R.id.log_in_request_callback_dialog_button_done) as TextView;

        dismissButton.setOnClickListener(
                object : View.OnClickListener{
                    override fun onClick(v: View?) {
                        logInRequestCallbackDialog.dismiss();
                    }

                }
        )

        val textResponse : TextView =
                logInRequestCallbackDialog.findViewById(R.id.log_in_request_callback_dialog_body_text) as TextView;

        val textTitle : TextView =
                logInRequestCallbackDialog.findViewById(R.id.log_in_request_callback_dialog_title_text) as TextView;

        textResponse.text = content;
        textTitle.text = title;

        logInRequestCallbackDialog.setOnKeyListener(
                object : DialogInterface.OnKeyListener{
                    override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
                        return if(keyCode == KeyEvent.KEYCODE_BACK){
                            logInRequestCallbackDialog.dismiss();
                            true
                        }else{
                            false
                        }
                    }
                }
        );

        logInRequestCallbackDialog.window.attributes
                .windowAnimations = R.style.LogInRequestCallbackDialogAnimations

        logInRequestCallbackDialog.window.setBackgroundDrawable(
                ColorDrawable(android.graphics.Color.TRANSPARENT)
        );
        logInRequestCallbackDialog.show();
    }

    private fun startMainActivity(){

        val sharedPreferencesHandler :SharedPreferencesHandler =
                SharedPreferencesHandler(this);
        sharedPreferencesHandler.setLogIn(true);
        val mainActivityIntent : Intent =
                Intent(this, MainActivity::class.java);

        super.startActivity(mainActivityIntent);
        super.finish();

    }

    private fun signUp( dialog : Dialog){

        val data : JSONObject = JSONObject();
        val name : String =
                (dialog.findViewById(R.id.sign_up_dialog_text_name) as TextView)
                        .text.toString()

        val lastName : String =
                (dialog.findViewById(R.id.sign_up_dialog_text_last_name) as TextView)
                        .text.toString()

        val nickName : String =
                (dialog.findViewById(R.id.sign_up_dialog_text_nick_name) as TextView)
                        .text.toString()

        val email : String =
                (dialog.findViewById(R.id.sign_up_dialog_text_email) as TextView)
                        .text.toString()

        val password : String =
                (dialog.findViewById(R.id.sign_up_dialog_text_password) as TextView)
                        .text.toString();

        val birthDate : String =
                (dialog.findViewById(R.id.sign_up_dialog_text_date) as TextView)
                        .text.toString();

        val genreRadioButton : ArrayList<RadioButton> = arrayListOf(

                dialog.findViewById(R.id.sign_up_dialog_genre_man) as RadioButton,
                dialog.findViewById(R.id.sign_up_dialog_genre_woman) as RadioButton

        );

        val placeName : String =
                (dialog.findViewById(R.id.sign_up_dialog_text_place) as Spinner).selectedItem.toString();

        var placeAbbr : String? = null;

        for(i in 0 until this.placesArray!!.length()){

            if(this.placesArray!!
                            .getJSONObject(i)
                            .getString("name") == placeName){

                placeAbbr = this.placesArray!!.getJSONObject(i).getString("abbr");

            }

        }

        val genreRadioButtonChecked : Array<Boolean> =
                Array<Boolean>(2,

                        { i ->

                            genreRadioButton[i].isChecked

                        }

                    );

        val genre : String =
                if(genreRadioButtonChecked[0]){
                    "Hombre"
                }else{
                    "Mujer"
                }

        val privacy : Boolean =
                (dialog.findViewById(R.id.sign_up_dialog_privacy) as CheckBox).isChecked;



        data.put("name", name);
        data.put("last_name", lastName);
        data.put("nick_name", nickName);
        data.put("email", email);
        data.put("password", password);
        data.put("birth_date", birthDate);
        data.put("place", placeAbbr);
        data.put("genre", genre);

        if(privacy){

            RequestHandler.requestPOST(
                    "registrar", this, data,
                    { response ->

                        signUpRequestCallback(response, dialog);

                    },
                    { error ->

                        signUpRequestCallbackError(error);

                    }
            )

        }else{
            this.showLogInRequestCallbackDialog(
                    super.getString(R.string.sign_up_privacy_false_title),
                    super.getString(R.string.sign_up_privacy_false_description)
            );
        }


    }

    private fun datePicker(date : TextView){

        //Preparando el date picker para la fecha de nacimiento
        //Se agrega la fecha al textview que tambien sirve para
        //mostrar el datepicker
        val onDateSetListenerAdapter : DatePickerDialog.OnDateSetListener =
                object : DatePickerDialog.OnDateSetListener{

                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                        var dateText : Date = Date(year, month, dayOfMonth);

                        date.text = dateText.toString();


                    }

                };

        val datePickerDialog : DatePickerDialog =
                DatePickerDialog(
                        this, onDateSetListenerAdapter, 2000, 3, 28
                );

        datePickerDialog.window.attributes.windowAnimations =
                R.style.LogInRequestCallbackDialogAnimations;

        datePickerDialog.show();

    }

    private fun getPlaces(){
        RequestHandler.requestGET(
                "paises", this,
                { response ->

                    if(response.getInt("status")==200){
                        placesArray = response.getJSONArray("message")
                    }else{
                        placesArray = null
                    }

                },
                { error ->  placesArray = null }
        );
    }

    private fun signUpRequestCallback(response : JSONObject, dialog : Dialog){

        if(response.getInt("status")==200){
            dialog.dismiss();
            this.showLogInRequestCallbackDialog(
                    super.getString(R.string.sign_up_dialog_callback_title_success),
                    response.getString("message")
            );

        }else{
            this.showLogInRequestCallbackDialog(
                    super.getString(R.string.log_in_dialog_callback_title_wrong),
                    response.get("message").toString()
            );
        }

    }

    private fun signUpRequestCallbackError(error : VolleyError){

        this.showLogInRequestCallbackDialog(
                super.getString(R.string.log_in_dialog_callback_title_error),
                error.message.toString()
        );

    }

    private fun checkSession(){

        val sharedPreferencesHandler :SharedPreferencesHandler =
                SharedPreferencesHandler(this);
        if(sharedPreferencesHandler.getLogIn()){

            this.startMainActivity();

        }

    }

    private fun getProfile(navigationView : NavigationView?){

        /*Toast.makeText(this,"Aqui obteniendo el perfil", Toast.LENGTH_SHORT).show();
        RequestHandler.requestGET(
                "perfil", this,
                { response -> getProfileCallback(response, navigationView) },
                { error ->  getProfileCallbackError(error)}
        )*/
        RequestHandler.requestGET("sesion", this, {r->Toast.makeText(this,r.toString(), Toast.LENGTH_SHORT).show();},{e->Toast.makeText(this,e.message, Toast.LENGTH_SHORT).show();})

    }

    private fun getProfileCallback(response : JSONObject, navigationView: NavigationView){

        if(response.getInt("status")==200){

            val userData : JSONObject =
                    response.getJSONObject("message")

            val sharedPreferencesHandler : SharedPreferencesHandler =
                    SharedPreferencesHandler(this);

            sharedPreferencesHandler.setProfile(userData);

            val header : View =
                    navigationView.getHeaderView(0);

            val nickName : TextView =
                    header.findViewById(R.id.main_activity_navigation_header_nick_name);

            val email : TextView =
                    header.findViewById(R.id.main_activity_navigation_header_email);

            nickName.text =
                    userData.getString("nick_name");

            email.text =
                    userData.getString("email");

            Toast.makeText(this,userData.toString(), Toast.LENGTH_SHORT).show();


        }else{

            Toast.makeText(this,response.get("message").toString(), Toast.LENGTH_SHORT).show();

        }

    }

    private fun getProfileCallbackError(error : VolleyError){

        Toast.makeText(this,error.message, Toast.LENGTH_SHORT).show();

    }



















}
