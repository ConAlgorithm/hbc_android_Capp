package com.hugboga.custom.data.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/1.
 */

public class FakeAIBean {
    String greetingNext;
    String greetingCur;

    ArrayList<hotDestinationList> hotDestinationList;
    public class hotDestinationList{
        int  destinationId;
        String destinationImageUrl;
        String destinationName;
        int destinationType;
        int guideCount;
    }

    @Override
    public String toString() {
        return  greetingCur+hotDestinationList.get(2).destinationName;
    }
}
