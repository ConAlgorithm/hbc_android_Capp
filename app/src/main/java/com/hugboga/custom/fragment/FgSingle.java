package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.constants.ResourcesConstants;
import com.hugboga.custom.data.bean.ArrivalBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.PromiseBean;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 次租填写行程
 */

@ContentView(R.layout.fg_single)
public class FgSingle extends BaseFragment {

	@ViewInject(R.id.send_airport)
	private TextView fContent;
	@ViewInject(R.id.send_where_tip)
	private TextView wTip;
	@ViewInject(R.id.send_where_title)
	private TextView wTitle;
	@ViewInject(R.id.send_where_content)
	private TextView wContent;
	@ViewInject(R.id.send_to_tip)
	private TextView tTip;
	@ViewInject(R.id.send_to_title)
	private TextView tTitle;
	@ViewInject(R.id.send_to_content)
	private TextView tContent;
	@ViewInject(R.id.send_date_time)
	private TextView sDateTime;

	private CityBean cityBean;//城市信息
	private ArrivalBean startBean;//起始地
	private ArrivalBean arrivalBean;//达到目的地
	private String serverDate;
	private String serverTime;

	/*@ViewInject(R.id.bottom_promise_wait)
	private TextView promiseWait;
	@ViewInject(R.id.bottom_promise_app)
	private TextView promiseApp;
*/


	@Override
	protected void initHeader() {
//		rightText.setVisibility(View.VISIBLE);
		setProgressState(0);
	}


