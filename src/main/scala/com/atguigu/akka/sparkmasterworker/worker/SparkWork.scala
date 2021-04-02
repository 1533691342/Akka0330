package com.atguigu.akka.sparkmasterworker.worker

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.atguigu.akka.sparkmasterworker.common.{HeartBeat, RegisterWorkerInfo, RegisteredWorkerInfo, SendHeartBeat}
import com.typesafe.config.ConfigFactory

class SparkWork(masterHost:String,masterPort:Int) extends Actor{
  //masterPorx 是master的代理以及引用 ref
  var masterPorx:ActorSelection = _
  //id 是随机的
  val id=java.util.UUID.randomUUID().toString
  override def preStart(): Unit = {
    println("preStart() 调用")
    //初始化 masterPorx
    masterPorx=context
      .actorSelection(s"akka.tcp://SparkMaster@${masterHost}:${masterPort}/user/SparkMaster-01")

    println("masterPorx="+masterPorx)

  }


  override def receive: Receive = {
    case "start" =>{
      println("worker 启动了")
      //发出一个注册消息
      masterPorx ! RegisterWorkerInfo(id,16,16 * 1024)
    }
    case RegisteredWorkerInfo =>{
      println("workerid = " + id + "  注册成功")
      //当注册成功后，就定义一个定时器，每隔一定时间发送sendHeartBeat 给自己
      import scala.concurrent.duration._
      import context.dispatcher
      /**
       *
       * 功能2： worker定时发送心跳：
       * 说明：
       * 0 millis 表示 立即触发定时器，不延时
       * 3000 millis 表示每隔 3秒执行一次
       * self 代表发送给自己
       * SendHeartBeat 代表发送的内容
       */
      context.system.scheduler.schedule(0 millis,3000 millis,
        self,SendHeartBeat)

    }
    case  SendHeartBeat =>{
      println("worker = " +id + " 给master发送心跳")
      masterPorx ! HeartBeat(id)

    }
  }
}
object SparkWork{
  def main(args: Array[String]): Unit = {
    val workerHost="127.0.0.1"
    val workerPost=10001
    val masterHost="127.0.0.1"
    val masterPort=10005

    val config= ConfigFactory.parseString(
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname=127.0.0.1
         |akka.remote.netty.tcp.port=10002
           """.stripMargin
    )

    ///先创建 ActorSystem
    val sparkWorkSystem = ActorSystem("SparkWork", config)

    //创建 SparkWork -actor
    val sparkWorkRef = sparkWorkSystem.actorOf(Props(new SparkWork(masterHost,masterPort)), "SparkWork-01")

    //启动SparkMaster
    sparkWorkRef ! "start"

  }

}
