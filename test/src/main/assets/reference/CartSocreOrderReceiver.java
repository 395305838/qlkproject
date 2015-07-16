package reference;
///**
// *
// */
//package com.qlk.market.receiver;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//
//import com.qlk.market.fragment.content.AddressShowFragment;
//import com.qlk.market.fragment.content.OrdersFragment;
//import com.qlk.market.fragment.tab.TabCardYesLoginFragment;
//import com.qlk.market.fragment.tab.TabCartFragment;
//import com.qlk.market.application.BaseActivity;
//
///**
// * @author xiaocoder
// * @Description:购物车 积分 订单 发生变化时 , 发送广播
// * @date 2015-1-16 下午1:52:46
// */
//public class CartSocreOrderReceiver extends BroadcastReceiver {
//    public static String CART_CHANGED_ACTION = "cart_change_action"; // 只要是注册了该串的, 购物车的变动就会接收到该广播
//    public static String ORDER_CHANGED_ACTION = "order_change_action"; // 只要是注册了该串的, 订单的变动就会接收到该广播
//    public static String CARD_CHANGED_ACTION = "card_change_action"; // 只要是注册了该串的, 积分的变动就会更新健康卡的内容
//    public static String ADDRESS_CHANGED_ACTION = "address_change_action"; // 只要是注册了该串的, 积分的变动就会更新健康卡的内容
//
//    public static String COMMAND_KEY = "command_key"; // 命令的指示
//    public static String COMMAND_KEY_2 = "command_key_2"; // 命令的指示
//    public static String COMMAND_VALUE_CART_NUM_CHANGED = "cart_num_changed";
//    public static String COMMAND_VALUE_CART_RESET = "cart_reset"; // 登录与注销的时刻, 之前选中的索引需要重置,再更新列表
//    public static String COMMAND_VALUE_ORDER_CHANGED = "order_changed";
//    public static String COMMAND_VALUE_CARD_CHANGED = "card_changed";
//    public static String COMMAND_VALUE_ADDRESS_CHANGED = "address_changed";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        BaseActivity activity = (BaseActivity) context;
//        if (intent != null) {
//            String command_value = intent.getStringExtra(COMMAND_KEY);
//            if (COMMAND_VALUE_CART_NUM_CHANGED.equals(command_value)) {
//                TabCartFragment cart_fragment = activity.getFragmentByTag(TabCartFragment.class.getSimpleName());
//                if (cart_fragment == null) {
//                    return;
//                }
//                String command_value_2 = intent.getStringExtra(COMMAND_KEY_2);
//                if (command_value_2 != null && command_value_2.equals(COMMAND_VALUE_CART_RESET)) {
//                    // 注销或者登录 , 购物车的记录索引的集合需要重置, 且更新列表
//                    cart_fragment.resetCart();
//                    return;
//                }
//                // 购物车的数量变化了
//                cart_fragment.updateCart();
//            } else if (COMMAND_VALUE_ORDER_CHANGED.equals(command_value)) {
//                OrdersFragment orders_fragment = activity.getFragmentByTag(OrdersFragment.class.getSimpleName());
//                orders_fragment.requestForOrders(1);
//            } else if (COMMAND_VALUE_CARD_CHANGED.equals(command_value)) {
//                TabCardYesLoginFragment logined_fragment = activity.getFragmentByTag(TabCardYesLoginFragment.class.getSimpleName());
//                logined_fragment.reqeustUpdateInfo();
//            } else if (COMMAND_VALUE_ADDRESS_CHANGED.equals(command_value)) {
//                AddressShowFragment address_fragment = activity.getFragmentByTag(AddressShowFragment.class.getSimpleName());
//                address_fragment.requestAddressList();
//            }
//        }
//    }
// }
