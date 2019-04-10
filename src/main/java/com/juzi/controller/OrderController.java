package com.juzi.controller;

import com.juzi.domain.OrderInfo;
import com.juzi.domain.User;
import com.juzi.result.CodeMsg;
import com.juzi.result.Result;
import com.juzi.service.GoodsService;
import com.juzi.service.OrderService;
import com.juzi.vo.GoodsVo;
import com.juzi.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: juzi
 * @date: 2019/4/9
 * @time: 20:53
 */
@Controller
@RequestMapping("/order")
public class OrderController {


    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/detail")
    @ResponseBody
    public Result<OrderDetailVo> detail(Model model, User user,
                                        @RequestParam("orderId") long orderId) {
        model.addAttribute("user", user);
        //判断用户是否登录
        if (null == user) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if(null == orderInfo){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(orderInfo.getGoodsId());

        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(orderInfo);
        vo.setGoods(goodsVo);

        return Result.success(vo);
    }

}
