package reference;
//package com.xiaocoder.android.fw.general.receiver;
//
//import java.util.Iterator;
//import java.util.List;
//
//import android.content.Context;
//
//import com.qlk.market.application.MyApplication;
//import com.qlk.market.application.MyConfig;
//import com.qlk.market.bean.JsonBean;
//import com.qlk.market.bean.TalkBean;
//import com.qlk.market.db.DaoFactory;
//import com.qlk.market.db.IDao;
//import com.qlk.market.fragment.body.content.TalkFragment;
//import com.qlk.market.utils.util.JsonUtil;
//import com.tencent.android.tpush.XGPushBaseReceiver;
//import com.tencent.android.tpush.XGPushClickedResult;
//import com.tencent.android.tpush.XGPushConfig;
//import com.tencent.android.tpush.XGPushRegisterResult;
//import com.tencent.android.tpush.XGPushShowedResult;
//import com.tencent.android.tpush.XGPushTextMessage;
//
//public class PushMessageReceiver extends XGPushBaseReceiver {
//
//	/**
//	 * 
//	 * XGPushRegisterResult提供的方法列表:
//	 * 
//	 * getToken() String "" 设备的token
//	 * 
//	 * XGPushConfig.getToken(context) 当设备一旦注册成功后，便会将token存储在本地，之后可通过XGPushConfig.getToken(context)接口获取。
//	 * 
//	 * getAccessId() long 0 获取注册的accessId
//	 * 
//	 * getAccount() String "" 获取注册绑定的账号
//	 * 
//	 * getTicket() string "" 登陆态票据
//	 * 
//	 * getTicketType() short 0 票据类型
//	 */
//	@Override
//	public void onRegisterResult(Context context, int errorCode, XGPushRegisterResult message) {
//		// Logs.longToast(context, "信鸽回调了 -- >XGPushBaseReceiver-----onRegisterResult()");
//
//		if (context == null || message == null) {
//			return;
//		}
//		XGPushConfig.getToken(context);
//
//		// Logs.longToast(context, message.getDeviceId()); //与token不一样
//
//		// String text = null;
//		// if (errorCode == XGPushBaseReceiver.SUCCESS) {
//		// text = message + "注册成功";
//		// // 在这里拿token
//		// String token = message.getToken();
//		// } else {
//		// text = message + "注册失败，错误码：" + errorCode;
//		// }
//		// Log.d(LogTag, text);
//		// show(context, text);
//	}
//
//	/**
//	 * 当通知在标题栏中提醒后,会调用该方法,可以在此保存APP收到的通知 notifiShowedRlt.getContent() notifiShowedRlt.getCustomContent();
//	 */
//	@Override
//	public void onNotifactionShowedResult(Context context, XGPushShowedResult notifiShowedRlt) {
//		// Logs.longToast(context, "信鸽回调了 -- >XGPushBaseReceiver-----onNotifactionShowedResult()");
//
//		if (context == null || notifiShowedRlt == null) {
//			return;
//		}
//		String text = "通知被展示 ，title:" + notifiShowedRlt.getTitle() + ",content:" + notifiShowedRlt.getContent() + ",custom_content:" + notifiShowedRlt.getCustomContent()
//				+ "notifiShowedRlt.getActivity():" + notifiShowedRlt.getActivity() + "notifiShowedRlt.getMsgId():" + notifiShowedRlt.getMsgId();
//		System.out.println(text);
//
//		// 通知
//		// getTitle-->标题
//		// getContent-->内容
//		// getCustomContent-->键值对
//	}
//
//	/**
//	 * 反注册操作切勿过于频繁，可能会造成后台同步延时,切换别名无需反注册，多次注册自动会以最后一次为准。
//	 */
//	@Override
//	public void onUnregisterResult(Context context, int errorCode) {
//		// Logs.longToast(context, "信鸽回调了 -- >XGPushBaseReceiver-----onUnregisterResult()");
//		if (context == null) {
//			return;
//		}
//		// String text = null;
//		// if (errorCode == XGPushBaseReceiver.SUCCESS) {
//		// text = "反注册成功";
//		// } else {
//		// text = "反注册失败" + errorCode;
//		// }
//		// Log.d(LogTag, text);
//		// show(context, text);
//
//	}
//
//	@Override
//	public void onSetTagResult(Context context, int errorCode, String tagName) {
//		// Logs.longToast(context, "信鸽回调了 -- >XGPushBaseReceiver-----onSetTagResult()");
//	}
//
//	@Override
//	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
//		// Logs.longToast(context, "信鸽回调了 -- >XGPushBaseReceiver-----onDeleteTagResult()");
//	}
//
//	// 点击删除时会回调 -->小米手机
//	@Override
//	public void onNotifactionClickedResult(Context context, XGPushClickedResult message) {
//		// Logs.longToast(context, "信鸽回调了 -- >XGPushBaseReceiver-----onNotifactionClickedResult()");
//		if (context == null || message == null) {
//			return;
//		}
//		// String text = "通知被打开 :" + message;
//		// 获取自定义key-value
//		// String customContent = message.getCustomContent();
//		// if (customContent != null && customContent.length() != 0) {
//		// try {
//		// JSONObject obj = new JSONObject(customContent);
//		// // key1为前台配置的key
//		// if (!obj.isListBlank("key")) {
//		// String value = obj.getString("key");
//		// Log.d(LogTag, "get custom value:" + value);
//		// }
//		// // ...
//		// } catch (JSONException e) {
//		// e.printStackTrace();
//		// }
//		// }
//		// APP自主处理的过程。。。
//		// Log.d(LogTag, text);
//		// show(context, text);
//	}
//
//	/**
//	 * 1. 通知： 指的是在设备的通知栏展示的内容，由信鸽SDK完成所有的操作，APP可以监听通知被打开的行为，也就是说在前台下发的通知不需要APP做任何处理 ，默认会展示在通知栏。
//	 * 
//	 * 2. 消息： 指的是由信鸽下发给APP的内容，需要APP继承XGPushBaseReceiver接口实现并自主处理所有操作过程 ，也就是说，下发的消息默认是不会展示在通知栏的，信鸽只负责将消息从信鸽服务器下发到APP这个过程，不负责消息的处理逻辑，需要APP自己实现。
//	 * 
//	 * getContent() String "" 消息正文内容，通常只需要下发本字段即可-->可以在这里传json
//	 * 
//	 * getCustomContent() String "" 消息自定义key-value -->null
//	 * 
//	 * getTitle() string "" 消息标题 （注意：从前台下发消息命令字中的描述不属于标题）-->null
//	 * 
//	 * 详细的看文档
//	 */
//	@Override
//	public void onTextMessage(Context context, XGPushTextMessage message) {
//		// Logs.longToast(context, "信鸽回调了 -- >XGPushBaseReceiver-----onTextMessage()");
//		//
//		// Logs.printJson("message.toString()-->" + message.toString() + "----message.getContent()-->" + message.getContent() + "---message.getCustomContent()-->" + message.getCustomContent()
//		// + "----message.getTitle()-->" + message.getTitle());
//		//
//		// System.out.println("message.toString()-->" + message.toString() + "----message.getContent()-->" + message.getContent() + "---message.getCustomContent()-->" + message.getCustomContent()
//		// + "----message.getTitle()-->" + message.getTitle());
//		//
//		// FileOutputStream fos = null;
//		// try {
//		// fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/getjson2.text");
//		// fos.write(message.toString().getBytes());
//		// fos.flush();
//		// } catch (Exception e) {
//		//
//		// } finally {
//		// if (fos != null) {
//		// try {
//		// fos.close();
//		// } catch (IOException e) {
//		// e.printStackTrace();
//		// }
//		// }
//		// }
//
//		// 12-16 13:49:48.990: I/System.out(14196): TPushTextMessage [title=七乐康, content=测试, customContent={"receptionist":"admin"}]
//
//		// --------------------------------------以下正式版---------------------------------------------
//		String message_content = message.getContent(); // 消息内容
//		String customer_service_name_json = message.getCustomContent(); // 客服名字
//		JsonBean customer_service_name_bean = JsonUtil.getJsonParseData(customer_service_name_json);
//		System.out.println(message.toString());
//		TalkBean ask_bean = null;
//		if (customer_service_name_bean != null) {
//			// 表示解析成功 , 获取客服名字
//			// String customer_service_name = customer_service_name_bean.getString("receptionist");
//			String customer_service_name = "七乐康药师";
//			// 存入bean
//			ask_bean = new TalkBean();
//			ask_bean.setType(TalkFragment.SHE);
//			ask_bean.setUsername(customer_service_name);
//			ask_bean.setMessage(message_content);
//			ask_bean.setTime(System.currentTimeMillis() + "");
//		} else {
//			// 表示解析失败
//			return;
//		}
//
//		if (TalkFragment.adapter != null) {
//			// 存在该界面更新即可
//			TalkFragment.beans.add(ask_bean);
//			TalkFragment.adapter.update(TalkFragment.beans);
//			TalkFragment.adapter.notifyDataSetChanged();
//			TalkFragment.qlk_id_talk_listview.setSelection(TalkFragment.beans.size());
//		}
//
//		IDao<TalkBean> dao = DaoFactory.getDaoInstance(context, MyConfig.ASKDAO);
//		// 存入数据库
//		dao.insert(ask_bean);
//
//		// 暂时-->如果记录大于500,则只保存最近的250
//		if (dao.queryCount() > 500) {
//			List<TalkBean> all_beans = dao.queryAll();
//			// 仅保留最近的250条记录,剩下的删除
//			int delete_num = all_beans.size() - 250;
//			for (Iterator<TalkBean> it = all_beans.iterator(); it.hasNext() && delete_num > 0; delete_num--) {
//				TalkBean bean = it.next();
//				dao.delete(bean.getTime());
//			}
//		}
//		// ---------------------------------------以上正式版----------------------------------------------------
//
//		// -------------------------------以下后台推送测试用的----------------------------
//		// String message_content = message.getContent(); // 消息内容
//		// AskBean bean = new AskBean();
//		// bean.setType(AskPage.SHE);
//		// bean.setUsername("qlk");
//		// bean.setMessage(message_content);
//		// bean.setTime(System.currentTimeMillis() + "");
//		// if (AskPage.adapter != null) {
//		// // 存在该界面更新即可
//		// AskPage.beans.add(bean);
//		// AskPage.adapter.update(AskPage.beans);
//		// AskPage.adapter.notifyDataSetChanged();
//		// AskPage.listview.setSelection(AskFragment2.beans.size());
//		// }
//		// TalkDaoImpl dao = new TalkDaoImpl(context);
//		// dao.insert(bean);
//
//		// System.out.println("message.toString()" + message.toString());
//		// System.out.println("message.getContent()->" + message.getContent());
//		// System.out.println("message.getCustomContent()->" + message.getCustomContent());
//		// System.out.println(" message.getTitle()->" + message.getTitle());
//		// ---------------------------------------以上后台推送测试用的-------------------------------
//
//	}
//
//	/**
//	 * 通知栏提示消息
//	 * 
//	 * @param context
//	 *            用户没有退出 单推 没有任何处理 ，用户退出了单推没有声音，如果是全推 通知的声音是用户可以控制的
//	 * @param msg
//	 */
//	@SuppressWarnings({ "deprecation" })
//	private void showNotificaction(Context context, String message) {
//		// Logs.longToast(context, "信鸽回调了 -- >XGPushBaseReceiver-----showNotificaction()");
//
//		// Notification mNotified = new Notification(); // 创建一个Notification
//		// mNotified.icon = R.drawable.ic_launcher; // Notification状态栏的图标
//		// mNotified.tickerText = message;
//		// mNotified.when = System.currentTimeMillis();
//
//		// Intent in = new Intent(context, MainActivity.class);
//		// PendingIntent pi = PendingIntent.getActivity(context, 1, in, PendingIntent.FLAG_UPDATE_CURRENT);
//		// mNotified.setLatestEventInfo(context, "消息", message, pi);
//
//		// Notification状态栏的声音
//		// AudioManager mAudioManager = (AudioManager) context
//		// .getSystemService(Context.AUDIO_SERVICE);
//		// if (mAudioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT
//		// && mAudioManager.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE)
//		// {
//		// // if (sound == 1) {
//		// Uri uri = Uri.parse("android.resource://"
//		// + context.getPackageName() + "/raw/zlzs");
//		// mNotified.sound = uri;
//		// }
//
//		// mNotified.defaults |= Notification.DEFAULT_VIBRATE;
//		// mNotified.flags = Notification.FLAG_AUTO_CANCEL;
//		// NotificationManager mNM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		// mNM.notify(1, mNotified);
//	}
//
//}