	protected void initView() {
//		promiseWait.setVisibility(View.GONE);
//		promiseApp.setVisibility(View.GONE);
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

	@Event({R.id.send_btn, R.id.send_where_layout, R.id.send_to_layout, R.id.send_flight_layout, R.id.send_time_layout,
//			R.id.bottom_promise_layout,
			R.id.submit_order_tip})
	private void onClickView(View view) {
		switch (view.getId()){
			case R.id.send_flight_layout:
				startFragment(new FgChooseCity() );
				break;
			case R.id.send_where_layout:
				if(cityBean!=null) {
				   /* FgArrivalSearch fg = new FgArrivalSearch();
					Bundle bundle = new Bundle();
					bundle.putString(KEY_FROM, "from");
					bundle.putInt(FgArrivalSearch.KEY_CITY_ID, cityBean.cityId);
					bundle.putString(FgArrivalSearch.KEY_LOCATION, cityBean.location);
					startFragment(fg,bundle);*/
				}else{
					Toast.makeText(getActivity(), "先选择城市", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.send_to_layout:
				if(cityBean!=null) {
				  /*  FgArrivalSearch fg = new FgArrivalSearch();
					Bundle bundle = new Bundle();
					bundle.putString(KEY_FROM, "to");
					bundle.putInt(FgArrivalSearch.KEY_CITY_ID, cityBean.cityId);
					bundle.putString(FgArrivalSearch.KEY_LOCATION, cityBean.location);
					startFragment(fg,bundle);*/
				}else{
					Toast.makeText(getActivity(), "先选择城市", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.send_time_layout:
				if(startBean==null) {
					Toast.makeText(getActivity(), "请先选择城市", Toast.LENGTH_LONG).show();
					return ;
				}
				showDaySelect();
				break;
			/*case R.id.bottom_promise_layout:
				showPromiseDialog();
				break;*/
			case R.id.send_btn:
				startFgCar();
				break;
			case R.id.submit_order_tip:
				/*Bundle bundle = new Bundle();
				bundle.putString(FgWebInfo.Web_URL, ResourcesConstants.H5_NOTICE);
				startFragment(new FgWebInfo(),bundle);*/
				break;
		}
	}
	/**
	 * 承诺
	 */
/*	private void showPromiseDialog() {
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
	}*/
	private void startFgCar() {
		if(cityBean==null){
			Toast.makeText(getActivity(), "选择用车城市", Toast.LENGTH_LONG).show();
			return;
		}else if(startBean ==null){
			Toast.makeText(getActivity(), "选择起始目的地", Toast.LENGTH_LONG).show();
			return;
		}else if(arrivalBean ==null){
			Toast.makeText(getActivity(), "选择达到目的地", Toast.LENGTH_LONG).show();
			return;
		}else if(serverDate==null||serverTime==null){
			Toast.makeText(getActivity(), "选择服务日期", Toast.LENGTH_LONG).show();
			return;
		}
		/*FgCar fg = new FgCar();
		Bundle bundle = new Bundle();
		bundle.putSerializable(FgCar.KEY_CITY, cityBean);
		bundle.putSerializable(FgCar.KEY_START,startBean);
		bundle.putSerializable(FgCar.KEY_ARRIVAL,arrivalBean);
		bundle.putString(FgCar.KEY_TIME,serverDate+" "+serverTime);
		fg.setArguments(bundle);
		startFragment(fg);*/
	}

	@Override
	public int getBusinessType() {
		mBusinessType = Constants.BUSINESS_TYPE_RENT;
		setGoodsType(mBusinessType);
		return mBusinessType;
	}

	@Override
	public void onFragmentResult(Bundle bundle) {
		MLog.w(this + " onFragmentResult " + bundle);
		String from = bundle.getString(KEY_FRAGMENT_NAME);
		if(FgChooseCity.class.getSimpleName().equals(from)){
			cityBean = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);
			fContent.setText(cityBean.name);
			startBean= null;
			arrivalBean = null;
			wTip.setVisibility(View.VISIBLE);
			wTitle.setVisibility(View.GONE);
			wContent.setVisibility(View.GONE);
			wTitle.setText("");
			wContent.setText("");
			tTip.setVisibility(View.VISIBLE);
			tTitle.setVisibility(View.GONE);
			tContent.setVisibility(View.GONE);
			tTitle.setText("");
			tContent.setText("");
		/*}else if(FgArrivalSearch.class.getSimpleName().equals(from)){
			String fromKey = bundle.getString(KEY_FROM);
			if("from".equals(fromKey)){
				startBean = (ArrivalBean) bundle.getSerializable(FgArrivalSearch.KEY_ARRIVAL);
				wTip.setVisibility(View.GONE);
				wTitle.setVisibility(View.VISIBLE);
				wContent.setVisibility(View.VISIBLE);
				wTitle.setText(startBean.placeName);
				wContent.setText(startBean.placeDetail);
			}else if("to".equals(fromKey)){
				arrivalBean = (ArrivalBean) bundle.getSerializable(FgArrivalSearch.KEY_ARRIVAL);
				tTip.setVisibility(View.GONE);
				tTitle.setVisibility(View.VISIBLE);
				tContent.setVisibility(View.VISIBLE);
				tTitle.setText(arrivalBean.placeName);
				tContent.setText(arrivalBean.placeDetail);
			}
			collapseSoftInputMethod();*/
		}
	}


	public void showDaySelect() {
		/*Calendar cal = Calendar.getInstance();
		MyDatePickerListener myDatePickerDialog = new MyDatePickerListener(sDateTime);
		DatePickerDialog dpd =   DatePickerDialog.newInstance(
				myDatePickerDialog, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		cal = Calendar.getInstance();
		dpd.setMinDate(cal);
		cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
		dpd.setMaxDate(cal);
		dpd.show(this.getActivity().getFragmentManager(), "DatePickerDialog");   //显示日期设置对话框*/

	}
	/*
     * Function  :       自定义MyDatePickerDialog类，用于实现DatePickerDialog.OnDateSetListener接口，
     *                           当点击日期设置对话框中的“设置”按钮时触发该接口方法
     */
	/*class MyDatePickerListener implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
		TextView mTextView;
		MyDatePickerListener(TextView textView){
			this.mTextView = textView;
		}
		@Override
		public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
			int month = monthOfYear+1;
			String monthStr = String.format("%02d", month);
			String dayOfMonthStr = String.format("%02d", dayOfMonth);
			serverDate = year+"-"+monthStr+"-"+dayOfMonthStr;
			showTimeSelect();
		}
	}*/
	/*public void showTimeSelect() {
		Calendar cal = Calendar.getInstance();
		MyTimePickerDialogListener myTimePickerDialog = new MyTimePickerDialogListener();
		com.wdullaer.materialdatetimepicker.time.TimePickerDialog datePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(myTimePickerDialog, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
		datePickerDialog.show(this.getActivity().getFragmentManager(), "TimePickerDialog");                //显示日期设置对话框
	}*/
	/*
         * Function  :       自定义MyDatePickerDialog类，用于实现DatePickerDialog.OnDateSetListener接口，
         *                           当点击日期设置对话框中的“设置”按钮时触发该接口方法
         */
	/*class MyTimePickerDialogListener implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {


		@Override
		public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
			String hour = String.format("%02d", hourOfDay);
			String minuteStr = String.format("%02d", minute);
			serverTime = hour+":"+minuteStr;
			sDateTime.setText(serverDate+" "+serverTime);
		}
	}*/

}
