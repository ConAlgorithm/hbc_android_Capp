package com.hugboga.custom.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.request.RequestTest;
import com.hugboga.custom.data.request.RequestTest2;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FgTest.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FgTest#newInstance} factory method to
 * create an instance of this fragment.
 */
@ContentView(R.layout.fragment_fg_test)
public class FgTest extends BaseFragment implements View.OnTouchListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FgTest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FgTest.
     */
    // TODO: Rename and change types and number of parameters
    public static FgTest newInstance(String param1, String param2) {
        FgTest fragment = new FgTest();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            /*throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*/
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Event(value = {R.id.fg_test_btn,R.id.fg_test_btn2},type = View.OnClickListener.class)
    private void onClickView(View view){
        LogUtil.e("onClickView "+view);
        switch (view.getId()){
            case R.id.fg_test_btn:
                requestTest();
                break;
            case R.id.fg_test_btn2:
                requestTest2();
                break;


        }
    }


    private void requestTest() {
        RequestTest request = new RequestTest();
        HttpRequestUtils.request(request, this);

    }
    private void requestTest2() {
        RequestTest2 request = new RequestTest2();
        HttpRequestUtils.request(request, this);
    }
    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if(request instanceof  RequestTest){
            RequestTest mRequest = (RequestTest)request;
          Toast.makeText(getActivity(), mRequest.getData().toString(), Toast.LENGTH_LONG).show();
        }else if(request instanceof  RequestTest2){
            RequestTest2 mRequest = (RequestTest2)request;
          Toast.makeText(getActivity(), mRequest.getData().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
