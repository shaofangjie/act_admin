layui.config({
    base: '/asset/js/'
    , version: 'v1'
}).extend({
    treetable: 'module/treetable'
});
layui.use(['form', 'table', 'jquery', 'admin', 'layer', 'treetable'], function () {
    var $ = layui.jquery,
        form = layui.form,
        table = layui.table,
        treetable = layui.treetable,
        layer = layui.layer,
        admin = layui.admin;

    var queryParams = function () {
        var param = {resourceSearchForm: {}};
        param.resourceSearchForm.resourceName = $("#resourceName").val();
        return param;
    };

    var initTreeTable = function () {
        treetable.render({
            elem: '#resourcesList',
            treeColIndex: 1,
            treeSpid: 0,
            treeIdName: 'id',
            treePidName: 'pid',
            loading: true,
            url: '/authority/AdminResources/list',
            page: false,
            cols: [[
                {field: 'id', title: 'ID', width: '10%'},
                {field: 'sourceName', title: '资源名称', width: '20%'},
                {title: '资源类型', width: '10%', templet: '#type'},
                {field: 'sourceOrder', title: '排序', width: '10%'},
                {field: 'sourceFunction', title: '资源方法', width: '30%'},
                {title: '是否启用', width: '10%', templet: '#enable'},
                {title: '操作', width: '10%', templet: '#operation'}
            ]],
            where: queryParams()
        });
    };

    initTreeTable();

    $("#addButton").click(function () {

        var w = ($(window).width() * 0.9);
        var h = ($(window).height() - 50);

        layer.open({
            type: 2,
            area: [w + 'px', h + 'px'],
            fix: false, //不固定
            maxmin: true,
            shadeClose: true,
            shade: 0.4,
            title: '添加权限资源',
            content: '/authority/AdminResources/add'
        });

        $("a.layui-layer-close").click(function () {
            initTreeTable();
        });

    });

    table.on('tool(resourcesList)', function (obj) { //注：tool是工具条事件名，resourcesList是table原始容器的属性 lay-filter="对应的值"
        var layEvent = obj.event; //获得 lay-event 对应的值
        var data = obj.data; //获得当前行数据
        var dataId = data.id;
        console.log(layEvent + '-----' + dataId);
        if (layEvent === 'edit') {

            var w = ($(window).width() * 0.9);
            var h = ($(window).height() - 50);

            layer.open({
                type: 2,
                area: [w + 'px', h + 'px'],
                fix: false, //不固定
                maxmin: true,
                shadeClose: true,
                shade: 0.4,
                title: '修改权限资源',
                content: '/authority/AdminResources/edit/' + dataId
            });

            $("a.layui-layer-close").click(function () {
                initTreeTable();
            });

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
                                initTreeTable();
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