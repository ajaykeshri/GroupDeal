package com.shotang.shotang.shotang.groupdeal;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.shotang.shotang.shotang.R;
import com.shotang.shotang.shotang.RetrofitApiClient;
import com.shotang.shotang.shotang.analytics.EventSender;
import com.shotang.shotang.shotang.cart.models.PaymentTypeDetail;
import com.shotang.shotang.shotang.catalog.BaseActivity;
import com.shotang.shotang.shotang.groupdeal.model.GroupDealResponse;
import com.shotang.shotang.shotang.groupdeal.model.PaymentMethodResponse;
import com.shotang.shotang.shotang.groupdeal.network.IGroupDealService;
import com.shotang.shotang.shotang.misc.APIUrl;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shotang.shotang.shotang.analytics.EventSender.GROUP_DEAL_PAYMENT;


/**
 * Created by ajay on 21/8/17.
 */

public class PaymentMethodActivity extends BaseActivity {

    private static final String TAG = PaymentMethodActivity.class.getSimpleName();
    PaymentMethodResponse cartResponse;
    private static final String TOTAL_AMOUNT ="TOTAL_AMOUNT" ;

    RadioGroup paymentRadioGroup;
    RadioButton cashRadioButton;
    CheckBox walletCheck;
    LinearLayout lineLAyoutPaymengtMethod,progressbarContainer;
    RadioButton cardOnDeliveryRadioButton;
    RadioButton onlinePaymentRadioButton;
    RadioButton creditRadioButton;
    private PaymentTypeDetail.PaymentType paymentType;
    private List<PaymentTypeDetail> paymentTypesList;
    private GroupDealCartConfirmOrderFragment groupDealCartConfirmOrderFragment;
    float totalAmount;
    int totalQuantity;
    List<GroupDealResponse.Variant> variantList;
    private String paymentCode;
    private  int dealId;
    private String productId,sellerProductOptionId;
    private int cartAmount;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method_activity);

        EventSender.sendGAScreenEvent(GROUP_DEAL_PAYMENT);



        lineLAyoutPaymengtMethod = (LinearLayout) findViewById(R.id.lineLAyoutPaymengtMethod);
        lineLAyoutPaymengtMethod.setVisibility(View.GONE);



        Bundle bundle=getIntent().getExtras();
         cartAmount =bundle.getInt("cartAmount");
         productId=bundle.getString("productId");
         totalAmount = bundle.getFloat("totalPrice");
         totalQuantity = bundle.getInt("totalQuantity");
         variantList=bundle.getParcelableArrayList("variantList");
         dealId=bundle.getInt("dealId");
        sellerProductOptionId=bundle.getString("sellerProductOptionId");

        progressbarContainer=(LinearLayout)findViewById(R.id.progressbar_container);
        progressbarContainer.setVisibility(View.VISIBLE);
         initToolbar();
         getCartDetails();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base_activity, menu);
        MenuItem profile = menu.findItem(R.id.action_person_profile);
        profile.setVisible(false);
        return true;

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCartDetails() {

        Log.d(TAG,"totalQuantity  place order clicked  "+ totalQuantity);
        Log.d(TAG,"totalQuantity totalPrice  place order clicked  "+ totalAmount);
        String checkoutType = "group_deal";

        IGroupDealService iGroupDealService = RetrofitApiClient.changeApiBaseUrl(APIUrl.baseApi).build().create(IGroupDealService.class);
        Call<PaymentMethodResponse> cartResponseCall = iGroupDealService.getCartDetails1(String.valueOf(totalAmount), checkoutType);
        cartResponseCall.enqueue(new Callback<PaymentMethodResponse>() {
            @Override
            public void onResponse(Call<PaymentMethodResponse> call, Response<PaymentMethodResponse> response) {
                progressbarContainer.setVisibility(View.GONE);
                cartResponse = response.body();
                showUI(cartResponse);

            }

            @Override
            public void onFailure(Call<PaymentMethodResponse> call, Throwable t) {
                progressbarContainer.setVisibility(View.GONE);
            }
        });

    }

    private void showUI(final PaymentMethodResponse cartResponse) {

        groupDealCartConfirmOrderFragment=new GroupDealCartConfirmOrderFragment();

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putInt("cartAmount",cartAmount);
        bundle.putFloat(TOTAL_AMOUNT, totalAmount);
        bundle.putParcelableArrayList("productVariant", (ArrayList<? extends Parcelable>) variantList);
        bundle.putInt("dealId",dealId);
        bundle.putString("productId",productId);
        bundle.putString("sellerProductOptionId",sellerProductOptionId);
        // bundle.putParcelable("groupDealResponse",groupDealResponse);
        groupDealCartConfirmOrderFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.cartActivityCartFL,groupDealCartConfirmOrderFragment).commitNowAllowingStateLoss();

        paymentRadioGroup = (RadioGroup) findViewById(R.id.paymentTypeRadio);
        cashRadioButton = (RadioButton) findViewById(R.id.paymentTypeCashRadio);
        walletCheck = (CheckBox) findViewById(R.id.checkBoxWallet);
        cardOnDeliveryRadioButton = (RadioButton) findViewById(R.id.paymentCardRadio);
        creditRadioButton = (RadioButton) findViewById(R.id.paymentTypeCreditRadio);
       // onlinePaymentRadioButton = (RadioButton) findViewById(R.id.paymentTypeOnline);

        paymentTypesList = cartResponse.getCart().getPaymentMethods();
        if (paymentTypesList==null && paymentTypesList.size()==0){
            lineLAyoutPaymengtMethod.setVisibility(View.GONE);
        }else {
            lineLAyoutPaymengtMethod.setVisibility(View.VISIBLE);
        }
        for (PaymentTypeDetail detail : paymentTypesList) {
            switch (detail.type) {
                case cash:
                    cashRadioButton.setVisibility(detail.isVisible?View.VISIBLE:View.GONE);
                    cashRadioButton.setTag(detail);
                    cashRadioButton.setText(detail.getText());
                    cashRadioButton.setEnabled(detail.isAvailable);
                    break;
                case credit:
                    creditRadioButton.setText(detail.getText());
                    creditRadioButton.setVisibility(detail.isVisible?View.VISIBLE:View.GONE);
                    creditRadioButton.setTag(detail);
                    creditRadioButton.setEnabled(detail.isAvailable);
                    break;
                case card:
                    cardOnDeliveryRadioButton.setText(detail.getText());
                    cardOnDeliveryRadioButton.setVisibility(detail.isVisible?View.VISIBLE:View.GONE);
                    cardOnDeliveryRadioButton.setTag(detail);
                    cardOnDeliveryRadioButton.setEnabled(detail.isAvailable);
                    break;
              /*  case pg:
                    onlinePaymentRadioButton.setText(detail.getText());
                    onlinePaymentRadioButton.setVisibility(detail.isVisible?View.VISIBLE:View.GONE);
                    onlinePaymentRadioButton.setTag(detail);
                    onlinePaymentRadioButton.setEnabled(detail.isAvailable);
                    break;
*/
            }


        }

        paymentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                RadioButton button = (RadioButton)group.findViewById(checkedId);

               if (button.isChecked()){
                   if (cashRadioButton.isChecked()){
                       paymentType= PaymentTypeDetail.PaymentType.cash;
                       paymentCode="cod";
                       //groupDealCartConfirmOrderFragment.getBestPriceText(paymentType,"cod");
                   }else if (creditRadioButton.isChecked()){
                       paymentType= PaymentTypeDetail.PaymentType.credit;
                       paymentCode="credit";
                   }
                   else if (cardOnDeliveryRadioButton.isChecked()){
                       paymentType=PaymentTypeDetail.PaymentType.card;
                       paymentCode="card";
                   }
                   else if (onlinePaymentRadioButton.isChecked()){
                       paymentType=PaymentTypeDetail.PaymentType.pg;
                       paymentCode="netbanking";
                   }
               }
                groupDealCartConfirmOrderFragment.getType(paymentType,paymentCode);

                if(cartResponse.getCart().getWallet()!=null) {
                    if (cartResponse.getCart().getWallet().getAmount() > 0) {
                        walletCheck.setVisibility(View.VISIBLE);
                        walletCheck.setText(cartResponse.getCart().getWallet().getRedeemText());
                    }
                    else
                    {
                        walletCheck.setVisibility(View.GONE);
                    }



                    if(walletCheck.getVisibility() == View.VISIBLE && walletCheck.isChecked())
                    {
                        groupDealCartConfirmOrderFragment.walletAmount(cartResponse.getCart().getWallet().getAmount());
                    }
                }


            }
        });

        walletCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  float walletAmount=cartResponse.getCart().getWallet().getAmount();

                int count = paymentRadioGroup.getChildCount();
                PaymentTypeDetail detail = null,selectedDetail = null;
                for (int i=0;i<count;i++) {
                    RadioButton button = (RadioButton)paymentRadioGroup.getChildAt(i);
                    detail = (PaymentTypeDetail) button.getTag();

                    if (button.isChecked()) {

                        if(walletCheck.isChecked() ) {


                                button.setText(detail.getText() + "(" + (totalAmount - walletAmount) + ")");

                        }
                        else
                        {
                            button.setText(detail.getText());
                        }
                    }

                }

                if(walletCheck.isChecked())
                {
                    groupDealCartConfirmOrderFragment.walletAmount(cartResponse.getCart().getWallet().getAmount());
                }
                else
                {
                    groupDealCartConfirmOrderFragment.walletAmount(0.0f);
                }


            }
        });





    }


}
