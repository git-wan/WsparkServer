package cn.com.wushang.wspark.util;

import java.util.TimeZone;
import org.apache.log4j.Logger;

public class RtUtils {
	static Logger logger = Logger.getLogger(RtUtils.class);

	public static void dump() {
		String timeZone = TimeZone.getDefault().getID();
		String javaVersion = System.getProperty("java.version");
		String javaVendor = System.getProperty("java.vendor");
		String vmVersion = System.getProperty("java.vm.version");
		String vmVendor = System.getProperty("java.vm.vendor");
		String vmName = System.getProperty("java.vm.name");

		logger.info("虚拟机名字:" + vmName);
		logger.info("虚拟机版本:" + vmVersion);
		logger.info("虚拟机厂商:" + vmVendor);
		logger.info("系统版本:" + javaVersion);
		logger.info("系统厂商:" + javaVendor);
		logger.info("系统时区:" + timeZone);
	}
}
