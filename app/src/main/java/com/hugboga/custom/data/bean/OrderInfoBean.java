package com.hugboga.custom.data.bean;


public class OrderInfoBean  implements  IBaseBean{

    /**
     * serviceDepartTime : 2016-06-07 09:00:00
     * priceActual : 6404
     * payDeadTime : 13小时29分钟
     * orderno : R191447716919
     */

    private String serviceDepartTime;
    private double priceActual;
    private String payDeadTime;
    private String orderno;

    public String getServiceDepartTime() {
        return serviceDepartTime;
    }

    public void setServiceDepartTime(String serviceDepartTime) {
        this.serviceDepartTime = serviceDepartTime;
    }

    public double getPriceActual() {
        return priceActual;
    }

    public void setPriceActual(double priceActual) {
        this.priceActual = priceActual;
    }

    public String getPayDeadTime() {
        return payDeadTime;
    }

    public void setPayDeadTime(String payDeadTime) {
        this.payDeadTime = payDeadTime;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }
}
