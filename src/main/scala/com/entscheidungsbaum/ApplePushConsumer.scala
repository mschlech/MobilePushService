package com.entscheidungsbaum

import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.builder.RouteBuilder

import akka.actor.ActorRef
import akka.actor.actorRef2Scala
case class PushType
class ApplePushConsumer(appleQueue: String) extends RouteBuilder {

  println(" ApplePushConsumer =[" + appleQueue + "]")
  def endpointUri = "file:///Users/marcus/tmp/apple"

  //  override def activationTimeout = 10 seconds
  //  override def replyTimeout = 30 seconds
  //  override def autoack = false

  def configure {
    from("activemq:queue:activemqApple").process(new Processor() {
      def process(exchange: Exchange) {
        println("inside the exchange process => " + exchange.getContext())
      }
    }).to("file:///Users/marcus/tmp/apple")

  }

}