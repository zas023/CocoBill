package com.copasso.cocobill.utils;

import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.local.MonthDetailBean;

import java.util.ArrayList;
import java.util.List;

public  class BillUtils {

    /**
     * 包装账单展示列表
     */
    public static MonthDetailBean packageList(List<BBill> list){
        MonthDetailBean bean=new MonthDetailBean();
        float t_income = 0;
        float t_outcome = 0;
        List<MonthDetailBean.DaylistBean> daylist=new ArrayList<>();
        List<BBill> beanList=new ArrayList<>();
        float income = 0;
        float outcome = 0;

        String preDay = "";  //记录前一天的时间
        for (int i=0;i<list.size();i++){
            BBill bBill=list.get(i);
            //计算总收入支出
            if (bBill.isIncome())
                t_income+=bBill.getCost();
            else
                t_outcome+=bBill.getCost();
            //判断后一个账单是否于前者为同一天
            if(preDay.equals(DateUtils.getDay(bBill.getCrdate()))){
                if (bBill.isIncome())
                    income+=bBill.getCost();
                else
                    outcome+=bBill.getCost();
                beanList.add(bBill);
                if((i+1)==list.size()){
                    //局部变量防止引用冲突
                    List<BBill> tmpList=new ArrayList<>();
                    tmpList.addAll(beanList);

                    MonthDetailBean.DaylistBean tmpDay=new MonthDetailBean.DaylistBean();
                    tmpDay.setList(tmpList);
                    tmpDay.setMoney("支出：" + outcome + " 收入：" + income);
                    tmpDay.setTime(DateUtils.getDay(bBill.getCrdate()));
                    daylist.add(tmpDay);
                }
            }else{
                if(i!=0){
                    //局部变量防止引用冲突
                    List<BBill> tmpList=new ArrayList<>();
                    tmpList.addAll(beanList);

                    MonthDetailBean.DaylistBean tmpDay=new MonthDetailBean.DaylistBean();
                    tmpDay.setList(tmpList);
                    tmpDay.setMoney("支出：" + outcome + " 收入：" + income);
                    tmpDay.setTime(preDay);
                    daylist.add(tmpDay);
                    //清空前一天的数据
                    beanList.clear();
                    income=0;
                    outcome=0;
                }
                beanList.add(bBill);
                preDay=DateUtils.getDay(bBill.getCrdate());
            }
        }

        bean.setT_income(String.valueOf(t_income));
        bean.setT_outcome(String.valueOf(t_outcome));
        bean.setDaylist(daylist);
        return bean;
    }
}
