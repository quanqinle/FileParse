package com.github.quanqinle.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志输出代理类。
 * <p>
 * 主要完成业务代码和日志工具间的解耦，使切换日志工具更加方便。
 * <p>
 * 如：将Log4j改为common-logging或自己的独立实现，则只需调整该类即可。
 * <p>
 * 简化了调用方式：如使用log4j，通常用法是先生成一个实例，再调用输出方法。 现在，只需直接使用 Logs.debug();等静态方法即可
 *
 * @author yangzi
 * <p>
 * 2008-09-08
 * @version 1.0
 * @since 1.5
 */

public class LogUtil {
    /**
     * 是否打印日志，true表示打印日志，false表示不打印。
     * <p>
     * 开发阶段可以将其设为ture，运行阶段可以设为false
     */
    private static final boolean enabled = true;
    /**
     * 是否进行源代码定位，ture表示输出源代码所在类以及代码行
     */
    private static boolean showLocSrc = true;
    /**
     * 指定的日志级别
     */
    private static int level = 1;
    /**
     * 日志级别：普通
     */
    private static final int info = 1;
    /**
     * 日志级别：调试
     */
    private static final int debug = 2;
    /**
     * 日志级别：警告
     */
    private static final int warn = 3;
    /**
     * 日志级别：错误
     */
    private static final int error = 4;
    /**
     * 消息所属和消息内容的分隔符
     */
    private static final String msgSplit = ":";
    /**
     * 该类的名称，用于识别该类的堆栈
     */
    private static final String thisClassName = LogUtil.class.getName();

    /**
     * 默认输出日志的日志工具：log4j
     */
    public static final Logger logger = LoggerFactory.getLogger(LogUtil.class);


    private LogUtil() {
    }

    public static void main(String[] args) {
        LogUtil.info("调试信息");
    }

    /**
     * 根据日志级别，输出日志。
     * <p>
     * 如果要改变日志输出工具，
     * <p>
     * 如：由原来的log4j改为System.out.println()或logging，则只需改动此类即可。
     *
     * @param level   日志级别
     * @param message 日志消息
     * @param ste     堆栈信息。
     *                <p>
     *                如果不需要输出源代码信息，则只需将静态成员属性 showLocSrc设为false即可。
     */
    private static void log(int level, String message, StackTraceElement[] ste) {
        if (ste != null) {
            // 加入源代码定位
            message = getStackMsg(ste) + msgSplit + message;// fixme todo
        }
        // 转入具体实现，此处为log4j，可以改为其他不同的日志实现。
        switch (level) {
            case info:
                logger.info(message);
                break;
            case debug:
                logger.debug(message);
                break;
            case warn:
                logger.warn(message);
                break;
            case error:
                logger.error(message);
                break;
            default:
                logger.debug(message);
        }
    }

    /**
     * 根据堆栈信息得到源代码行信息
     * <p>
     * 原理：本工具类的堆栈下一行即为源代码的最原始堆栈。
     *
     * @param ste 堆栈信息
     * @return 调用输出日志的代码所在的类.方法.代码行的相关信息
     * <p>
     * 如：com.MyClass 类里的 fun()方法调用了Logs.debug("test");
     * <p>
     * 则堆栈信息为: com.MyClass.fun(MyClass.java 代码行号)
     */
    private static String getStackMsg(StackTraceElement[] ste) {
        if (ste == null) {
            return null;
        }
        boolean srcFlag = false;
        for (int i = 0; i < ste.length; i++) {
            StackTraceElement s = ste[i];
            // 如果上一行堆栈代码是本类的堆栈，则该行代码则为源代码的最原始堆栈。
            if (srcFlag) {
                return s == null ? "" : s.toString();
            }
            // 定位本类的堆栈
            if (thisClassName.equals(s.getClassName())) {
                srcFlag = true;
            }
        }
        return null;
    }

    /**
     * 输出info信息
     *
     * @param message
     */
    public static void info(String message) {
        // 如果禁止日志或者输出级别不符，则返回。
        if (!enabled || info < level)
            return;
        if (showLocSrc) {
            log(info, message, Thread.currentThread().getStackTrace());
        } else {
            log(info, message, null);
        }
    }

    /**
     * 输出debug信息
     *
     * @param message
     */
    public static void debug(String message) {
        // 如果禁止日志或者输出级别不符，则返回。
        if (!enabled || debug < level)
            return;
        if (showLocSrc) {
            log(debug, message, Thread.currentThread().getStackTrace());
        } else {
            log(debug, message, null);
        }
    }

    /**
     * 输出warn信息
     *
     * @param message
     */
    public static void warn(String message) {
        // 如果禁止日志或者输出级别不符，则返回。
        if (!enabled || warn < level)
            return;
        if (showLocSrc) {
            log(warn, message, Thread.currentThread().getStackTrace());
        } else {
            log(warn, message, null);
        }
    }

    /**
     * 输出error信息
     *
     * @param message
     */
    public static void error(String message) {
        // 如果禁止日志或者输出级别不符，则返回。
        if (!enabled || error < level)
            return;
        if (showLocSrc) {
            log(error, message, Thread.currentThread().getStackTrace());
        } else {
            log(error, message, null);
        }
    }

    /**
     * 获取当前文件名和行号，用于打印log
     *
     * @return
     * @author 权芹乐 2014-12-2
     */
    public static String getFileAndLine() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return "[" + ste.getFileName() + ":" + ste.getLineNumber() + "]";
    }
}
