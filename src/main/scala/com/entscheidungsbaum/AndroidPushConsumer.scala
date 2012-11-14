package com.entscheidungsbaum

import akka.actor.actorRef2Scala
import akka.camel.Ack
import akka.camel.CamelMessage
import akka.camel.Consumer

class AndroidPushConsumer(androidQueue: String) extends Consumer { 
  println(" AndroidPushConsumer =[" + androidQueue + "]" )
  def endpointUri = androidQueue

//  override def activationTimeout = 10 seconds
//  override def replyTimeout = 30 seconds
//  override def autoack = false

  def receive = {

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