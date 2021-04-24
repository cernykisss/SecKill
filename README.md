# SecKill
登录功能：1、密码两次md5加密 前端到后端一次 后端到数据库一次 每个用户用一个salt
         2、分布式session：第一种方法：通过第三方redis存用户信息，实现不同客户端的访问 e.g. key: user:1g31fy431432f value: 用户信息的json格式 其中需要配置redis的序列化 不然存的是二进制字节码
                           第二种方法：springsession
         3、参数校验使用注解 添加validation依赖 展示错误信息使用全局的异常处理 
         4、自定义用户参数userargumentresolver 在controller入参前做好校验 判断user是否存在 获取用户信息 最后传入controller
在数据库中做了用户表 商品表 秒杀商品表 通过商品表的id与秒杀商品表进行外键关联 后用jmeter在win 与 Linux下进行压测 发现出现了超卖问题
         
         
