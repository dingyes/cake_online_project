package net.onest.cakeonlineprj;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.onest.cakeonlineprj.customer.OrderDetailActivity;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class MyReceiver extends JPushMessageReceiver {

    /**
     * 当收到自定义消息时被触发
     *
     * @param context
     * @param customMessage 自定义消息
     */
    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
        Log.e("dyy", "收到自定义消息");
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 当收到通知消息被点击时触发
     *
     * @param context
     * @param notificationMessage
     */
    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
    }

    /**
     * 当收到通知消息时被触发
     *
     * @param context
     * @param notificationMessage
     */
    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
        if (notificationMessage.isRichPush) {
            Log.e("dy", "收到富文本通知消息");
        } else {
            Log.e("dy", "收到普通文本通知消息");
        }
    }
}
