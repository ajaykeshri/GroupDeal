package com.shotang.shotang.shotang.groupdeal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shotang.shotang.shotang.R;
import com.shotang.shotang.shotang.RetrofitApiClient;
import com.shotang.shotang.shotang.groupdeal.model.GroupDealResponse;
import com.shotang.shotang.shotang.groupdeal.network.IGroupDealService;
import com.shotang.shotang.shotang.misc.APIUrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by ajay on 4/8/17.
 */

public class GroupDealFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView noProduct;
    private GroupDealMainAdapter groupDealMainAdapter;
    private GroupDealResponse groupDealResponse;
    private GroupDealResponse.GroupDealStatus type;
    private LinearLayout progressbarContainer;



    public static  GroupDealFragment newIntance(GroupDealResponse.GroupDealStatus type, GroupDealResponse groupDealResponse ){
        GroupDealFragment groupDealFragment=new GroupDealFragment();
        Bundle b = new Bundle();
        b.putSerializable("type", type);
        b.putParcelable("groupDealResponse",groupDealResponse);
        groupDealFragment.setArguments(b);

        return groupDealFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = (GroupDealResponse.GroupDealStatus)getArguments().getSerializable("type");
        groupDealResponse = getArguments().getParcelable("groupDealResponse");
    }



    private void showUI() {
        // progressbarContainer.setVisibility(View.VISIBLE);

        List<GroupDealResponse.GroupDealData> groupDealDataList=groupDealResponse.getData();
        List<GroupDealResponse.GroupDealData> resultGroupDealData=new ArrayList<>();
        if (groupDealDataList!=null &&groupDealDataList.size()>0) {
            for (GroupDealResponse.GroupDealData data : groupDealDataList) {

                if (data.getStatus() ==type) {
                    resultGroupDealData.add(data);
                }
            }
        }
        noProduct.setVisibility(View.GONE);
        if (resultGroupDealData.size()==0||resultGroupDealData==null){
            noProduct.setVisibility(View.VISIBLE);
            //  progressbarContainer.setVisibility(View.GONE);
            if (type == GroupDealResponse.GroupDealStatus.LIVE){
                noProduct.setText("No deals here at the moment. You will notified when a new deal is active.");
            }else if(type == GroupDealResponse.GroupDealStatus.UPCOMING){
                noProduct.setText("No upcoming deals here at the moment. You will notified when a new deal is active.");

            }else if(type == GroupDealResponse.GroupDealStatus.CLOSED){
                noProduct.setText("No Deals !");
            }
        }
        Collections.reverse(resultGroupDealData);
        groupDealMainAdapter=new GroupDealMainAdapter(resultGroupDealData,getActivity(),type);
        recyclerView.setAdapter(groupDealMainAdapter);// set adapter on recyclerview
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.groupdeal_fragment,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView_groupdeal) ;
        noProduct=(TextView)view.findViewById(R.id.text_no_product);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressbarContainer=(LinearLayout) view.findViewById(R.id.progressbar_container);
        showUI();

        return view;
    }


}
