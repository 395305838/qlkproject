package reference;

import java.io.File;
import java.io.FileFilter;

public class DemoFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) {
		if (pathname.getName().startsWith(".")) {
			return false;
		} else {
			return true;
		}
	}
}
