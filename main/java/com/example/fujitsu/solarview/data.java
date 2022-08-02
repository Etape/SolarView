package com.example.fujitsu.solarview;

public class data{
    public data() {
    }

    public double getData_datetime() {
        return data_datetime;
    }

    public void setData_datetime(long data_datetime) {
        this.data_datetime = data_datetime;
    }

    public String getTest_type() {
        return test_type;
    }

    public void setTest_type(String test_type) {
        this.test_type = test_type;
    }

    public float getPyro() {
        return pyro;
    }

    public void setPyro(float pyro) {
        this.pyro = pyro;
    }

    public float getAnemo() {
        return anemo;
    }

    public void setAnemo(float anemo) {
        this.anemo = anemo;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getAir_temper() {
        return air_temper;
    }

    public void setAir_temper(float air_temper) {
        this.air_temper = air_temper;
    }

    public float getPanel_temper() {
        return panel_temper;
    }

    public void setPanel_temper(float panel_temper) {
        this.panel_temper = panel_temper;
    }

    public float getThermo1() {
        return thermo1;
    }

    public void setThermo1(float thermo1) {
        this.thermo1 = thermo1;
    }

    public float getThermo2() {
        return thermo2;
    }

    public void setThermo2(float thermo2) {
        this.thermo2 = thermo2;
    }

    public float getCurrent_battery() {
        return current_battery;
    }

    public void setCurrent_battery(float current_battery) {
        this.current_battery = current_battery;
    }

    private long data_datetime;
   private String test_type;
   private float pyro;
   private float anemo;
    private float humidity;
   private float air_temper;
   private float panel_temper ;
   private float thermo1 ;
   private float thermo2 ;
   private float current_battery;
    private float current_charge;
    private float tensionBat;
    private float tensionPan;

    public float getCurrent_charge() {
        return current_charge;
    }

    public void setCurrent_charge(float current_charge) {
        this.current_charge = current_charge;
    }

    public float getTensionBat() {
        return tensionBat;
    }

    public void setTensionBat(float tensionBat) {
        this.tensionBat = tensionBat;
    }

    public float getTensionPan() {
        return tensionPan;
    }

    public void setTensionPan(float tensionPan) {
        this.tensionPan = tensionPan;
    }

    public data(long data_datetime, String test_type, float pyro, float anemo, float humidity, float air_temper, float panel_temper, float thermo1, float thermo2, float current_battery, float current_charge, float tensionBat, float tensionPan) {
        this.data_datetime = data_datetime;
        this.test_type = test_type;
        this.pyro = pyro;
        this.anemo = anemo;
        this.humidity = humidity;
        this.air_temper = air_temper;
        this.panel_temper = panel_temper;
        this.thermo1 = thermo1;
        this.thermo2 = thermo2;
        this.current_battery = current_battery;
        this.current_charge = current_charge;
        this.tensionBat = tensionBat;
        this.tensionPan = tensionPan;
    }
}

