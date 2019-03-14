package com.omninos.gwappprovider.modelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkListModel {
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }


    public class Detail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("providerStatus")
        @Expose
        private String providerStatus;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("providerId")
        @Expose
        private String providerId;
        @SerializedName("serviceId")
        @Expose
        private String serviceId;
        @SerializedName("paymentType")
        @Expose
        private String paymentType;
        @SerializedName("locationName")
        @Expose
        private String locationName;
        @SerializedName("locationLatitude")
        @Expose
        private String locationLatitude;
        @SerializedName("locationLongitude")
        @Expose
        private String locationLongitude;
        @SerializedName("pickDate")
        @Expose
        private String pickDate;
        @SerializedName("pickServiceTime")
        @Expose
        private String pickServiceTime;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("comments")
        @Expose
        private String comments;
        @SerializedName("rejectReason")
        @Expose
        private String rejectReason;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("updated")
        @Expose
        private String updated;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("userimage")
        @Expose
        private String userimage;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("createdTime")
        @Expose
        private String createdTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProviderStatus() {
            return providerStatus;
        }

        public void setProviderStatus(String providerStatus) {
            this.providerStatus = providerStatus;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProviderId() {
            return providerId;
        }

        public void setProviderId(String providerId) {
            this.providerId = providerId;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getLocationLatitude() {
            return locationLatitude;
        }

        public void setLocationLatitude(String locationLatitude) {
            this.locationLatitude = locationLatitude;
        }

        public String getLocationLongitude() {
            return locationLongitude;
        }

        public void setLocationLongitude(String locationLongitude) {
            this.locationLongitude = locationLongitude;
        }

        public String getPickDate() {
            return pickDate;
        }

        public void setPickDate(String pickDate) {
            this.pickDate = pickDate;
        }

        public String getPickServiceTime() {
            return pickServiceTime;
        }

        public void setPickServiceTime(String pickServiceTime) {
            this.pickServiceTime = pickServiceTime;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getRejectReason() {
            return rejectReason;
        }

        public void setRejectReason(String rejectReason) {
            this.rejectReason = rejectReason;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserimage() {
            return userimage;
        }

        public void setUserimage(String userimage) {
            this.userimage = userimage;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

    }
}
