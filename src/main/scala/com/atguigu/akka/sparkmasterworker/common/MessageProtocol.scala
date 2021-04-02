package com.atguigu.akka.sparkmasterworker.common

//worker 注册信息 //MessageProtocol.scala
case class RegisterWorkerInfo(id: String, cpu: Int, ram: Int)

//这个是 WorkerInfo:这个信息需要保存在master 的 hashmap中的（该hashmap是用于
// 管理worker），这个 WorkerInfo 会 扩展 ， 比如增加worker上一次的心跳时间
case class WorkerInfo(val id: String, val cpu: Int, val ram: Int) {
  var lastHeartBeat: Long = System.currentTimeMillis()
}

//当 worker 注册成功，服务器返回一个 RegisteredWorkerInfo 对象
case object RegisteredWorkerInfo

/**
 * 功能2： worker定时发送心跳
 */
// worker 每隔一段时间由定时器发送给自己一个消息
case object SendHeartBeat

//worker 每隔一段时间由定时器触发，而向 master 发送的协议消息
case class HeartBeat(id: String)