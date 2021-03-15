package com.flitzen.cng.test_customer_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.CandG;
import com.flitzen.cng.R;
import com.flitzen.cng.model.CustomerModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.WebApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivityCustomerA_Z extends Fragment {

    RecyclerView mRecyclerView;
    WaveSideBarView_A_Z mSideBarView;
    CityAdapterTest adapter;
    ArrayList<CustomerModelTest> customerList = new ArrayList<>();
    ArrayList<String> chare = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    Map<String, Integer> mapIndex;
    int fill = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_customer_a_z, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mSideBarView = view.findViewById(R.id.side_view);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case 0:
                        return 1;
                    case 1:
                        return 3;
                    default:
                        return -1;
                }
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);

        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        mRecyclerView.addItemDecoration(decoration);

        new Thread(new Runnable() {
            @Override
            public void run() {
                /*Type listType = new TypeToken<ArrayList<City>>() {
                }.getType();
                Gson gson = new Gson();
                final List<City> list = gson.fromJson(City.DATA, listType);
                Collections.sort(list, new LetterComparator());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new CityAdapter(getActivity(), list);
                        mRecyclerView.setAdapter(adapter);
                    }
                });*/

                getCustomerList();

            }
        }).start();

        mSideBarView.setOnTouchLetterChangeListener(new WaveSideBarView_A_Z.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = adapter.getLetterPosition(letter);

                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });

        return view;
    }

    private void getCustomerList() {
        try {
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<CustomerModel> call = webApi.customerApi(getResources().getString(R.string.api_key));
            call.enqueue(new Callback<CustomerModel>() {
                @Override
                public void onResponse(Call<CustomerModel> call, retrofit2.Response<CustomerModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            String[] op_name_array = new String[response.body().getData().size()];
                            customerList.clear();
                            chare.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                CustomerModel.Result customerModelTest = response.body().getData().get(i);
                                if (chare.contains(response.body().getData().get(i).getName().substring(0, 1))) {
                                    customerList.add(new CustomerModelTest(customerModelTest.getCustomerId(), customerModelTest.getName(), customerModelTest.getPhone_no(), "0"));
                                } else {
                                    customerList.add(new CustomerModelTest(customerModelTest.getCustomerId(), customerModelTest.getName(), customerModelTest.getPhone_no(), "1"));
                                    customerList.add(new CustomerModelTest(customerModelTest.getCustomerId(), customerModelTest.getName(), customerModelTest.getPhone_no(), "0"));
                                    chare.add(response.body().getData().get(i).getName().substring(0, 1));
                                }

                                op_name_array[i] = response.body().getData().get(i).getName().toUpperCase();
                            }
                            Collections.sort(customerList, new LetterComparator());
                            Arrays.asList(op_name_array);
                            System.out.println("===========customer List    " + customerList.size());
                            adapter = new CityAdapterTest(getActivity(), customerList, gridLayoutManager);
                            mRecyclerView.setAdapter(adapter);

                        } else {
                            new CToast(getContext()).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                    } else {
                        new CToast(getContext()).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<CustomerModel> call, Throwable t) {
                    new CToast(getContext()).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            });
        } catch (Exception e) {

            new CToast(getContext()).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
    }
}