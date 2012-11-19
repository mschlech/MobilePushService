package com.entscheidungsbaum

import org.apache.camel.Message
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import akka.camel.Consumer
import akka.camel.CamelMessage
import akka.actor.Actor
case class PushType
class ApplePushConsumer(activeMq:String) extends Actor{

  println(" ApplePushConsumer ")
 // def endpointUri = activeMq

  //  override def activationTimeout = 10 seconds
  //  override def replyTimeout = 30 seconds
  //  override def autoack = false

  def receive = {

    case msg: CamelMessage => {

      println("applePush [%s] " format msg.toString())
      //  sender ! Ack
    }
    
    case _ => { println("no request initiated !!!!") }

  }

  def processDispatcher(dispatchCamelRequest: PushType) = dispatchCamelRequest match {
    case s: PushType => sender ! s
    case _ => println("nothing")
  }
}