package com.abd.classroom1;

/**
 * Created by Abd on 3/7/2016.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class MessagesListAdapter extends BaseAdapter {
    private Context context;
    private List<ChatMessageModel> messagesItems;
    public MessagesListAdapter(Context context, List<ChatMessageModel> navDrawerItems) {
        this.context = context;
        this.messagesItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return messagesItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messagesItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /**
         * The following list not implemented reusable list items as list items
         * are showing incorrect data Add the solution if you have one
         * */

        ChatMessageModel m = messagesItems.get(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // Identifying the message owner
        if (messagesItems.get(position).isSelf()) {
            // message belongs to you, so load the right aligned layout
            convertView = mInflater.inflate(R.layout.list_item_message_right,
                    null);

            ImageView allok1 = (ImageView)convertView.findViewById(R.id.allok1);
            ImageView allok2 = (ImageView)convertView.findViewById(R.id.allok2);
            allok1.setVisibility(m.getAllok1());
            allok2.setVisibility(m.getAllok2() );
        } else {
            // message belongs to other person, load the left aligned layout
            convertView = mInflater.inflate(R.layout.list_item_message_left,
                    null);
        }

        TextView lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
        TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);
        ImageView img = (ImageView)convertView.findViewById(R.id.picMsg);

        txtMsg.setText(m.getSimpleMessage());
        lblFrom.setText(m.getSenderName());
        if(m.getMessageType().equals("IMG") ||m.getMessageType().equals("FLE")) {
            img.setImageBitmap(m.getImage());
        }
        if(m.getMessageType().equals("OK")){
            img.setImageResource(R.drawable.l1lik2);
        }

        return convertView;
    }
}