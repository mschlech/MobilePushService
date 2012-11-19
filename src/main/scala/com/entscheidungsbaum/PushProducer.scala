package com.entscheidungsbaum

import akka.actor.Actor
import akka.camel.Producer

class PushProducer(uri: String) extends Actor with Producer {

  println("producer activated ")
  def endpointUri = uri
  override def oneway: Boolean = true
  
}