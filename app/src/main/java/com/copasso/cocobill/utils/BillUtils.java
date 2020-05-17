package com.copasso.cocobill.utils;

import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.local.MonthAccountBean;
import com.copasso.cocobill.model.bean.local.MonthChartBean;
import com.copasso.cocobill.model.bean.local.MonthListBean;
import com.copasso.cocobill.model.bean.remote.CoBill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包装账单展示列表工具类
 */
public class BillUtils {

    /**
     * 账单按天分类
     * @param list
     * @return
     */
    public static MonthListBean packageDetailList(List<BBill> list) {
        MonthListBean bean = new MonthListBean();
        float t_income = 0;
        float t_outcome = 0;
        List<MonthListBean.DaylistBean> daylist = new ArrayList<>();
        List<BBill> beanList = new ArrayList<>();
        float income = 0;
        float outcome = 0;

        String preDay = "";  //记录前一天的时间
        for (int i = 0; i < list.size(); i++) {
            BBill bBill = list.get(i);
            //计算总收入支出
            if (bBill.isIncome())
                t_income += bBill.getCost();
            else
                t_outcome += bBill.getCost();

            //判断后一个账单是否于前者为同一天
            if (i == 0 || preDay.equals(DateUtils.getDay(bBill.getCrdate()))) {

                if (bBill.isIncome())
                    income += bBill.getCost();
                else
                    outcome += bBill.getCost();
                beanList.add(bBill);

                if (i==0)
                    preDay = DateUtils.getDay(bBill.getCrdate());
            } else {
                //局部变量防止引用冲突
                List<BBill> tmpList = new ArrayList<>();
                tmpList.addAll(beanList);
                MonthListBean.DaylistBean tmpDay = new MonthListBean.DaylistBean();
                tmpDay.setList(tmpList);
                tmpDay.setMoney("支出：" + outcome + " 收入：" + income);
                tmpDay.setTime(preDay);
                daylist.add(tmpDay);

                //清空前一天的数据
                beanList.clear();
                income = 0;
                outcome = 0;

                //添加数据
                if (bBill.isIncome())
                    income += bBill.getCost();
                else
                    outcome += bBill.getCost();
                beanList.add(bBill);
                preDay = DateUtils.getDay(bBill.getCrdate());
            }
        }

        if (beanList.size() > 0) {
            //局部变量防止引用冲突
            List<BBill> tmpList = new ArrayList<>();
            tmpList.addAll(beanList);
            MonthListBean.DaylistBean tmpDay = new MonthListBean.DaylistBean();
            tmpDay.setList(tmpList);
            tmpDay.setMoney("支出：" + outcome + " 收入：" + income);
            tmpDay.setTime(DateUtils.getDay(beanList.get(0).getCrdate()));
            daylist.add(tmpDay);
        }

        bean.setT_income(String.valueOf(t_income));
        bean.setT_outcome(String.valueOf(t_outcome));
        bean.setT_total(String.valueOf(t_income-t_outcome));
        bean.setDaylist(daylist);
        return bean;
    }

    /**
     * 账单按类型分类
     * @param list
     * @return
     */
    public static MonthChartBean packageChartList(List<BBill> list) {
        MonthChartBean bean = new MonthChartBean();
        float t_income = 0;
        float t_outcome = 0;

        Map<String, List<BBill>> mapIn = new HashMap<>();
        Map<String, Float> moneyIn = new HashMap<>();
        Map<String, List<BBill>> mapOut = new HashMap<>();
        Map<String, Float> moneyOut = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            BBill bBill = list.get(i);
            //计算总收入支出
            if (bBill.isIncome()) t_income += bBill.getCost();
            else t_outcome += bBill.getCost();

            //账单分类
            String sort = bBill.getSortName();
            List<BBill> listBill;
            if (bBill.isIncome()) {
                if (mapIn.containsKey(sort)) {
                    listBill = mapIn.get(sort);
                } else {
                    listBill = new ArrayList<>();
                }
                if (moneyIn.containsKey(sort))
                    moneyIn.put(sort, moneyIn.get(sort) + bBill.getCost());
                else
                    moneyIn.put(sort, bBill.getCost());
                listBill.add(bBill);
                mapIn.put(sort, listBill);
            } else {
                if (mapOut.containsKey(sort)) {
                    listBill = mapOut.get(sort);
                } else {
                    listBill = new ArrayList<>();
                }
                if (moneyOut.containsKey(sort))
                    moneyOut.put(sort, moneyOut.get(sort) + bBill.getCost());
                else
                    moneyOut.put(sort, bBill.getCost());
                listBill.add(bBill);
                mapOut.put(sort, listBill);
            }
        }

        List<MonthChartBean.SortTypeList> outSortlist = new ArrayList<>();    //账单分类统计支出
        List<MonthChartBean.SortTypeList> inSortlist = new ArrayList<>();    //账单分类统计收入

