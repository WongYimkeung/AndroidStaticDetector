# AndroidStaticDetector

### 版本发布日志

|    日期    |  版本  | 更新内容                               |
| :--------: | :----: | :------------------------------------- |
| 2021.08.16 | V1.0.0 | Android静态代码检测器初版              |
| 2021.08.20 | V1.1.0 | 支持分包Apk检测                        |
| 2021.09.07 | V1.2.0 | 添加权限检测功能，使用JSON作为输入格式 |

## Android静态检测器实现

### 需求背景

- SDK集成后，无法知道接口是否有被调用
- 隐私相关代码和权限声明检测

### 环境准备

- Java开发环境：JDK
- Android开发环境：Android SDK
- 反编译工具：apktool

### 预备知识

- Android项目构成

- Apk文件内容

- Apk反编译

- smali

  类定义：L包名/类名;

  > Landroid/telephony/TelephonyManager;

  方法定义：方法名称(方法参数)方法返回值

  >.method ... getImei()Ljava/lang/String;

  类的方法调用方式：L包名/类名;->方法名称(方法参数)方法返回值

  >Landroid/telephony/TelephonyManager;->getImei()Ljava/lang/String;

### 实现思路

​	找到目标smali代码，与Apk反编译出来的smali文件进行文本匹配。

### 实现流程

1. 处理配置参数

2. 反编译APK

3. 处理目标文件（包括目标接口文件、隐私相关代码和权限声明）

4. 处理AndroidManifest.xml

5. 处理smali源码

6. 输出结果

### 具体实现

1. ArgumentsProcessor-处理配置参数

2. DecompileProcessor-反编译APK

   > apktool d xxx.apk

   AndroidManifest.xml

   smali

3. TargetProcessor-处理目标文件（包括目标接口文件、隐私相关代码和权限声明）

   目标接口文件解析：找到目标接口文件所在位置，解析目标接口文件定义的全部方法（smali方法定义），构造MethodInfo

   隐私相关代码：解析JSON文件获取，构造MethodInfo

   和权限声明：解析JSON文件获取，构造PermissionInfo

4. ManifestProcessor-处理AndroidManifest.xml

   XML解析：权限声明格式uses-permission

5. AnalyzeProcessor-处理smali源码

   文本匹配和解析，构造CallInfo

6. PdfProcessor-输出结果

### 注意事项

- 不要使用混淆后的包进行检验，这样无法知道隐私代码是在具体哪个第三方SDK中调用的
- 实现过程要注意分包（MultiDex）的情况处理，即反编译后多个smali开头的目录情况

## Android静态检测器使用

### 工具文件说明

- apktool & apktool.jar：反编译工具

- config.properties：配置文件

- source.json：需要对比的smali代码，目前包含隐私相关的代码，JSON格式

- permiassion.json：需要对比的权限声明，JSON格式

- ApkAnalyzer-1.0.0.jar：Android静态扫描器

### 配置参数说明

- apkPath：需要扫描的Apk路径

- excludeDir：不需要扫描的包路径

- sourceFile：需要对比的smali代码文件路径

- permissionFile：需要对比的权限声明文件路径

- interfaceFile：需要解析的接口文件路径，指Apk里面

### 使用说明

> #使用方式一：
>
> java -jar ApkAnalyzer-1.0.0.jar configFile=config.properties
>
> #使用方式二：
>
> java -jar ApkAnalyzer-1.0.0.jar apkPath=xxx.apk sourceFile=source.json permissionFile=permission.json
