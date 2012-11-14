package com.entscheidungsbaum

import akka.actor.actorRef2Scala
import akka.camel.Ack
import akka.camel.CamelMessage
import akka.camel.Consumer
import akka.actor.ActorRef

class ApplePushConsumer(appleQueue: String, next : ActorRef) extends Consumer {

  println(" ApplePushConsumer =[" + appleQueue + "]" )
  def endpointUri = appleQueue

//  override def activationTimeout = 10 seconds
//  override def replyTimeout = 30 seconds
//  override def autoack = false

  def receive = {

    case msg: CamelMessage => {

      println("applePush [%s] ") //format msg.bodyAs[String])
      sender ! Ack
    }
    case _ => {println("no request initiated ")}

  }

  def processDispatcher(dispatchCamelRequest: PushType) = dispatchCamelRequest match {
    case s: PushType => sender ! s
    case _ => println("nothing")
  }
}