package com.flitzen.cng.test_customer_list;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.flitzen.cng.R;
import com.flitzen.cng.model.CustomerModel;

import java.util.List;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;

public class CityAdapter extends BaseTurboAdapter<CustomerModelTest, BaseViewHolder> {

    Context context;
    List<CustomerModelTest> data;
    GridLayoutManager gridLayoutManager;

  /*  public CityAdapter(Context context) {
        super(context);
    }*/

    public CityAdapter(Context context, List<CustomerModelTest> data, GridLayoutManager gridLayoutManager) {
        super(context, data);
        this.gridLayoutManager=gridLayoutManager;
        System.out.println("===========data   "+data.size());
    }

    @Override
    protected int getDefItemViewType(int position) {
        CustomerModelTest city = getItem(position);
        //return city.type;
        return Integer.parseInt(city.getType());
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0){
            return new CityHolder(inflateItemView(R.layout.item_city, parent));
        }
        else{
            return new PinnedHolder(inflateItemView(R.layout.item_pinned_header, parent));
        }
    }


    @Override
    protected void convert(BaseViewHolder holder, CustomerModelTest item) {
        if (holder instanceof CityHolder) {
            ((CityHolder) holder).city_name.setText(item.getName());
        }else {
            String letter = item.getName().substring(0, 1);
            ((PinnedHolder) holder).city_tip.setText(letter);
        }
    }

    public int getLetterPosition(String letter){
        for (int i = 0 ; i < getData().size(); i++){
            if(getData().get(i).getType().equalsIgnoreCase("1") && getData().get(i).getName().equals(letter)){
                return i;
            }
        }
        return -1;
    }

    class CityHolder extends BaseViewHolder {

        TextView city_name;

        public CityHolder(View view) {
            super(view);
            city_name = findViewById(R.id.city_name);
        }
    }


    class PinnedHolder extends BaseViewHolder {

        TextView city_tip;

        public PinnedHolder(View view) {
            super(view);
            city_tip = findViewById(R.id.city_tip);
        }
    }
}
