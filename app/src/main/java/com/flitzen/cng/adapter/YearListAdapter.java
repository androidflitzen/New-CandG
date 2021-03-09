package com.flitzen.cng.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flitzen.cng.R;
import com.flitzen.cng.utils.SharePref;

import java.util.List;

public class YearListAdapter extends ArrayAdapter<String> {
    private List<String> objects;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public YearListAdapter(Context context,
                           List<String> objects) {
        super(context, 0,objects);
        this.objects = objects;
        this.context = context;
        sharedPreferences = SharePref.getSharePref(context);
        editor = sharedPreferences.edit();
        editor.putString(SharePref.QT_YEAR, "0");
        editor.commit();
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.custom_month_selection_layout, parent, false);
        LinearLayout linMain=row.findViewById(R.id.linMain);
        TextView label=row.findViewById(R.id.txtMonthName);
        ImageView imgCheck=row.findViewById(R.id.imgCheck);
        RelativeLayout relMain=row.findViewById(R.id.relMain);
        label.setText(objects.get(position));
        if(sharedPreferences.getString(SharePref.QT_YEAR,"").equals(String.valueOf(position))){
            imgCheck.setVisibility(View.VISIBLE);
           /* relMain.setBackgroundColor(context.getResources().getColor(R.color.red_on_swipe));
            label.setTextColor(context.getResources().getColor(R.color.white));*/
        }else {
            imgCheck.setVisibility(View.GONE);
        }
       /* linMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCheck.setVisibility(View.VISIBLE);
                TextView textView=((HomeActivity)getContext()).findViewById(R.id.txtMonthName);
                Spinner txtSpinnerMonth=((HomeActivity)getContext()).findViewById(R.id.txtSpinnerMonth);
                textView.setText(objects.get(position));
                editor.putString(SharePref.QT_MONTH, String.valueOf(position));
                editor.commit();
                notifyDataSetChanged();
                txtSpinnerMonth.clearFocus();
            }
        });*/

       /* if (position == 0) {//Special style for dropdown header
            label.setTextColor(context.getResources().getColor(R.color.text_hint_color));
        }*/

        return row;
    }
}
