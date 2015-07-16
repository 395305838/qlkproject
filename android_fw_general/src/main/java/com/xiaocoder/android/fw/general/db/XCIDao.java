/**
 *
 */
package com.xiaocoder.android.fw.general.db;

import java.util.List;

/**
 * @author xiaocoder
 *         2014-12-10 上午9:11:48
 */
public interface XCIDao<T> {

    void insert(T obj);

    int delete(String ids); // 删除一种商品的所有item , 如不同度数的 同品牌眼镜

    int delete_unique(String unique_id);// 删除指定item

    int deleteAll();

    int update(T obj, String unique_id); // 更新指定的item

    List<T> query(String ids); // 查询一种商品 , 可能有多个item

    T query_unique(String unique_id); // 查询一个item

    List<T> queryAll(); // 查询所有商品

    List<T> queryPage(int pageNum, int capacity); // 分页

    int queryCount(); // 查询所有商品数量

}