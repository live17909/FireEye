package com.github.chenyoca.validation.runners;

/**
 * User: chenyoca@gmail.com
 * Date: 2014-06-26
 * Host
 */
public class HostRunner extends TestRunner{

    static final String HOST_REGEX = "^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,65}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$";


    public HostRunner(){
        super("请输入有效的主机地址！");
    }

    @Override
    public boolean test(CharSequence inputValue) {
        return isMatched(IPv4Runner.IPV4_REGEX, inputValue) || isMatched(HOST_REGEX, inputValue);
    }

}
