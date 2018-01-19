package com.hugboga.custom.data.bean.ai;

import java.util.List;

/**
 * Created by HONGBO on 2017/12/5 15:02.
 */

public class AiRequestInfo {
    public String customServiceStatus;
    public String customServiceId;
    public String serviceTypeId;
    public String destinationId;
    public String destinationName;
    public String destinationType;
    public String destinationCountryId;
    public String destinationCountryName;
    public String guideCount;
    public String durationOptId;
    public String accompanyOptId;
    public String userWant;
    public String distinctId;
    public List<UserSaid> regardsList;
    public List<UserSaid> userSaidList;
    public String questionId;
    public String askDuoDuoSessionID;

    public void setData(FakeAIQuestionsBean data) {
        if (data.chooseServiceTypeId != null) {
            serviceTypeId = data.chooseServiceTypeId;
        }
        if (data.regardsList != null) {
            regardsList = data.regardsList;
        }
        if (data.chooseDestinationId != null) {
            destinationId = data.chooseDestinationId;
        }
        if (data.chooseDestinationType != null) {
            destinationType = data.chooseDestinationType;
        }
        if (data.chooseDurationId != null) {
            durationOptId = data.chooseDurationId;
        }
        if (data.chooseAccompanyId != null) {
            accompanyOptId = data.chooseAccompanyId;
        }
        if (data.customServiceId != null) {
            customServiceId = data.customServiceId;
        }
        if (data.chooseDestinationCountryId != null) {
            destinationCountryId = data.chooseDestinationCountryId;
        }
        if (data.chooseDestinationCountryName != null) {
            destinationCountryName = data.chooseDestinationCountryName;
        }

        if (data.chooseDestinationType != null) {
            destinationType = data.chooseDestinationType;
        }
        if (data.userSaidList != null) {
            userSaidList = data.userSaidList;

        }
    }
}
