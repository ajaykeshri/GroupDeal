package com.shotang.shotang.shotang.groupdeal;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shotang.shotang.shotang.R;
import com.shotang.shotang.shotang.groupdeal.model.GroupDealResponse;

import java.util.List;

/**
 * Created by ajay on 7/8/17.
 */

public class GroupDealCashBackAdapter extends RecyclerView.Adapter<GroupDealCashBackAdapter.DealAdapter> {

    List<GroupDealResponse.Slot> slotList;


    public GroupDealCashBackAdapter(List<GroupDealResponse.Slot> slots) {
        this.slotList=slots;

    }



    @Override
    public GroupDealCashBackAdapter.DealAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.currentdealitem,parent,false);
        return new GroupDealCashBackAdapter.DealAdapter(view);
    }

    @Override
    public void onBindViewHolder(GroupDealCashBackAdapter.DealAdapter holder, int position) {
        GroupDealResponse.Slot slot=slotList.get(position);
        if (slot.isActive()) {
            holder.parentLayout.setBackgroundColor(Color.parseColor("#DCF2C8"));
        } else {
            holder.parentLayout.setBackgroundColor(holder.parentLayout.getContext().getResources().getColor(R.color.white));
        }
        holder.currentPrice.setText(String.valueOf(slot.getCashback()));
        holder.currentUnit.setText(String.valueOf(slot.getDisplayString()));

    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }

    public class DealAdapter extends RecyclerView.ViewHolder {
        public TextView currentPrice,currentUnit;
        View parentLayout;

        public DealAdapter(View view) {
            super(view);
            parentLayout = view.findViewById(R.id.parent_layout);
            currentPrice=(TextView)view.findViewById(R.id.textView_currentdeal_price);
            currentUnit=(TextView)view.findViewById(R.id.textView_currentdeal_unit);

        }
    }
}
