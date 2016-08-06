package com.ccc.www.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ccc.www.view.XListView;

public class XListViewUtil {

	public static void endload(XListView xlistview) {
		xlistview.stopLoadMore();
		xlistview.stopRefresh();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		String time = dateFormat.format(new Date().getTime());
		xlistview.setRefreshTime(time);
	}

}
