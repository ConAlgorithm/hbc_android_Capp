package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 17/6/22.
 */
public class FilterGuideOptionsBean implements Serializable{

    public ArrayList<GuideLanguage> guideForeignLangs;
    public ArrayList<GuideLanguage> guideLocalLangs;
    public ArrayList<GuideSkillLabel> guideSkillLabels;
    public int numOfPerson;


    public static class GuideLanguage extends FilterItemBase{
        public String cnName;
        public int langCode;

        @Override
        public String getTagId() {
            return "" + langCode;
        }

        @Override
        public String getName() {
            return cnName;
        }

        @Override
        public Object clone() {
            GuideLanguage guideLanguage = null;
            try {
                guideLanguage = (GuideLanguage) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return guideLanguage;
        }
    }

    public static class GuideSkillLabel extends FilterItemBase{
        public String labelName;
        public int labelId;

        @Override
        public String getTagId() {
            return "" + labelId;
        }

        @Override
        public String getName() {
            return labelName;
        }

        @Override
        public Object clone() {
            GuideSkillLabel guideSkillLabel = null;
            try {
                guideSkillLabel = (GuideSkillLabel) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return guideSkillLabel;
        }
    }
}
