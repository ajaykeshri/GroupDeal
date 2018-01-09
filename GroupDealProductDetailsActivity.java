package com.shotang.shotang.shotang.groupdeal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shotang.shotang.shotang.Login;
import com.shotang.shotang.shotang.MyApplication;
import com.shotang.shotang.shotang.R;
import com.shotang.shotang.shotang.RetrofitApiClient;
import com.shotang.shotang.shotang.analytics.EventSender;
import com.shotang.shotang.shotang.catalog.BaseCatalogActivity;
import com.shotang.shotang.shotang.catalog.CustomImageButton;
import com.shotang.shotang.shotang.groupdeal.model.CheckoutResponse;
import com.shotang.shotang.shotang.groupdeal.model.GroupDealResponse;
import com.shotang.shotang.shotang.groupdeal.network.IGroupDealService;
import com.shotang.shotang.shotang.misc.APIUrl;
import com.shotang.shotang.shotang.misc.UtilsClass;
import com.shotang.shotang.shotang.model.share.ShareResponse;
import com.shotang.shotang.shotang.recyclerviewutils.DividerItemDecoration;
import com.shotang.shotang.shotang.uicomponents.CustomRecyclerView;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shotang.shotang.shotang.MyApplication.getContext;
import static com.shotang.shotang.shotang.analytics.EventSender.GROUP_DEAL;
import static com.shotang.shotang.shotang.analytics.EventSender.GROUP_DEAL_BUY_NOW_CLICK;
import static com.shotang.shotang.shotang.analytics.EventSender.GROUP_DEAL_CHECKEDOUT_CLICKED;
import static com.shotang.shotang.shotang.analytics.EventSender.GROUP_DEAL_DETAIL;
import static com.shotang.shotang.shotang.analytics.EventSender.GROUP_DEAL_SHARE_CLICKED;
import static com.shotang.shotang.shotang.misc.APIUrl.CART_REQUEST;

public class GroupDealProductDetailsActivity extends BaseCatalogActivity implements GroupDealVariantTypeAdapter.OnLsitenerVariantAdapter {

    public static final String TAG = "GroupDealProduct";

    public static int dealNumber=1;

    public int hasOffer = 0;


    private TextView modelNameTV, modelDescriptionTV,endTime, progessDeal,currentDealPrice,oldPrice,shareDealWithRetailers,placeOder;
    private TextProgressBar pb;
    private ImageView modelImageView;
    private RelativeLayout relativeLayoutTop;

    private ImageView plusActionIV, minusActionIV;
    private TextView cartQuantityTV, offerPriceTV, actualPriceTV, offTV;

    private LinearLayout priceLL;

    private LinearLayout stockAvailableLL;
    private Button buyNow;

    private TextView minQuantityMsgTV, minQuantityValueTV;

    private RecyclerView currentUnitPriceRecycle,cashbackRecycle,varanatTypeRecycle;
    private LinearLayout slabsContainer;
    private CustomRecyclerView slabsRecyclerView;
    private CustomImageButton watchListImage;
    private RecyclerView watchListOfferTextList;
    private  Toolbar toolbar;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<GroupDealResponse.Slot> slotList;
    private  CheckoutResponse checkoutResponse;
    private NestedScrollView scrollView;
    private  int height;
    private  GroupDealResponse groupDealResponse;
    private TextView unitBought,unitLeft,cashBack;
    private WebView termAndCondition;
    private LinearLayout progressBarContainer;
    private View viewDealDivider,view_nobackback;
    private int totalQuantity;
    private float perUnitPrice;
    private float totalPrice;
    private List<GroupDealResponse.Variant> variantList;
    private int dealId;
    private String productID,sellerProductOptionId,productName;
    private int  achievedQuantity=0;
    private int  targetQuantity=0;
    private LinearLayout alrealyBougtLinearlayout, unitleftLinearLayout,cashBackLinearLayout,linearLayout_cashback;


