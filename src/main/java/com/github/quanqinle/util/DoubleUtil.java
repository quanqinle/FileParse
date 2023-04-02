package com.github.quanqinle.util;

import java.math.BigDecimal;import java.math.RoundingMode;

/**
 * Double数据的操作
 */
public class DoubleUtil {

	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 2;
	// 默认乘法运算精度
	private static final int DEF_MUL_SCALE = 2;

	// 这个类不能实例化
	private DoubleUtil() {

	}

	/**
	 * 加法运算
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}
	/**
	 * 加法运算
	 * @param addend
	 * @param augend
	 * @return {@code addend + ... + augend}
	 */
	public static double add(double addend, double... augend) {
		BigDecimal b1 = new BigDecimal(Double.toString(addend));
		
		for (double d : augend) {
			BigDecimal b2 = new BigDecimal(Double.toString(d));
			b1 = b1.add(b2);
		}
		return b1.doubleValue();
	}

	/**
	 * 减法运算
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 减法运算
	 * @param minuend 被减数
	 * @param subtrahends 减数
	 * @return {@code minuend - ... - subtrahends}
	 */
	public static double sub(double minuend, double... subtrahends) {
		BigDecimal b1 = new BigDecimal(Double.toString(minuend));
		for (double d : subtrahends) {
			BigDecimal b2 = new BigDecimal(Double.toString(d));
			b1 = b1.subtract(b2);
		}
		
		return b1.doubleValue();
	}
	
	/**
	 * 乘法运算，四舍五入保留2位小数。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		return mul(v1, v2, DEF_MUL_SCALE);
	}

	/**
	 * 乘法运算，精确到小数点以后指定位数
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @param scale
	 *            需要精确到小数点以后几位
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2, int scale) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).setScale(scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 除法运算，四舍五入精确到2位小数
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字“四舍五入”。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            需要精确到小数点以后几位
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		/**
		 * ROUND_HALF_UP: 遇到.5的情况时往上近似,例: 1.5 ->2;
		 * ROUND_HALF_DOWN : 遇到.5的情况时往下近似,例: 1.5->1;
		 */
		return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
	}
}