package com.shotang.shotang.shotang.groupdeal;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shotang.shotang.shotang.MyApplication;
import com.shotang.shotang.shotang.R;
import com.shotang.shotang.shotang.groupdeal.model.GroupDealResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ajay on 16/8/17.
 */

public class ProductDealAdapter extends RecyclerView.Adapter<ProductDealAdapter.DealAdapter> {

    List<GroupDealResponse.Product> productsList;


    public ProductDealAdapter(List<GroupDealResponse.Product> productsList, Activity activity) {
        this.productsList=productsList;

    }

    @Override
    public ProductDealAdapter.DealAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_deal_adaptor,parent,false);
        return new ProductDealAdapter.DealAdapter(view);
    }


    @Override
    public void onBindViewHolder(ProductDealAdapter.DealAdapter holder, int position) {
        GroupDealResponse.Product product=productsList.get(position);
        holder.modelNameTV.setText(product.getName());
        holder.modelDescriptionTV.setText(product.getDescription());
        if (product.getUserQtyMsg()!=null){
            holder.buyMessageShowHide.setVisibility(View.VISIBLE);
            holder.alreadyBuyMessage.setText(product.getUserQtyMsg());
        }else{
            holder.buyMessageShowHide.setVisibility(View.GONE);
        }

        if (holder.modelImageView != null) {
            Picasso.with(MyApplication.getContext())
                    .load(product.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.modelImageView);
        } else {
            holder.modelImageView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class DealAdapter extends RecyclerView.ViewHolder {
        public TextView modelNameTV, modelDescriptionTV,alreadyBuyMessage;
        public ImageView modelImageView;
        public LinearLayout buyMessageShowHide;



        public DealAdapter(View view) {
            super(view);
            modelImageView = (ImageView) itemView.findViewById(R.id.product_card_product_iv);
            modelNameTV = (TextView) itemView.findViewById(R.id.product_card_product_name_tv);
            modelDescriptionTV = (TextView) itemView.findViewById(R.id.product_card_product_desc_tv);
            alreadyBuyMessage = (TextView) itemView.findViewById(R.id.textview_already_buy_message);
            buyMessageShowHide=(LinearLayout)itemView.findViewById(R.id.linearLayout_buy_message);

        }

    }
}
