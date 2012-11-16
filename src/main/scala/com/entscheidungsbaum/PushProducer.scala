package com.entscheidungsbaum

import akka.actor.Actor
import org.apache.camel.Producer
import akka.camel.CamelMessage



class PushProducer extends Actor { // with Producer {

  def endpointUri = ""
  
 def receive = {
    
    case msg: CamelMessage => println(msg)
  }
}