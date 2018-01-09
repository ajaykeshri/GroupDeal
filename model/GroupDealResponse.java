package com.shotang.shotang.shotang.groupdeal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ajay on 3/8/17.
 */

public class GroupDealResponse implements Parcelable {

    public enum GroupDealStatus
    {
        LIVE,
        UPCOMING,
        CLOSED
    }

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("data")
    @Expose
    private List<GroupDealData> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    protected GroupDealResponse(Parcel in) {
        status = in.readInt();
        message = in.readString();
    }

    public static final Creator<GroupDealResponse> CREATOR = new Creator<GroupDealResponse>() {
        @Override
        public GroupDealResponse createFromParcel(Parcel in) {
            return new GroupDealResponse(in);
        }

        @Override
        public GroupDealResponse[] newArray(int size) {
            return new GroupDealResponse[size];
        }
    };

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<GroupDealData> getData() {
        return data;
    }

    public void setData(List<GroupDealData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeString(message);
    }

    public static class GroupDealData implements Parcelable  {

        @SerializedName("deal_id")
        @Expose
        private int dealId;
        @SerializedName("status")
        @Expose
        private GroupDealResponse.GroupDealStatus status;
        @SerializedName("target_quantity")
        @Expose
        private String targetQuantity;
        @SerializedName("achieved_quantity")
        @Expose
        private String  achievedQuantity;
        @SerializedName("start_date")
        @Expose
        private String startDate;
        @SerializedName("end_date")
        @Expose
        private String endDate;
        @SerializedName("server_time")
        @Expose
        private String serverDate;
        @SerializedName("seller_id")
        @Expose
        private String sellerId;
        @SerializedName("sku_type")
        @Expose
        private String skuType;
        @SerializedName("slots")
        @Expose
        private List<Slot> slots = null;
        @SerializedName("allocated_quantity")
        @Expose
        private int allocatedQuantity;
        @SerializedName("ordered_quantity")
        @Expose
        private int orderedQuantity;
        @SerializedName("available_quantity")
        @Expose
        private int availableQuantity;
        @SerializedName("deal_completion")
        @Expose
        private int dealCompletion;
        @SerializedName("products")
        @Expose
        private List<Product> products = null;

        @SerializedName("nextSlotUnitMessage")
        @Expose
        private String nextSlotUnitMessage;
        @SerializedName("nextSlotCashBackMessage")
        @Expose
        private String nextSlotCashBackMessage;
        @SerializedName("tnc_link")
        @Expose
        private String tncLink;


        protected GroupDealData(Parcel in) {
            dealId = in.readInt();
            status = (GroupDealStatus) in.readValue(GroupDealStatus.class.getClassLoader());
            targetQuantity = in.readString();
            achievedQuantity = in.readString();
            startDate = in.readString();
            endDate = in.readString();
            serverDate = in.readString();
            sellerId = in.readString();
            skuType = in.readString();
            allocatedQuantity = in.readInt();
            orderedQuantity = in.readInt();
            availableQuantity = in.readInt();
            dealCompletion = in.readInt();
            nextSlotUnitMessage = in.readString();
            nextSlotCashBackMessage = in.readString();
        }

        public static final Creator<GroupDealData> CREATOR = new Creator<GroupDealData>() {
            @Override
            public GroupDealData createFromParcel(Parcel in) {
                return new GroupDealData(in);
            }

            @Override
            public GroupDealData[] newArray(int size) {
                return new GroupDealData[size];
            }
        };

        public String getTncLink() {
            return tncLink;
        }

        public void setTncLink(String tncLink) {
            this.tncLink = tncLink;
        }

        public String getNextSlotUnitMessage() {
            return nextSlotUnitMessage;
        }

        public void setNextSlotUnitMessage(String nextSlotUnitMessage) {
            this.nextSlotUnitMessage = nextSlotUnitMessage;
        }

        public String getNextSlotCashBackMessage() {
            return nextSlotCashBackMessage;
        }

        public void setNextSlotCashBackMessage(String nextSlotCashBackMessage) {
            this.nextSlotCashBackMessage = nextSlotCashBackMessage;
        }



        public int getDealId() {
            return dealId;
        }

        public void setDealId(int dealId) {
            this.dealId = dealId;
        }

        public GroupDealStatus getStatus() {
            return status;
        }

        public void setStatus(GroupDealStatus status) {
            this.status = status;
        }

        public String getTargetQuantity() {
            return targetQuantity;
        }

        public void setTargetQuantity(String targetQuantity) {
            this.targetQuantity = targetQuantity;
        }

        public String getAchievedQuantity() {
            return achievedQuantity;
        }

