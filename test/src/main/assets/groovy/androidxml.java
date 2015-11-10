package groovy;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class androidxml {

	public static void main(String[] args) {
		initUI();
	}

	public static String CONMENT_KEY = "CONMENT";
	public static String LAYOUT_MODEL = "xc_id_model_layout";
	public static String LAYOUT_TITLE = "xc_id_model_titlebar";
	public static String LAYOUT_CONTENT = "xc_id_model_content";
	public static String LAYOUT_NONET = "xc_id_model_no_net";
	public static String LINE = "\r\n";

	public static JFrame frame;
	public static JTextField textfield;
	public static JButton button;
	public static JTextArea area;
	public static JScrollPane scrollPane;
	public static String ENCODING = "utf-8";

	private static String AUTHORITY_PRIVATE = "private";
	private static String AUTHORITY_PUBLIC = "public";

	public static void initUI() {

		frame = new JFrame("初始化控件");
		textfield = new JTextField();
		button = new JButton("解析");
		area = new JTextArea(30, 100);
		scrollPane = new JScrollPane(area);

		frame.setBounds(200, 100, 1000, 600);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.add(button, BorderLayout.EAST);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		area.setText("请输入xml的绝对路径 , androidStudio为ctrl+shift+c");

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String result = parse(area.getText().toString().trim());
				area.setText("");
				area.setText(result);
			}
		});
	}

	public static String parse(String path) {

		StringBuilder sb = new StringBuilder();
		try {

			Map<String, String> map = new LinkedHashMap<String, String>();

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();

			parser.setInput(new FileInputStream(path), ENCODING);

			int event;
			int count = 0;
			while ((event = parser.getEventType()) != XmlPullParser.END_DOCUMENT) {
				count++;

				if (event == XmlPullParser.START_TAG) {

					String keyId = parser.getAttributeValue(null, "android:id");

					if (keyId != null) {

						String packageName = parser.getName();
						int lastIndex = packageName.lastIndexOf(".");

						if (lastIndex > 0) {
							packageName = packageName.substring(lastIndex + 1);
						}

						if ("include".equals(packageName)) {
							packageName = "ViewGroup";
						}

						map.put(keyId.substring(keyId.lastIndexOf("/") + 1,
								keyId.length()).trim(), packageName);
					}

				} else if (event == XmlPullParser.TEXT) {

					String str = parser.getPositionDescription();
					int start = str.indexOf("<!--");
					int end = str.lastIndexOf("-->");
					if (start > 0 && end > 0 && end > start) {
						String content = str.substring(start + 4, end);
						map.put(CONMENT_KEY + count, parseUnicode(content));
					}
				}

				parser.next();
			}

			for (Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (check(key)) {
					if (key.contains(CONMENT_KEY)) {
						sb.append("/**" + value + "*/" + LINE);
					} else {
						sb.append(AUTHORITY_PRIVATE + " " + value + " " + key
								+ ";" + LINE);
					}
				}
			}

			sb.append(LINE);

			for (Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (check(key)) {
					if (!key.contains(CONMENT_KEY)) {
						sb.append(key + " = getViewById(R.id." + key + ");"
								+ LINE);
					}
				}
			}

			sb.append(LINE);

			for (Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (check(key)) {
					if (!key.contains(CONMENT_KEY)) {
						sb.append(key + " = (" + value + ")findViewById(R.id."
								+ key + ");" + LINE);
					}
				}
			}
			sb.append(LINE);
			sb.append(LINE);
			sb.append("---------------------------------以下是adapter中的holder可能用到的--------------------------------");
			sb.append(LINE);
			sb.append(LINE);

			for (Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (check(key)) {
					if (!key.contains(CONMENT_KEY)) {
						sb.append("holder." + key + " = (" + value
								+ ")convertView.findViewById(R.id." + key
								+ ");" + LINE);
					}
				}
			}

			sb.append(LINE);

			sb.append("public class NameHolder{" + LINE);
			for (Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (check(key)) {
					if (key.contains(CONMENT_KEY)) {
						sb.append("	/**" + value + "*/" + LINE);
					} else {
						sb.append("	" + value + " " + key + ";" + LINE);
					}
				}
			}
			sb.append(LINE);
			sb.append("	public NameHolder(View convertView){" + LINE + LINE);

			for (Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (check(key)) {
					if (!key.contains(CONMENT_KEY)) {
						sb.append("		" + key + " = (" + value
								+ ")convertView.findViewById(R.id." + key
								+ ");" + LINE);
					}
				}
			}

			sb.append(LINE);

			sb.append("	}" + LINE);
			sb.append("}");

			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static boolean check(String key) {
		if (key.equals(LAYOUT_MODEL) || key.equals(LAYOUT_TITLE)
				|| key.equals(LAYOUT_CONTENT) || key.equals(LAYOUT_NONET)) {
			return true;
		} else {
			return true;
		}

	}

	public static String parseUnicode(String line) {
		int len = line.length();
		char[] out = new char[len];// 保存解析以后的结果
		int outLen = 0;
		for (int i = 0; i < len; i++) {
			char aChar = line.charAt(i);
			if (aChar == '\\') {
				aChar = line.charAt(++i);
				if (aChar == 'u') {
					int value = 0;
					for (int j = 0; j < 4; j++) {
						aChar = line.charAt(++i);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed \\uxxxx encoding.");
						}
					}
					out[outLen++] = (char) value;
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					out[outLen++] = aChar;
				}
			} else {
				out[outLen++] = aChar;
			}
		}
		return new String(out, 0, outLen);
	}

}
