# SecKill
登录功能：1、密码两次md5加密 前端到后端一次 后端到数据库一次 每个用户用一个salt 2、分布式session：第一种方法：通过第三方redis存用户信息，实现不同客户端的访问 e.g. key: user:1g31fy431432f value: 用户信息的json格式 其中需要配置redis的序列化 不然存的是二进制字节码 第二种方法：springsession 3、参数校验使用注解 添加validation依赖 展示错误信息使用全局的异常处理 4、自定义用户参数userargumentresolver 在controller入参前做好校验 判断user是否存在 获取用户信息 最后传入controller 在数据库中做了用户表 商品表 秒杀商品表 通过商品表的id与秒杀商品表进行外键关联 后用jmeter在win 与 Linux下进行压测 发现出现了超卖问题 页面优化：1.把商品页 和商品详情页html作为字符串存入redis 先判断redis中有没有对应的key 有的话直接获取 没有的话通过thymeleafviewresolver手动渲染 再存入redis 2.商品详情页面静态化：通过detailvo传参到前端页面 前端通过ajax接受 手动渲染 秒杀页面静态化：控制器返回respbean.success()/respbean.error(RespBeanEnum.ERROR) 解决超卖：1.在更新数据库商品库存时候增强健壮性， 先判断是否库存大于0， 如果大于0在库存减一, 如果更新结果result为0， 那就不创建订单 2.请求都是并行的，为了解决同一个用户在同一时间秒杀多个商品，在数据库订单表中加入唯一索引：商品id+用户id 3.秒杀订单存入redis ordercontroller通过redis获取秒杀订单

秒杀前把数据预加载进redis 1.通过redis预减库存 减少数据库访问 2.内存标记 减少redis访问 3.请求进入mq 异步操作 异步下单 （4.增强数据库 分库分表
         
