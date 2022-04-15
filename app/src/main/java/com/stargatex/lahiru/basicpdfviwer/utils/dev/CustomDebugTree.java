package com.stargatex.lahiru.basicpdfviwer.utils.dev;

import timber.log.Timber;

/**
 * @author Lahiru Jayawickrama (lahiruj)
 * @version 1.0
 */
public class CustomDebugTree extends Timber.DebugTree {
    @Override
    protected String createStackElementTag(StackTraceElement element) {
        return String.format("(%s:%s)#%s ",
                element.getFileName(),
                element.getLineNumber(),
                element.getMethodName());
    }
}
