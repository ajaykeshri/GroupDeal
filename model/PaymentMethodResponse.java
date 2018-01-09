package com.shotang.shotang.shotang.groupdeal.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shotang.shotang.shotang.cart.models.PaymentTypeDetail;

import java.util.List;

/**
 * Created by ajay on 21/8/17.
 */

public class PaymentMethodResponse {

    @SerializedName("response")
    @Expose
    private Response response;
    @SerializedName("cart")
    @Expose
    private Cart cart;

    @Override
    public String toString() {
        return "PaymentMethodResponse{" +
                "response=" + response +
                ", cart=" + cart +
                '}';
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public static class Cart {
        @Override
        public String toString() {
            return "Cart{" +
                    "creditDetails=" + creditDetails +
                    ", wallet=" + wallet +
                    ", hasCard=" + hasCard +
                    ", paymentMethods=" + paymentMethods +
                    '}';
        }

        @SerializedName("credit_details")
        @Expose
        private CreditDetails creditDetails;
        @SerializedName("wallet")
        @Expose
        private Wallet wallet;
        @SerializedName("has_card")
        @Expose
        private Boolean hasCard;
        @SerializedName("payment_methods")
        @Expose
        private List<PaymentTypeDetail> paymentMethods = null;

        public CreditDetails getCreditDetails() {
            return creditDetails;
        }

        public void setCreditDetails(CreditDetails creditDetails) {
            this.creditDetails = creditDetails;
        }

        public Wallet getWallet() {
            return wallet;
        }

        public void setWallet(Wallet wallet) {
            this.wallet = wallet;
        }

        public Boolean getHasCard() {
            return hasCard;
        }

        public void setHasCard(Boolean hasCard) {
            this.hasCard = hasCard;
        }

        public List<PaymentTypeDetail> getPaymentMethods() {
            return paymentMethods;
        }

        public void setPaymentMethods(List<PaymentTypeDetail> paymentMethods) {
            this.paymentMethods = paymentMethods;
        }

    }

    public  static  class PaymentMethod {
        @Override
        public String toString() {
            return "PaymentMethod{" +
                    "type='" + type + '\'' +
                    ", text='" + text + '\'' +
                    ", isAvailable=" + isAvailable +
                    ", isVisible=" + isVisible +
                    ", walletAmount=" + walletAmount +
                    '}';
        }

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("is_available")
        @Expose
        private Boolean isAvailable;
        @SerializedName("is_visible")
        @Expose
        private Boolean isVisible;
        @SerializedName("wallet_amount")
        @Expose
        private int walletAmount;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Boolean getIsAvailable() {
            return isAvailable;
        }

        public void setIsAvailable(Boolean isAvailable) {
            this.isAvailable = isAvailable;
        }

        public Boolean getIsVisible() {
            return isVisible;
        }

        public void setIsVisible(Boolean isVisible) {
            this.isVisible = isVisible;
        }

        public int getWalletAmount() {
            return walletAmount;
        }

        public void setWalletAmount(int walletAmount) {
            this.walletAmount = walletAmount;
        }

    }

    public static class Response {
        @Override
        public String toString() {
            return "Response{" +
                    "id=" + id +
                    ", message='" + message + '\'' +
                    '}';
        }

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("message")
        @Expose
        private String message;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

    public  static  class Wallet {
        @Override
        public String toString() {
            return "Wallet{" +
                    "amount=" + amount +
                    ", redeemText='" + redeemText + '\'' +
                    ", payText='" + payText + '\'' +
                    '}';
        }

        @SerializedName("amount")
        @Expose
        private int amount;
        @SerializedName("redeem_text")
        @Expose
        private String redeemText;
        @SerializedName("pay_text")
        @Expose
        private String payText;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getRedeemText() {
            return redeemText;
        }

        public void setRedeemText(String redeemText) {
            this.redeemText = redeemText;
        }

        public String getPayText() {
            return payText;
        }

        public void setPayText(String payText) {
            this.payText = payText;
        }

    }

    public static class CreditDetails {
        @Override
        public String toString() {
            return "CreditDetails{" +
                    "text=" + text +
                    ", buttonVisible=" + buttonVisible +
                    '}';
        }

        @SerializedName("text")
        @Expose
        private Object text;
        @SerializedName("button_visible")
        @Expose
        private Boolean buttonVisible;

        public Object getText() {
            return text;
        }

        public void setText(Object text) {
            this.text = text;
        }

        public Boolean getButtonVisible() {
            return buttonVisible;
        }

        public void setButtonVisible(Boolean buttonVisible) {
            this.buttonVisible = buttonVisible;
        }

    }
}
