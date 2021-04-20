package com.example.serviciospublicos.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.serviciospublicos.R;
import com.example.serviciospublicos.modelo.ClEventualidad;

import java.util.List;

public class EventualidadAdapter extends BaseAdapter {


    Activity activity;
    List<ClEventualidad> users;
    LayoutInflater inflater;

    //short to create constructer using command+n for mac & Alt+Insert for window


    public EventualidadAdapter(Activity activity) {
        this.activity = activity;
    }

    public EventualidadAdapter(Activity activity, List<ClEventualidad> users) {
        this.activity   = activity;
        this.users      = users;

        inflater        = activity.getLayoutInflater();
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if (view == null){

            view = inflater.inflate(R.layout.listvieweventualidad, viewGroup, false);

            holder = new ViewHolder();

            holder.tvUserName = (TextView)view.findViewById(R.id.tv_user_name);
            holder.ivCheckBox = (ImageView) view.findViewById(R.id.iv_check_box);

            view.setTag(holder);
        }else
            holder = (ViewHolder)view.getTag();

        ClEventualidad model = users.get(i);

        holder.tvUserName.setText(model.getNombre());

        if (model.getIsSelected())
            holder.ivCheckBox.setBackgroundResource(R.drawable.checked);

        else
            holder.ivCheckBox.setBackgroundResource(R.drawable.check);

        return view;

    }

    public void updateRecords(List<ClEventualidad> users){
        if (users!= null) {
            this.users = users;

            notifyDataSetChanged();
        }
    }

    class ViewHolder{

        TextView tvUserName;
        ImageView ivCheckBox;

    }
}