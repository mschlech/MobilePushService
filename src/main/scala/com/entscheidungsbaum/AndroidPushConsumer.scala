package com.entscheidungsbaum

import akka.actor.actorRef2Scala
import akka.camel.Ack
import akka.camel.CamelMessage
import akka.camel.Consumer
import akka.camel.Producer
import akka.actor.Actor

class AndroidPushConsumer(androidQueue: String) extends Actor with Producer { 
  println(" AndroidPushConsumer =[" + androidQueue + "]" )
  def endpointUri = "file:///Users/marcus/tmp"

//  override def activationTimeout = 10 seconds
//  override def replyTimeout = 30 seconds
//  override def autoack = false

  override def transformOutgoingMessage(msg:Any) = msg match {

    case msg: CamelMessage => {

      println("androidPush [%s] ") //format msg.bodyAs[String])
      sender ! Ack
    }
    case _ => {println("no request initiated ")}

  }

  def processDispatcher(dispatchCamelRequest: PushType) = dispatchCamelRequest match {
    case s: PushType => sender ! s
    case _ => println("nothing")
  }

  

}