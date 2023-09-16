package com.hugo.service;

import com.hugo.dto.ShoppingCartDTO;
import com.hugo.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> list();

    void delete(ShoppingCartDTO shoppingCartDTO);

    void clean();
}
