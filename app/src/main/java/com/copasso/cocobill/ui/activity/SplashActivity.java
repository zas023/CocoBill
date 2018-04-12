package com.copasso.cocobill.ui.activity;

import android.content.Intent;
import com.copasso.cocobill.R;
import com.copasso.cocobill.utils.ThemeManager;
import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

/**
 * Created by zhouas666 on 2017/12/14.
 */
public class SplashActivity extends AwesomeSplash {

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!

    @Override
    public void initSplash(ConfigSplash configSplash) {

        /* you don't have to override every property */

        //Customize Circular Reveal
        //设置主题颜色
        String theme=ThemeManager.getInstance().getCurThemeName(this);
        String[] themes = ThemeManager.getInstance().getThemes();
        if(theme.equals(themes[0])){
            configSplash.setBackgroundColor(R.color.colorPrimary);
        }else if(theme.equals(themes[1])){
            configSplash.setBackgroundColor(R.color.colorPrimaryBlack);
        }else if(theme.equals(themes[2])){
            configSplash.setBackgroundColor(R.color.colorPrimaryGreen);
        }else if(theme.equals(themes[3])){
            configSplash.setBackgroundColor(R.color.colorPrimaryBlue);
        }else if(theme.equals(themes[4])){
            configSplash.setBackgroundColor(R.color.colorPrimaryPurple);
        }else if(theme.equals(themes[5])){
            configSplash.setBackgroundColor(R.color.colorPrimaryOrange);
        }else if(theme.equals(themes[6])){
            configSplash.setBackgroundColor(R.color.colorPrimaryBrown);
        }
        //configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(3000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.mipmap.ic_launcher_round); //or any other drawable
        configSplash.setAnimLogoSplashDuration(500); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Title
        configSplash.setTitleSplash("My Coco Bill App");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(2000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
        configSplash.setTitleFont("fonts/volatire.ttf"); //provide string to your font located in assets/fonts/

    }

    /**
     * 监听动画完成事件
     */
    @Override
    public void animationsFinished() {

        //transit to another activity here
        //or do whatever you want
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
