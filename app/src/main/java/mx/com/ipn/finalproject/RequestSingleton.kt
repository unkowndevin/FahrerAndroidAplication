package mx.com.ipn.proyectologin

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class RequestSingleton private constructor(context : Context) {

    //Using companion object instead of static for singleton
    companion object {

        //Getter fon instance of myself
        @Volatile
        private var self : RequestSingleton? = null;
        fun getIns(context : Context) =
                self ?: synchronized(this){
                    self
                            ?: RequestSingleton(context)
                }
    }

    //Create request queue, using lazy delegate
    val queue : RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext);
    }

    //Method to add request to the queue
    fun <T> addRequest(request : Request<T>){
        this.queue.add(request);
    }


}