package com.shotang.shotang.shotang.groupdeal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shotang.shotang.shotang.Response;

import java.util.List;

/**
 * Created by ajay on 10/8/17.
 */

public class CheckoutResponse implements Parcelable{

    @SerializedName("wallet_amount")
    @Expose
    private String walletAmount;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("payment_code")
    @Expose
    private String paymentCode;
    @SerializedName("data")
    @Expose
    private List<CheckoutData> data = null;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @SerializedName("response")
    @Expose
    private Response response;

    public CheckoutResponse() {

    }

    public CheckoutResponse(Parcel in) {
        walletAmount = in.readString();
        paymentMethod = in.readString();
        paymentCode = in.readString();
    }

    public static final Creator<CheckoutResponse> CREATOR = new Creator<CheckoutResponse>() {
        @Override
        public CheckoutResponse createFromParcel(Parcel in) {
            return new CheckoutResponse(in);
        }

        @Override
        public CheckoutResponse[] newArray(int size) {
            return new CheckoutResponse[size];
        }
    };

    public String getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(String walletAmount) {
        this.walletAmount = walletAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public List<CheckoutData> getData() {
        return data;
    }

    public void setData(List<CheckoutData> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(walletAmount);
        dest.writeString(paymentMethod);
        dest.writeString(paymentCode);
    }


    public static class CheckoutData {

        @SerializedName("deal_id")
        @Expose
        private int dealId;
        @SerializedName("products")
        @Expose
        private List<CheckoutProduct> products = null;

        @SerializedName("total_amount")
        @Expose
        private float totalAmount;

        public int getDealId() {
            return dealId;
        }

        public void setDealId(int dealId) {
            this.dealId = dealId;
        }

        public List<CheckoutProduct> getProducts() {
            return products;
        }

        public void setProducts(List<CheckoutProduct> products) {
            this.products = products;
        }
        public float getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(float totalAmount) {
            this.totalAmount = totalAmount;
        }

    }

    public static class CheckoutProduct {

        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("seller_product_id")
        @Expose
        private String sellerProductId;
        @SerializedName("variants")
        @Expose
        private List<GroupDealResponse.Variant> variants = null;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getSellerProductId() {
            return sellerProductId;
        }

        public void setSellerProductId(String sellerProductId) {
            this.sellerProductId = sellerProductId;
        }

        public List<GroupDealResponse.Variant> getVariants() {
            return variants;
        }

        public void setVariants(List<GroupDealResponse.Variant> variants) {
            this.variants = variants;
        }

    }

    public static class CheckoutVariant  implements Parcelable{

        @SerializedName("product_option_id")
        @Expose
        private String productOptionId;
        @SerializedName("seller_product_option_value_id")
        @Expose
        private String sellerProductOptionValueId;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        public CheckoutVariant() {}
        public CheckoutVariant(Parcel in) {
            productOptionId = in.readString();
            sellerProductOptionValueId = in.readString();
            quantity = in.readString();
        }

        public static final Creator<CheckoutVariant> CREATOR = new Creator<CheckoutVariant>() {
            @Override
            public CheckoutVariant createFromParcel(Parcel in) {
                return new CheckoutVariant(in);
            }

            @Override
            public CheckoutVariant[] newArray(int size) {
                return new CheckoutVariant[size];
            }
        };

        public String getProductOptionId() {
            return productOptionId;
        }

        public void setProductOptionId(String productOptionId) {
            this.productOptionId = productOptionId;
        }

        public String getSellerProductOptionValueId() {
            return sellerProductOptionValueId;
        }

        public void setSellerProductOptionValueId(String sellerProductOptionValueId) {
            this.sellerProductOptionValueId = sellerProductOptionValueId;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(productOptionId);
            dest.writeString(sellerProductOptionValueId);
            dest.writeString(quantity);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CheckoutVariant that = (CheckoutVariant) o;

            return productOptionId != null ? productOptionId.equals(that.productOptionId) : that.productOptionId == null;

        }

        @Override
        public int hashCode() {
            return productOptionId != null ? productOptionId.hashCode() : 0;
        }
    }
}
