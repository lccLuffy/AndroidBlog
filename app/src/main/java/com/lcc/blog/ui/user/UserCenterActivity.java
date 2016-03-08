package com.lcc.blog.ui.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lcc.blog.R;
import com.lcc.blog.adapter.UserFragmentAdapter;
import com.lcc.blog.base.BaseActivity;
import com.lcc.blog.model.User;
import com.lcc.blog.service.user.UserService;
import com.lcc.blog.utils.RetrofitUtil;
import com.lcc.blog.utils.UserManager;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCenterActivity extends BaseActivity {

    @Bind(R.id.cat_title)
    TextView username_tv;

    @Bind(R.id.email)
    TextView email_tv;

    @Bind(R.id.cat_avatar)
    ImageView avatar_iv;

    @Bind(R.id.viewPage)
    ViewPager viewPager;

    @Bind(R.id.viewpagerTab)
    SmartTabLayout viewpagerTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        actionBar.setDisplayShowTitleEnabled(false);;
    }

    private void init() {
        setupUserInfo(UserManager.getUser());
        UserFragmentAdapter fragmentAdapter = new UserFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        viewpagerTab.setViewPager(viewPager);
        avatar_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE);
            }
        });
    }

    private static final int CROP = 22;
    private static final int CHOOSE = 11;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean success = true;
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE) {
                if (data != null) {
                    startPhotoZoom(data.getData());
                } else {
                    success = false;
                }
            } else if (requestCode == CROP) {
                if (data != null)
                {
                    do {
                        Bitmap avatar = data.getParcelableExtra("data");
                        avatar_iv.setImageBitmap(avatar);
                        final File file = new File(Environment.getExternalStorageDirectory(),"haha.jpg");
                        if(!file.exists())
                        {
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                success = false;
                                break;
                            }
                        }
                        if(saveBitmap(file,avatar))
                        {
                            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),Bitmap2Bytes(avatar));
                            UserService userService = RetrofitUtil.create(UserService.class);
                            userService.uploadAvatar(requestBody).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    toast(response.body());
                                    file.deleteOnExit();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    toast(t.toString());
                                    file.deleteOnExit();
                                }
                            });
                        }
                    }while (false);

                } else {
                    success = false;
                }
            }
        } else {
            success = false;
        }

        if (!success) {
            toast("上传失败");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX",300);
        intent.putExtra("outputY",300);

        intent.putExtra("return-data", true);

        startActivityForResult(intent, CROP);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_center;
    }

    private void setupUserInfo(User user) {
        if (user != null) {
            username_tv.setText(user.username);
            email_tv.setText(user.email);
            if (user.avatar != null)
                user.avatar = user.avatar.trim();
            if (!TextUtils.isEmpty(user.avatar)) {
                Glide.with(this).load(user.avatar).diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar_iv);
            }
        }

    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    public boolean saveBitmap(File f,Bitmap mBitmap){
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try
        {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
