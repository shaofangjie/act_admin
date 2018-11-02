layui.config({
    base: '/asset/js/'
    , version: 'v1'
}).use('admin');
layui.use(['form', 'table', 'jquery', 'admin'], function() {
    var $ = layui.jquery,
        table = layui.table,
        form = layui.form,
        admin = layui.admin;

    form.verify({
        iconfont: function (value) {
            var reg = /^&#[A-Za-z0-9]+;$/;
            if (isEmptyString(value)) {
                return "请输入资源图标";
            } else if(!reg.test(value)){
                return "资源图标格式不合法";
            }
        },
        resourceName: function (value) {
            var reg = /^[\u4E00-\u9FA5A-Za-z0-9_]{3,17}$/;
            if (isEmptyString(value)) {
                return "请输入资源名称";
            } else if(!reg.test(value)){
                return "资源名只能为4-18位的汉字字母数字下划线组合";
            }
        },
        resourceUrl: function (value) {
            var reg = /^[A-Za-z0-9/._-]+$/;
            if (isEmptyString(value)) {
                return "请输入资源路由";
            } else if(!reg.test(value)){
                return "资源路由格式不合法";
            }
        },
        resourceFun: function (value) {
            var reg = /^[A-Za-z0-9/._-]+$/;
            if (isEmptyString(value)) {
                return "请输入资源方法";
            } else if(!reg.test(value)){
                return "资源方法格式不合法";
            }
        },
        resourceOrder: function (value) {
            var reg = /^[0-9]*$/;
            if (isEmptyString(value)) {
                return "请输入资源排序ID";
            } else if(!reg.test(value)){
                return "资源排序ID格式不合法";
            }
        }

    });

    form.on('submit(add)', function (data) {
        $.ajax({
            url:'/authority/AdminResources/add',
            method:'POST',
            data:data.field,
            success:function(data){
                if(data.errcode === 0){
                    layer.msg(data.msg, {time: 2000, icon:5});
                    $("#reset").click();
                }else{
                    if(data.detail === 1){
                        if ($.trim(data.msg["resourceAddForm.resourcePid"]).length !== 0){
                            layer.msg(data.msg["resourceAddForm.resourcePid"], {time: 2000, icon:5});
                        } else if ($.trim(data.msg["resourceAddForm.resourceType"]).length !== 0) {
                            layer.msg(data.msg["resourceAddForm.resourceType"], {time: 2000, icon:5});
                        } else if ($.trim(data.msg["resourceAddForm.enable"]).length !== 0) {
                            layer.msg(data.msg["resourceAddForm.enable"], {time: 2000, icon:5});
                        } else if ($.trim(data.msg["resourceAddForm.iconfont"]).length !== 0) {
                            layer.msg(data.msg["resourceAddForm.iconfont"], {time: 2000, icon:5});
                        } else if ($.trim(data.msg["resourceAddForm.resourceName"]).length !== 0) {
                            layer.msg(data.msg["resourceAddForm.resourceName"], {time: 2000, icon:5});
                        } else if ($.trim(data.msg["resourceAddForm.resourceUrl"]).length !== 0) {
                            layer.msg(data.msg["resourceAddForm.resourceUrl"], {time: 2000, icon:5});
                        } else if ($.trim(data.msg["resourceAddForm.resourceFun"]).length !== 0) {
                            layer.msg(data.msg["resourceAddForm.resourceFun"], {time: 2000, icon:5});
                        } else if ($.trim(data.msg["resourceAddForm.resourceOrder"]).length !== 0) {
                            layer.msg(data.msg["resourceAddForm.resourceOrder"], {time: 2000, icon:5});
                        }

                    } else {
                        layer.msg(data.msg, {time: 2000, icon:5});
                    }

                }
            },
            error:function (data) {
            }
        });
        return false;
    });

});