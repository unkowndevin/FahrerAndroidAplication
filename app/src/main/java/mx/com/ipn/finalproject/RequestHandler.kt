package mx.com.ipn.proyectologin

import android.content.Context
import android.hardware.Camera
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class RequestHandler constructor(){

    companion object {

        //URL, must format to complete
        private val URL : String = "https://undevin.herokuapp.com/undevin///%s";

        //GET requests handler
        fun <T> requestGET(
                    url : String /*The action to the API*/,
                    context : Context /*Context of an activity*/,
                    /*Function to get a callback of request*/
                    callback : (response : JSONObject) -> T,
                    errorCallback : (error : VolleyError) -> T){

            //Create a JSON request object to receive json response
            val jsonReq : JsonObjectRequest =
                    JsonObjectRequest(
                        Request.Method.GET,
                        this.URL.format(url),
                        null,
                        Response.Listener { response ->
                            callback(response)
                        },
                        Response.ErrorListener{error ->
                            errorCallback(error);
                        }
                    );

            //Add request to the queue
            RequestSingleton.getIns(context).addRequest(jsonReq);
        }

        //POST requests handler
        fun <Any> requestPOST(
                url : String,
                context : Context,
                data : JSONObject/*Send data as JSON to the API*/,
                callback : (response : JSONObject) -> Any,
                errorCallback: (error : VolleyError) -> Any){
            val jsonReq : JsonObjectRequest =
                    JsonObjectRequest(
                        Request.Method.POST,
                        this.URL.format(url),
                        data,
                        Response.Listener{
                            response -> callback(response)
                        },
                        Response.ErrorListener {
                            error -> errorCallback(error)
                        }
                    );

            //Add to queue
            RequestSingleton.getIns(context).addRequest(jsonReq);
        }

    }



}