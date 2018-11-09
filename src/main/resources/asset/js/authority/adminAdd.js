layui.config({
    base: '/asset/js/'
    , version: 'v1'
}).use('admin');
layui.use(['form', 'jquery', 'admin'], function () {
    var $ = layui.jquery,
        form = layui.form,
        admin = layui.admin;

    form.verify({
        userName: function (value) {
            var reg = /^[a-zA-Z0-9]\w{3,19}$/;
            if (isEmptyString(value)) {
                return "请输入用户名";
            } else if(!reg.test(value)){
                return "用户名只能为4-18位的汉字字母数字下划线组合";
            }
        },
        password: function (value) {
            if (isEmptyString(value)) {
                return "请输入密码";
            }
        },
        nickName: function (value) {
            var reg = /^[\u4E00-\u9FA5A-Za-z0-9_]{1,11}$/;
            if (isEmptyString(value)) {
                return "请输入昵称";
            } else if(!reg.test(value)){
                return "用户名只能为2-12位的汉字字母数字下划线组合";
            }
        }
    });

    form.on('submit(add)', function (data) {
        $.ajaxSetup({
            data:{
                "adminAddForm.password": hex_md5($("#password").val())
            }
        });
        $.ajax({
            url: '/authority/Admin/add',
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