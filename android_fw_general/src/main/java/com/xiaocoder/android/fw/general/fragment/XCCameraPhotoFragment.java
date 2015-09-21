package com.xiaocoder.android.fw.general.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.base.XCBaseFragment;
import com.xiaocoder.android.fw.general.util.UtilDate;
import com.xiaocoder.android.fw.general.util.UtilOom;
import com.xiaocoder.android_fw_general.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author xiaocoder
 * @Description:
 * @date 2015-1-6 下午10:50:22
 */
public class XCCameraPhotoFragment extends XCBaseFragment {
    ImageView xc_id_photo_camera_imageview;
    public static final int CAMERA_REQUEST_CODE = 0;// 打开当地相册的请求码
    public static final int RESIZE_REQUEST_CODE = 1;// 裁剪的请求码

    public File temp_photo_file;

    public boolean is_allow_resize; // 是否允许裁剪图片，默认为不允许
    public int image_id;

    public interface OnCaremaSelectedFileListener {
        void onCaremaSelectedFile(File file);
    }

    OnCaremaSelectedFileListener listener;

    public void setOnCaremaSelectedFileListener(OnCaremaSelectedFileListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return init(inflater, R.layout.xc_l_fragment_photo_camera);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.xc_id_fragment_photo_camera_imageview) {
            getTakePhoto();
        }
    }

    public void getTakePhoto() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            temp_photo_file = new File(Environment.getExternalStorageDirectory(), UUID.randomUUID().toString());
            if (!temp_photo_file.exists()) {
                try {
                    temp_photo_file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    XCApp.shortToast("创建文件失败");
                    return;
                }
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp_photo_file));
            cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } else {
            XCApp.shortToast("请插入sd卡");
        }
    }

    public void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        if (temp_photo_file != null && temp_photo_file.exists()) {
                            final Uri uri = Uri.fromFile(temp_photo_file);
                            if (is_allow_resize) {
                                resizeImage(uri);
                            } else {

                                XCApp.getBase_cache_threadpool().execute(new Runnable() {
                                    Bitmap bitmap;

                                    @Override
                                    public void run() {
                                        bitmap = UtilOom.getBitmapForLargeByUri(getActivity(), uri, 500, Bitmap.Config.RGB_565);

                                        XCApp.getBase_handler().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                getImage(bitmap);
                                                if (bitmap != null) {
                                                    bitmap.recycle();
                                                    bitmap = null;
                                                }
                                            }
                                        });
                                    }
                                });

                            }
                        } else {
                            XCApp.shortToast("获取图片失败");
                        }
                    } else {
                        XCApp.shortToast("未找到存储卡，无法存储照片！");
                    }
                    break;

                case RESIZE_REQUEST_CODE:
                    // 裁剪之后,关闭裁剪的activity后,会调用这个方法,
                    if (data != null) { // 加入data不等于空,即可以去到bitmap
                        getResizeImage(data);
                    }
                    break;
            }
        }
    }

    // 从这里获取最后返回的图片
    private void getImage(Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                if (temp_photo_file != null && temp_photo_file.exists()) {
                    temp_photo_file.delete();
                }
                File file = new File(createDir(), "photo" + getTime() + ".jpg");
                fos = new FileOutputStream(file);
//                bitmap = Bitmap.createScaledBitmap(bitmap, 700, 700, true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
                if (listener != null) {
                    listener.onCaremaSelectedFile(file);
                }
            } else {
                XCApp.shortToast("未检测到SD卡");
                if (listener != null) {
                    listener.onCaremaSelectedFile(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    if (null != bitmap) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            // 保存到本地
            FileOutputStream fos = null;
            try {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    if (temp_photo_file != null && temp_photo_file.exists()) {
                        temp_photo_file.delete();
                    }
                    File file = new File(createDir(), "photo" + getTime() + ".jpg");
                    fos = new FileOutputStream(file);
//                    bitmap = Bitmap.createScaledBitmap(bitmap, 700, 700, true);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();
                    if (listener != null) {
                        listener.onCaremaSelectedFile(file);
                    }
                } else {
                    XCApp.shortToast("未检测到SD卡");
                    if (listener != null) {
                        listener.onCaremaSelectedFile(null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                        if (null != bitmap) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String save_photo_dir = ""; // XCConfig.CHAT_PHOTO_FILE

    public void setSave_photo_dir(String save_photo_dir) {
        this.save_photo_dir = save_photo_dir;
    }

    public File createDir() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/" + save_photo_dir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public String getTime() {
        return UtilDate.format(new Date(), UtilDate.FORMAT_FULL_S);
    }

    public void setImage(int drawable_id) {
        this.image_id = drawable_id;
        if (xc_id_photo_camera_imageview != null) {
            xc_id_photo_camera_imageview.setImageResource(drawable_id);
        }
    }

    public void setIsAllowResizeImage(boolean is_allow_resize) {
        this.is_allow_resize = is_allow_resize;
    }

    @Override
    public void initWidgets() {
        xc_id_photo_camera_imageview = getViewById(R.id.xc_id_fragment_photo_camera_imageview);
        if (image_id > 0) {
            xc_id_photo_camera_imageview.setImageResource(image_id);
        }
    }

    @Override
    public void listeners() {
        xc_id_photo_camera_imageview.setOnClickListener(this);
    }
}
