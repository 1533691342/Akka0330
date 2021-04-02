package com.atguigu.akka.actor

/**
 * 说明：
 * 当程序执行
 * aActorRef = actorFactory.actorOf(Props[AActor],"aActor") 会完成如下任务：
 * 1、actorFactory 是 actorSystem("actorFactory") 这样创建的
 * 2、这里的 Props[AActor] 会使用反射机制，创建一个 AActor 对象，如果是
 * actorFactory.actorOf(Props (new AActor[bActorRef]),"aActorRef") 形式，就是
 * 使用 new 的方式创建一个 AActor 对象，注意 Props() 是小括号
 * 3、会 创建一个 AActor 对象的代理对象 aActorRef ，使用 aActorRef 才能发送消息
 * 4、会在底层创建 Dispather Message , 是一个线程池，用于分发消息，消息是发送
 * 到对应的 Actor 的 MailBox
 **/


/**
 * 说明：
 * 1、当继承了 Actor 之后，就是一个 Actor ，核心方法 Receive ，方法重写
 */
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
class SayHelloActor extends  Actor{
  /**
   * 说明：
   * 1、receive 方法会被 Actor 的 MailBox(实现了 Runnable 接口)调用
   * 2、当 Actor 的 MailBox 接收到消息，就会调用 receive
   * 3、type Receive = PartialFunction[Any,Unit]
   * @return
   */
  override def receive: Receive = {
    case "hello" =>println("收到 hello ，回应 hello too:)")
    case "ok" =>println("收到 ok ，回应 ok too:)")
    case "exit" =>{
      println("接收到 exit 指令，退出系统")
      context.stop(self) //停止 actoref
      context.system.terminate() //退出 actorSystem
    }
    case _ =>println("匹配不了")



  }
}

object  SayHelloActorDemo{
  /**
   * 1、先创建一个 ActorSystem ,专门用于创建 Actor
   */
  private val actorFactory=ActorSystem("actoryFactory")
  /**
   * 2、创建一个 Actor 的同时，返回 Actor 的 ActorRef
   * 说明：
   * 1）Props[SayHelloActor] 创建了一个 SayHelloActor 实例，使用反射
   * 2）"sayHelloActor" 给 actor 取名
   * 3）sayHelloActorRef:ActorRef  就是  Props[SayHelloActor] 的 ActorRef
   * 4）创建的  sayHelloActor 实例被 ActorSystem 接管
   */
  private val sayHelloActorRef:ActorRef
  =actorFactory.actorOf(Props[SayHelloActor],"sayHelloActor")

  def main(args: Array[String]): Unit = {
    //给 SayHelloActor 发消息（邮箱）
    sayHelloActorRef ! "hello"
    sayHelloActorRef ! "ok"
    sayHelloActorRef ! "ok~"
    //退出 ActorSystem
    sayHelloActorRef ! "exit"
  }

}
