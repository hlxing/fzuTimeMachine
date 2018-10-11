# fzuTimeMachine

## 项目介绍

值此福州大学六十周年校庆之际，开发小程序项目【福大时光机】，让全球福大人共享美好福大记忆，共庆母校六十诞辰。(By 聪明又好看的叶经理)

## 功能逻辑流程图

![功能逻辑流程图](https://ws1.sinaimg.cn/large/7343c247ly1fvjwc5uxyij20ur0fvt9e.jpg)

## 项目成员

小黄数名，小叶数名，小钱一名，小沈一名

## 后端

### 参与开发

规范文档参见[BACKEND.md](https://github.com/hlxing/fzuTimeMachine/blob/master/BACKEND.md)，任何未遵循该规范的代码将拒绝合并。Pull Request前请仔细阅读该文档，如有问题可提ISSUE或私聊我

### 环境准备

JDK1.8 + Intellij IDEA + Mysql+Redis + Natapp(可选)

#### 1.安装 lombok 插件(魔法糖工具),减少冗余代码,并进行相关配置

#### 2.创建数据库 timeMachine,导入[sql文件](https://github.com/hlxing/fzuTimeMachine/blob/master/timemachine.sql),增加用户 timeMachine,密码 123456,并分配其相应权限

#### 3.安装并启动 redis

#### 4.安装并启动 natapp(可选,小程序授权回调及七牛云oos上传回调专用),并修改 application.yml

#### 5.启动项目,打开[swagger2接口页面](http://localhost/swagger-ui.html#/)

### 接口文档

接口文档参见[INTERFACE.md](https://github.com/hlxing/fzuTimeMachine/blob/master/INTERFACE.md)，包括接口概要及其具体说明
