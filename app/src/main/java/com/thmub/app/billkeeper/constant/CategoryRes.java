package com.thmub.app.billkeeper.constant;

import com.thmub.app.billkeeper.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Enosh on 2020-03-15
 * Github: https://github.com/zas023
 */
public class CategoryRes {

    public static final String IC_NAME_SETTING = "Setting";

    /**
     * 一般
     */
    public static final String IC_NAME_OTHER = "Other";
    /**
     * 餐饮
     */
    public static final String IC_NAME_CAN_YIN = "CanYin";
    /**
     * 交通
     */
    public static final String IC_NAME_JIAO_TONG = "JiaoTong";
    /**
     * 购物
     */
    public static final String IC_NAME_GOU_WU = "GouWu";
    /**
     * 服饰
     */
    public static final String IC_NAME_FU_SHI = "FuShi";
    /**
     * 日用品
     */
    public static final String IC_NAME_RI_YONG_PIN = "RiYongPin";
    /**
     * 娱乐
     */
    public static final String IC_NAME_YU_LE = "YuLe";
    /**
     * 食材
     */
    public static final String IC_NAME_SHI_CAI = "ShiCai";
    /**
     * 零食
     */
    public static final String IC_NAME_LING_SHI = "LingShi";
    /**
     * 烟酒茶
     */
    public static final String IC_NAME_YAN_JIU_CHA = "YanJiuCha";
    /**
     * 学习
     */
    public static final String IC_NAME_XUE_XI = "XueXi";
    /**
     * 医疗
     */
    public static final String IC_NAME_YI_LIAO = "YiLiao";
    /**
     * 住房
     */
    public static final String IC_NAME_ZHU_FANG = "ZhuFang";
    /**
     * 水电煤
     */
    public static final String IC_NAME_SHUI_DIAN_MEI = "ShuiDianMei";
    /**
     * 通讯
     */
    public static final String IC_NAME_TONG_XUN = "TongXun";
    /**
     * 人情来往
     */
    public static final String IC_NAME_REN_QING = "RenQing";

    /**
     * 薪资
     */
    public static final String IC_NAME_XIN_ZI = "XinZi";
    /**
     * 奖金
     */
    public static final String IC_NAME_JIANG_JIN = "JiangJin";
    /**
     * 借入
     */
    public static final String IC_NAME_JIE_RU = "JieRu";
    /**
     * 收债
     */
    public static final String IC_NAME_SHOU_ZHAI = "ShouZhai";
    /**
     * 利息收入
     */
    public static final String IC_NAME_LI_XIN_SHOU_RU = "LixiShouRu";
    /**
     * 投资回收
     */
    public static final String IC_NAME_TOU_ZI_HUI_SHOU = "TouZiHuiShou";
    /**
     * 意外所得
     */
    public static final String IC_NAME_YI_WAI_SUO_DE = "YiWaiSuoDe";
    /**
     * 投资收益
     */
    public static final String IC_NAME_TOU_ZI_SHOU_YI = "TouZiShouYi";

    /**
     * 卡片
     */
    public static final String IC_NAME_CARD = "Card";
    /**
     * 停车
     */
    public static final String IC_NAME_PARK = "Park";
    /**
     * 火车
     */
    public static final String IC_NAME_TRAIN = "Train";
    /**
     * 旅行
     */
    public static final String IC_NAME_TRAVEL = "Travel";
    /**
     * 趋势
     */
    public static final String IC_NAME_TREND = "Trend";
    /**
     * 红酒
     */
    public static final String IC_NAME_WINE = "Wine";

