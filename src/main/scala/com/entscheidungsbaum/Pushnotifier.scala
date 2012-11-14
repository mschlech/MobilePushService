package com.entscheidungsbaum

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import akka.camel.CamelMessage
import akka.camel.Consumer

case class PushType(s: Any)

class Pushnotifier(producer: ActorRef) extends Actor with Consumer {
  def endpointUri = "jetty:http://0.0.0.0:1113/apn"

  def receive = {

    //  case pushRequest: CamelMessage => processDispatcher(PushType(pushRequest.getHeader("apnType")))
    case _ => println("did not understand the request")
  }

  def processDispatcher(dispatchCamelRequest: PushType) = dispatchCamelRequest match {
    case s: PushType => sender ! s
    case _ => println("nothing")
  }

}
