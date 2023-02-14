package com.example.foodplanner.features.plan.models;

import com.example.foodplanner.features.common.models.MealItem;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayData dayData = (DayData) o;
        return ParentItemTitle.equals(dayData.ParentItemTitle) && ChildItemList.equals(dayData.ChildItemList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ParentItemTitle, ChildItemList);
    }

    public List<MealItem> getChildItemList() {
        return ChildItemList;
    }

    public void setChildItemList(
            List<MealItem> childItemList) {
        ChildItemList = childItemList;
    }
}
