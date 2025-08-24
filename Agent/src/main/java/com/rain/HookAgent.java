package com.rain;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.named;

class HookAgent {
    // 启动时注入入口，在JVM启动参数中加入 -javaagent:agent.jar
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("[Agent] premain called with args: " + agentArgs);
        init(agentArgs, instrumentation);
    }

    // 运行时 attach 入口，配合 ByteBuddyAgent.attach 使用
    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("[Agent] agentmain called with args: " + agentArgs);
//        System.out.println(instrumentation.isRetransformClassesSupported());
        init(agentArgs, instrumentation);
    }

    /**
     * 公共初始化逻辑，premain/agentmain 都会调用
     */
    private static void init(String agentArgs, Instrumentation instrumentation) {
        new AgentBuilder.Default()
                .with(AgentBuilder.InjectionStrategy.UsingUnsafe.INSTANCE)// 避免因为类加载器隔离导致偶发失败，尤其是在多应用、多模块的 Tomcat 部署中
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .disableClassFormatChanges()
                .type(ElementMatchers.nameStartsWith("com.rain.pojo.User"))
                .transform((builder, td, cl, jm, pd) -> builder.method(named("getAge")).intercept(FixedValue.value("hooked!")))
                .installOn(instrumentation);

    }
}
