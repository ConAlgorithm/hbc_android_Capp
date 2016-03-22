package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Calendar;

/**
 * 接机填写选择航班
 */
public class FgPickFlight extends BaseFragment {

	public static final String KEY_AIRPORT = "key_airport";
	
	@ViewInject(R.id.pick_flight_no)
	private TextView flightNo;
	@ViewInject(R.id.pick_flight_time)
	private TextView time1;
	@ViewInject(R.id.pick_flight_from)
	private TextView cityFrom;
	@ViewInject(R.id.pick_flight_to)
	private TextView cityTo;
	@ViewInject(R.id.pick_flight_time2)
	private TextView time2;

	private int cityFromId = -1;//起始城市ID
	private int cityToId = -1;//到达城市ID

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg_pick_flight, null);
		view.findViewById(R.id.pick_btn).setBackgroundResource(Constants.BtnBg.get(mBusinessType));
		view.findViewById(R.id.pick_btn2).setBackgroundResource(Constants.BtnBg.get(mBusinessType));
		return view;
	}

	@Override
	protected int getBusinessType() {
		if(mBusinessType==-1)
		mBusinessType = Constants.BUSINESS_TYPE_PICK;
		setGoodsType(mBusinessType);
		return mBusinessType;
	}

	@Override
	public void onDataRequestSucceed(BaseRequest request) {

	}
	@Override
	protected void inflateContent() {
	}

	@Event({R.id.pick_btn,
			R.id.pick_btn2,
			R.id.flight_layout_1,
			R.id.flight_layout_2,
			R.id.pick_flight_from,
			R.id.pick_flight_to,
			R.id.pick_flight_time,
			R.id.pick_flight_time2,
	})
	private void onClickView(View view) {
		switch (view.getId()){
			case R.id.flight_layout_1:
				collapseSoftInputMethod();
				selectTap(0);
				break;
			case R.id.flight_layout_2:
				collapseSoftInputMethod();
				selectTap(1);
				break;
			case R.id.pick_btn:
				startFlightByNo();
				break;
			case R.id.pick_btn2:
				startFlightByCity();
				break;
			case R.id.pick_flight_from:{
				FgChooseCity city = new FgChooseCity();
				Bundle bundle = new Bundle();
				bundle.putString(KEY_FROM,"startAddress");
				city.setArguments(bundle);
				startFragment(city);
				break;
			}
			case R.id.pick_flight_to:{
				FgChooseCity city = new FgChooseCity();
				Bundle bundle = new Bundle();
				bundle.putString(KEY_FROM,"end");
				city.setArguments(bundle);
				startFragment(city);
				break;
			}
			case R.id.pick_flight_time:
				showDaySelect(time1);
				break;
			case R.id.pick_flight_time2:
				showDaySelect(time2);
				break;

		}
	}


	private void selectTap(int index){
		View view =getView();
		TextView text1 = (TextView) view.findViewById(R.id.flight_tap_1);
		TextView text2 = (TextView) view.findViewById(R.id.flight_tap_2);
		View layout1 = view.findViewById(R.id.flight_content1);
		View layout2 = view.findViewById(R.id.flight_content2);
		if(index==1){
			view.findViewById(R.id.flight_tap_line1).setVisibility(View.GONE);
			view.findViewById(R.id.flight_tap_line2).setVisibility(View.VISIBLE);
			text1.setTextColor(getResources().getColor(R.color.my_content_btn_color));
			text2.setTextColor(getResources().getColor(R.color.basic_black));
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);
		}else{
			view.findViewById(R.id.flight_tap_line1).setVisibility(View.VISIBLE);
			view.findViewById(R.id.flight_tap_line2).setVisibility(View.GONE);
			text1.setTextColor(getResources().getColor(R.color.basic_black));
			text2.setTextColor(getResources().getColor(R.color.my_content_btn_color));
			layout1.setVisibility(View.VISIBLE);
			layout2.setVisibility(View.GONE);
		}
	}


	@Override
	public void onFragmentResult(Bundle bundle) {
		MLog.w(this + " onFragmentResult " + bundle);
		String from = bundle.getString(KEY_FROM);
		if("startAddress".equals(from)){
			CityBean city = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);
			if(city!=null){
				cityFrom.setText(city.name);
				cityFromId = city.cityId;
			}
		}else if("end".equals(from)){
			CityBean city = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);
			if(city!=null){
				cityTo.setText(city.name);
				cityToId = city.cityId;
			}
		}else if("FlightList".equals(from)){
			finishForResult(bundle);
		}
	}

	@Override
	protected void initHeader() {
		setProgressState(0);
		selectTap(0);
		fgTitle.setText(getString(R.string.title_pick_flight));
	}

	@Override
	protected void initView() {

	}

	@Override
	protected Callback.Cancelable requestData() {
		return null;
	}

	/**
	 * 根据航班查
	 */
	private void startFlightByNo(){
		String noStr =flightNo.getText().toString();
		String time1Str =time1.getText().toString();
		if(TextUtils.isEmpty(noStr)){
			Toast.makeText(getActivity(), "请填写航班号", Toast.LENGTH_LONG).show();
			return ;
		}
		if(TextUtils.isEmpty(time1Str)){
			Toast.makeText(getActivity(), "请选择航班时间", Toast.LENGTH_LONG).show();
			return ;
		}

		FgPickFlightList fragment = new FgPickFlightList();
		Bundle bundle =new Bundle();
		bundle.putString(FgPickFlightList.KEY_FLIGHT_NO,noStr.toUpperCase());
		bundle.putString(FgPickFlightList.KEY_FLIGHT_DATE,time1Str);
		bundle.putInt(FgPickFlightList.KEY_FLIGHT_TYPE, 1);
		fragment.setArguments(bundle);
		startFragment(fragment);
	}
	/**
	 * 根据城市查
	 */
	private void startFlightByCity() {
		String time2Str =time2.getText().toString();
		if(cityFromId==-1){
			Toast.makeText(getActivity(), "请选择起始城市", Toast.LENGTH_LONG).show();
			return ;
		}
		if(cityToId==-1){
			Toast.makeText(getActivity(), "请选择到达城市", Toast.LENGTH_LONG).show();
			return ;
		}
		if(TextUtils.isEmpty(time2Str)){
			Toast.makeText(getActivity(), "请选择航班时间", Toast.LENGTH_LONG).show();
			return ;
		}
		FgPickFlightList fragment = new FgPickFlightList();
		Bundle bundle =new Bundle();
		bundle.putInt(FgPickFlightList.KEY_FLIGHT_FROM, cityFromId);
		bundle.putInt(FgPickFlightList.KEY_FLIGHT_TO, cityToId);
		bundle.putInt(FgPickFlightList.KEY_FLIGHT_TYPE, 2);
		bundle.putString(FgPickFlightList.KEY_FLIGHT_DATE,time2Str);
		fragment.setArguments(bundle);
		startFragment(fragment);
	}

	public void showDaySelect( TextView textView) {
		Calendar cal = Calendar.getInstance();
		DatePickerDialog dpd = DatePickerDialog.newInstance(
				new MyDatePickerListener(textView),
				cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH)
		);
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
			String serverDate = year + "-" + monthStr + "-" + dayOfMonthStr;
			mTextView.setText(serverDate);
		}
	}

}
