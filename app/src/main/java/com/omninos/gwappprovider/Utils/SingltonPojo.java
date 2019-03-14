package com.omninos.gwappprovider.Utils;

import com.omninos.gwappprovider.modelClasses.ServiceModel;

import java.util.List;

public class SingltonPojo {

    String providerName, providerEmail, providerPassword, providerPhone, providerServiceType, drivingLicence, providerInsurance, providerQualification, MainServices, SubServices, Otp;
    String UserimagePath,latitude,lonitude,charges;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLonitude() {
        return lonitude;
    }

    public void setLonitude(String lonitude) {
        this.lonitude = lonitude;
    }

    public String getUserimagePath() {
        return UserimagePath;
    }

    public void setUserimagePath(String userimagePath) {
        UserimagePath = userimagePath;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderEmail() {
        return providerEmail;
    }

    public void setProviderEmail(String providerEmail) {
        this.providerEmail = providerEmail;
    }

    public String getProviderPassword() {
        return providerPassword;
    }

    public void setProviderPassword(String providerPassword) {
        this.providerPassword = providerPassword;
    }

    public String getProviderPhone() {
        return providerPhone;
    }

    public void setProviderPhone(String providerPhone) {
        this.providerPhone = providerPhone;
    }

    public String getProviderServiceType() {
        return providerServiceType;
    }

    public void setProviderServiceType(String providerServiceType) {
        this.providerServiceType = providerServiceType;
    }

    public String getDrivingLicence() {
        return drivingLicence;
    }

    public void setDrivingLicence(String drivingLicence) {
        this.drivingLicence = drivingLicence;
    }

    public String getProviderInsurance() {
        return providerInsurance;
    }

    public void setProviderInsurance(String providerInsurance) {
        this.providerInsurance = providerInsurance;
    }

    public String getProviderQualification() {
        return providerQualification;
    }

    public void setProviderQualification(String providerQualification) {
        this.providerQualification = providerQualification;
    }

    public String getMainServices() {
        return MainServices;
    }

    public void setMainServices(String mainServices) {
        MainServices = mainServices;
    }

    public String getSubServices() {
        return SubServices;
    }

    public void setSubServices(String subServices) {
        SubServices = subServices;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }

    public List<ServiceModel.SubService> subServicesData = null;

    public List<ServiceModel.SubService> getSubServicesData() {
        return subServicesData;
    }

    public void setSubServicesData(List<ServiceModel.SubService> subServicesData) {
        this.subServicesData = subServicesData;
    }

    public List<String> ServiceIdData = null;

    public List<String> getServiceIdData() {
        return ServiceIdData;
    }

    public void setServiceIdData(List<String> serviceIdData) {
        ServiceIdData = serviceIdData;
    }

    String SocialLoginStatus,SocialID;

    public String getSocialLoginStatus() {
        return SocialLoginStatus;
    }

    public void setSocialLoginStatus(String socialLoginStatus) {
        SocialLoginStatus = socialLoginStatus;
    }

    public String getSocialID() {
        return SocialID;
    }

    public void setSocialID(String socialID) {
        SocialID = socialID;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }
}