        public void setAchievedQuantity(String achievedQuantity) {
            this.achievedQuantity = achievedQuantity;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getSkuType() {
            return skuType;
        }

        public void setSkuType(String skuType) {
            this.skuType = skuType;
        }

        public List<Slot> getSlots() {
            return slots;
        }

        public void setSlots(List<Slot> slots) {
            this.slots = slots;
        }

        public int getAllocatedQuantity() {
            return allocatedQuantity;
        }

        public void setAllocatedQuantity(int allocatedQuantity) {
            this.allocatedQuantity = allocatedQuantity;
        }

        public int getOrderedQuantity() {
            return orderedQuantity;
        }

        public void setOrderedQuantity(int orderedQuantity) {
            this.orderedQuantity = orderedQuantity;
        }

        public int getAvailableQuantity() {
            return availableQuantity;
        }

        public void setAvailableQuantity(int availableQuantity) {
            this.availableQuantity = availableQuantity;
        }

        public int getDealCompletion() {
            return dealCompletion;
        }

        public void setDealCompletion(int dealCompletion) {
            this.dealCompletion = dealCompletion;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        public String getServerDate() {
            return serverDate;
        }

        public void setServerDate(String serverDate) {
            this.serverDate = serverDate;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(dealId);
            dest.writeValue(status);
            dest.writeString(targetQuantity);
            dest.writeString(achievedQuantity);
            dest.writeString(startDate);
            dest.writeString(endDate);
            dest.writeString(serverDate);
            dest.writeString(sellerId);
            dest.writeString(skuType);
            dest.writeInt(allocatedQuantity);
            dest.writeInt(orderedQuantity);
            dest.writeInt(availableQuantity);
            dest.writeInt(dealCompletion);
            dest.writeString(nextSlotUnitMessage);
            dest.writeString(nextSlotCashBackMessage);

        }
    }

    public static class Product {

        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("seller_product_id")
        @Expose
        private String sellerProductId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("brand_name")
        @Expose
        private String brandName;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("variants")
        @Expose
        private List<Variant> variants = null;

        @SerializedName("userQty")
        @Expose
        private int userQty;
        @SerializedName("userQtyMsg")
        @Expose
        private String userQtyMsg;

        public int getUserQty() {
            return userQty;
        }

        public void setUserQty(int userQty) {
            this.userQty = userQty;
        }

        public String getUserQtyMsg() {
            return userQtyMsg;
        }

        public void setUserQtyMsg(String userQtyMsg) {
            this.userQtyMsg = userQtyMsg;
        }


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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<Variant> getVariants() {
            return variants;
        }

        public void setVariants(List<Variant> variants) {
            this.variants = variants;
        }

    }
    public static class Slot {

        @SerializedName("min_qty")
        @Expose
        private int minQty;
        @SerializedName("price")
        @Expose
        private float price;
        @SerializedName("cashback")
        @Expose
        private float cashback;
        @SerializedName("isActive")
        @Expose
        private boolean isActive;

        @SerializedName("display_string")
        @Expose
        private String displayString;

        public int getMinQty() {
            return minQty;
        }

        public void setMinQty(int minQty) {
            this.minQty = minQty;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public float getCashback() {
            return cashback;
        }

        public void setCashback(float cashback) {
            this.cashback = cashback;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        public String getDisplayString() {
            return displayString;
        }

        public void setDisplayString(String displayString) {
            this.displayString = displayString;
        }
    }
    public static class Variant implements  Parcelable{

        @SerializedName("product_option_id")
        @Expose
        private String productOptionId;
        @SerializedName("seller_product_option_value_id")
        @Expose
        private String sellerProductOptionValueId;
        @SerializedName("allocated_quantity")
        @Expose
        private int allocatedQuantity;
        @SerializedName("ordered_quantity")
        @Expose
        private int orderedQuantity;
        @SerializedName("available_quantity")
        @Expose
        private int availableQuantity;
        @SerializedName("description")
        @Expose
        private String description;

        @SerializedName("quantity")
        //@Expose (deserialize = false,serialize = false)
        @Expose
        private int quantity;

        public String getQtyLeftMessage() {
            return qtyLeftMessage;
        }

        public void setQtyLeftMessage(String qtyLeftMessage) {
            this.qtyLeftMessage = qtyLeftMessage;
        }

        @SerializedName("qtyLeftMessage")
        @Expose
        private String qtyLeftMessage;


        protected Variant(Parcel in) {
            productOptionId = in.readString();
            sellerProductOptionValueId = in.readString();
            allocatedQuantity = in.readInt();
            orderedQuantity = in.readInt();
            availableQuantity = in.readInt();
            description = in.readString();
            quantity = in.readInt();
            qtyLeftMessage=in.readString();
        }

        public static final Creator<Variant> CREATOR = new Creator<Variant>() {
            @Override
            public Variant createFromParcel(Parcel in) {
                return new Variant(in);
            }

            @Override
            public Variant[] newArray(int size) {
                return new Variant[size];
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

        public int getAllocatedQuantity() {
            return allocatedQuantity;
        }

        public void setAllocatedQuantity(int allocatedQuantity) {
            this.allocatedQuantity = allocatedQuantity;
        }

        public int getOrderedQuantity() {
            return orderedQuantity;
        }

        public void setOrderedQuantity(int orderedQuantity) {
            this.orderedQuantity = orderedQuantity;
        }

        public int getAvailableQuantity() {
            return availableQuantity;
        }

        public void setAvailableQuantity(int availableQuantity) {
            this.availableQuantity = availableQuantity;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
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
            dest.writeInt(allocatedQuantity);
            dest.writeInt(orderedQuantity);
            dest.writeInt(availableQuantity);
            dest.writeString(description);
            dest.writeInt(quantity);
            dest.writeString(qtyLeftMessage);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Variant variant = (Variant) o;

            if (sellerProductOptionValueId != null ? !sellerProductOptionValueId.equals(variant.sellerProductOptionValueId) : variant.sellerProductOptionValueId != null)
                return false;

            return true;

        }

        @Override
        public int hashCode() {
            return sellerProductOptionValueId != null ? sellerProductOptionValueId.hashCode() : 0;
        }
    }
}
