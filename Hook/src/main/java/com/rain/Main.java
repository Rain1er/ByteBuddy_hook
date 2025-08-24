package com.rain;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;
import net.bytebuddy.agent.ByteBuddyAgent;


public class Main {
    public static void main(String[] args) {
        try {
            // 执行 jps -l 命令，如果这里环境变量不存在。那么手动执行/path/to/jdk/bin/jps -l
            Process process = Runtime.getRuntime().exec("jps -l");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            System.out.println("可用的 JVM 进程：");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 用户选择 PID
            Scanner scanner = new Scanner(System.in);
            System.out.print("请输入要 hook 的 JVM PID：");
            String pid = scanner.nextLine();

            // 用户输入 agent 路径
            System.out.print("请输入 agent 的路径：");
            String agentPath = scanner.nextLine();
            File agentJar = new File(agentPath);

            // Attach agent
            ByteBuddyAgent.attach(agentJar, pid);
            System.out.println("Agent 已附加到进程 " + pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
