package com.atguigu.akka.sparkmasterworker.master

import akka.actor.{Actor, ActorSystem, Props}
import com.atguigu.akka.sparkmasterworker.common.{HeartBeat, RegisterWorkerInfo, RegisteredWorkerInfo, WorkerInfo}
import com.typesafe.config.ConfigFactory

import scala.collection.mutable

/**
 * Spark Master Worker 进程通讯项目:
 * 功能1：worker 完成注册
 * 要求：
 * worker 注册到 Master ,Master 完成注册，并回复 worker 注册成功
 * 功能2： worker定时发送心跳
 * 要求：
 * worker 定时发送心跳给Master，Master 能够接收到，并更新 worker 上一次心跳时间
 */
class SparkMaster extends  Actor{

  //定义一个hashmap，管理worker
  val workers=mutable.Map[String,WorkerInfo]()

  override def receive: Receive = {
    case "start" => println("master 服务器启动了.............")
    case RegisterWorkerInfo(id,cpu,ram)=>{
      //接收到 worker 注册信息
      if ( !workers.contains(id)){
        //创建 WorkerInfo 对象
        val workerInfo=new WorkerInfo(id,cpu,ram)
        //加入到 workers
        workers += ((id,workerInfo))
        println("服务器的 workers = " + workers)
        //回复消息：注册成功
        sender() ! RegisteredWorkerInfo
      }
    }

      // 功能2： worker定时发送心跳
    case HeartBeat(id)=>{
      //更新对应的的 worker 的心跳时间
       // 1、从workers 的hashmap 中取出 workerInfo
      val workerInfo=workers(id)
      workerInfo.lastHeartBeat = System.currentTimeMillis()
      println("master 更新了 " + id + "心跳时间........")

    }

  }
}

object SparkMaster{
  def main(args: Array[String]): Unit = {
    //先创建 ActorSystem

    //创建 config 对象，指定协议类型，监听 ip 和 端口
    val config= ConfigFactory.parseString(
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname=127.0.0.1
         |akka.remote.netty.tcp.port=10005
           """.stripMargin
    )
    val sparkMasterSystem = ActorSystem("SparkMaster", config)
    //创建SparkMaster -actor
    val sparkMasterRef = sparkMasterSystem.actorOf(Props[SparkMaster], "SparkMaster-01")

    //启动SparkMaster
    sparkMasterRef ! "start"
  }

}