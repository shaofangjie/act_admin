layui.config({
    base: '/asset/js/'
    , version: 'v1'
}).extend({
    authtree: 'module/authtree'
});
layui.use(['form', 'jquery', 'authtree', 'admin'], function() {
    var $ = layui.jquery,
        form = layui.form,
        admin = layui.admin,
        authtree = layui.authtree;

    var roleId = $("#roleId").val();

    $.ajax({
        url: '/authority/AdminRole/resourceTree/'+roleId,
        dataType: 'json',
        success: function (data) {
            // 渲染时传入渲染目标ID，树形结构数据（具体结构看样例，checked表示默认选中），以及input表单的名字
            authtree.render('#authTree', data.auth, {
                inputname: 'authids[]',
                layfilter: 'lay-check-auth',
                openall: false
            });
        }
    });

    form.verify({
        roleName: function (value) {
            var reg = /^[\u4E00-\u9FA5A-Za-z0-9_]{3,17}$/;
            if (isEmptyString(value)) {
                return "请输入角色名称";
            } else if(!reg.test(value)){
                return "角色名只能为4-18位的汉字字母数字下划线组合";
            }
        }
    });

    form.on('submit(edit)', function (data) {

        var authStr = authtree.getChecked('#authTree').join();

        if (isEmptyString(authStr)) {
            layer.msg("权限不能为空", {time: 2000, icon:5});
            return false;
        }

        $.ajaxSetup({
            data:{
                "roleEditForm.authStr": authtree.getChecked('#authTree').join()
            }
        });
        $.ajax({
            url:'/authority/AdminRole/edit',
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
                    var errmsgs = data.msg;
                    var errstr = '';
                    for (var i in errmsgs) {
                        errstr += errmsgs[i] + '<br />';
                    }
                    layer.alert(errstr, {icon: 5});
                } else {
                    if (!isEmptyString(data.msg))  {
                        layer.msg(data.msg, {time: 2000, icon:5});
                    } else {
                        layer.msg("操作失败", {time: 2000, icon:5});
                    }
                }
            }
        });
        return false;
    });

});