package com.hugo.service;

import com.hugo.vo.BusinessDataVO;
import com.hugo.vo.DishOverViewVO;
import com.hugo.vo.OrderOverViewVO;
import com.hugo.vo.SetMealOverViewVO;

public interface WorkspaceService {
    BusinessDataVO getBusinessData();

    DishOverViewVO getDishOverView();

    SetMealOverViewVO getSetMealOverView();

    OrderOverViewVO getOrderOverView();
}
