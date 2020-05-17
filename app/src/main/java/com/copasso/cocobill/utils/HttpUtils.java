package com.copasso.cocobill.utils;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.copasso.cocobill.common.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zhouas666 on 2017/10/27.
 */

public class HttpUtils {

    /**
     * 网络请求，返回JsonString
     * @param urlInfo
     * @return
     */
    public static String request(String urlInfo) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();

        String getURL = Constants.BASE_URL + urlInfo;

        try {
            URL url = new URL(getURL);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 网络请求图片，返回Drawable
     * @param imageUrl
     * @param imageName
     * @return
     */
    public static Drawable loadImageFromNetwork(String imageUrl, String imageName)
    {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(
                    new URL(imageUrl).openStream(), imageName);
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }

        return drawable ;
    }

    /**
     * 返回某月账单
     * @param handler
     */
    public static void getMonthBills(final Handler handler,
                                     final int userid, final String year, final String month) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 得到网络返回数据
                String result = request("/bill/user/"+userid+"/"+year+"/"+month);
                if (result != null) {
                    Message message = new Message();
                    message.obj = result;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    /**
     * 删除账单
     * @param handler
     */
    public static void deleteBillById(final Handler handler, final int billId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 得到网络返回数据
                String result = request("/bill/delete/"+billId);
                if (result != null) {
                    Message message = new Message();
                    message.obj = result;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    /**
     * 添加账单
     *  http://139.199.176.173:8080/bill/add?userid=1&typeid=1&cost=100&crdate=1508336703000&content=test&income=true
     * @param handler
     */
    public static void addBill(final Handler handler, final Float cost, final String content,
                               final int userid, final int sortid, final int payid, final String crdate, final boolean income) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 得到网络返回数据
                String result = request("/bill/add?"+"userid="+userid+"&sortid="+sortid+"&payid="+payid
                        +"&cost="+cost+"&crdate="+crdate+"&content="+content+"&income="+income);
                if (result != null) {
                    Message message = new Message();
                    message.obj = result;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    /**
     * 更新账单
     *  http://139.199.176.173:8080/ssmBillBook/bill/update?id=118&&userid=1&sortid=8&payid=3&cost=100&crdate=2017-12-20%2023:29:50&content=test&income=true
     * @param handler
     */
    public static void updateBill(final Handler handler, final int id, final Float cost, final String content,
                                  final int userid, final int sortid, final int payid, final String crdate, final boolean income) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 得到网络返回数据
                String result = request("/bill/update?id="+id+"&userid="+userid+"&sortid="+sortid+"&payid="+payid
                        +"&cost="+cost+"&crdate="+crdate+"&content="+content+"&income="+income);
                if (result != null) {
                    Message message = new Message();
                    message.obj = result;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }


    /**
     * 更新用户信息
     * @param handler
     * @param id
     * @param username
     * @param gender
     * @param phone
     * @param mail
     */
    public static void updateUser(final Handler handler, final int id, final String username, final String gender,
                                  final String phone, final String mail) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 得到网络返回数据
                String result = request("/user/update?id="+id+"&username="+username+"&gender="+gender+"&phone="+phone
                        +"&mail="+mail);
                if (result != null) {
                    Message message = new Message();
                    message.obj = result;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    /**
     * http://139.199.176.173:8080/ssmBillBook/note/user/1
     * @param handler
     * @param userid
     */
    public static void getNote(final Handler handler, final int userid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 得到网络返回数据
                String result = request("/note/user/"+userid);
                if (result != null) {
                    Message message = new Message();
                    message.obj = result;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    /**
     * 返回某月账单分类统计
     * http://139.199.176.173:8080/ssmBillBook/bill/chart/1/2017/12
     * @param handler
     * @param userid
     */
    public static void getMonthChart(final Handler handler,
                                     final int userid, final String year, final String month) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 得到网络返回数据
                String result = request("/bill/chart/"+userid+"/"+year+"/"+month);
                if (result != null) {
                    Message message = new Message();
                    message.obj = result;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    /**
     * 返回某月账单支付方式统计
     * http://139.199.176.173:8080/ssmBillBook/bill/chart/1/2017/12
     * @param handler
     * @param userid
     */
    public static void getMonthAccount(final Handler handler,
                                       final int userid, final String year, final String month) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 得到网络返回数据
                String result = request("/bill/pay/"+userid+"/"+year+"/"+month);
                if (result != null) {
                    Message message = new Message();
                    message.obj = result;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }


    /**
     * 用户登录
     * @param handler
     * @param username
     * @param password
     */
    public static void userLogin(final Handler handler, final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 得到网络返回数据
                String result = request("/user/login?username="+username+"&password="+password);
                if (result != null) {
                    Message message = new Message();
                    message.obj = result;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    /**
     * 用户注册
     * @param handler
     * @param username
     * @param password
     */
    public static void userSign(final Handler handler, final String username, final String password, final String mail) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 得到网络返回数据
                String result = request("/user/sign?username="+username+"&password="+password+"&mail="+mail);
                if (result != null) {
                    Message message = new Message();
                    message.obj = result;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }
}
