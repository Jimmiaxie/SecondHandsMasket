package com.wmj.task;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
/**
 * 选择本地或相机拍摄图片
 * @author Administrator
 *
 */
public class ChoosePhotoWay {



	public Intent takePhoto(File outputImage) {

		try {
			if (outputImage.exists()) {
				outputImage.delete();
			}

			outputImage.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Uri  imageUri = Uri.fromFile(outputImage);
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		return intent;
	}

	public Intent givePhoto(File outputImage) {
		try {
			if (outputImage.exists()) {
				outputImage.delete();
			}

			outputImage.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Uri imageUri = Uri.fromFile(outputImage);
		Intent intent = new Intent("android.intent.action.PICK");
		intent.setType("image/*");
		intent.putExtra("crop", true);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		return intent;
	}

}