    /**
     * 所有分类图标
     */
    public static final List<String> ALL_ICON = Arrays.asList(
            IC_NAME_OTHER,
            IC_NAME_CAN_YIN,
            IC_NAME_JIAO_TONG,
            IC_NAME_GOU_WU,
            IC_NAME_FU_SHI,
            IC_NAME_RI_YONG_PIN,
            IC_NAME_YU_LE,
            IC_NAME_SHI_CAI,
            IC_NAME_LING_SHI,
            IC_NAME_YAN_JIU_CHA,
            IC_NAME_XUE_XI,
            IC_NAME_YI_LIAO,
            IC_NAME_ZHU_FANG,
            IC_NAME_SHUI_DIAN_MEI,
            IC_NAME_TONG_XUN,
            IC_NAME_REN_QING,
            IC_NAME_XIN_ZI,
            IC_NAME_JIANG_JIN,
            IC_NAME_JIE_RU,
            IC_NAME_SHOU_ZHAI,
            IC_NAME_LI_XIN_SHOU_RU,
            IC_NAME_TOU_ZI_HUI_SHOU,
            IC_NAME_YI_WAI_SUO_DE,
            IC_NAME_TOU_ZI_SHOU_YI,
            IC_NAME_CARD,
            IC_NAME_PARK,
            IC_NAME_TRAIN,
            IC_NAME_TRAVEL,
            IC_NAME_TREND,
            IC_NAME_WINE);

    /**
     * 通过图标名查找对应图标ID（以防止Clean后ID变化）
     * @param iconName
     * @return
     */
    public static int resId(String iconName) {
        if (iconName == null) {
            return R.drawable.ic_category_other;
        }
        switch (iconName) {
            case IC_NAME_SETTING:
                return R.drawable.ic_category_setting;
            // 支出
            case IC_NAME_OTHER:
                return R.drawable.ic_category_other;
            case IC_NAME_CAN_YIN:
                return R.drawable.ic_category_food;
            case IC_NAME_JIAO_TONG:
                return R.drawable.ic_category_traffic;
            case IC_NAME_GOU_WU:
                return R.drawable.ic_category_shopping;
            case IC_NAME_FU_SHI:
                return R.drawable.ic_category_clothes;
            case IC_NAME_RI_YONG_PIN:
                return R.drawable.ic_category_daily_necessities;
            case IC_NAME_YU_LE:
                return R.drawable.ic_category_entertainment;
            case IC_NAME_SHI_CAI:
                return R.drawable.ic_category_food_ingredients;
            case IC_NAME_LING_SHI:
                return R.drawable.ic_category_snack;
            case IC_NAME_YAN_JIU_CHA:
                return R.drawable.ic_category_tobacco_tea;
            case IC_NAME_XUE_XI:
                return R.drawable.ic_category_study;
            case IC_NAME_YI_LIAO:
                return R.drawable.ic_category_medical;
            case IC_NAME_ZHU_FANG:
                return R.drawable.ic_category_house;
            case IC_NAME_SHUI_DIAN_MEI:
                return R.drawable.ic_category_electricity;
            case IC_NAME_TONG_XUN:
                return R.drawable.ic_category_communication;
            case IC_NAME_REN_QING:
                return R.drawable.ic_category_favor_pattern;
            // 收入
            case IC_NAME_XIN_ZI:
                return R.drawable.ic_category_salary;
            case IC_NAME_JIANG_JIN:
                return R.drawable.ic_category_reward;
            case IC_NAME_JIE_RU:
                return R.drawable.ic_category_lend;
            case IC_NAME_SHOU_ZHAI:
                return R.drawable.ic_category_dun;
            case IC_NAME_LI_XIN_SHOU_RU:
                return R.drawable.ic_category_interest;
            case IC_NAME_TOU_ZI_HUI_SHOU:
                return R.drawable.ic_category_invest_recovery;
            case IC_NAME_YI_WAI_SUO_DE:
                return R.drawable.ic_category_unexpected;
            case IC_NAME_TOU_ZI_SHOU_YI:
                return R.drawable.ic_category_invest_profit;
            // Other
            case IC_NAME_CARD:
                return R.drawable.ic_category_card;
            case IC_NAME_PARK:
                return R.drawable.ic_category_park;
            case IC_NAME_TRAIN:
                return R.drawable.ic_category_train;
            case IC_NAME_TRAVEL:
                return R.drawable.ic_category_travel;
            case IC_NAME_TREND:
                return R.drawable.ic_category_trend;
            case IC_NAME_WINE:
                return R.drawable.ic_category_wine;

            default:
                return R.drawable.ic_category_other;
        }
    }
}
