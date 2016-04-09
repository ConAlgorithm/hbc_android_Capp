package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.FlightBean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/22.
 */
public class ParserFlightBean extends ImplParser {
    @Override
    public FlightBean parseObject(JSONObject jsonObj) throws Exception {
        FlightBean flightBean = new FlightBean();
        flightBean.flightNo = jsonObj.optString("flightNo");
        flightBean.company = jsonObj.optString("company");
        flightBean.depTime = jsonObj.optString("depTime");
        flightBean.arrivalTime = jsonObj.optString("arrTime");
        flightBean.depAirportCode = jsonObj.optString("depAirportCode");
        flightBean.arrivalAirportCode = jsonObj.optString("arrAirportCode");
        flightBean.depAirportName = jsonObj.optString("depAirport");
        flightBean.arrAirportName = jsonObj.optString("arrAirport");
        flightBean.depTerminal = jsonObj.optString("depTerminal");
        flightBean.arrTerminal = jsonObj.optString("arrTerminal");
        flightBean.arrDate = jsonObj.optString("arrDate");
        flightBean.depDate = jsonObj.optString("depDate");
        flightBean.depCityName = jsonObj.optString("depCityName");
        flightBean.arrCityName = jsonObj.optString("arrCityName");
        return flightBean;
    }
}
