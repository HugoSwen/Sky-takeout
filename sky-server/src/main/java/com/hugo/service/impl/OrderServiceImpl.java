package com.hugo.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hugo.constant.MessageConstant;
import com.hugo.constant.OrdersConstant;
import com.hugo.context.BaseContext;
import com.hugo.dto.*;
import com.hugo.entity.*;
import com.hugo.exception.AddressBookBusinessException;
import com.hugo.exception.OrderBusinessException;
import com.hugo.exception.ShoppingCartBusinessException;
import com.hugo.mapper.*;
import com.hugo.result.PageResult;
import com.hugo.service.OrderService;
import com.hugo.utils.AddressUtil;
import com.hugo.utils.WeChatPayUtil;
import com.hugo.vo.OrderPaymentVO;
import com.hugo.vo.OrderStatisticsVO;
import com.hugo.vo.OrderSubmitVO;
import com.hugo.vo.OrderVO;
import com.hugo.websocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private AddressUtil addressUtil;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 判断地址蒲是否存在该地址
        Long addressBookId = ordersSubmitDTO.getAddressBookId();
        AddressBook addressBook = addressBookMapper.getById(addressBookId);
        if (addressBook == null)
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);

        // 检查用户收货地址是否超出配送范围
        addressUtil.checkOutOfRange(addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());

        // 判断购物车是否有数据
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(userId);
        if (shoppingCartList == null || shoppingCartList.isEmpty())
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        // 插入一条订单数据
        Orders orders = Orders.builder()
                .number(String.valueOf(System.currentTimeMillis()))
                .status(OrdersConstant.PENDING_PAYMENT)
                .userId(userId)
                .orderTime(LocalDateTime.now())
                .payStatus(OrdersConstant.UN_PAID)
                .phone(addressBook.getPhone())
                .consignee(addressBook.getConsignee())
                .address(addressBook.getDetail())
                .build();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orderMapper.insert(orders);
        Long orderId = orders.getId();

        // 插入n条订单明细数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orderId);
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);
        // 清空购物车
        shoppingCartMapper.clean(userId);
        // 封装VO数据
        return OrderSubmitVO.builder()
                .id(orderId)
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
    }

    @Override
    public OrderPaymentVO payOrder(OrdersPaymentDTO ordersPaymentDTO) {
        /*// 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal("0.01"), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));*/

        // 判断订单是否已支付
        String orderNumber = ordersPaymentDTO.getOrderNumber();
        Orders orders = orderMapper.getByNumber(orderNumber);
        if (orders.getPayStatus() == 1)
            throw new OrderBusinessException(MessageConstant.ORDER_PAID);

        return new OrderPaymentVO();
    }

    @Override
    public void paySuccess(String orderNumber) {
        Orders order = orderMapper.getByNumber(orderNumber);

        Orders orders = Orders.builder()
                .id(order.getId())
                .status(OrdersConstant.TO_BE_CONFIRMED)
                .payStatus(OrdersConstant.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();
        orderMapper.update(orders);

        // 商家来单提醒
        Map<String, Object> map = new HashMap<>();
        map.put("type", 1);
        map.put("orderId", order.getId());
        map.put("content", "订单号：" + orderNumber);

        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
    }

    @Override
    public OrderVO getById(Long id) {
        OrderVO orderVO = new OrderVO();

        Orders orders = orderMapper.getById(id);
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(id);
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }

    @Override
    public void userCancelOrder(Long id) {
        // 根据id查询订单
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在
        if (ordersDB == null)
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);

        //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (ordersDB.getStatus() > 2)
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);

        Orders orders = Orders.builder()
                .id(id)
                .status(OrdersConstant.CANCELLED)
                .cancelReason("用户取消")
                .cancelTime(LocalDateTime.now())
                .build();

        // 订单处于待接单状态下取消，需要进行退款
        if (ordersDB.getStatus().equals(OrdersConstant.TO_BE_CONFIRMED))
            //支付状态修改为 退款
            orders.setPayStatus(OrdersConstant.REFUND);

        orderMapper.update(orders);
    }

    @Override
    public PageResult pageQuery4User(int page, int pageSize, Integer status) {
        PageHelper.startPage(page, pageSize);

        OrdersPageQueryDTO pageQueryDTO = OrdersPageQueryDTO.builder()
                .userId(BaseContext.getCurrentId())
                .status(status)
                .build();
        Page<Orders> ordersPage = orderMapper.pageQuery(pageQueryDTO);

        List<OrderVO> orderList = new ArrayList<>();
        for (Orders orders : ordersPage) {
            Long orderId = orders.getId();

            List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderId);
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO);
            orderVO.setOrderDetailList(orderDetailList);
            orderList.add(orderVO);
        }

        return new PageResult(ordersPage.getTotal(), orderList);
    }

    @Override
    public void repeatOrder(Long id) {
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

        Long userId = BaseContext.getCurrentId();
        // TODO 写法待学习
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(x -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(x, shoppingCart);
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());

        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    @Override
    public PageResult pageQuery4Admin(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<Orders> ordersPage = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> orderVOList = ordersPage.stream().map(x -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(x, orderVO);
            // 查询订单菜品详情信息（订单中的菜品和数量）
            List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(x.getId());
            List<String> detailStringList = orderDetailList.stream().map(y ->
                    y.getName() + "*" + y.getNumber() + ";").collect(Collectors.toList());

            String orderDishes = String.join("", detailStringList);
            orderVO.setOrderDishes(orderDishes);

            return orderVO;
        }).collect(Collectors.toList());

        return new PageResult(ordersPage.getTotal(), orderVOList);
    }

    @Override
    public OrderStatisticsVO getOrderStatistics() {
        Integer toBeConfirmed = orderMapper.countByStatus(OrdersConstant.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countByStatus(OrdersConstant.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countByStatus(OrdersConstant.DELIVERY_IN_PROGRESS);

        return OrderStatisticsVO.builder()
                .toBeConfirmed(toBeConfirmed)
                .confirmed(confirmed)
                .deliveryInProgress(deliveryInProgress)
                .build();
    }

    @Override
    public void confirmOrder(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(OrdersConstant.CONFIRMED)
                .build();

        orderMapper.update(orders);
    }

    @Override
    public void rejectOrder(OrdersRejectionDTO ordersRejectionDTO) {
        Orders ordersDB = orderMapper.getById(ordersRejectionDTO.getId());

        if (ordersDB == null || !ordersDB.getStatus().equals(OrdersConstant.TO_BE_CONFIRMED))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);

        Orders orders = Orders.builder()
                .id(ordersRejectionDTO.getId())
                .status(OrdersConstant.CANCELLED)
                .payStatus(OrdersConstant.REFUND)
                .rejectionReason(ordersRejectionDTO.getRejectionReason())
                .cancelTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    @Override
    public void adminCancelOrder(OrdersCancelDTO ordersCancelDTO) {
        Orders ordersDB = orderMapper.getById(ordersCancelDTO.getId());

        Orders orders = Orders.builder()
                .id(ordersCancelDTO.getId())
                .status(OrdersConstant.CANCELLED)
                .cancelReason(ordersCancelDTO.getCancelReason())
                .cancelTime(LocalDateTime.now())
                .build();

        if (ordersDB.getPayStatus().equals(OrdersConstant.PAID))
            orders.setPayStatus(OrdersConstant.REFUND);

        orderMapper.update(orders);
    }

    @Override
    public void deliveryOrder(Long id) {
        Orders orderDB = orderMapper.getById(id);

        if (orderDB == null || !orderDB.getStatus().equals(OrdersConstant.CONFIRMED))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);

        Orders orders = Orders.builder()
                .id(id)
                .status(OrdersConstant.DELIVERY_IN_PROGRESS)
                .build();

        orderMapper.update(orders);
    }

    @Override
    public void completeOrder(Long id) {
        Orders orderDB = orderMapper.getById(id);

        if (orderDB == null || !orderDB.getStatus().equals(OrdersConstant.DELIVERY_IN_PROGRESS))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);

        Orders orders = Orders.builder()
                .id(id)
                .status(OrdersConstant.COMPLETED)
                .deliveryTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    @Override
    public void remindOrder(Long id) {
        Orders orderDB = orderMapper.getById(id);

        if (orderDB == null)
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);

        // 客户催单
        Map<String, Object> map = new HashMap<>();
        map.put("type", 2);
        map.put("orderId", id);
        map.put("content", "订单号：" + orderDB.getNumber());
        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
    }

}
