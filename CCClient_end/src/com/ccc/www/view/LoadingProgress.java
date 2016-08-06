package com.ccc.www.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ccc.ccclient_end.R;

public class LoadingProgress extends Dialog {
	private Context context = null;
	private static LoadingProgress progress = null;

	public LoadingProgress(Context context) {
		super(context);
		this.context = context;
	}

	public LoadingProgress(Context context, int theme) {
		super(context, theme);
	}

	public static LoadingProgress createDialog(Context context) {
		progress = new LoadingProgress(context, R.style.CustomProgressDialog);
		progress.setContentView(R.layout.loading_pro);
		/*
		 * View view =
		 * LayoutInflater.from(context).inflate(R.layout.loading_pro, null);
		 * progress.setContentView(view, new LinearLayout.LayoutParams(
		 * LinearLayout.LayoutParams.WRAP_CONTENT,
		 * LinearLayout.LayoutParams.WRAP_CONTENT));// 璁剧疆甯冨眬
		 */
		progress.getWindow().getAttributes().gravity = Gravity.CENTER;

		return progress;
	}
	
	
	public static LoadingProgress createRedBagDialog(Context context) {
		progress = new LoadingProgress(context, R.style.CustomProgressDialog);
		progress.setContentView(R.layout.loading_pro);
		/*
		 * View view =
		 * LayoutInflater.from(context).inflate(R.layout.loading_pro, null);
		 * progress.setContentView(view, new LinearLayout.LayoutParams(
		 * LinearLayout.LayoutParams.WRAP_CONTENT,
		 * LinearLayout.LayoutParams.WRAP_CONTENT));// 璁剧疆甯冨眬
		 */
		progress.getWindow().getAttributes().gravity = Gravity.BOTTOM;
		

		return progress;
	}

	/*
	 * public static LoadingProgress createDialogBg(Context context){ progress =
	 * new LoadingProgress(context,R.style.CustomProgressDialogBg);
	 * progress.setContentView(R.layout.loading_pro); View view =
	 * LayoutInflater.from(context).inflate(R.layout.loading_pro, null);
	 * progress.setContentView(view, new LinearLayout.LayoutParams(
	 * LinearLayout.LayoutParams.WRAP_CONTENT,
	 * LinearLayout.LayoutParams.WRAP_CONTENT));// 璁剧疆甯冨眬
	 * 
	 * progress.getWindow().getAttributes().gravity = Gravity.CENTER;
	 * 
	 * return progress; }
	 */

	public void onWindowFocusChanged(boolean hasFocus) {

		if (progress == null) {
			return;
		}

		ProgressBar imageView = (ProgressBar) progress
				.findViewById(R.id.loadingImageView);
		/*
		 * AnimationDrawable animationDrawable = (AnimationDrawable)
		 * imageView.getBackground(); animationDrawable.start();
		 */
	}

	/**
	 * 
	 * [Summary] setTitile 鏍囬�?
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public LoadingProgress setTitile(String strTitle) {
		return progress;
	}

	/**
	 * 
	 * [Summary] setMessage 鎻愮ず鍐呭
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public LoadingProgress setMessage(String strMessage) {
		TextView tvMsg = (TextView) progress
				.findViewById(R.id.id_tv_loadingmsg);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

		return progress;
	}
}
