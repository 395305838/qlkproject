/**
 * 
 */
package copy_new;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.adapter.XCBaseAdapterImage;
import com.xiaocoder.android.fw.general.adapter.XCViewHolder;
import com.xiaocoder.android.fw.general.json.XCJsonBean;
import com.xiaocoder.android.test.R;
import com.xiaocoder.android.test.list.GridActivity.TestAdatpter.ViewHolder;
import com.xiaocoder.android.test.new_copy.NameAdapter.NameViewHolder;

/**
 * @Description:
 * @author xiaocoder
 * @date 2015-3-21 下午6:18:52
 */
public class NameAdapter extends XCBaseAdapter<XCJsonBean> {

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		bean = list.get(position);
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_test_item, null);
			holder = new ViewHolder();
			holder.xc_id_adapter_test_textview = (TextView) convertView.findViewById(R.id.xc_id_adapter_test_textview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 获取和设置控件的显示值
		holder.xc_id_adapter_test_textview.setText(bean.getString("content"));
		// 加载图片
		return convertView;
	}

	public NameAdapter(Context context, List<XCJsonBean> list) {
		super(context, list);
	}

	class ViewHolder {
		TextView xc_id_adapter_test_textview;
	}
}
