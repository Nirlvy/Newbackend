package com.Nirlvy.Newbackend.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

public class CodeGenerator {

    public static void main(String[] args) {
        generate();
    }

    private static void generate() {
        FastAutoGenerator
                .create("jdbc:mysql://localhost:3306/newFreezer?serverTimeZone=GMT%2b8?allowPublicKeyRetrieval=true",
                        "root", "520624iL")
                .globalConfig(builder -> {
                    builder.author("Nirlvy") // 设置作者
                            // .enableSwagger() // 开启 swagger 模式
                            .outputDir(
                                    "/home/nirlvy/Documents/smart_freezer_total/Newbackend/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.Nirlvy.Newbackend") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    "/home/nirlvy/Documents/smart_freezer_total/Newbackend/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> builder.addInclude("FeedBack") // 设置需要生成的表名
                        .addTablePrefix("t_", "c_")// 设置过滤表前缀

                        .entityBuilder()
                        .enableFileOverride()
                        .enableLombok()

                        .mapperBuilder()
                        .enableFileOverride()

                        .serviceBuilder()
                        .enableFileOverride()

                        .controllerBuilder()
                        .enableRestStyle()
                        .enableFileOverride())
                .execute();
    }
}
