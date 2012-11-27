package com.entscheidungsbaum

import akka.camel.CamelMessage
import akka.camel.Consumer
import akka.actor.Actor

class PushHealthActor extends Actor {

  def endpointUri = "jetty:http://localhost:1112/health"
    
  //implicit val timeout = 30 seconds 
   def receive = {
    case msg:CamelMessage => {
      //val name = msg.headers("name").getOrElse("avava")
      println(msg)
    }
  }
}