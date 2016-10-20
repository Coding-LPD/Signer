package com.scnu.zhou.signer.component.util.image;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.scnu.zhou.signer.component.config.AppConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImagePicker {

	private ImagePicker(){}

	private static class ImagePickerHolder{

		private static final ImagePicker instance = new ImagePicker();
	}

	public static final ImagePicker getInstance(){

		return ImagePickerHolder.instance;
	}
	
	private String path = AppConfig.PhotoDir; // sd路径
	private String pictureName = "header.jpg";
	private Uri pictureUri;
	
	public static int STATE_CAMARE = 1;
	public static int STATE_ABLUM = 2;
	public static int STATE_CROP = 3;

	/**
	 * 从相册中选择
	 */
	public void pickFromAblum(Activity activity){
		Intent intent1 = new Intent(Intent.ACTION_PICK, null);
		intent1.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		activity.startActivityForResult(intent1, STATE_ABLUM);
	}


	/**
	 * 拍照获取
	 */
	public void pickFromCamera(Activity activity){

		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			//判断文件夹是否存在，我的文件夹路径是SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Photo_LJ/";
			File dir = new File(path);
			if(!dir.exists()){
				dir.mkdir();
			}
			pictureName = new SimpleDateFormat("yyMMddHHmmss")
					.format(new Date()) + ".jpg";
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//MediaStore.ACTION_IMAGE_CAPTURE
			Uri imageUri = Uri.fromFile(new File(path,pictureName));
			//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			activity.startActivityForResult(openCameraIntent, STATE_CAMARE);
		} else {
			Toast.makeText(activity, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
		}

/*
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdir();
		}

		pictureName = new SimpleDateFormat("yyMMddHHmmss")
				.format(new Date()) + ".jpg";


		Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri
				.fromFile(new File(dir, pictureName)));
		activity.startActivityForResult(intent2, STATE_CAMARE);// 采用ForResult打开*/
	}
	
	/**
	 * 调用系统的裁剪
	 * 
	 * @param uri
	 */
	public void cropPhoto(Activity activity, Uri uri) {
		
		pictureUri = uri;
		
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, STATE_CROP);
	}
	
	public String getPictureName(){
		return pictureName;
	}
	
	public Uri getPictureUri(){
		return pictureUri;
	}
}
