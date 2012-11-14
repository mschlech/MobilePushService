package com.entscheidungsbaum

import akka.camel.CamelMessage
import akka.camel.Consumer

class PushHealthActor extends Consumer {

  def endpointUri = "jetty:http://localhost:1112/health"
    
  //implicit val timeout = 30 seconds 
   def receive = {
    case msg:CamelMessage => {
      //val name = msg.headers("name").getOrElse("avava")
      println(msg)
    }
  }
}