package com.hugboga.custom.adapter;

import android.content.Context;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.models.SearchDestinationModel;
import com.hugboga.custom.models.SearchMoreModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 17/8/22.
 */

public class SearchAdapter extends EpoxyAdapter {

    SearchDestinationModel searchDestinationModel;
    SearchMoreModel searchMoreModel;
    List<SearchDestinationModel> searchDestinationModels = new ArrayList<>();

    public void addSearchDestinationModel(Context context,List<SearchGroupBean> list,String keyword) {
        if(searchDestinationModels!= null && searchDestinationModels.size()>0){
            searchDestinationModels.clear();
        }
        if(list!= null && list.size() >0){
            for(int i = 0; i<list.size() ; i++){
                searchDestinationModel = new SearchDestinationModel(context,list.get(i),keyword);
                searchDestinationModels.add(searchDestinationModel);
            }
            addModels(searchDestinationModels);
        }
        String searchMoreString = "搜索更多关于“" + keyword + "”的结果";
        searchMoreModel = new SearchMoreModel(context,searchMoreString,list,keyword);
        addModel(searchMoreModel);
    }


    public void removeModels(){
        if(getItemCount() > 0){
            removeAllModels();
        }
    }
}
