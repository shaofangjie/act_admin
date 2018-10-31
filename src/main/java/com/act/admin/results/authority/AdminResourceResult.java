package com.act.admin.results.authority;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-31
 * Time: 11:18 PM
 */
public class AdminResourceResult {

    private Long id;
    private int sourceType;
    private String sourceName;
    private int sourceOrder;
    private String sourceFunction;
    private int enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getSourceOrder() {
        return sourceOrder;
    }

    public void setSourceOrder(int sourceOrder) {
        this.sourceOrder = sourceOrder;
    }

    public String getSourceFunction() {
        return sourceFunction;
    }

    public void setSourceFunction(String sourceFunction) {
        this.sourceFunction = sourceFunction;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }
}
