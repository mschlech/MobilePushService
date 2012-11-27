package com.entscheidungsbaum

import akka.actor.{ActorSystem, ActorRef, actorRef2Scala, Actor}
import akka.camel.Ack
import akka.camel.CamelMessage
import akka.camel.Consumer
import akka.camel.Producer
import org.apache.camel.Exchange

class AndroidPushConsumer(actorSystem: ActorSystem,uri: String) extends Actor {
  println(" AndroidPushConsumer")

  //  override def activationTimeout = 10 seconds
  //  override def replyTimeout = 30 seconds
  //  override def autoack = false

  def receive = {

    case msg: CamelMessage => {

      println("androidPush [%s] " format msg.toString())
      //  sender ! Ack
    }

    case _ => { println("no request initiated !!!!") }

  }

}