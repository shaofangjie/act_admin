layui.config({
    base: '/asset/js/'
    , version: 'v1'
}).use('admin');
layui.use(['form', 'table', 'jquery', 'admin', 'layer'], function () {
    var $ = layui.jquery,
        form = layui.form,
        table = layui.table,
        layer = layui.layer,
        admin = layui.admin;

    form.verify({
        userName: function (value) {
            var reg = /^[\u4E00-\u9FA5A-Za-z0-9_]+$/;
            if (!isEmptyString(value) && !reg.test(value)) {
                return "用户名格式不合法";
            }
        },
        nickName: function (value) {
            var reg = /^[\u4E00-\u9FA5A-Za-z0-9_]+$/;
            if (!isEmptyString(value) && !reg.test(value)) {
                return "昵称格式不合法";
            }
        }
    });

    var queryParams = function () {
        var param = {adminSearchForm: {}};
        param.adminSearchForm.orderColumn = "whenCreated";
        param.adminSearchForm.orderDir = "asc";
        param.adminSearchForm.userName = $("#userName").val();
        param.adminSearchForm.nickName = $("#nickName").val();
        param.adminSearchForm.roleId = $("#roleId").val();
        return param;
    };

    var adminTable = table.render({
        elem: '#adminList',
        limits: [20, 40, 60, 100, 150, 300],
        limit: 20,
        skin: '#1E9FFF', //自定义选中色值
        loading: true,
        method: 'POST',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        page: true, //开启分页
        cols: [[
            {field: 'id', title: 'ID', width: '10%'},
            {field: 'userName', title: '用户名', width: '10%'},
            {field: 'nickName', title: '昵称', width: '10%'},
            {field: 'roleName', title: '角色', width: '15%'},
            {title: '状态', width: '10%', templet: '#enable'},
            {field: 'whenUpdated', title: '修改时间', width: '15%'},
            {field: 'whenCreated', title: '创建时间', width: '15%'},
            {title: '操作', width: '15%', templet: '#operation'}
        ]],
        url: '/authority/Admin/list',
        where: queryParams()
    });

    form.on('submit(searchBut)', function (data) {
        adminTable.reload({
            where: queryParams()
        });
    });

    table.on('tool(adminList)', function (obj) { //注：tool是工具条事件名，resourcesList是table原始容器的属性 lay-filter="对应的值"
        var layEvent = obj.event; //获得 lay-event 对应的值
        var data = obj.data; //获得当前行数据
        var dataId = data.id;
        console.log(layEvent + '-----' + dataId);
        if (layEvent === 'edit') {
            addPage('修改管理员', '/authority/Admin/edit/' + dataId);
        }
        if (layEvent === 'del') {
            layer.open({
                content: '您确定要删除 ' + data.roleName + ' 吗?',
                icon: 3,
                btn: ['确定', '取消'],
                yes: function (index, layero) {
                    $.ajax({
                        url:'/authority/Admin/del/'+dataId,
                        method:'GET',
                        success:function(data){
                            if(data.errcode === 0){
                                layer.msg(data.msg, {time: 2000, icon:1});
                                adminTable.reload({
                                    where: queryParams()
                                });
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
                }
            });
        }


    });


});