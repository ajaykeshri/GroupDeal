package com.shotang.shotang.shotang.groupdeal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shotang.shotang.shotang.R;
import com.shotang.shotang.shotang.analytics.EventSender;
import com.shotang.shotang.shotang.groupdeal.model.GroupDealResponse;
import com.shotang.shotang.shotang.misc.UtilsClass;

import java.util.ArrayList;
import java.util.List;

import static com.shotang.shotang.shotang.analytics.EventSender.GROUP_DEAL_VIEW_CLICKED;

/**
 * Created by ajay on 9/8/17.
 */

public class GroupDealMainAdapter extends RecyclerView.Adapter<GroupDealMainAdapter.MyAdapter> {

    private List<GroupDealResponse.GroupDealData> groupDealProduct;
    List<GroupDealResponse.Product> products;
    private Activity activity = null;
    private int  achievedQuantity=0;
    private int  targetQuantity=0;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GroupDealResponse.GroupDealStatus type;
    String yy;

    public GroupDealMainAdapter (List<GroupDealResponse.GroupDealData> groupDealProductList,Activity activity,GroupDealResponse.GroupDealStatus type) {
        this.groupDealProduct = groupDealProductList;
        this.activity=activity;
        this.type=type;
    }


    @Override
    public MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.groupdeal_item_product_card,parent,false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter holder, int position) {

        products=groupDealProduct.get(position).getProducts();
        //  GroupDealResponse.Product product = products.get(position);
        List<GroupDealResponse.Slot> slot=groupDealProduct.get(position).getSlots();
        List<String> minQuaty=new ArrayList<>();
      //  GroupDealResponse.Slot slot1=slot.get(position);
        for ( GroupDealResponse.Slot s:slot ) {
            minQuaty.add(String.valueOf(s.getMinQty()));
        }

        holder. ProductRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(activity);
        holder.ProductRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new ProductDealAdapter(groupDealProduct.get(position).getProducts(),activity);
        holder.ProductRecyclerView.setAdapter(mAdapter);

        for (GroupDealResponse.Slot  slotper :slot) {
            if (slotper.getIsActive()){
                if (slot.get(0).getPrice() == slotper.getPrice()){
                    holder.offerPriceTV.setText("\u20B9"+slotper.getPrice());
                    holder.actualPriceTV.setVisibility(View.GONE);
                }else{
                    holder.offerPriceTV.setVisibility(View.VISIBLE);
                    holder.offerPriceTV.setText("\u20B9"+slotper.getPrice());
                    holder.actualPriceTV.setText("\u20B9"+slot.get(0).getPrice());
                    holder.actualPriceTV .setPaintFlags(holder.actualPriceTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }

            }

        }

        if (type == GroupDealResponse.GroupDealStatus.LIVE){
            EventSender.sendGAEvent(EventSender.GROUP_DEAL,EventSender.GROUP_DEAL_LIVE_TAB);
            holder.viewdealRelativelayout.setVisibility(View.VISIBLE);
            holder.progessDeal.setVisibility(View.VISIBLE);
            holder.pb.setVisibility(View.VISIBLE);
            holder.viewDealDividerTop.setVisibility(View.GONE);
            holder.close_unitsold.setVisibility(View.GONE);
            holder.close_productQuantity.setVisibility(View.GONE);
            holder.viewdealDivider.setVisibility(View.VISIBLE);
            holder.viewDeal.setVisibility(View.VISIBLE);
            holder.dealTimeMessageShow.setText("Deal Ends In: ");
            dealEndTime(holder,position);
            dealProgress(holder,position);
        }else  if (type == GroupDealResponse.GroupDealStatus.UPCOMING){
            EventSender.sendGAEvent(EventSender.GROUP_DEAL,EventSender.GROUP_DEAL_UPCOMING_TAB);
            holder.dealTimeMessageShow.setText("Deal Starts: ");
            holder.endTime.setText(groupDealProduct.get(position).getStartDate());
            holder.progessDeal.setVisibility(View.GONE);
            holder.pb.setVisibility(View.GONE);
            holder.viewDealDividerTop.setVisibility(View.VISIBLE);
            holder.viewdealRelativelayout.setVisibility(View.GONE);
            holder.close_unitsold.setVisibility(View.GONE);
            holder.close_productQuantity.setVisibility(View.GONE);
            holder.viewdealDivider.setVisibility(View.GONE);
            holder.viewDeal.setVisibility(View.GONE);
        } else if (type == GroupDealResponse.GroupDealStatus.CLOSED) {
            EventSender.sendGAEvent(EventSender.GROUP_DEAL,EventSender.GROUP_DEAL_COLSE_TAB);
            holder.progessDeal.setVisibility(View.GONE);
            holder.pb.setVisibility(View.GONE);
            holder.viewDealDividerTop.setVisibility(View.VISIBLE);
            holder.dealTimeMessageShow.setText("Deal Closed: ");
            holder.endTime.setText(groupDealProduct.get(position).getEndDate());
            holder.close_unitsold.setVisibility(View.VISIBLE);
            holder.close_unitsold.setText(String.valueOf(groupDealProduct.get(position).getOrderedQuantity()) + " Units Sold");
            holder.close_productQuantity.setVisibility(View.VISIBLE);
            holder.viewdealDivider.setVisibility(View.VISIBLE);
            holder.viewDeal.setVisibility(View.GONE);
        }

        viewDeal(holder,position);
    }



    private void viewDeal(MyAdapter holder, final int position) {
        holder.viewDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventSender.sendGAEvent(EventSender.GROUP_DEAL,GROUP_DEAL_VIEW_CLICKED);
                EventSender.sendGAEvent(EventSender.GROUP_DEAL,GROUP_DEAL_VIEW_CLICKED,
                        String.valueOf(groupDealProduct.get(position).getDealId()));

                /*Intent intent=new Intent(activity,GroupDealProductDetailsActivity.class);
                intent.putExtra("dealId",groupDealProduct.get(position).getDealId());*/
                Intent intent=GroupDealProductDetailsActivity.getLaunchIntent(activity,String.valueOf(groupDealProduct.get(position).getDealId()));

                activity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return groupDealProduct.size();
    }

    public static class MyAdapter extends RecyclerView.ViewHolder {

        private TextView endTime, progessDeal,alreadyBuyMessage, dealTimeMessageShow,close_productQuantity,close_unitsold;
        private TextProgressBar pb;
        private ImageView modelImageView;
        private TextView  offerPriceTV, actualPriceTV;
        private  RecyclerView ProductRecyclerView;
        private View viewDealDividerTop,viewdealDivider;
        private RelativeLayout viewdealRelativelayout;

        private Button viewDeal;


        public MyAdapter(View itemView) {
            super(itemView);
            endTime=(TextView)itemView.findViewById(R.id.text_endtime);
            progessDeal=(TextView)itemView.findViewById(R.id.textview_progress);
            pb=(TextProgressBar)itemView.findViewById(R.id.textProgressBar);
            modelImageView = (ImageView) itemView.findViewById(R.id.product_card_product_iv);
            offerPriceTV=(TextView)itemView.findViewById(R.id.current_deal_price);
            viewDeal=(Button)itemView.findViewById(R.id.button_viewdeal);
            actualPriceTV=(TextView)itemView.findViewById(R.id.textView_originalprice);
            ProductRecyclerView=(RecyclerView)itemView.findViewById(R.id.recyclerView_groupdeal_product);
            dealTimeMessageShow=(TextView)itemView.findViewById(R.id.text_dealtime_message);
            viewDealDividerTop=itemView.findViewById(R.id.view_deal);
            viewdealRelativelayout=(RelativeLayout)itemView.findViewById(R.id.viewdeal_relativelayout);
            close_productQuantity=(TextView)itemView.findViewById(R.id.tv_product_quantity) ;
            close_unitsold=(TextView)itemView.findViewById(R.id.tv_unitsold) ;
            viewdealDivider=itemView.findViewById(R.id.view_viewdeal);
        }
    }

    private void dealProgress(final MyAdapter viewHolder, int position) {

        targetQuantity= groupDealProduct.get(position).getAllocatedQuantity();

        achievedQuantity =groupDealProduct.get(position).getOrderedQuantity();


        viewHolder.progessDeal.setText(achievedQuantity+" Sold "+"/ " +targetQuantity);
        viewHolder.pb.setProgress(achievedQuantity);

         viewHolder.pb.setMax(targetQuantity);
         viewHolder.progessDeal.setText(achievedQuantity+" Sold "+"/ " +targetQuantity);

         viewHolder.pb.setProgress(achievedQuantity);

    }

    private void dealEndTime(final MyAdapter viewHolder, int position) {
        String serverTime=groupDealProduct.get(position).getServerDate();
        String dealEndTime=groupDealProduct.get(position).getEndDate();
        //  2017-08-13 00:00:00

        long startDealMili=UtilsClass.calculateTimeToMiliSecond(serverTime);
        long endDealMili=UtilsClass.calculateTimeToMiliSecond(dealEndTime);
        long deatTime=endDealMili-startDealMili;
        new CountDownTimer(deatTime, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;

                long elapsedHours = millisUntilFinished / hoursInMilli;
                millisUntilFinished = millisUntilFinished % hoursInMilli;

                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                millisUntilFinished = millisUntilFinished % minutesInMilli;

                long elapsedSeconds = millisUntilFinished / secondsInMilli;

                String yy = String.format(String.format("  %dh: %dm: %ds", elapsedHours, elapsedMinutes, elapsedSeconds));
                viewHolder.endTime.setText(yy);
            }

            @Override
            public void onFinish() {
                viewHolder.endTime.setText("Deal Close!");
            }
        }.start();

    }

}
