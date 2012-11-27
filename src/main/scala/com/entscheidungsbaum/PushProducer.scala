package com.entscheidungsbaum

import akka.actor.Actor
import akka.camel.Producer
import akka.actor.ActorSystem
import akka.actor.Props

class PushProducer extends Actor with Producer {

  println("producer activated " + this.camelContext.getName())

  def endpointUri = "direct:pushService"

  override def oneway: Boolean = true
}