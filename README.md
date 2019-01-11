# CocoBill
[English Version](https://github.com/zas023/CocoBill/blob/master/README_en.md) | 中文版

:closed_book:一个数据存储使用的Bmob,采用MVP架构的Android记账本APP

在我们生活中经常不知道钱花在什么地方，但是我们自己又不是很想写在记账本上进行管理,因为关于记账，我们每个人都有自己的需求，虽然市面上有许多优秀的记账app,所以对于自己来说，开发一个属于自己的软件来记账是个非常有趣的事情，但一直因为时间安排的原因未能实现。 
所以趁着本学期程序实践的机会，顺便做一个项目，后台采用ssm框架，如果有不足的地方，希望看到的大神给予指点建议，不胜感激！

博客地址：http://blog.csdn.net/adminpd/article/details/78942212

PS：最近很多人问我有关后台的问题，后台数据服务使用的是bmob后端云，如果没用过，请先到 https://www.bmob.cn/?invite_code=@qcg 注册或自行百度，很好上手的，很适合个人开发者及小公司。若想写自己的后端，可以查看第一版代码。

整体项目（源码及数据库结构）我发布在 https://www.bmob.cn/shop/detail/448 上，购买后可以一键迁移我的数据库结构到你的账号里。创建bmob账号选择免费版的就完全够用，除非你用户量过10万了。

## 下载资源

发布在以下平台：

| 平台 | 下载地址 |
| ---- | ---- |
| 酷安 | <a href='https://www.coolapk.com/apk/174308'><img alt='去酷安下载' src='https://ws4.sinaimg.cn/large/006tNc79ly1fsphx16ybdj30go06st8q.jpg' height="60"/></a> |

## 版本日志

v2:后台使用Bmob后端云

v1:后台地址(采用ssm框架)：https://github.com/zas023/ssmBillBook


v0.5.1: 2019.01.11
- 更新依赖；
- 重构项目，优化结构；

v0.4.1: 2018.03.31
- 添加本地账单；
- 实现账单同步；

v0.3.1: 2017.12.28
- 添加上传头像功能；
- 添加修改主题功能；
- 添加账单分类管理；
- 添加侧滑删除编辑；

## 程序效果
<p>
<img width="32%" src="https://github.com/zas023/CocoBill/blob/master/imgs/Screenshot_2019-01-11-15-51-35-185_com.copasso.co.png" />
<img width="32%" src="https://github.com/zas023/CocoBill/blob/master/imgs/Screenshot_2019-01-11-15-51-44-801_com.copasso.co.png" />
<img width="32%" src="https://github.com/zas023/CocoBill/blob/master/imgs/Screenshot_2019-01-11-15-51-51-359_com.copasso.co.png" />
<img width="32%" src="https://github.com/zas023/CocoBill/blob/master/imgs/Screenshot_2017-12-30-18-12-25-810_com.copasso.co.png" />
<img width="32%" src="https://github.com/zas023/CocoBill/blob/master/imgs/Screenshot_2017-12-30-18-12-33-936_com.copasso.co.png" />
<img width="32%" src="https://github.com/zas023/CocoBill/blob/master/imgs/Screenshot_2017-12-30-18-13-09-779_com.copasso.co.png" />
<img width="32%" src="https://github.com/zas023/CocoBill/blob/master/imgs/Screenshot_2017-12-30-18-13-16-119_com.copasso.co.png" />
<img width="32%" src="https://github.com/zas023/CocoBill/blob/master/imgs/Screenshot_2017-12-30-18-16-39-866_com.copasso.co.png" />
<img width="32%" src="https://github.com/zas023/CocoBill/blob/master/imgs/Screenshot_2019-01-11-15-54-08-048_com.copasso.co.png" />
</p>

##### 注：本程序素材来源网络，如有影响你的权益，请及时联系本人


## TODO
- [x] 帐薄总支出、收入显示。
- [x] 账目数据增加编辑功能。
- [x] 账目数据增加归类功能。
- [x] 账目数据增加同步功能。
- [x] 自定义分类、支付方式。
- [x] 统计功能。
- [x] 换肤功能。
- [ ] 检测更新。
- [ ] 应用瘦身。
- [ ] 密码锁功能。

## Thanks
- butterknife: https://github.com/JakeWharton/butterknife
- glide: https://github.com/bumptech/glide
- okhttp: https://github.com/square/okhttp
- MPAndroidChart: https://github.com/PhilJay/MPAndroidChart
- Android-PickerView: https://github.com/Bigkoo/Android-PickerView
- Material-dialogs: https://github.com/afollestad/material-dialogs
- About: https://github.com/drakeet/about-page
- Multitype: https://github.com/drakeet/MultiType
- Greendao: https://github.com/greenrobot/greenDAO
- Eventbus: https://github.com/greenrobot/eventbus
- Bmob: https://bmob.cn
