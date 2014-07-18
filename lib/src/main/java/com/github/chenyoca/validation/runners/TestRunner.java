package com.github.chenyoca.validation.runners;

import com.github.chenyoca.validation.LazyLoader;
import com.github.chenyoca.validation.Type;

import java.util.regex.Pattern;

/**
 * AUTH: YooJia.Chen (Yoojia.Chen@gmail.com)
 * DATE: 2014-06-25
 * Test runner.
 */
public abstract class TestRunner {

    protected enum ExtraType {
        Int, Float, String
    }

    public final Type testType;
    protected final double[] extraFloat = new double[2];
    // Access Level : Public, For InputType used this properties.
    public final long[] extraLong = new long[2];
    protected final String[] extraString = new String[2];

    protected ExtraType extraType = ExtraType.String;
    protected String message;
    private LazyLoader lazyLoader;

    protected TestRunner(Type testType, String message){
        this.testType = testType;
        this.message = message;
    }

    public TestRunner(String message){
        this.testType = Type.Custom;
        this.message = message;
    }

    /**
     * Perform Test
     * @param input Input value
     * @return True if passed, false otherwise.
     */
    public boolean perform(String input){
        performLazyLoader();
        formatMessage();
        return test(input);
    }

    /**
     * Test implement for sub class
     * @param inputValue Input value
     * @return True if passed, false otherwise.
     */
    protected abstract boolean test(String inputValue);

    /**
     * Check if set Int/Flow extra value for test
     * @param name Name of test runner
     */
    protected void checkIntFlowValues(String name){
        if (ExtraType.String.equals(extraType))
            throw new IllegalArgumentException(name +
                    " ONLY accept Int/Float/Double values( set by 'setValues(...)' )!");
    }

    /**
     * Check if set Int extra value for test
     * @param name Name of test runner
     */
    protected void checkIntValues(String name){
        if (!ExtraType.Int.equals(extraType))
            throw new IllegalArgumentException(name +
                    " ONLY accept Int values( set by 'setValues(...)' ) !");
    }

    /**
     * Format the message with extra value
     */
    protected void formatMessage(){
        if (message == null) return;
        switch (extraType){
            case Float:
                message = message.replace("{$1}","" + extraFloat[0])
                        .replace("{$2}","" + extraFloat[1]);
                break;
            case Int:
                message = message.replace("{$1}","" + extraLong[0])
                        .replace("{$2}","" + extraLong[1]);
                break;
            case String:
                if (extraString[0] != null){
                    message = message.replace("{$1}", extraString[0]);
                }
                if (extraString[1] != null){
                    message = message.replace("{$2}", extraString[1]);
                }
                break;
        }
    }

    /**
     * Reload extra value from lazy loader
     */
    private void performLazyLoader(){
        if (lazyLoader != null){
            setValues(lazyLoader.intValues());
            setValues(lazyLoader.doubleValues());
            setValues(lazyLoader.stringValues());
        }
    }

    /**
     * Call when test runner finish configuration and added to runners array.
     * In this method, Impl Runner can check extra value and value type.
     */
    public void onAdded(){}

    public String getMessage(){
        return message == null? "" : message;
    }

    public void setLazyLoader(LazyLoader lazyloader){
        this.lazyLoader = lazyloader;
    }

    public void setMessage(String message){
        this.message = message;
    }

    /**
     * Set Int extra value for test runner.
     * @param values Int extra
     */
    public void setValues(long... values){
        checkValuesLength(values.length);
        extraType = ExtraType.Int;
        if ( 1 == values.length){
            extraLong[0] = values[0];
        }else{
            extraLong[0] = values[0];
            extraLong[1] = values[1];
        }
    }

    /**
     * Set String extra value for test runner.
     * @param values String extra
     */
    public void setValues(String... values){
        checkValuesLength(values.length);
        extraType = ExtraType.String;
        if ( 1 == values.length){
            extraString[0] = values[0];
        }else{
            extraString[0] = values[0];
            extraString[1] = values[1];
        }
    }

    /**
     * Set Flow/Double extra value for test runner.
     * @param values Flow/Double extra
     */
    public void setValues(double... values){
        checkValuesLength(values.length);
        extraType = ExtraType.Float;
        if ( 1 == values.length){
            extraFloat[0] = values[0];
        }else if ( 2 == values.length){
            extraFloat[0] = values[0];
            extraFloat[1] = values[1];
        }
    }

    private void checkValuesLength(int length){
        if (length == 0 || length > 2){
            throw new IllegalArgumentException("Test extra values ONLY accept 1 or 2 values.");
        }
    }

    protected static boolean isMatched(String regex, CharSequence inputValue){
        Pattern p = Pattern.compile(regex);
        return p.matcher(inputValue).matches();
    }
}
