package com.copasso.cocobill.ui.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.copasso.cocobill.BuildConfig;
import com.copasso.cocobill.R;

import androidx.annotation.NonNull;
import me.drakeet.multitype.Items;
import me.drakeet.support.about.AbsAboutActivity;
import me.drakeet.support.about.Card;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.Contributor;
import me.drakeet.support.about.License;

/**
 * Created by Zhouas666 on 2019-01-10
 * Github: https://github.com/zas023
 * <p>
 * 关于activity
 */
public class AboutActivity extends AbsAboutActivity {
    @Override
    protected void onCreateHeader(@NonNull ImageView icon, @NonNull TextView slogan, @NonNull TextView version) {
        icon.setImageResource(R.mipmap.ic_launcher);
        slogan.setText("CocoBill");
        version.setText("v " + BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onItemsCreated(@NonNull Items items) {
        items.add(new Category("About and Help"));
        items.add(new Card(getString(R.string.about_introduce)));

        items.add(new Category("Developers"));
        items.add(new Contributor(R.mipmap.avatar_zhouas666, "zhouas666", "Developer & designer", "https://github.com/zas023"));

        items.add(new Category("Open Source Licenses"));
        items.add(new License("Glide", "bumptech", License.APACHE_2, "com.github.bumptech.glide:glide:4.8.0"));
        items.add(new License("MPAndroidChart", "PhilJay", License.APACHE_2, "com.github.PhilJay:MPAndroidChart:v3.1.0-alpha"));
        items.add(new License("MultiType", "drakeet", License.APACHE_2, "https://github.com/drakeet/MultiType"));
        items.add(new License("About-page", "drakeet", License.APACHE_2, "https://github.com/drakeet/about-page"));
        items.add(new License("Dialogs", "afollestad", License.APACHE_2, "com.afollestad.material-dialogs:core:0.9.6.0"));
        items.add(new License("PickerView", "contrarywind", License.APACHE_2, "com.contrarywind:Android-PickerView:4.1.6"));
        items.add(new License("Greenrobot", "greenrobot", License.APACHE_2, "org.greenrobot:greendao:3.2.2"));
        items.add(new License("Greenrobot", "greenrobot", License.APACHE_2, "org.greenrobot:eventbus:3.1.1"));
        items.add(new License("Bmob", "bmob", License.APACHE_2, "cn.bmob.android:bmob-sdk:3.6.8-rc7"));
        items.add(new License("RxJava", "reactivex", License.APACHE_2, "io.reactivex.rxjava2:rxjava:2.2.2"));
        items.add(new License("RxAndroid", "reactivex", License.APACHE_2, "io.reactivex.rxjava2:rxandroid:2.1.0"));
        items.add(new License("OkIO", "squareup", License.APACHE_2, "com.squareup.okio:okio:2.1.0"));
        items.add(new License("OkHttp", "squareup", License.APACHE_2, "com.squareup.okhttp3:okhttp:3.12.0"));
        items.add(new License("Gson", "google", License.APACHE_2, "com.google.code.gson:gson:2.8.5"));
    }
}
