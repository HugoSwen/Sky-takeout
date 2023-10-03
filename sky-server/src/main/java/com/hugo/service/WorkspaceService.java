package com.hugo.service;

import com.hugo.vo.BusinessDataVO;
import com.hugo.vo.DishOverViewVO;
import com.hugo.vo.OrderOverViewVO;
import com.hugo.vo.SetMealOverViewVO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface WorkspaceService {
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    DishOverViewVO getDishOverView();

    SetMealOverViewVO getSetMealOverView();

    OrderOverViewVO getOrderOverView();
}
