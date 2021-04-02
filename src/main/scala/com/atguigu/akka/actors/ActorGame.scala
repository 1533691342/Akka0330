package com.atguigu.akka.actors

import akka.actor.{ActorRef, ActorSystem, Props}

object ActorGame  extends App{
  //创建 ActorSystem
  val actorfactory=ActorSystem("actorfactory")

  //先创建BActor 引用/代理
  val bActorRef : ActorRef =  actorfactory.actorOf(Props[BActor],"bActor")

  //创建 AActor 的引用
  val aActorRef: ActorRef = actorfactory.actorOf(Props(new AActor(bActorRef)), "aActor")

  // aActor 出招
  aActorRef ! "start"

}
