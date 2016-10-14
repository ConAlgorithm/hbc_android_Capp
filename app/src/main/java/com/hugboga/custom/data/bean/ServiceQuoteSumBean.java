package com.hugboga.custom.data.bean;

import java.util.List;

/**
 * Created  on 16/4/16.
 */
public class ServiceQuoteSumBean implements IBaseBean ,Cloneable{
    public int avgSpend;
    public List<DayQuoteBean> dayQuotes;
    public int numOfPerson;
    public int totalPrice;


}
