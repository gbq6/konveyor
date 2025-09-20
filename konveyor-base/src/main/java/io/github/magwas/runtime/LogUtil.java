package io.github.magwas.runtime;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class LogUtil {
	static Set<String> debuggedClasses = new HashSet<>();

	static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static void addDebuggedClass(Class<?> debuggedClass) {
		debuggedClasses.add(debuggedClass.getName());
		logger.setLevel(Level.FINEST);
	}

	public static void clearDebuggedClasses() {
		debuggedClasses.removeIf(x -> true);
	}

	public static void debug(Object... args) {
		debug(3, args);
	}

	public static void debug(int depth, Object... args) {
		if (debuggedClasses == null) return;
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[depth];
		String name = stackTraceElement.getClassName();
		if (!debuggedClasses.contains(name)) return;
		List<String> params = Stream.of(args).map(Object::toString).toList();
		String method = stackTraceElement.getMethodName();
		String builder = "DEBUG " + name + ' ' + method + ' ' + stackTraceElement.getLineNumber() + ':'
				+ String.join(",", params);
		System.err.println(builder);
	}

	public static void warning(Object... args) {
		warning(3, args);
	}

	public static void warning(int depth, Object... args) {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[depth];
		doLog(stackTraceElement, Level.WARNING, args);
	}

	public static void info(Object... args) {
		info(3, args);
	}

	public static void info(int depth, Object... args) {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[depth];
		doLog(stackTraceElement, Level.INFO, args);
	}

	private static void doLog(StackTraceElement stackTraceElement, Level level, Object... args) {
		String name = stackTraceElement.getClassName();
		List<String> params = Stream.of(args).map(Object::toString).toList();
		String method = stackTraceElement.getMethodName();
		String builder = String.valueOf(stackTraceElement.getLineNumber()) + ':' + String.join(",", params);
		logger.logp(level, name, method, builder);
	}
}
