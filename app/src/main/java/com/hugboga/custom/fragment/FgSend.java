package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.PromiseAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.constants.ResourcesConstants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.ArrivalBean;
import com.hugboga.custom.data.bean.PromiseBean;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 送机填写行程
 */
@ContentView(R.layout.fg_send)
public class FgSend extends BaseFragment {

	@ViewInject(R.id.send_airport)
	private TextView fContent;
	@ViewInject(R.id.send_where_tip)
	private TextView wTip;
	@ViewInject(R.id.send_where_title)
	private TextView wTitle;
	@ViewInject(R.id.send_where_content)
	private TextView wContent;
	@ViewInject(R.id.send_date_time)
	private TextView sDateTime;

	@ViewInject(R.id.bottom_promise_wait)
	private TextView promiseWait;
	@ViewInject(R.id.bottom_promise_app)
	private TextView promiseApp;

	private AirPort airPortBean;//航班信息
	private ArrivalBean arrivalBean;//达到目的地
	private String serverDate;
	private String serverTime;


	protected void initView() {
		promiseWait.setVisibility(View.GONE);
		promiseApp.setVisibility(View.GONE);
	}

	@Override
	protected Callback.Cancelable requestData() {
		return null;
	}

	@Override
	public void onDataRequestSucceed(BaseRequest request) {
	}

	@Override
	protected void inflateContent() {
	}

