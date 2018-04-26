package mx.com.ipn.finalproject

import android.content.Context
import android.graphics.Color
import android.graphics.Color.WHITE
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.message_card_sample.view.*
import org.json.JSONObject

class ChatRecyclerViewAdapter(dataSet : ArrayList<MessageStructure>) :
        RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder>() {

    private val data : ArrayList<MessageStructure> = dataSet;
    private val MESSAGE_TYPE_SENT = 0;
    private val MESSAGE_TYPE_RECEIVED = 1;


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val textView : TextView
                = itemView.findViewById(R.id.diagnostic_message_card_sample_content);

        fun set(message : String){
            textView.text = message;
        }



    }

    override fun getItemCount(): Int {

        return data.size;

    }

    override fun onBindViewHolder(holder: ChatRecyclerViewAdapter.ViewHolder, position: Int) {

        holder!!.set(data[position].MESSAGE);

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ChatRecyclerViewAdapter.ViewHolder {

        if(viewType == MESSAGE_TYPE_SENT){

            val view : View =
                    LayoutInflater.from(parent!!.context).inflate(
                        R.layout.message_card_sample,
                        parent,
                        false
                    );

            return ChatRecyclerViewAdapter.ViewHolder(view);

        }else{

            val view : View =
                    LayoutInflater.from(parent!!.context).inflate(
                            R.layout.message_card_sample_receive,
                            parent,
                            false
                    );

            return ChatRecyclerViewAdapter.ViewHolder(view);
        }

    }

    override fun getItemViewType(position: Int): Int {

        if(data[position].MESSAGE_TYPE==MESSAGE_TYPE_SENT){

            return MESSAGE_TYPE_SENT;

        }else{

            return MESSAGE_TYPE_RECEIVED;

        }

    }












}