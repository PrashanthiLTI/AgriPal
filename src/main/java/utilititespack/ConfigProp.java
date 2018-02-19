package utilititespack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProp {
	public static Properties prop = new Properties();
	public static String filepath;
	static {
			filepath = System.getProperty("user.dir");
				System.out.println(filepath);
				filepath = filepath + "/" + "Config.properties";
				try {
					prop.load(new FileInputStream(filepath));
				} catch (IOException e) {
					e.printStackTrace();
				}
				}

	public static String getPropertyValue(String key) {
		return prop.getProperty(key);
	}
	
}
