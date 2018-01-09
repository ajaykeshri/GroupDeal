package com.shotang.shotang.shotang.groupdeal;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.shotang.shotang.shotang.R;
import com.shotang.shotang.shotang.groupdeal.model.GroupDealResponse;

import java.util.List;

import static com.shotang.shotang.shotang.MyApplication.getContext;

/**
 * Created by ajay on 8/8/17.
 */

class GroupDealVariantTypeAdapter extends RecyclerView.Adapter<GroupDealVariantTypeAdapter.DealAdapter> {

    public interface OnLsitenerVariantAdapter{
        void passQuantity(int totalQuantity);
        void passTotalAmount(float totalAmount);
        void passVariant(GroupDealResponse.Variant variant);

    }

    OnLsitenerVariantAdapter onLsitenerVariantAdapter;
    List<GroupDealResponse.Variant> variantList;
    int   totalQuantity;
    private Context mContext;
    private float perUnitPrice;
    private float totalPrice=0;

    public GroupDealVariantTypeAdapter(List<GroupDealResponse.Variant> variantList, Context context, float perUnitPrice) {
        this.variantList=variantList;
        this.mContext=context;
        this.perUnitPrice=perUnitPrice;

    }

    @Override
    public GroupDealVariantTypeAdapter.DealAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.varianttype_dealitem,parent,false);
        return new GroupDealVariantTypeAdapter.DealAdapter(view);
    }

    @Override
    public void onBindViewHolder(final GroupDealVariantTypeAdapter.DealAdapter holder, int position) {
        onLsitenerVariantAdapter=(OnLsitenerVariantAdapter)mContext;
        final GroupDealResponse.Variant variant=variantList.get(position);
        holder.productDescription.setText("Variant: "+variant.getDescription());
        holder.productMessage.setText(variant.getQtyLeftMessage());
       // holder.prodiuctMessage.setText(variant.getDescription());
        if (variant.getAvailableQuantity()>0){
            holder.noStock.setVisibility(View.GONE);
            holder.relativeLayoutAddItem.setVisibility(View.VISIBLE);
        }else {
            holder.noStock.setVisibility(View.VISIBLE);
            holder.relativeLayoutAddItem.setVisibility(View.GONE);
        }
        holder.plusButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantityAdded.getText().toString());
                quantity++;
                if (variant.getAvailableQuantity()>=quantity){
                    holder.quantityAdded.setText(""+quantity);
                    onLsitenerVariantAdapter.passQuantity(quantity);
                    totalPrice+=  perUnitPrice;
                    onLsitenerVariantAdapter.passTotalAmount(totalPrice);
                    variant.setQuantity(quantity);
                    onLsitenerVariantAdapter.passVariant(variant);
                }else{
                    Snackbar snackbar;
                    snackbar = Snackbar.make(v, "Only " +variant.getAvailableQuantity()+" quantity available", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tabHilightColor));
                    snackbar.show();
                }

            }
        });
        holder.minusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantityAdded.getText().toString());
                if (quantity>0){
                    quantity--;
                    holder.quantityAdded.setText(""+quantity);
                    onLsitenerVariantAdapter.passQuantity(quantity);
                    totalPrice-=  perUnitPrice;
                    onLsitenerVariantAdapter.passTotalAmount(totalPrice);
                    variant.setQuantity(quantity);
                    onLsitenerVariantAdapter.passVariant(variant);

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return variantList.size();
    }

    public class DealAdapter extends RecyclerView.ViewHolder {
        public TextView productDescription, productMessage, quantityAdded,noStock;
        public ImageView plusButton,minusbutton;
        public RelativeLayout relativeLayoutAddItem;


        public DealAdapter(View view) {
            super(view);
            productDescription =(TextView)view.findViewById(R.id.textview_description);
            productMessage =(TextView)view.findViewById(R.id.textView_qtyLeftMessage);
            plusButton=(ImageView)view.findViewById(R.id.imageview_plus);
            minusbutton=(ImageView)view.findViewById(R.id.imageview_minus);
            quantityAdded=(TextView)view.findViewById(R.id.textview_variant_cart_quantity);
            relativeLayoutAddItem=(RelativeLayout)view.findViewById(R.id.variant_qty_avbl_container);
            noStock=(TextView)view.findViewById(R.id.tv_nostock);


        }
    }
}