	@Event({R.id.send_btn, R.id.send_where_layout, R.id.send_flight_layout, R.id.send_time_layout, R.id.bottom_promise_layout, R.id.submit_order_tip})
	private void onClickView(View view) {
		switch (view.getId()){
			case R.id.send_flight_layout:
//				startFragment(new FgOrder());
				startFragment(new FgChooseAirport() );
				break;
			case R.id.send_where_layout:
				if(airPortBean!=null) {
				    FgArrivalSearch fg = new FgArrivalSearch();
					Bundle bundle = new Bundle();
					bundle.putInt(FgArrivalSearch.KEY_CITY_ID, airPortBean.cityId);
					bundle.putString(FgArrivalSearch.KEY_LOCATION, airPortBean.location);
					fg.setArguments(bundle);
					startFragment(fg);
				}else{
					Toast.makeText(getActivity(), "先选择机场", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.send_time_layout:
				if(airPortBean==null) {
					Toast.makeText(getActivity(), "先选择机场", Toast.LENGTH_LONG).show();
					return;
				}
				showDaySelect();
				break;
			case R.id.bottom_promise_layout:
				showPromiseDialog();
				break;
			case R.id.send_btn:
				startFgCar();
				break;
			case R.id.submit_order_tip:
				Bundle bundle = new Bundle();
				bundle.putString(FgWebInfo.WEB_URL, ResourcesConstants.H5_NOTICE);
				startFragment(new FgWebInfo(), bundle);
				break;
		}
	}
	/**
	 * 承诺
	 */
	private void showPromiseDialog() {
		int[] types ={0,3,4};
		ArrayList<PromiseBean> list = new ArrayList<PromiseBean>();
		for(int type:types){
			list.add(Constants.PromiseMap.get(type));
		}
		PromiseAdapter adapter = new PromiseAdapter(getActivity());
		adapter.setList(list);
		new AlertDialog.Builder(getActivity())
				.setAdapter(adapter,null)
				.setCancelable(true)
				.show()
				.setCanceledOnTouchOutside(true);
	}
	private void startFgCar() {
		if(airPortBean==null){
			Toast.makeText(getActivity(), "选择机场", Toast.LENGTH_LONG).show();
			return;
		}else if(arrivalBean ==null){
			Toast.makeText(getActivity(), "选择达到目的地", Toast.LENGTH_LONG).show();
			return;
		}else if(serverDate==null){
			Toast.makeText(getActivity(), "选择服务时间", Toast.LENGTH_LONG).show();
			return;
		}
		FgCar fg = new FgCar();
		Bundle bundle = new Bundle();
		bundle.putSerializable(FgCar.KEY_AIRPORT, airPortBean);
		bundle.putSerializable(FgCar.KEY_ARRIVAL,arrivalBean);
		bundle.putString(FgCar.KEY_TIME,serverDate+" "+serverTime);
		fg.setArguments(bundle);
		startFragment(fg);
	}

	@Override
	public int getBusinessType() {
		mBusinessType = Constants.BUSINESS_TYPE_SEND;
		setGoodsType(mBusinessType);
		return mBusinessType;
	}

	@Override
	public void onFragmentResult(Bundle bundle) {
		MLog.w(this + " onFragmentResult " + bundle);
		String from = bundle.getString(KEY_FRAGMENT_NAME);
		if(FgChooseAirport.class.getSimpleName().equals(from)){
			airPortBean = (AirPort) bundle.getSerializable(FgChooseAirport.KEY_AIRPORT);
			fContent.setText(airPortBean.cityName+" "+airPortBean.airportName);
			arrivalBean =null;
			wTip.setVisibility(View.VISIBLE);
			wTitle.setVisibility(View.GONE);
			wContent.setVisibility(View.GONE);
		}else if(FgArrivalSearch.class.getSimpleName().equals(from)){
			arrivalBean = (ArrivalBean) bundle.getSerializable("arrival");
			wTip.setVisibility(View.GONE);
			wTitle.setVisibility(View.VISIBLE);
			wContent.setVisibility(View.VISIBLE);
			wTitle.setText(arrivalBean.placeName);
			wContent.setText(arrivalBean.placeDetail);
			collapseSoftInputMethod();
		}
	}

	@Override
	protected void initHeader() {
//		rightText.setVisibility(View.VISIBLE);
//		setProgressState(0);
//		fgTitle.setText(getString(R.string.title_send));
	}


	public void showDaySelect() {
		Calendar cal = Calendar.getInstance();
		MyDatePickerListener myDatePickerDialog = new MyDatePickerListener(sDateTime);
		DatePickerDialog dpd =   DatePickerDialog.newInstance(
				myDatePickerDialog, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		cal = Calendar.getInstance();
		dpd.setMinDate(cal);
		cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
		dpd.setMaxDate(cal);
		dpd.show(this.getActivity().getFragmentManager(), "DatePickerDialog");   //显示日期设置对话框

	}
	/*
     * Function  :       自定义MyDatePickerDialog类，用于实现DatePickerDialog.OnDateSetListener接口，
     *                           当点击日期设置对话框中的“设置”按钮时触发该接口方法
     */
	class MyDatePickerListener implements DatePickerDialog.OnDateSetListener {
		TextView mTextView;
		MyDatePickerListener(TextView textView){
			this.mTextView = textView;
		}
		@Override
		public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
			int month = monthOfYear+1;
			String monthStr = String.format("%02d", month);
			String dayOfMonthStr = String.format("%02d", dayOfMonth);
			serverDate = year+"-"+monthStr+"-"+dayOfMonthStr;
			showTimeSelect();
		}
	}
	public void showTimeSelect() {
		Calendar cal = Calendar.getInstance();
		MyTimePickerDialogListener myTimePickerDialog = new MyTimePickerDialogListener();
		TimePickerDialog datePickerDialog = TimePickerDialog.newInstance(myTimePickerDialog, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true);
		datePickerDialog.show(this.getActivity().getFragmentManager(), "TimePickerDialog");                //显示日期设置对话框
	}
	/*
         * Function  :       自定义MyDatePickerDialog类，用于实现DatePickerDialog.OnDateSetListener接口，
         *                           当点击日期设置对话框中的“设置”按钮时触发该接口方法
         */
	class MyTimePickerDialogListener implements TimePickerDialog.OnTimeSetListener {
		@Override
		public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
			String hour = String.format("%02d", hourOfDay);
			String minuteStr = String.format("%02d", minute);
			serverTime = hour+":"+minuteStr;
			sDateTime.setText(serverDate+" "+serverTime);
		}
	}
}
