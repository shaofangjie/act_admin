layui.use(['form', 'admin', 'jquery'], function () {
    var form = layui.form;
    var $ = layui.jquery;

    form.verify({
        username: function (value) {
            var reg = /^[a-zA-Z0-9]\w{3,19}$/;
            if (isEmptyString(value)) {
                return "请输入用户名";
            } else if(!reg.test(value)){
                return "帐号只能为4-20位的数字字母下划线组合，且不能以下划线开头。";
            }
        },
        password: function (value) {
            if (isEmptyString(value)) {
                return "请输入密码";
            }
        },
        captcha: function (value) {
            if (isEmptyString(value)) {
                return "请输入验证码";
            }
        }
    });

    form.on('submit(login)', function (data) {
        $.ajaxSetup({
            data:{
                "loginForm.password": hex_md5($("#password").val())
            }
        });
        $.ajax({
            url:'/login',
            method:'POST',
            data:data.field,
            success:function(data){
                if(data.errcode === 0){
                    window.location.href='/'
                }else{
                    if(data.detail === 1){
                        if (!isEmptyString(data.msg["loginForm.userName"])){
                            layer.msg(data.msg["loginForm.userName"], {time: 2000, icon:5});
                        } else if (!isEmptyString(data.msg["loginForm.password"])) {
                            layer.msg(data.msg["loginForm.password"], {time: 2000, icon:5});
                        } else if (!isEmptyString(data.msg["loginForm.captcha"])) {
                            layer.msg(data.msg["loginForm.captcha"], {time: 2000, icon:5});
                        }

                    } else {
                        layer.msg(data.msg, {time: 2000, icon:5});
                    }

                    $(".captcha").click();
                }
            },
            error:function (data) {
            }
        });
        return false;
    });

});