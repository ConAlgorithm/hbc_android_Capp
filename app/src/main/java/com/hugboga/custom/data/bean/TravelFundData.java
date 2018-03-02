package com.hugboga.custom.data.bean;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 16/5/26.
 */
public class TravelFundData implements Serializable {

    public double availableBalanceAmount;                // 可用余额
    public double frozenBalanceAmount;                   // 冻结余额
    public double totalIncomeAmount;                     // 累计收入
    public double totalExpenseAmount;                    // 累计支出

    public int incomeTotalCount;                         // 收入总个数
    public ArrayList<TravelFundItemBean> incomeLogList;  // 收入流水列表

    public int expenseTotalCount;                        // 支出总个数
    public ArrayList<TravelFundItemBean> expenseLogList; // 支出流水列表

    public static class TravelFundItemBean implements Serializable {
        public String title;   // 标题
        public String amount;  // 金额
        public String date;    // 日期 格式 yyyy-MM-dd
    }
}
