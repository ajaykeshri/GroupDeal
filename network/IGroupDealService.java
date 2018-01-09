package com.shotang.shotang.shotang.groupdeal.network;

import com.shotang.shotang.shotang.cart.models.CartResponse;
import com.shotang.shotang.shotang.cart.models.CartSuccessResponse;
import com.shotang.shotang.shotang.groupdeal.model.CheckoutResponse;
import com.shotang.shotang.shotang.groupdeal.model.GroupDealResponse;
import com.shotang.shotang.shotang.groupdeal.model.PaymentMethodResponse;
import com.shotang.shotang.shotang.model.GeneralResponse;
import com.shotang.shotang.shotang.model.share.ShareResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ajay on 7/8/17.
 */

public interface IGroupDealService {



        @GET("offers/v2/group_deals")
        Call<GroupDealResponse> getDetailsGroupDeal();
        @GET("offers/v2/group_deals")
        Call<GroupDealResponse> getDetailsGroupDeal(@Query("deal_id") int dealId);

        @POST("checkout/group_deal")
        Call<CheckoutResponse> postCheckoutData(@Body CheckoutResponse checkoutResponse);

        @POST("checkout/group_deal/validate")
        Call<CheckoutResponse> validationCheckout(@Body CheckoutResponse checkoutResponse);

        @GET("checkout/v2/payment_method&total_amount=10&checkout_type=group_deal")
        Call<CartResponse> getCartDetails();

        @GET("checkout/v2/payment_method")
        Call<PaymentMethodResponse> getCartDetails1(@Query("total_amount") String total_amount,
                                                    @Query("checkout_type")String checkout_type);

        @GET("checkout/shareables/liveGDShare")
        Call<ShareResponse> getGDShareMessage(@Query("type") String type,
                                              @Query("group_deal_id")int groupdealId);


}
