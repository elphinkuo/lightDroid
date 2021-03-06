lightDroid
==========

This is an independence injection framework, which is created for building lighter faster Android app in a more agile way. There are 2 new concepts:Page and Task, just like the Fragment and Activity in Android normal develop, but with many optimization.

本框架要实现和解决的主要问题有：

  1 自主管理的UI界面栈
  
  2 明确的MVC模式，原Acitity中既有视图逻辑 也有控制逻辑，现在使用明确的Page与Controller将视图与控制分离解耦
  
  3 提升UI性能 有效降低Android应用的卡顿延迟现象
  
  4 对程序性能实现有效的统计与监控
  
  5 稳定性与健壮性
  
  6 单元测试

本框架的设计原则

  1 以来抽象而非具体实现 低耦合 可扩展 灵活度高
  
  2 真正意义的MVC模式 UI与逻辑明确分离
  
  3 遵循单一职责的设计 逻辑简单 提高可测性
  
  4 考虑后续可维护性



整体应用架构如下：


  ![App Architecture](https://raw.githubusercontent.com/elphinkuo/lightDroid/master/images/AppArchitecture.PNG)
  


            
有关UI界面栈
  实现在/framework/app/fpstack中
  
  
  ![fpstack figure](https://raw.githubusercontent.com/elphinkuo/lightDroid/master/images/fpstack_image1.PNG)
  
  
  ![fpstack framework architecture](https://raw.githubusercontent.com/elphinkuo/lightDroid/master/images/fpstack_image2.PNG)
  
  
  
  
有关MVC
  实现在/framework/app/mvc中
  
  ![MVC Map](https://raw.githubusercontent.com/elphinkuo/lightDroid/master/images/MVC.PNG)
  
  具体阐释稍后补上
