package mx.com.ipn.finalproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private val data : ArrayList<MessageStructure> =
            arrayListOf(
                    MessageStructure(0, "Hola"),
                    MessageStructure(1, "Adios")
            );

    private val adapter : ChatRecyclerViewAdapter =
            ChatRecyclerViewAdapter(this.data);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chat_toolbar.title = "";
        super.setSupportActionBar(chat_toolbar);
        super.getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        super.getSupportActionBar()!!.setHomeButtonEnabled(true);

        diagnostic_chat_recycler_view.setHasFixedSize(true);
        diagnostic_chat_recycler_view.layoutManager =
                LinearLayoutManager(this);

        diagnostic_chat_recycler_view.adapter = this.adapter;


    }

    fun agregar(view : View){

        this.data.add(MessageStructure(0,"agregado"));
        this.adapter.notifyItemInserted(this.data.size-1);
        this.adapter.notifyDataSetChanged();
        diagnostic_chat_recycler_view
                .smoothScrollToPosition(this.adapter.itemCount)
        this.data.add(MessageStructure(1,"agregado"));
        this.adapter.notifyItemInserted(this.data.size-1);
        this.adapter.notifyDataSetChanged();
        diagnostic_chat_recycler_view
                .smoothScrollToPosition(this.adapter.itemCount)


    }
}
