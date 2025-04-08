package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ProductListAdapter extends BaseAdapter {
    public List<ProductModel> products;
    public Context context;

    public ProductListAdapter(Context context, List<ProductModel> products){
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position); //lay item theo vi tri trong list
    }

    @Override
    public long getItemId(int position) {
        return products.get(position).getId(); //lay id sp trong list
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewPd;
        if (convertView == null){
            viewPd = View.inflate(parent.getContext(), R.layout.list_item_product, null);
        } else {
            viewPd = convertView;
        }
        ProductModel pd = (ProductModel) getItem(position);
        ImageView imgPd = viewPd.findViewById(R.id.imgProduct);
        TextView tvName = viewPd.findViewById(R.id.tvNameProduct);
        TextView tvPrice = viewPd.findViewById(R.id.tvPriceProduct);

        tvName.setText(pd.getName());
        tvPrice.setText(String.valueOf(pd.getPrice()));
        Picasso.get().load(pd.getImage()).into(imgPd);

        return viewPd;
    }
}