    public static Intent getLaunchIntent(Context context, String productId) {
        Intent intent = null;
        String newUri = null;
        try {
            URI oldUri = new URI(context.getString(R.string.schema_shotang)+"://"+context.getString(R.string.host)+"/"+context.getString(R.string.path_groupdeal_id));
            URI resolved = oldUri.resolve(productId);
            dealNumber= Integer.parseInt(productId);
            newUri = resolved.toString();
            intent = new Intent(context, GroupDealProductDetailsActivity.class);
            intent.setData(Uri.parse(newUri));

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupdeal_activity_product_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EventSender.sendGAScreenEvent(GROUP_DEAL_DETAIL);
        variantList=new ArrayList<>();
        modelNameTV = (TextView) findViewById(R.id.product_card_product_name_tv);
        modelDescriptionTV = (TextView) findViewById(R.id.product_card_product_desc_tv);
        modelImageView = (ImageView) findViewById(R.id.product_card_product_iv);
        relativeLayoutTop = (RelativeLayout) findViewById(R.id.relatiylayout_groupdeal);

        plusActionIV = (ImageView) findViewById(R.id.variant_plus_iv);
        minusActionIV = (ImageView) findViewById(R.id.variant_minus_iv);

        cartQuantityTV = (TextView) findViewById(R.id.variant_cart_quantity_tv);
        offerPriceTV = (TextView) findViewById(R.id.variant_offer_price_tv);
        actualPriceTV = (TextView) findViewById(R.id.variant_actual_price_tv);

        priceLL = (LinearLayout) findViewById(R.id.variant_price_ll);

        stockAvailableLL = (LinearLayout) findViewById(R.id.variant_qty_avbl_container);
        minQuantityMsgTV = (TextView) findViewById(R.id.variant_min_qty_msg_tv);
        minQuantityValueTV = (TextView) findViewById(R.id.variant_min_qty_value_tv);

        slabsContainer = (LinearLayout) findViewById(R.id.variant_slabs_container);

        slabsRecyclerView = (CustomRecyclerView) findViewById(R.id.variant_slabs_rv);
        endTime=(TextView)findViewById(R.id.text_endtime);
        progessDeal=(TextView)findViewById(R.id.textview_progress);
        pb=(TextProgressBar)findViewById(R.id.textProgressBar);
        currentDealPrice=(TextView)findViewById(R.id.current_deal_price);
        currentUnitPriceRecycle=(RecyclerView)findViewById(R.id.currentdeal_unitprice_rv);
        cashbackRecycle=(RecyclerView)findViewById(R.id.currentdea_cashback_rv) ;
        varanatTypeRecycle=(RecyclerView)findViewById(R.id.varianttype_rv);
        shareDealWithRetailers=(TextView)findViewById(R.id.textView_shareDeal) ;
        buyNow=(Button)findViewById(R.id.button_buynow);
        placeOder=(TextView)findViewById(R.id.placeorder);

        scrollView = (NestedScrollView) findViewById(R.id.scrollView_groupdeal);
        cashBack=(TextView)findViewById(R.id.tv_cashback__perunit);
        unitBought=(TextView)findViewById(R.id.tv_already_bought);
        unitLeft=(TextView)findViewById(R.id.tv_unit_left);
        progressBarContainer = (LinearLayout) findViewById(R.id.progressbar_container);
        viewDealDivider=findViewById(R.id.viewDeal_divider);
        oldPrice=(TextView)findViewById(R.id.tv_oldprice);
        alrealyBougtLinearlayout=(LinearLayout) findViewById(R.id.alrealy_bougt_linearlayout);
        unitleftLinearLayout=(LinearLayout)findViewById(R.id.linearLayout_unit_left);
        cashBackLinearLayout=(LinearLayout)findViewById(R.id.linearLayout_cashback);
        termAndCondition=(WebView)findViewById(R.id.tv_termscondition_groupdeal);
        linearLayout_cashback=(LinearLayout)findViewById(R.id.linearLayout_cashback_deal) ;
        view_nobackback=findViewById(R.id.view_nobackback);


       /* this code for divider between list of variants
        */
        varanatTypeRecycle.addItemDecoration(new DividerItemDecoration(
                ContextCompat.getDrawable(this, R.drawable.divider_10dp), LinearLayoutManager.VERTICAL));

       /*
         creating height of scroll view to show variant of product to add and remove
        */

        ViewTreeObserver vto = scrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    GroupDealProductDetailsActivity.this.scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    GroupDealProductDetailsActivity.this.scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width  = scrollView.getMeasuredWidth();
                height  = scrollView.getMeasuredHeight();
                Log.d(TAG, "HEIGHT  "  + height +"width  " + width);

            }
        });






        if (getIntent().getExtras()!=null){
            final Bundle intentBundle = getIntent().getExtras();
            dealNumber=Integer.parseInt(getIntent().getData().getPathSegments().get(1));
        }
       // dealNumber=intent.getInt("dealId");
        // fetch Group deal data

        progressBarContainer.setVisibility(View.VISIBLE);
        getShareData(dealNumber);
        getProductDetails(dealNumber);


        /* Populate the Cart fragment */
        if (findViewById(R.id.modelDetailsCartFL) != null) {
            if (savedInstanceState != null) {
                return;
            }


        }

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventSender.sendGAEvent(GROUP_DEAL,GROUP_DEAL_BUY_NOW_CLICK);
                EventSender.sendGAEvent(GROUP_DEAL,GROUP_DEAL_BUY_NOW_CLICK,productID);
                 scrollView.smoothScrollBy(0,height);

            }
        });



    }

    private void getShareData(int dealNumber) {
        IGroupDealService apiService = RetrofitApiClient.changeApiBaseUrl(APIUrl.baseApi).build().create(IGroupDealService.class);
        Call<ShareResponse> callGroupDealResponse=apiService.getGDShareMessage("LIVE_GD", dealNumber);
        callGroupDealResponse.enqueue(new Callback<ShareResponse>() {
            @Override
            public void onResponse(Call<ShareResponse> call, final Response<ShareResponse> response) {
                if(response.body()!=null) {
                    if (response.body().getStatus() == 1) {
                        shareDealWithRetailers.setVisibility(View.VISIBLE);
                        shareDealWithRetailers.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                shareGrouDeal(response.body().getData().getShareableMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ShareResponse> call, Throwable throwable) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_catalog2, menu);
        MenuItem watchList = menu.findItem(R.id.action_watchlist);
        MenuItem search = menu.findItem(R.id.action_search);
        MenuItem alarm = menu.findItem(R.id.action_alarm);
        MenuItem profile = menu.findItem(R.id.action_person_profile);
        MenuItem share=menu.findItem(R.id.action_share);
        MenuItem refresh=menu.findItem(R.id.action_refresh);
        watchList.setVisible(false);
        search.setVisible(false);
        alarm.setVisible(false);
        profile.setVisible(false);
        share.setVisible(false);
        refresh.setVisible(true);

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
               // getProductDetails(dealNumber);
//                shareGrouDeal(groupDealResponse.getData().get(0).getProducts().get(0).getName());
                return  true;
            case  R.id.action_refresh:
                finish();
                startActivity(getIntent());
                getProductDetails(dealNumber);

                return  true;

        }
        return super.onOptionsItemSelected(item);
    }
    private void shareGrouDeal(String shareGroudeal) {
        EventSender.sendGAEvent(GROUP_DEAL,GROUP_DEAL_SHARE_CLICKED);
        EventSender.sendGAEvent(GROUP_DEAL,GROUP_DEAL_SHARE_CLICKED,productID);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,shareGroudeal );
        sendIntent.setType("text/plain");
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(sendIntent, "Share"));
        } else {
            Toast.makeText(GroupDealProductDetailsActivity.this, "No apps found", Toast.LENGTH_LONG).show();
        }
    }

    private void setCashBack(GroupDealResponse groupDealResponse) {

        if (groupDealResponse.getData().get(0).getSlots().get(0).getCashback()==0){
            cashbackRecycle.setVisibility(View.GONE);
            linearLayout_cashback.setVisibility(View.GONE);
            view_nobackback.setVisibility(View.GONE);
            cashBackLinearLayout.setVisibility(View.GONE);
        }else{
            cashBackLinearLayout.setVisibility(View.VISIBLE);
            linearLayout_cashback.setVisibility(View.VISIBLE);
            cashbackRecycle.setVisibility(View.VISIBLE);
            view_nobackback.setVisibility(View.VISIBLE);

            cashbackRecycle.setHasFixedSize(true);
            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            cashbackRecycle.setLayoutManager(mLayoutManager);
            // specify an adapter (see also next example)
            mAdapter = new GroupDealCashBackAdapter(groupDealResponse.getData().get(0).getSlots());
            cashbackRecycle.setAdapter(mAdapter);

        }

    }

    private void setCurrentPrice(GroupDealResponse groupDealResponse) {
        currentUnitPriceRecycle.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        currentUnitPriceRecycle.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new CurrentDealAdapter(groupDealResponse.getData().get(0).getSlots());
        currentUnitPriceRecycle.setAdapter(mAdapter);
    }
    private void varanatTypeDeal(GroupDealResponse groupDealResponse) {
        varanatTypeRecycle.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        varanatTypeRecycle.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
      /*  mAdapter = new GroupDealVariantTypeAdapter(groupDealResponse.getData().get(0).getProducts().get(0).getVariants(),GroupDealProductDetailsActivity.this,perUnitPrice);
        varanatTypeRecycle.setAdapter(mAdapter);*/
        if (groupDealResponse.getData().get(0).getProducts().get(0).getVariants()!=null){
            mAdapter = new GroupDealVariantTypeAdapter(groupDealResponse.getData().get(0).getProducts().get(0).getVariants(),GroupDealProductDetailsActivity.this,perUnitPrice);
            varanatTypeRecycle.setAdapter(mAdapter);
        }
        else{
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == CART_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                getProductDetails(dealNumber);
            }
        }
    }

    private void showUI(final GroupDealResponse groupDealResponse) {
        relativeLayoutTop.setVisibility(View.VISIBLE
        );
        shareDealWithRetailers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareGrouDeal(groupDealResponse.getData().get(0).getProducts().get(0).getName());
            }
        });

        if (groupDealResponse.getData().size()>0 && groupDealResponse.getData().get(0).getTncLink() != null) {
            termAndCondition.getSettings().setJavaScriptEnabled(true);
            termAndCondition.loadUrl(groupDealResponse.getData().get(0).getTncLink());
            termAndCondition.setWebViewClient(new WebViewClient());

        }

        long currServerTimeMilli=
                UtilsClass.calculateTimeToMiliSecond(groupDealResponse.getData().get(0).getServerDate());
        long endDealMilli=UtilsClass.calculateTimeToMiliSecond(groupDealResponse.getData().get(0).getEndDate());
        long deatTime=endDealMilli-currServerTimeMilli;

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
                endTime.setText(yy);
            }

            @Override
            public void onFinish() {
                endTime.setText("Deal Close!");
            }
        }.start();

        /////////////////////////


        targetQuantity= groupDealResponse.getData().get(0).getAllocatedQuantity();
        achievedQuantity =groupDealResponse.getData().get(0).getOrderedQuantity();

        progessDeal.setText(achievedQuantity+" Sold "+"/ " +targetQuantity);
        pb.setProgress(achievedQuantity);

        pb.setMax(targetQuantity);
        progessDeal.setText(achievedQuantity+" Sold "+"/ " +targetQuantity);

        pb.setProgress(achievedQuantity);

        GroupDealResponse.Product product = groupDealResponse.getData().get(0).getProducts().get(0);
        productID=product.getProductId();
        productName=product.getName();
        dealId= groupDealResponse.getData().get(0).getDealId();
        sellerProductOptionId=groupDealResponse.getData().get(0).getSellerId();
        slotList= groupDealResponse.getData().get(0).getSlots();
        for (GroupDealResponse.Slot slot :slotList) {
            if (slot.getIsActive()) {

                if (slotList.get(0).getPrice() == slot.getPrice()){
                    currentDealPrice.setText("\u20B9" +slot.getPrice()+"");
                    oldPrice.setVisibility(View.GONE);
                    perUnitPrice= slot.getPrice();
                }else {
                    oldPrice.setVisibility(View.VISIBLE);
                    currentDealPrice.setText("\u20B9" +slot.getPrice());
                    oldPrice.setText("\u20B9" + slotList.get(0).getPrice());
                    oldPrice .setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    perUnitPrice= slot.getPrice();


                }


            }

        }


        modelNameTV.setText(product.getName());

        modelDescriptionTV.setText(product.getDescription());
        if (modelImageView != null) {
            Picasso.with(getContext())
                    .load(product.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(modelImageView);
        } else {
            modelImageView.setVisibility(View.GONE);
        }

        if(groupDealResponse.getData().get(0).getNextSlotCashBackMessage()!=null){
            cashBackLinearLayout.setVisibility(View.VISIBLE);
            cashBack.setText(groupDealResponse.getData().get(0).getNextSlotCashBackMessage());

        }else {
            cashBackLinearLayout.setVisibility(View.GONE);
        }
            if (groupDealResponse.getData().get(0).getNextSlotUnitMessage()!=null){
            unitleftLinearLayout.setVisibility(View.VISIBLE);
            unitLeft.setText(groupDealResponse.getData().get(0).getNextSlotUnitMessage());
        }else {
            unitleftLinearLayout.setVisibility(View.GONE);
        }

        if (product.getUserQtyMsg()!= null) {
            viewDealDivider.setVisibility(View.VISIBLE);
            alrealyBougtLinearlayout.setVisibility(View.VISIBLE);
          //  unitBought.setVisibility(View.VISIBLE);
            unitBought.setText(product.getUserQtyMsg());
        }else{
            alrealyBougtLinearlayout.setVisibility(View.GONE);
           // unitBought.setVisibility(View.GONE);
           // viewDealDivider.setVisibility(View.GONE);
        }



        setCurrentPrice(groupDealResponse);
        setCashBack(groupDealResponse);
        varanatTypeDeal(groupDealResponse);
        validationCheck(groupDealResponse);


    }

    private void validationCheck( GroupDealResponse groupDealResponse) {




        placeOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CheckoutResponse.CheckoutData> checkoutDataList=new ArrayList<>();
                List<CheckoutResponse.CheckoutProduct> checkoutProductList=new ArrayList<>();

                final CheckoutResponse checkoutResponse1=new CheckoutResponse();
                CheckoutResponse.CheckoutData checkoutData=new CheckoutResponse.CheckoutData();
                CheckoutResponse.CheckoutProduct checkoutProduct=new CheckoutResponse.CheckoutProduct();

                if (totalQuantity>0){
                    checkoutData.setDealId(dealId);
                    checkoutData.setTotalAmount(totalPrice);
                    checkoutProduct.setProductId(productID);
                    checkoutProduct.setSellerProductId(sellerProductOptionId);


                    //Add variant in products
                    checkoutProduct.setVariants(variantList);

                    checkoutProductList.add(checkoutProduct);

                    checkoutData.setProducts(checkoutProductList);
                    checkoutDataList.add(checkoutData);

                    checkoutResponse1.setData(checkoutDataList);
                    IGroupDealService iRetrofitService= RetrofitApiClient.changeApiBaseUrl(APIUrl.baseApi).build().create(IGroupDealService.class);
                    Call<CheckoutResponse> checkoutResponseCall=iRetrofitService.validationCheckout(checkoutResponse1);
                    checkoutResponseCall.enqueue(new Callback<CheckoutResponse>() {
                        @Override
                        public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
                            if (response.body()!=null ){

                                if (response.body().getResponse().getId()==1){
                                    Log.d(TAG, "response message:" +response.body().getResponse().getMessage() );
                                    //Toast.makeText(GroupDealProductDetailsActivity.this,response.body().getResponse().getMessage() ,Toast.LENGTH_LONG).show();
                                    EventSender.sendGAEvent(GROUP_DEAL,GROUP_DEAL_CHECKEDOUT_CLICKED);
                                    Intent intentPaymentmethod=new Intent(GroupDealProductDetailsActivity.this,PaymentMethodActivity.class);
                                    intentPaymentmethod.putExtra("productId",productID);
                                    Log.d(TAG,"totalQuantity place order clicked  "+ totalQuantity);
                                    Log.d(TAG,"totalQuantity totalPrice  place order clicked  "+ perUnitPrice);
                                    intentPaymentmethod.putExtra("totalQuantity",totalQuantity);
                                    intentPaymentmethod.putExtra("totalPrice",totalPrice);
                                    intentPaymentmethod.putParcelableArrayListExtra("variantList", (ArrayList<? extends Parcelable>) variantList);
                                    intentPaymentmethod.putExtra("dealId",dealNumber);
                                    intentPaymentmethod.putExtra("sellerProductOptionId",sellerProductOptionId);
                                    startActivity(intentPaymentmethod);////


                                }else{
                                    Log.d(TAG, "Error :" +response.body().getResponse().getMessage() );
                                    Toast.makeText(GroupDealProductDetailsActivity.this,response.body().getResponse().getMessage() ,Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(getIntent());

                                }

                            }else{
                                Toast.makeText(GroupDealProductDetailsActivity.this,"Server Error  ! try again" ,Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                            t.getStackTrace();
                            Toast.makeText(GroupDealProductDetailsActivity.this,"Server Error  ! try again" ,Toast.LENGTH_LONG).show();

                        }
                    });



                }else {
                    Snackbar snackbar;
                    snackbar = Snackbar.make(v, "No item added in cart", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tabHilightColor));
                    snackbar.show();

                }

            }
        });


    }

    private void getProductDetails(final int productId) {

        IGroupDealService apiService = RetrofitApiClient.changeApiBaseUrl(APIUrl.baseApi).build().create(IGroupDealService.class);
        Call<GroupDealResponse> callGroupDealResponse=apiService.getDetailsGroupDeal(productId);
        callGroupDealResponse.enqueue(new Callback<GroupDealResponse>() {
            @Override
            public void onResponse(Call<GroupDealResponse> call, Response<GroupDealResponse> responseGroupDeal) {
                if (responseGroupDeal!=null && responseGroupDeal.body()!=null){
                    groupDealResponse=responseGroupDeal.body();
                    if(groupDealResponse.getData().size()>0)
                        if(groupDealResponse.getData().get(0).getStatus().equals(GroupDealResponse.GroupDealStatus.LIVE))
                            showUI(groupDealResponse);
                        else{
                            Toast.makeText(MyApplication.getContext(),"This Deal is closed currently.\n Please have a look at our live deals.",Toast.LENGTH_LONG).show();
                            startActivity(GroupDealMainActivity.getLaunchIntent(getBaseContext()));
                            finish();
                        }
                    else{
                        Toast.makeText(MyApplication.getContext(),"The deal you are trying to access doesn't exist.\n Please have a look at our live deals.",Toast.LENGTH_LONG).show();
                        startActivity(GroupDealMainActivity.getLaunchIntent(getBaseContext()));
                        finish();
                    }
                    progressBarContainer.setVisibility(View.GONE);

                }else if(responseGroupDeal.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
                    Toast.makeText(MyApplication.getContext(),"Please Login",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(GroupDealProductDetailsActivity.this,Login.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<GroupDealResponse> call, Throwable t) {
                t.getStackTrace();

                progressBarContainer.setVisibility(View.GONE);

            }
        });


    }


    @Override
    public void passQuantity(int totalQuantity) {
        this.totalQuantity=totalQuantity;
        Log.d(TAG,"totalQuantity passQuantity "+ totalQuantity);
    }

    @Override
    public void passTotalAmount(float totalAmount) {
        totalPrice=totalAmount;
    }

    @Override
    public void passVariant(GroupDealResponse.Variant variant) {
        if (variantList.contains(variant)) {
            variantList.remove(variant);
            variantList.add(variant);
        } else {
            variantList.add(variant);
        }

    }
}