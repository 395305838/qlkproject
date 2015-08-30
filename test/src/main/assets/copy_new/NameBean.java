package copy_new;

import com.xiaocoder.android.fw.general.json.XCJsonBean;

//1 JsonType 的过滤标记可以打印出 返回的json串的每个字段的 类型
//2 解析的时候如解析的类型错误了， 该bean里内置了try catch，并会返回正确的类型，仅 int String  long  double
//3 bean内的字段可以自动生成， JsonBean的标记中可以 打印出bean， 复制后格式化即可

public class NameBean extends XCJsonBean {

}
