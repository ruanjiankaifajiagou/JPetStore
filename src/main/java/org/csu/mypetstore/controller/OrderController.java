package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Order;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.AccountService;
import org.csu.mypetstore.service.CatalogService;
import org.csu.mypetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {


    @Autowired
    private OrderService orderService;

    @Autowired
    private AccountService accountService;


    @GetMapping("/ConfirmOrder")
    public String ConfirmOrder(int orderID,Model model){
        Order order = orderService.getOrder(orderID);
        model.addAttribute("order",order);
        return "Order/ConfirmOrder";
    }

    @GetMapping("/ListOrders")
    public String ListOrders(String username,Model model){
        List<Order> orderList = orderService.getOrdersByUsername(username);
        model.addAttribute("orderList",orderList);
        return "Order/ListOrders";
    }

    @GetMapping("/NewOrderForm")
    public String NewOrderForm(int orderId,Model model){
        Order order = new Order();
        if (accountService == null) {
            System.out.println("You must sign on before attempting to check out.  Please sign on and try checking out again.");
            return "account/signon";
        } else {
            order = orderService.getOrder(orderId);
            model.addAttribute("order",order);
            return "Order/NewOrderForm";
        }
    }

    @GetMapping()
    public String newOrder(boolean shippingAddressRequired,boolean confirm,int orderId,Model model) {
        Order order = orderService.getOrder(orderId);

        if (shippingAddressRequired) {
            shippingAddressRequired = false;
            model.addAttribute("order",order);
            return "Order/ShippingForm";
        } else if (!confirm) {
            model.addAttribute("order",order);
            return "Order/ConfirmOrder";
        } else if (order!= null) {
            orderService.insertOrder(order);

//            CartActionBean cartBean = (CartActionBean) session.getAttribute("/actions/Cart.action");
//            cartBean.clear();

            System.out.println("Thank you, your order has been submitted.");

            return "/Order/ViewOrder";
        } else {
            System.out.println("An error occurred processing your order (order was null).");
            return "common/error";
        }
    }

    @GetMapping("/ShippingForm")
    public String ShippingForm(String s,Model model){
        return "Order/ShippingForm";
    }

    @GetMapping("/ViewOrder")
    public String ViewOrder(int orderId,Model model){
        Order order = orderService.getOrder(orderId);
        model.addAttribute("order",order);
        return "Order/ViewOrder";
    }




}
