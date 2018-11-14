# act_admin

##项目初始化步骤：
```
1、git拉取代码到本地
2、导入项目到IDEA
3、修改/src/main/resources/conf/dev/db.properties配置文件中的:
    db.url=
    db.username=
    db.password=    
    db.ddl.drop=true(只需第一次运行设置为true)
    db.ddl.create=true(只需第一次运行设置为true)
4、运行com.act.admin.AppEntry中的main方法即可启动服务并自动装载初始化数据
5、第一次运行并生成数据库后可将db.properties配置文件中的关于初始化数据库配置项修改为false：
    db.ddl.drop=false
    db.ddl.create=false
6、浏览器访问 http://localhost:8000/
    用户名：admin
    密码：admin
    
    注意：验证码dev模式下后台不校验，随便填写四位字符即可。

```
##演示地址：[act-admin.ga](http://act-admin.ga/)