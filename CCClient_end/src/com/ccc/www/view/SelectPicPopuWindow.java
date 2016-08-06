package com.ccc.www.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.ccc.ccclient_end.R;

public class SelectPicPopuWindow extends PopupWindow {
	private Button btn_take_photo,btn_take_pic,btn_take_cancel;
	private View rootView;
	private int type;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public SelectPicPopuWindow(Activity activity,OnClickListener itemsOnClick) {
		LayoutInflater inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rootView=inflater.inflate(R.layout.popu_select_pic, null);
		btn_take_photo=(Button) rootView.findViewById(R.id.btn_take_photo);
		btn_take_pic=(Button) rootView.findViewById(R.id.btn_take_pic);
		btn_take_cancel=(Button) rootView.findViewById(R.id.btn_take_cancel);
		btn_take_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		btn_take_photo.setOnClickListener(itemsOnClick);
		btn_take_pic.setOnClickListener(itemsOnClick);
		this.setContentView(rootView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		rootView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height=rootView.findViewById(R.id.popu_layou).getTop();
				int eventY=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(eventY<height){
						dismiss();
					}
				}
				return true;
			}
		});
	}
}

