package com.hugboga.custom.adapter;

import android.app.Activity;
import android.content.Context;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;
import com.hugboga.custom.data.bean.city.ServiceConfigVo;
import com.hugboga.custom.models.city.CityWhatModel;
import com.hugboga.custom.models.ai.AiResultBannerModel;
import com.hugboga.custom.models.ai.AiResultSkuMoreModel;
import com.hugboga.custom.models.ai.AiResultTitleModel;

import java.util.List;

/**
 * Ai adapter
 * Created by HONGBO on 2017/12/5 16:25.
 */

public class AiResultAdapter extends SkuAdapter {

    public AiResultAdapter(Context mContext) {
        super(mContext);
    }

    public void showAiResult(DestinationHomeVo vo, UnicornServiceActivity.Params params, String destinationTitle) {
        this.data = vo;
        //快速了解目的地,添加广告条，新手指引
        if (vo.beginnerDirection != null) {
            addModel(new AiResultTitleModel(mContext.getString(R.string.ai_result_banner_title_fast)));
            addModel(new AiResultBannerModel((Activity) mContext, vo.beginnerDirection));
        }
        // 按照您的行程旅行小管家建议,添加推荐玩法结果
        if (vo.destinationGoodsList != null && vo.destinationGoodsList.size() > 0) {
            addModel(new AiResultTitleModel(destinationTitle == null ? mContext.getString(R.string.ai_result_banner_title_sku) : destinationTitle));
            addGoods(vo.destinationGoodsList);
            // 添加查看全部玩法Banner
            addModel(new AiResultSkuMoreModel(mContext, vo));
        }
        // 添加您也可以自己包车畅玩Banner,添加主动下单入口
        if (vo.serviceConfigList != null && vo.serviceConfigList.size() > 0) {
            addModel(new AiResultTitleModel(mContext.getString(isHaveDaily(vo.serviceConfigList) ? R.string.ai_result_banner_title_do :
                    R.string.ai_result_banner_title_do2)));
            addConfig(vo.serviceConfigList);
        }
        // 咨询旅行小管家确认行程
        addModel(new AiResultTitleModel(mContext.getString(R.string.ai_result_banner_title_call)));
        //添加旅行小管家入口
        CityWhatModel cityWhatModel = new CityWhatModel(mContext);
        if (params != null) {
            cityWhatModel.setParams(params);
        }
        addModel(cityWhatModel);
    }

    /**
     * 搜索是否结果中有包车
     *
     * @param configs
     * @return
     */
    private boolean isHaveDaily(List<ServiceConfigVo> configs) {
        for (ServiceConfigVo vo : configs) {
            if (vo.serviceType == 3) {
                return true;
            }
        }
        return false;
    }
}
