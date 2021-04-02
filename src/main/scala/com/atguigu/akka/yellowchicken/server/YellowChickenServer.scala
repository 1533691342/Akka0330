package com.atguigu.akka.yellowchicken.server

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.atguigu.akka.yellowchicken.common.{ClientMessage, ServerMessage}
import com.typesafe.config.ConfigFactory

class YellowChickenServer extends  Actor{
  override def receive: Receive = {
    case "start" => println("start 小黄鸡客服开始工作.......")

    //如果接收到 ClientMessage
    case ClientMessage(mes) => {
      mes match {

      //使用match -- case 匹配（模糊）
      case "大数据学费"
      => sender() ! ServerMessage("35000RMB")
      case "学校地址"
      => sender() ! ServerMessage("xx路xx号")
      case "学习什么技术"
      => sender() ! ServerMessage("大数据 前端 python")
      case _ => ServerMessage("你说的是什么")
    }
  }

  }
}

//主程序-入口
object YellowChickenServer extends App{

  val host="127.0.0.1" //服务端 IP 地址
  val port=9999
  //创建 config 对象，指定协议类型，监听 ip 和 端口
  val config= ConfigFactory.parseString(
    s"""
    |akka.actor.provider="akka.remote.RemoteActorRefProvider"
    |akka.remote.netty.tcp.hostname=$host
    |akka.remote.netty.tcp.port=$port
    """.stripMargin
  )


  //创建 ActorSystem
  // URL 统一资源定位、
  //Server 是 Actor 的名字
  val serverActorSystem: ActorSystem = ActorSystem("Server",config)
  //创建 YellowChickenServer 的 actor 和返回 actorRef
  // YellowChickenServer 是 ActorSystem 的名字；
  val yellowChickenServerRef:ActorRef=
  serverActorSystem.actorOf(Props[YellowChickenServer],"YellowChickenServer")

//启动
  yellowChickenServerRef ! "start"

}