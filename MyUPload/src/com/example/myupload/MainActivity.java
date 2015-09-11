package com.example.myupload;

import java.io.File;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	public String URL_UploadImage = "http://www.xmqq88.com:8888/private_upload/";
	public String URL_loadImage = "http://www.xmqq88.com:8888/private_download/";

	protected static final int TO_UPLOAD_FILE = 1;
	protected static final int UPLOAD_FILE_DONE = 2; //
	public static final int TO_SELECT_PHOTO = 3;
	private static final String TAG = "xmqqtest";

	Button selectImage, test_upload_btn, download_btn;
	TextView result_txt;
	ImageView img;
	
	String filename;
	String picPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		selectImage = (Button) this.findViewById(R.id.selectImage);
		selectImage.setOnClickListener(this);

		img = (ImageView) findViewById(R.id.img);
		result_txt = (TextView) findViewById(R.id.uploadImageResult);
		download_btn = (Button) findViewById(R.id.download_btn);
		download_btn.setOnClickListener(this);

		test_upload_btn = (Button) findViewById(R.id.test_upload_btn);
		test_upload_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectImage:
			Intent intent = new Intent(this, SelectPicActivity.class);
			startActivityForResult(intent, TO_SELECT_PHOTO);
			break;
		case R.id.test_upload_btn:
			if(null != filename && !"".equals(filename.trim())){
				uploadMethod();
			}
			break;
		case R.id.download_btn:
			downLoadMethod();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO) {
			picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
			// picPath = "/storage/sdcard0/DCIM/Camera/IMG_20150822_131512.jpg";
			// /storage/sdcard0/DCIM/Camera/IMG_20150822_131512.jpg
			File fileName = new File(picPath);
			filename  = fileName.getName();
			Log.d(TAG, "最终选择的图片=" + filename);
			//Bitmap bm = BitmapFactory.decodeFile(picPath);
			//img.setImageBitmap(bm);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void downLoadMethod() {
		// TODO Auto-generated method stub
		BitmapUtils bitmapUtils = new BitmapUtils(this);

		Log.d(TAG, ">>>>>>>>>>>path = " + URL_loadImage + filename);
		bitmapUtils.display(img, URL_loadImage + "guoxiaojun/" + filename);

	}

	private void uploadMethod() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", "guoxiaojun/"+filename);
		params.addBodyParameter("Content-Type", "image/jpeg");
		File file = new File(picPath);

		Log.d(TAG, ">>>>" + file.exists());

		params.addBodyParameter("file", file);

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, URL_UploadImage, params,
				new RequestCallBack<String>() {
			@Override
			public void onStart() {
				result_txt.setText("conn...");
			}

			@Override
			public void onLoading(long total, long current,
					boolean isUploading) {
				if (isUploading) {
					result_txt.setText("upload: " + current + "/"
							+ total);
				} else {
					result_txt.setText("reply: " + current + "/"
							+ total);
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				result_txt.setText("reply:  上传成功" );
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				result_txt.setText(error.getExceptionCode() + ":->"
						+ msg);
			}
		});

	}

}
