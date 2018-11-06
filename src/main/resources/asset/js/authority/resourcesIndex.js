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
        sourceName: function (value) {
            var reg = /^[\u4E00-\u9FA5A-Za-z0-9_]{3,17}$/;
            if (!isEmptyString(value) && !reg.test(value)) {
                return "资源名格式不合法";
            }
        }
    });

    var queryParams = function () {
        var param = {resourceSearchForm: {}};
        param.resourceSearchForm.orderColumn = "whenCreated";
        param.resourceSearchForm.orderDir = "asc";
        param.resourceSearchForm.resourceName = $("#resourceName").val();
        return param;
    };

    var resourceTable = table.render({
        elem: '#resourcesList',
        limits: [20, 40, 60, 100, 150, 300],
        limit: 20,
        skin: '#1E9FFF', //自定义选中色值
        loading: true,
        method: 'POST',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        page: true, //开启分页
        cols: [[
            {field: 'id', title: 'ID', width: '10%'},
            {title: '资源类型', width: '10%', templet: '#type'},
            {field: 'sourceName', title: '资源名称', width: '20%'},
            {field: 'sourceOrder', title: '排序', width: '10%'},
            {field: 'sourceFunction', title: '资源方法', width: '30%'},
            {title: '是否启用', width: '10%', templet: '#enableOperation'},
            {title: '操作', width: '10%', templet: '#operation'}
        ]],
        url: '/authority/AdminResources/list',
        where: queryParams()
    });

    form.on('submit(searchBut)', function (data) {
        resourceTable.reload({
            where: queryParams()
        });
    });

    table.on('tool(resourcesList)', function (obj) { //注：tool是工具条事件名，resourcesList是table原始容器的属性 lay-filter="对应的值"
        var layEvent = obj.event; //获得 lay-event 对应的值
        var data = obj.data; //获得当前行数据
        var dataId = data.id;
        console.log(layEvent + '-----' + dataId);
        if (layEvent === 'edit') {
            addPage('修改权限资源', '/authority/AdminResources/edit/' + dataId);
        }
        if (layEvent === 'del') {
            layer.open({
                content: '您确定要删除 ' + data.sourceName + ' 吗?',
                icon: 3,
                btn: ['确定', '取消'],
                yes: function (index, layero) {
                    $.ajax({
                        url:'/authority/AdminResources/del/'+dataId,
                        method:'GET',
                        success:function(data){
                            if(data.errcode === 0){
                                layer.msg(data.msg, {time: 2000, icon:1});
                                resourceTable.reload({
                                    where: queryParams()
                                });
                            }else{
                                layer.msg(data.msg, {time: 2000, icon:5});
                            }
                        },
                        error:function (error) {
                            data = JSON.parse(error.responseText);
                            layer.msg(data.msg, {time: 2000, icon:5});
                        }
                    });
                }
            });
        }


    });


});