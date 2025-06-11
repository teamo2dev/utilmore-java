package kr.teamo2.utils.loggingUtil;


import org.aspectj.lang.annotation.Pointcut;

public class LoggingPointCut {

    @Pointcut("@annotation(kr.teamo2.utils.loggingUtil.LogEnabled)")
    public void logEnabled() {}

    @Pointcut("args(..)")
    public void zeroArgs() {}

    @Pointcut("args(*, ..)")
    public void oneArgs() {}

    @Pointcut("args(*, *, ..)")
    public void twoArgs() {}

    @Pointcut("args(*, *, *, ..)")
    public void threeArgs() {}
}
