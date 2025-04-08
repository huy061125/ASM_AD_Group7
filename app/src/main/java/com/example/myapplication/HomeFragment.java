package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.Adapter.ProductListAdapter;
import com.example.myapplication.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ListView lvProducts = view.findViewById(R.id.lvProductList);
        List<ProductModel> productModels = new ArrayList<>();
        productModels.add(new ProductModel(1, "Iphone 16 pro max", "https://cdn2.cellphones.com.vn/insecure/rs:fill:0:0/q:90/plain/https://cellphones.com.vn/media/wysiwyg/Phone/Apple/iPhone-16/iPhone-16-Pro-Max-16.jpg", 30000000));
        productModels.add(new ProductModel(2, "Iphone 16 ", "https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/iphone-16-1_3.png", 28000000));
        productModels.add(new ProductModel(3, "Iphone 15 pro max", "https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/iphone-15-plus_1_.png", 26000000));
        productModels.add(new ProductModel(4, "Iphone 15", "https://cdn2.cellphones.com.vn/insecure/rs:fill:0:0/q:90/plain/https://cellphones.com.vn/media/wysiwyg/Phone/Apple/iphone_15/dien-thoai-iphone-15-20.jpg", 24000000));
        productModels.add(new ProductModel(5, "Iphone 14 pro max", "https://taozinsaigon.com/files_upload/product/09_2022/thumbs/600_iphone_14_pro_max_tim_3.png", 22000000));
        productModels.add(new ProductModel(6, "Iphone 14", "https://cdn2.cellphones.com.vn/insecure/rs:fill:0:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/iphone-14-plus_1__1.png", 20000000));

        ProductListAdapter pdAdapter = new ProductListAdapter(getContext(), productModels);
        lvProducts.setAdapter(pdAdapter);

        lvProducts.setClickable(true);
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductModel pd = (ProductModel) lvProducts.getItemAtPosition(position);
                String name = pd.getName();
                int price = pd.getPrice();
                Toast.makeText(getActivity(), name + " - " + price, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}