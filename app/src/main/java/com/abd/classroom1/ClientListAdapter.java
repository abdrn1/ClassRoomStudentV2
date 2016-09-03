package com.abd.classroom1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Abd on 3/5/2016.
 */
public class ClientListAdapter extends ArrayAdapter implements View.OnClickListener {

    private final List<ClientModel> list;
    private final Activity context;
    private OnClientListAdapterInteraction theActivity;
    private String userID;

    public ClientListAdapter(Activity context, List<ClientModel> list) {
        super(context, R.layout.clients_list, list);
        this.context = context;
        this.theActivity = (OnClientListAdapterInteraction) context;
        this.list = list;
    }

    @Override
    public void onClick(View v) {

        Toast.makeText(v.getContext(),
                "Click View Message", Toast.LENGTH_LONG).show();

    }

    static class ViewHolder {
        protected TextView userid;
        protected TextView lastStatus;
        protected CheckBox checkbox;
        protected TextView unreadCounter;
        protected ImageView img;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.clients_list, null);
            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.userid = (TextView) view.findViewById(R.id.clientName);
            viewHolder.lastStatus = (TextView) view.findViewById(R.id.last_status);
            viewHolder.img = (ImageView) view.findViewById(R.id.clientImage);
            viewHolder.unreadCounter = (TextView) view.findViewById(R.id.unread_mesage);
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.clientselected);

            viewHolder.userid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClientModel element = (ClientModel) viewHolder.checkbox
                            .getTag();
                    theActivity.ShowMessagesViewer(element);
                    element.unreadMsgCounter = 0;
                    //  Toast.makeText(v.getContext(),
                    //        "Click View Message : " + element.getClientID(), Toast.LENGTH_LONG).show();
                }
            });

            viewHolder.unreadCounter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClientModel element = (ClientModel) viewHolder.checkbox
                            .getTag();
                    theActivity.ShowMessagesViewer(element);
                    element.unreadMsgCounter = 0;

                    // Toast.makeText(v.getContext(),
                    //   "Click View Message : " + element.getClientID(), Toast.LENGTH_LONG).show();
                }
            });
            viewHolder.checkbox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            ClientModel element = (ClientModel) viewHolder.checkbox
                                    .getTag();
                            element.setClientSelected(buttonView.isChecked());

                        }
                    });
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.userid.setText(list.get(position).getClientName());
        holder.lastStatus.setText(list.get(position).getLastStatus());
        holder.checkbox.setChecked(list.get(position).isClientSelected());
        holder.img.setImageResource(list.get(position).getClientImage());
        holder.unreadCounter.setText(Integer.toString(list.get(position).unreadMsgCounter));
        return view;
    }


    public interface OnClientListAdapterInteraction {
        // TODO: Update argument type and name
        public void ShowMessagesViewer(ClientModel clientModel);

    }

}
