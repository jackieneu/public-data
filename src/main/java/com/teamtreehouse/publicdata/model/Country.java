package com.teamtreehouse.publicdata.model;

import javax.persistence.*;

@Entity
public class Country {
    @Id
//    @Column(length=3)
    private String code;

    @Column(length=32)
    private String name;

    @Column(length=11)
    private Double internetUsers;

    @Column(length=11)
    private Double adultLiteracyRate;

    //Default constructor for JPA
    public Country(){}

    public Country(CountryBuilder builder){
        this.code = builder.code;
        this.name = builder.name;
        this.internetUsers = builder.internetUsers;
        this.adultLiteracyRate = builder.adultLiteracyRate;
    }

    @Override
    public String toString() {
        return String.format("%-32s",name) +
                (internetUsers == null ? String.format("%20s","--") : String.format("%20.2f",internetUsers)) +
                (adultLiteracyRate == null ? String.format("%20s","--") : String.format("%20.2f",adultLiteracyRate));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(Double internetUsers) {
        this.internetUsers = internetUsers;
    }

    public Double getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(Double adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }

    public static class CountryBuilder{
        private String code;
        private String name;
        private Double internetUsers;
        private Double adultLiteracyRate;

        public CountryBuilder(String code, String name){
            this.code = code;
            this.name = name;
        }

        public CountryBuilder withInternetUsers(Double internetUsers){
            this.internetUsers = internetUsers;
            return this;
        }

        public CountryBuilder withAdultLiteracyRate(Double adultLiteracyRate){
            this.adultLiteracyRate = adultLiteracyRate;
            return this;
        }

        public Country build(){
            return new Country(this);
        }
    }
}
