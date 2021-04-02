package com.atguigu.akka.actors

import akka.actor.Actor

class BActor extends Actor{
  override def receive: Receive = {
    case "我打" =>{
      println("BActor(乔峰) 挺猛  看我降龙十八掌")
      //通过 sender() 可以获取到发现消息的 actor 的 ref
      sender() ! "我打" // ! 是操作符
    }
  }
}
