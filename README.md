lightDroid
==========

This is an independence injection framework, which is created for building lighter faster Android app in a more agile way. There are 2 new concepts:Page and Task, just like the Fragment and Activity in Android normal develop, but with many optimization.

The main purpose and achievements of this repo are:

  1 Self-managed UI stack, independet from native design of Android system
  
  2 Explicit MVC mode. The original Activity in Android system contains both View and Controller, lightDroid decoupled the View and Controller in Android Activity, as Page and Controller.
  
  3 Based on the decoupled Page and Controller, improve the launching time of Page, than Activity. Effectively reduce the response time of Android UI.
  
  4 More efficient monitor and statistics for Andorid Application.
  
  5 Improve stability and robustness
  
  6 Unit test.



Whole Structure as：


  ![App Architecture](https://raw.githubusercontent.com/elphinkuo/lightDroid/master/images/AppArchitecture.PNG)
  


            
UI stack:
  Implementation is in /framework/app/fpstack
  
  
  ![fpstack figure](https://raw.githubusercontent.com/elphinkuo/lightDroid/master/images/fpstack_image1.PNG)
  
  
  ![fpstack framework architecture](https://raw.githubusercontent.com/elphinkuo/lightDroid/master/images/fpstack_image2.PNG)
  
  
  
  
MVC
  Implementation is in /framework/app/mvc中
  
  ![MVC Map](https://raw.githubusercontent.com/elphinkuo/lightDroid/master/images/MVC.PNG)
  


Design Principles

  1 Rely on abstraction rather than concrete implementation Low coupling, extensible, high flexibility
  
  2 True MVC, UI decoupled with Controller
  
  3 Follow a single responsibility design, simple logic, improve testability
  
  4 Consider maintainability
