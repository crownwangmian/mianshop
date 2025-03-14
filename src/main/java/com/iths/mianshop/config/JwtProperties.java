package com.iths.mianshop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;


@ConfigurationProperties(prefix = "wm.jwt")
public class JwtProperties {
    private String location;
    private String alias;
    private String password;
    private Duration tokenTTL;
    private List<String> excludePaths;

    public JwtProperties() {
    }

    public JwtProperties(String location, String alias, String password, Duration tokenTTL, List<String> excludePaths) {
        this.location = location;
        this.alias = alias;
        this.password = password;
        this.tokenTTL = tokenTTL;
        this.excludePaths = excludePaths;
    }

    /**
     * 获取
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 获取
     * @return alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 设置
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 获取
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     * @return tokenTTL
     */
    public Duration getTokenTTL() {
        return tokenTTL;
    }

    /**
     * 设置
     * @param tokenTTL
     */
    public void setTokenTTL(Duration tokenTTL) {
        this.tokenTTL = tokenTTL;
    }

    /**
     * 获取
     * @return excludePaths
     */
    public List<String> getExcludePaths() {
        return excludePaths;
    }

    /**
     * 设置
     * @param excludePaths
     */
    public void setExcludePaths(List<String> excludePaths) {
        this.excludePaths = excludePaths;
    }

    public String toString() {
        return "JwtProperties{location = " + location + ", alias = " + alias + ", password = " + password + ", tokenTTL = " + tokenTTL + ", excludePaths = " + excludePaths + "}";
    }
}
