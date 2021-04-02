package com.atguigu.akka.yellowchicken.client

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.atguigu.akka.yellowchicken.common.{ClientMessage, ServerMessage}
import com.typesafe.config.ConfigFactory

import scala.io.StdIn

class CustomerActor(serverHost:String,serverPort:Int) extends  Actor{
  //定义一个 YellowChickenServerRef
  var serverActorRef: ActorSelection= _

  //在Actor 中的方法 PreStart 方法，会在 actor 运行前执行
  //在akka 开发中，将初始化的工作，放在preStart 方法
  override def preStart(): Unit = {
    println("preStart() 执行")
    serverActorRef = context.actorSelection(s"akka.tcp://Server@${serverHost}:${serverPort}/user/YellowChickenServer")

    println("serverActorRef=" + serverActorRef)
  }

  override def receive: Receive = {
    case "start" => println("start，客户端运行，可以咨询问题")
    case mes:String => {
      //转发给服务器 小黄鸡客服
      //使用了样例类 ClientMessage case class 的apply方法
      serverActorRef ! ClientMessage(mes)
    }
      //如果接收到服务器的回复
    case ServerMessage(mes)=>{
      println(s"收到小黄鸡客服(server)的回复 : $mes")
    }

  }
}

//主程序-入口
object CustomerActor extends  App {
  val (clienHost,clientPort,serverHost,serverPort)=
    ("127.0.0.1",9990,"127.0.0.1",9999)

  //创建 config 对象，指定协议类型，监听 ip 和 端口
  val config= ConfigFactory.parseString(
    s"""
       |akka.actor.provider="akka.remote.RemoteActorRefProvider"
       |akka.remote.netty.tcp.hostname=$clienHost
       |akka.remote.netty.tcp.port=$clientPort
    """.stripMargin
  )

  //创建 ActorSystem
   val clientActorSystem: ActorSystem
  = ActorSystem("client", config)
  //创建 CustomerActor 的实例和引用

   val customerActorRef: ActorRef
   = clientActorSystem.actorOf(Props(
     new CustomerActor(serverHost, serverPort)), "CustomerActor")

  //启动 customerActorRef
  customerActorRef ! "start"

  //客户端可以发送给消息给服务器
  while (true){
    println("输入要自问的问题")
    val mes=StdIn.readLine()
    customerActorRef ! mes
  }


}