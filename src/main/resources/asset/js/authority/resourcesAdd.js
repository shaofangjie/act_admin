layui.config({
    base: '/asset/js/'
    , version: 'v1'
}).extend({
    iconPicker: 'module/iconPicker'
});
layui.use(['form', 'jquery', 'admin', 'iconPicker'], function () {
    var $ = layui.jquery,
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
            // console.log(data);
        }
    });

    form.verify({
        iconfont: function (value) {
            var reg = /^[A-Za-z0-9-]+$/;
            if(!isEmptyString(value) && !reg.test(value)){
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
            if(!isEmptyString(value) && !reg.test(value)){
                return "资源路由格式不合法";
            }
        },
        resourceFun: function (value) {
            var reg = /^[A-Za-z0-9/._-]+$/;
            if(!isEmptyString(value) && !reg.test(value)){
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

    form.on('select(resourcePid)', function(data){
        var resourceType = $("#resourcePid option:selected").attr('type');
    });

    form.on('select(resourceType)', function(data){
        var resourceType = data.value;
        var iconDiv = $("#iconDiv");
        if (resourceType == 0 ) {
            iconDiv.show();
            $("#urlDiv").hide();
            $("#funDiv").hide();
        } else if (resourceType == 1) {
            iconDiv.show();
            $("#urlDiv").show();
            $("#funDiv").show();
        } else {
            iconDiv.hide();
            $("#urlDiv").show();
            $("#funDiv").show();
        }
    });

    form.on('submit(add)', function (data) {
        $.ajax({
            url: '/authority/AdminResources/add',
            method: 'POST',
            data: data.field,
            success: function (data) {
                if (data.errcode === 0) {
                    layer.msg(data.msg, {time: 2000, icon: 1});
                    $("#reset").click();
                } else {
                    layer.msg(data.msg, {time: 2000, icon: 5});
                }
            },
            error: function (error) {
                data = JSON.parse(error.responseText);
                if (data.detail === 1) {
                    var errmsgs = data.msg;
                    var errstr = '';
                    for (var i in errmsgs) {
                        errstr += errmsgs[i] + '<br />';
                    }
                    layer.alert(errstr, {icon: 5});
                } else {
                    if (!isEmptyString(data.msg)) {
                        layer.msg(data.msg, {time: 2000, icon: 5});
                    } else {
                        layer.msg("操作失败", {time: 2000, icon: 5});
                    }
                }
            }
        });
        return false;
    });

});