        for (Map.Entry<String, List<BBill>> entry : mapOut.entrySet()) {
            MonthChartBean.SortTypeList sortTypeList = new MonthChartBean.SortTypeList();
            sortTypeList.setList(entry.getValue());
            sortTypeList.setSortName(entry.getKey());
            sortTypeList.setSortImg(entry.getValue().get(0).getSortImg());
            sortTypeList.setMoney(moneyOut.get(entry.getKey()));
            sortTypeList.setBack_color(StringUtils.randomColor());
            outSortlist.add(sortTypeList);
        }
        for (Map.Entry<String, List<BBill>> entry : mapIn.entrySet()) {
            MonthChartBean.SortTypeList sortTypeList = new MonthChartBean.SortTypeList();
            sortTypeList.setList(entry.getValue());
            sortTypeList.setSortName(entry.getKey());
            sortTypeList.setSortImg(entry.getValue().get(0).getSortImg());
            sortTypeList.setMoney(moneyIn.get(entry.getKey()));
            sortTypeList.setBack_color(StringUtils.randomColor());
            inSortlist.add(sortTypeList);
        }

        bean.setOutSortlist(outSortlist);
        bean.setInSortlist(inSortlist);
        bean.setTotalIn(t_income);
        bean.setTotalOut(t_outcome);
        return bean;
    }

    /**
     * 账单按支付方式分类
     * @param list
     * @return
     */
    public static MonthAccountBean packageAccountList(List<BBill> list) {

        MonthAccountBean bean = new MonthAccountBean();
        float t_income = 0;
        float t_outcome = 0;

        Map<String, List<BBill>> mapAccount = new HashMap<>();
        Map<String, Float> mapMoneyIn = new HashMap<>();
        Map<String, Float> mapMoneyOut = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            BBill bBill = list.get(i);
            //计算总收入支出
            if (bBill.isIncome()) t_income += bBill.getCost();
            else t_outcome += bBill.getCost();

            String pay = bBill.getPayName();

            if (mapAccount.containsKey(pay)) {
                List<BBill> bBills = mapAccount.get(pay);
                bBills.add(bBill);
                mapAccount.put(pay, bBills);
            } else {
                List<BBill> bBills = new ArrayList<>();
                bBills.add(bBill);
                mapAccount.put(pay, bBills);
            }

            if (bBill.isIncome()) {
                if (mapMoneyIn.containsKey(pay)) {
                    mapMoneyIn.put(pay, mapMoneyIn.get(pay) + bBill.getCost());
                } else {
                    mapMoneyIn.put(pay, bBill.getCost());
                }
            } else {
                if (mapMoneyOut.containsKey(pay)) {
                    mapMoneyOut.put(pay, mapMoneyOut.get(pay) + bBill.getCost());
                } else {
                    mapMoneyOut.put(pay, bBill.getCost());
                }
            }
        }

        List<MonthAccountBean.PayTypeListBean> payTypeListBeans = new ArrayList<>();    //账单分类统计支出
        for (Map.Entry<String, List<BBill>> entry : mapAccount.entrySet()) {
            MonthAccountBean.PayTypeListBean payTypeListBean = new MonthAccountBean.PayTypeListBean();
            payTypeListBean.setBills(entry.getValue());
            //先判断当前支付方式是否有输入或支出
            //因为有可能只有支出或收入
            if (mapMoneyIn.containsKey(entry.getKey()))
                payTypeListBean.setIncome(mapMoneyIn.get(entry.getKey()));
            if (mapMoneyOut.containsKey(entry.getKey()))
                payTypeListBean.setOutcome(mapMoneyOut.get(entry.getKey()));
            payTypeListBean.setPayImg(entry.getValue().get(0).getPayImg());
            payTypeListBean.setPayName(entry.getValue().get(0).getPayName());
            payTypeListBeans.add(payTypeListBean);
        }

        bean.setTotalIn(t_income);
        bean.setTotalOut(t_outcome);
        bean.setList(payTypeListBeans);
        return bean;
    }

    /**
     * CoBill=>BBill
     *
     * @param coBill
     * @return
     */
    public static BBill toBBill(CoBill coBill) {
        BBill bBill = new BBill();
        bBill.setRid(coBill.getObjectId());
        bBill.setVersion(coBill.getVersion());
        bBill.setIncome(coBill.getIncome());
        bBill.setCrdate(coBill.getCrdate());
        bBill.setSortImg(coBill.getSortImg());
        bBill.setSortName(coBill.getSortName());
        bBill.setPayImg(coBill.getPayImg());
        bBill.setPayName(coBill.getPayName());
        bBill.setUserid(coBill.getUserid());
        bBill.setContent(coBill.getContent());
        bBill.setCost(coBill.getCost());

        return bBill;
    }
}
