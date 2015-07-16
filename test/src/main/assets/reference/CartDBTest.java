package reference;
//package com.xiaocoder.android.fw.general.template.db.example2;
//
//import java.util.List;
//
//import android.test.AndroidTestCase;
//
//import com.xiaocoder.android.fw.general.application.XCBaseConfig;
//
//public class CartDBTest extends AndroidTestCase {
//
//	public void testInsert() {
//		CartDaoImpl dao = new CartDaoImpl(getContext());
//		// for (int i = 0; i <= 6; i++) {
//		// dao.insert(new CartBean(i + "activity_id", i + "goods_id", i + "sku_attr_ids", 123, 1234562222222L, i + "sku_items", i + "goods_number", i + "goods_name", i + "restriction_number", i
//		// + "buy_number", i + "to_db_price", i + "goods_img"));
//		// }
//
//		dao.insert(new CartBean("activity_id", "goods_id", "sku_attr_ids", 123, 22L, "sku_items", "goods_number", "goods_name", "restriction_number", "buy_number", "to_db_price", "goods_img", System
//				.currentTimeMillis() + "", 0));
//	}
//
//	public void testInsert2() {
//		IDao<CartBean> dao = DaoFactory.getDaoInstance(getContext(), XCBaseConfig.CARTDAO);
//		for (int i = 0; i <= 6; i++) {
//			dao.insert(new CartBean(i + "activity_id", i + "goods_id", i + "sku_attr_ids", 123456, 22, i + "sku_items", i + "goods_number", i + "goods_name", i + "restriction_number", i
//					+ "buy_number", i + "to_db_price", i + "goods_img", System.currentTimeMillis() + "", 0));
//		}
//	}
//
//	public void testDelete() {
//		CartDaoImpl dao = new CartDaoImpl(getContext());
//		dao.delete("1goods_img");
//	}
//
//	public void testDelete2() {
//		CartDaoImpl dao = new CartDaoImpl(getContext());
//		dao.delete_unique("1418285113697");
//	}
//
//	public void testUpdate() {
//		CartDaoImpl dao = new CartDaoImpl(getContext());
//		dao.update(new CartBean("update" + "activity_id", "update" + "goods_id", "update" + "sku_attr_ids", 123456, 333333333333L, "update" + "sku_items", "update" + "goods_number", "update"
//				+ "goods_name", "update" + "restriction_number", "update" + "buy_number", "update" + "to_db_price", "update" + "goods_img", System.currentTimeMillis() + "", 0), "1418285607634");
//	}
//
//	public void testQuery() {
//		IDao<CartBean> dao = DaoFactory.getDaoInstance(getContext(), XCBaseConfig.CARTDAO);
//		System.out.println(dao.query("2goods_img"));
//	}
//
//	public void testQueryAll() {
//		CartDaoImpl dao = new CartDaoImpl(getContext());
//		List<CartBean> beans = dao.queryAll();
//		for (CartBean bean : beans) {
//			System.out.println("testdb-->" + bean);
//		}
//	}
//
//	public void testQueryCount() {
//		IDao<CartBean> dao = DaoFactory.getDaoInstance(getContext(), XCBaseConfig.CARTDAO);
//
//		// CartDaoImpl dao = new CartDaoImpl(getContext());
//		System.out.println(dao.queryCount());
//	}
//
//	public void testQueryPage() {
//		CartDaoImpl dao = new CartDaoImpl(getContext());
//		List<CartBean> beans = dao.queryPage(1, 10);
//		for (CartBean bean : beans) {
//			System.out.println(bean);
//		}
//	}
//}
