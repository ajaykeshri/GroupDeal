package com.shotang.shotang.shotang.groupdeal;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shotang.shotang.shotang.R;
import com.shotang.shotang.shotang.RetrofitApiClient;
import com.shotang.shotang.shotang.analytics.EventSender;
import com.shotang.shotang.shotang.cart.models.PaymentTypeDetail;
import com.shotang.shotang.shotang.groupdeal.model.CheckoutResponse;
import com.shotang.shotang.shotang.groupdeal.model.GroupDealResponse;
import com.shotang.shotang.shotang.groupdeal.network.IGroupDealService;
import com.shotang.shotang.shotang.misc.APIUrl;
import com.shotang.shotang.shotang.reverse.BulkDealProductDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shotang.shotang.shotang.analytics.EventSender.CARD_PAYMENT_METHOD;
import static com.shotang.shotang.shotang.analytics.EventSender.CART_CATEGORY;
import static com.shotang.shotang.shotang.analytics.EventSender.GROUP_DEAL;
import static com.shotang.shotang.shotang.analytics.EventSender.GROUP_DEAL_CONFIRM_ORDER_CLICKED;

/**
 * Created by ajay on 21/8/17.
 */

public class GroupDealCartConfirmOrderFragment extends Fragment{

    private static final String TOTAL_AMOUNT ="TOTAL_AMOUNT" ;
    private static final String TAG = "GroupDealCartConfirmOrd";
    private TextView confirmOrderTV, cartValueTV, cartIconTV,totalTV;
    private  PaymentTypeDetail.PaymentType  paymentType;
    private Snackbar snackbar;
    private String productId,sellerProductOptionId;
    float totalAMount;
    private String walletAmount ="0";
    private String paymentCode;
    private  List<GroupDealResponse.Variant>variantList;
    private  int dealId;
    private  GroupDealResponse groupDealResponse;
    private ProgressBar progressBar;
    private  int  cartAmount;

    public void getType(PaymentTypeDetail.PaymentType type,String paymentCode){
        this.paymentType=type;
      //  this.paymentCode=paymentCode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       final View rootView = inflater.inflate(R.layout.groupdeal_fragment_cart_footer, container, false);
//         totalAMount=getArguments().getInt("cartAmount");
         totalAMount=getArguments().getFloat(TOTAL_AMOUNT);
         variantList=getArguments().getParcelableArrayList("productVariant");
         dealId=getArguments().getInt("dealId");
         productId=getArguments().getString("productId");
        sellerProductOptionId=getArguments().getString("sellerProductOptionId");
        //groupDealResponse=getArguments().getParcelable("groupDealResponse");
        progressBar =(ProgressBar) rootView.findViewById(R.id.footer_loading_progressbar);
        Typeface iconFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        cartIconTV = (TextView) rootView.findViewById(R.id.footer_icon_tv);
        cartIconTV.setTypeface(iconFont);
        confirmOrderTV=(TextView)rootView.findViewById(R.id.footer_viewcart_confirm);
        totalTV=(TextView)rootView.findViewById(R.id.footer_total_tv);


        totalTV.setText(getString(R.string.rupees_symbol)+ totalAMount);

            confirmOrderTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (paymentType == null) {
                        Snackbar snackbar;
                        snackbar = Snackbar.make(v, "Please select a payment method", Snackbar.LENGTH_LONG);

                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tabHilightColor));
                        snackbar.show();
                    }
                    else if(totalAMount < 500) {
                        snackbar = Snackbar.make(v, "Add more items to confirm order", Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tabHilightColor));
                        snackbar.show();
                    }
                    else {
                        String paymentTypeGet = "";
                        if (paymentType == PaymentTypeDetail.PaymentType.cash) {
                            paymentTypeGet = "cash";
                            paymentCode = "cod";
                            //EventSender.sendCleverTapEvent(CART_CATEGORY, COD_PAYMENT_METHOD);
                        } else if (paymentType == PaymentTypeDetail.PaymentType.credit) {
                            paymentTypeGet = "credit";
                            paymentCode = "credit";
                            // EventSender.sendCleverTapEvent(CART_CATEGORY, CREDIT_PAYMENT_METHOD);
                        } else if (paymentType == PaymentTypeDetail.PaymentType.card) {
                            paymentTypeGet = "card";
                            paymentCode = "card";
                            EventSender.sendGAEvent(CART_CATEGORY, CARD_PAYMENT_METHOD);
                        }
                        confirmOrder(paymentTypeGet, paymentCode);
                    }


                }
            });





        return rootView;
    }

    public void walletAmount(float  walletAmount){
        this.walletAmount= String.valueOf(walletAmount);
    }

    private void confirmOrder(String paymentTypeGet,String paymentCode) {
        progressBar.setVisibility(View.VISIBLE);

        EventSender.sendGAEvent(GROUP_DEAL,GROUP_DEAL_CONFIRM_ORDER_CLICKED);

        List<CheckoutResponse.CheckoutData> checkoutDataList=new ArrayList<>();
        List<CheckoutResponse.CheckoutProduct> checkoutProductList=new ArrayList<>();

        CheckoutResponse checkoutResponse1=new CheckoutResponse();
        CheckoutResponse.CheckoutData checkoutData=new CheckoutResponse.CheckoutData();
        CheckoutResponse.CheckoutProduct checkoutProduct=new CheckoutResponse.CheckoutProduct();



        checkoutResponse1.setPaymentMethod(paymentTypeGet);
        checkoutResponse1.setPaymentCode(paymentCode);
        checkoutResponse1.setWalletAmount(walletAmount);

        checkoutData.setDealId(dealId);

        checkoutProduct.setProductId(productId);
        checkoutProduct.setSellerProductId(sellerProductOptionId);


        //Add variant in products
        checkoutProduct.setVariants(variantList);

        checkoutProductList.add(checkoutProduct);

        checkoutData.setProducts(checkoutProductList);
        checkoutDataList.add(checkoutData);

        checkoutResponse1.setData(checkoutDataList);
        //  checkoutResponse.setData(checkoutDataList);

        IGroupDealService iRetrofitService= RetrofitApiClient.changeApiBaseUrl(APIUrl.baseApi).build().create(IGroupDealService.class);
        Call<CheckoutResponse> checkoutResponseCall=iRetrofitService.postCheckoutData(checkoutResponse1);
        checkoutResponseCall.enqueue(new Callback<CheckoutResponse>() {
            @Override
            public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
               if (response.body()!=null ){

                if (response.body().getResponse().getId()==1){
                    Log.d(TAG, "response message:" +response.body().getResponse().getMessage() );
                    Toast.makeText(getActivity(),response.body().getResponse().getMessage() ,Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    totalTV.setText(getString(R.string.rupees_symbol)+ " 0");
                    Intent intent =new Intent(getActivity(),GroupDealMainActivity.class);
                    startActivity(intent);

                }else{
                    Log.d(TAG, "response message:" +response.body().getResponse().getMessage() );
                    Toast.makeText(getActivity(),response.body().getResponse().getMessage() ,Toast.LENGTH_LONG).show();
                    Intent intent =new Intent(getActivity(),BulkDealProductDetailsActivity.class);
                    intent.putExtra("dealNumber",dealId);
                    startActivity(intent);
                }

               }else{
                   Toast.makeText(getActivity(),"Some error ! try again" ,Toast.LENGTH_LONG).show();
                   progressBar.setVisibility(View.GONE);
               }
            }

            @Override
            public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                t.getStackTrace();
                progressBar.setVisibility(View.GONE);

            }
        });

    }


}
