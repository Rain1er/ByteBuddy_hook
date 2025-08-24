# ByteBuddy_hook

## 功能
动态修改Java类的字节码，支持静态注入和动态注入两种方式。  

## 使用方法
`mvn clean package`
1. 静态注入：在JVM启动参数中加入 `-javaagent:agent.jar`  
2. 动态注入：`java -jar hook.jar`
> 注：需要将目标环境的jdk加入环境变量中

