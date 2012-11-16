package com.entscheidungsbaum

import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import akka.actor.ActorSystem
import akka.actor.Props
import com.sun.org.apache.bcel.internal.generic.PUSH

class PushnotifierAppWith //extends Bootable {
{
  val pushServiceActor = ActorSystem("pushnotifier")

  def startup = {
    //    pushServiceActor.actorOf(Props(new InternalActor), "internalActor")

 //   pushServiceActor.actorOf(Props(new ApplePushConsumer("activemq:queue:activemqApple", internalActorRef)), "applepushconsumer") ! Start

  //  pushServiceActor.actorOf(Props(new AndroidPushConsumer("activemq:queue:activemqAndroid")), "androidpushconsumer") ! Start

    //    pushServiceActor.actorOf(Props(new QProducer("activemq:queue:activemqApple")))
  }

  def shutdown = {
    pushServiceActor.shutdown
  }
}
