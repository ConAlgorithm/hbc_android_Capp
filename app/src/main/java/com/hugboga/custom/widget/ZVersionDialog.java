/*
 * ZProgressDialog.java [V1.0.0]
 * classes:com.zongfi.zfutil.widget.ZProgressDialog
 * ZHZEPHI Create at 2015年2月4日 上午11:37:20
 */
package com.hugboga.custom.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hugboga.custom.R;

/**
 * com.zongfi.zfutil.widget.ZProgressDialog
 * 
 * @author ZHZEPHI Create at 2015年2月4日 上午11:37:20
 */
public class ZVersionDialog extends Dialog {

	@SuppressWarnings("unused")
	private Context context;
	private ProgressBar progresBar;
	private TextView step;

	/**
	 * @param context
	 */
	public ZVersionDialog(Context context) {
		super(context, android.R.style.Theme_Material_Light_Dialog);
		this.context = context;
	}
	
	/**
	 * 
	 */
	public ZVersionDialog(Context context, int theme) {
		super(context, theme);
        this.context = context;
        
        setCancelable(false); // dialog模态
		
		// 加载布局文件
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.z_version_update_layout, null);
		progresBar = (ProgressBar) view.findViewById(R.id.version_update_progress);
		step = (TextView) view.findViewById(R.id.version_update_step);

		setContentView(view); // dialog添加视图
	}

	/**
	 * 更新进度条 Created by ZHZEPHI at 2015年4月15日 下午6:11:14
	 * 
	 * @param totle
	 * @param progress
	 */
	public void updateProgress(Long totle, Long progress) {
		progresBar.setMax(100);
		Long pro = progress*100/totle;
		progresBar.setProgress(pro.intValue());
		step.setText(String.valueOf(pro) + "%");
	}
}
