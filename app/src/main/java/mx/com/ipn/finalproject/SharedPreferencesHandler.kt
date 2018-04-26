package mx.com.ipn.finalproject

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

class SharedPreferencesHandler constructor(context : Context) {

    //Declaring static vars
    companion object {
        private val SHARED_PREFERENCES_SLIDER_KEY = "mx.com.ipn.fainalproject.PREFERENCES";
    }

    //Declaring shared preferences basics

    private var context : Context = context;
    private val PREFERENCES_MODE : Int = Context.MODE_PRIVATE;
    private var sharedPreferences : SharedPreferences =
            this.context.getSharedPreferences(
                    SHARED_PREFERENCES_SLIDER_KEY,
                    this.PREFERENCES_MODE
            );

    //Declaring methods to set and get the first time launch value

    fun setFirstTimeLaunch(isFirstTime : Boolean){
        with(sharedPreferences.edit()){

            this.putBoolean(
                    "IsFirstTimeLaunch", isFirstTime
            )
            this.commit();

        }
    }

    fun getIsFirstTimeLaunch() : Boolean{
        return this.sharedPreferences.getBoolean(
                "IsFirstTimeLaunch",
                true
        );
    }

    fun setLogIn(logged : Boolean){

        with(this.sharedPreferences.edit()){

            this.putBoolean(
                    "IsLogged", logged
            );
            this.commit();

        }

    }

    fun getLogIn() : Boolean{

        return this.sharedPreferences.getBoolean("IsLogged", false);

    }

    fun setProfile(data : JSONObject){

        with(this.sharedPreferences.edit()){

            this.putString("userProfile", data.toString());
            this.commit();

        }

    }

    fun getProfile() : String{

        return this.sharedPreferences.getString("userProfile", "");

    }


    }