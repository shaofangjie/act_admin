layui.config({
    base: '/asset/js/'
    , version: 'v1'
}).extend({
    iconPicker: 'module/iconPicker/iconPicker'
});
layui.use(['iconPicker', 'form', 'table', 'jquery', 'admin'], function() {
    var $ = layui.jquery,
        table = layui.table,
        form = layui.form,
        iconPicker = layui.iconPicker,
        admin = layui.admin;

    iconPicker.render({
        // 选择器，推荐使用input
        elem: '#iconPicker',
        // 数据类型：fontClass/unicode，推荐使用fontClass
        type: 'fontClass',
        // 是否开启搜索：true/false
        search: true,
        // 是否开启分页
        page: true,
        // 每页显示数量，默认12
        limit: 12,
        // 点击回调
        click: function (data) {
            console.log(data);
        }
    });

    iconPicker.checkIcon('iconPicker', $("#iconPicker").val());

    form.verify({
        iconfont: function (value) {
            var reg = /^[A-Za-z0-9-]+$/;
            if (isEmptyString(value)) {
                return "请选择资源图标";
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

    form.on('submit(edit)', function (data) {
        $.ajax({
            url:'/authority/AdminResources/edit',
            method:'POST',
            data:data.field,
            success:function(data){
                if(data.errcode === 0){
                    layer.msg(data.msg, {time: 2000, icon:1});
                }else{
                    layer.msg(data.msg, {time: 2000, icon:5});
                }
            },
            error:function (error) {
                data = JSON.parse(error.responseText);
                if(data.detail === 1){
                    if (!isEmptyString(data.msg["resourceAddForm.resourcePid"])){
                        layer.msg(data.msg["resourceAddForm.resourcePid"], {time: 2000, icon:5});
                    } else if (!isEmptyString(data.msg["resourceAddForm.resourceType"])) {
                        layer.msg(data.msg["resourceAddForm.resourceType"], {time: 2000, icon:5});
                    } else if (!isEmptyString(data.msg["resourceAddForm.resourceType"])) {
                        layer.msg(data.msg["resourceAddForm.resourceType"], {time: 2000, icon:5});
                    } else if (!isEmptyString(data.msg["resourceAddForm.enable"])) {
                        layer.msg(data.msg["resourceAddForm.enable"], {time: 2000, icon:5});
                    } else if (!isEmptyString(data.msg["resourceAddForm.iconfont"])) {
                        layer.msg(data.msg["resourceAddForm.iconfont"], {time: 2000, icon:5});
                    } else if (!isEmptyString(data.msg["resourceAddForm.resourceName"])) {
                        layer.msg(data.msg["resourceAddForm.resourceName"], {time: 2000, icon:5});
                    } else if (!isEmptyString(data.msg["resourceAddForm.resourceUrl"])) {
                        layer.msg(data.msg["resourceAddForm.resourceUrl"], {time: 2000, icon:5});
                    } else if (!isEmptyString(data.msg["resourceAddForm.resourceFun"])) {
                        layer.msg(data.msg["resourceAddForm.resourceFun"], {time: 2000, icon:5});
                    } else if (!isEmptyString(data.msg["resourceAddForm.resourceOrder"])) {
                        layer.msg(data.msg["resourceAddForm.resourceOrder"], {time: 2000, icon:5});
                    }

                } else {
                    layer.msg(data.msg, {time: 2000, icon:5});
                }
            }
        });
        return false;
    });

});