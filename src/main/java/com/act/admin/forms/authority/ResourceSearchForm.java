package com.act.admin.forms.authority;


import com.act.admin.commonutil.validation.EmptyOrPattern;
import com.act.admin.constraints.RegexpConsts;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-31
 * Time: 10:09 PM
 */

public class ResourceSearchForm {

    @EmptyOrPattern(regexp = RegexpConsts.SOURCENAME, message = "资源名格式不合法")
    private String resourceName;
    @EmptyOrPattern(regexp = RegexpConsts.ORDERCOLUMN, message = "排序字段不合法")
    private String orderColumn;
    @EmptyOrPattern(regexp = RegexpConsts.ORDERDIR, message = "排序方式不合法")
    private String orderDir;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public String getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(String orderDir) {
        this.orderDir = orderDir;
    }
}
