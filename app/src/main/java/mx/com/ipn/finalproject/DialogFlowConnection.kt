package mx.com.ipn.finalproject

import ai.api.AIConfiguration
import ai.api.AIListener
import ai.api.android.AIService
import ai.api.model.AIError
import ai.api.model.AIRequest
import ai.api.model.AIResponse
import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask


class DialogFlowConnection private constructor(context: Context):
        AsyncTask<AIRequest, Void, AIResponse>() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var self : DialogFlowConnection? = null;
        fun getIns(context : Context) =
                self ?: synchronized(this){
                    self
                            ?: DialogFlowConnection(context)
                }
    }


    private val context = context;
    private val config : AIConfiguration? =
            AIConfiguration(
                    "",
                    AIConfiguration.SupportedLanguages.Spanish);
    private var aiService : AIService? = AIService.getService(context
            , this.config as ai.api.android.AIConfiguration?);


    override fun doInBackground(vararg params: AIRequest?): AIResponse {



        return AIResponse()
    }

    override fun onPostExecute(result: AIResponse?) {



    }


}