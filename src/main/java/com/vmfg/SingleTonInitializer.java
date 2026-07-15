package com.vmfg;

import java.lang.reflect.InvocationTargetException;
import java.util.TreeMap;

public class SingleTonInitializer {

	private static SingleTonInitializer singleInstance = null;
	public static TreeMap<String, Integer> deviceDetails = new TreeMap<>();
	public static int lineOffsetCount;

	private SingleTonInitializer() {

	}

	public static SingleTonInitializer getInstance() throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		if (singleInstance == null)
			singleInstance = new SingleTonInitializer();

		return singleInstance;
	}
}
