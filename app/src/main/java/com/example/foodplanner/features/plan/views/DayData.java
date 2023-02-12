package com.example.foodplanner.features.plan.views;

import com.example.foodplanner.features.common.models.MealItem;

import java.util.List;

public class DayData {
    public String getParentItemTitle() {
        return ParentItemTitle;
    }

    public void setParentItemTitle(String parentItemTitle) {
        ParentItemTitle = parentItemTitle;
    }

    private String ParentItemTitle;
    private List<MealItem> ChildItemList;

    public DayData(String ParentItemTitle, List<MealItem> ChildItemList) {
        this.ParentItemTitle = ParentItemTitle;
        this.ChildItemList = ChildItemList;
    }

    public List<MealItem> getChildItemList() {
        return ChildItemList;
    }

    public void setChildItemList(
            List<MealItem> childItemList) {
        ChildItemList = childItemList;
    }
}
