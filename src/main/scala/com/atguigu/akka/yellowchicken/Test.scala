package com.atguigu.akka.yellowchicken

import com.typesafe.config.ConfigFactory

object Test {
  def main(args: Array[String]): Unit = {

    val (clienHost,clientPort,serverHost,serverPort)=
      ("127.0.0.1",9990,"127.0.0.1",9999)

    //创建 config 对象，指定协议类型，监听 ip 和 端口

    println(  s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname=$clienHost
         |akka.remote.netty.tcp.port=$clientPort
    """
        .stripMargin
    )


  }

}
