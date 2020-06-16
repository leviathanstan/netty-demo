### 模块
- nat，尝试使用udp nat打洞。失败告终，推测为多重nat等因素导致，过于复杂不再深入
- src，学习netty过程中的demo，绝大部分来自《netty in action》
- longpoll/longpollretry/resend/im，尝试使用netty进行通信的实践

### 编写历程
1. module:longpoll
    - 使用IdleStateHandler来实现维持心跳，不过没什么意义，因为如果连接断开也无能为力
2. module:longpollretry
    - 客户端：在longpoll基础上，为心跳消息添加listener，如果心跳失败则回调重连逻辑
    - 服务端：如果心跳失败，主动和客户端断开连接
3. module:resend
    - 为消息发送结果添加listener，当发送失败则添加到ScheduledExecutorService以延迟执行下一次重发
4. module:im(**待完善**)
    - 缝合怪，组合longpollretry和resend模块，实现重连和重发（仅客户端）
    - 下沉netty逻辑，仅开放通信接口
