package com.copasso.cocobill.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.os.*;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.copasso.cocobill.R;
import com.copasso.cocobill.bean.UserBean;
import com.copasso.cocobill.utils.*;
import com.copasso.cocobill.view.CommonItemLayout;

import android.net.Uri;
import okhttp3.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhouas666 on 2017/12/24.
 * 用户信息管理中心
 */
public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.user_info_toolbar)
    Toolbar toolbar;
    @BindView(R.id.rlt_update_icon)
    RelativeLayout iconRL;
    @BindView(R.id.img_icon)
    ImageView iconIv;
    @BindView(R.id.cil_username)
    CommonItemLayout usernameCL;
    @BindView(R.id.cil_sex)
    CommonItemLayout sexCL;
    @BindView(R.id.cil_phone)
    CommonItemLayout phoneCL;
    @BindView(R.id.cil_email)
    CommonItemLayout emailCL;

    //当前用户
    private UserBean currentUser;

    //选择图片来源
    private AlertDialog iconDialog;
    private AlertDialog genderDialog;
    private AlertDialog phoneDialog;
    private AlertDialog emailDialog;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static final int GENDER_MAN = 0;
    protected static final int GENDER_FEMALE = 1;
    private static final int CROP_SMALL_PICTURE = 2;

    //图片路径
    protected static Uri tempUri = null;

    @Override
    protected int getLayout() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initEventAndData() {
        //初始化Toolbar
        toolbar.setTitle("账户");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBack();
                finish();
            }
        });

        //获取当前账户信息
        currentUser = SharedPUtils.getCurrentUser(this);
        if (currentUser != null) {
            //加载到布局中
            initData();
            //加载当前头像
            String imgPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/" + currentUser.getImage();
            Log.i(TAG,imgPath);
            File file = new File(imgPath);
            if (file.exists()) {
                //加载图片
                Glide.with(this).load(file).into(iconIv);

            }
        }
    }

    /**
     * 将用户信息更新到布局中
     */
    private void initData() {
        usernameCL.setRightText(currentUser.getUsername());
        sexCL.setRightText(currentUser.getGender());
        phoneCL.setRightText(currentUser.getPhone());
        emailCL.setRightText(currentUser.getMail());
    }

    /**
     * 监听点击事件
     *
     * @param view
     */
    @OnClick({R.id.rlt_update_icon, R.id.cil_username,
            R.id.cil_sex, R.id.cil_phone, R.id.cil_email})
    public void onViewClicked(final View view) {
        switch (view.getId()) {

            case R.id.rlt_update_icon:  //头像
                if (iconDialog == null) {
                    iconDialog = new AlertDialog.Builder(this).setItems(new String[]{"相册", "相机"},
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case CHOOSE_PICTURE: // 选择本地照片
                                            Intent openAlbumIntent = new Intent(
                                                    Intent.ACTION_GET_CONTENT);
                                            openAlbumIntent.setType("image/*");
                                            startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                                            break;
                                        case TAKE_PICTURE: // 拍照
                                            takePicture();
                                            break;
                                    }
                                }
                            }).create();
                }
                if (!iconDialog.isShowing()) {
                    iconDialog.show();
                }
                break;
            case R.id.cil_username:
                Toast.makeText(getApplicationContext(), "江湖人行不更名，坐不改姓！",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.cil_sex:  //性别
                if (genderDialog == null) {
                    genderDialog = new AlertDialog.Builder(this).setItems(new String[]{"男", "女"},
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case GENDER_MAN: // 男性
                                            if (currentUser.getGender().equals("F")) {
                                                currentUser.setGender("M");
                                                sexCL.setRightText(currentUser.getGender());
                                            }
                                            break;
                                        case GENDER_FEMALE: // 女性
                                            if (currentUser.getGender().equals("M")) {
                                                currentUser.setGender("F");
                                                sexCL.setRightText(currentUser.getGender());
                                            }
                                            break;
                                    }
                                }
                            }).create();
                }
                if (!genderDialog.isShowing()) {
                    genderDialog.show();
                }
                break;
            case R.id.cil_phone:  //电话修改
                final EditText editText = new EditText(UserInfoActivity.this);
                String phone = currentUser.getPhone();
                if (phone != null) {
                    editText.setText(currentUser.getPhone());
                    //将光标移至文字末尾
                    editText.setSelection(currentUser.getPhone().length());
                }
                if (phoneDialog == null) {
                    phoneDialog = new AlertDialog.Builder(this)
                            .setTitle("电话")
                            .setView(editText)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String input = editText.getText().toString();
                                    if (input.equals("")) {
                                        Toast.makeText(getApplicationContext(), "内容不能为空！" + input,
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (StringUtils.checkPhoneNumber(input)) {
                                            currentUser.setPhone(input);
                                            phoneCL.setRightText(input);
                                        } else {
                                            Snackbar.make(view, "请输入正确的电话号码", Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create();
                }
                if (!phoneDialog.isShowing()) {
                    phoneDialog.show();
                }
                break;
            case R.id.cil_email:  //邮箱修改
                final EditText emailEditText = new EditText(UserInfoActivity.this);
                emailEditText.setText(currentUser.getMail());
                //将光标移至文字末尾
                emailEditText.setSelection(currentUser.getMail().length());
                if (emailDialog == null) {
                    emailDialog = new AlertDialog.Builder(this)
                            .setTitle("邮箱")
                            .setView(emailEditText)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String input = emailEditText.getText().toString();
                                    if (input.equals("")) {
                                        Toast.makeText(getApplicationContext(), "内容不能为空！" + input,
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (StringUtils.checkEmail(input)) {
                                            currentUser.setMail(input);
                                            emailCL.setRightText(input);
                                        } else {
                                            Snackbar.make(view, "请输入正确的邮箱格式", Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create();
                }
                if (!emailDialog.isShowing()) {
                    emailDialog.show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 返回操作
     */
    public void doBack() {
        if (currentUser != null) {
            HttpUtils.updateUser(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.obj.toString().length() > 0) {
                        SharedPUtils.setCurrentUser(UserInfoActivity.this, currentUser);
                    } else {
                        Toast.makeText(UserInfoActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }, currentUser.getId(), currentUser.getUsername(), currentUser.getGender(), currentUser.getPhone(), currentUser.getMail());
        }
        setResult(RESULT_OK, new Intent());
    }

    /**
     * 监听Activity返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(intent.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (intent != null) {
                        setImageToView(intent); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
            doBack();
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 拍照
     */
    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "image.jpg");
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(UserInfoActivity.this,
                    "com.copasso.cocobill.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
        }
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = ImageUtils.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            iconIv.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    /**
     * 保存头像并上传服务器
     *
     * @param bitmap
     */
    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        String imagename = Constants.currentUserId + "_" + String.valueOf(System.currentTimeMillis());
        String imagePath = ImageUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), imagename + ".png");
        currentUser.setImage(imagename + ".png");
        SharedPUtils.setCurrentUser(UserInfoActivity.this, currentUser);
        if (imagePath != null) {
            OkHttpClient mOkHttpClient = new OkHttpClient();
            File file = new File(imagePath);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", imagename + ".png",
                            RequestBody.create(MediaType.parse("image/png"), file))
                    .build();

            Request request = new Request.Builder()
                    .header("Authorization", "Client-ID " + "...")
                    .url("http://139.199.176.173:8080/ssmBillBook/file/upload")
                    .post(requestBody)
                    .build();

            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i(TAG, response.body().string());
                }
            });
        }
    }

    /**
     * 权限请求
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }

}
