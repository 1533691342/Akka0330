package com.atguigu.akka.yellowchicken.common

/**
 * 使用样例类来构建协议
 * 客户端发送给 服务器协议（序列化的对象）
 * 样例类默认实现序列化
 */

case class ClientMessage(mes:String)

//服务器段发给客户端的协议（样例类对象）
case class  ServerMessage(mes:String)
