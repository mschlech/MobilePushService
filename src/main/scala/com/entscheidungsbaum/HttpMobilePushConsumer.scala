/**
 *
 */
package com.entscheidungsbaum

import org.apache.activemq.camel.component.ActiveMQComponent
import org.apache.camel.Exchange
import org.apache.camel.Processor
import akka.camel.Consumer
import org.apache.camel.builder.RouteBuilder
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.camel.CamelExtension
import akka.camel.CamelMessage
import akka.camel.Producer
import akka.util.Timeout
import akka.actor.Props
import scala.concurrent.duration._
import akka.util

/**
 * @author marcus
 *
 */
class HttpMobilePushConsumer(pushServiceActor: ActorSystem, producer:ActorRef) extends Actor with Consumer {

  def endpointUri = "jetty:http://localhost:11112/pushnotifier"
  implicit val timeout = util.Timeout(10 seconds)

  override val camel = CamelExtension(pushServiceActor)

  override val camelContext = camel.context

  def receive = {
   case msg: CamelMessage =>
      val apnType = msg.getHeaders.get("apnType")
      println("CAMEL Message Headers =" + msg.getHeaders.toString + " \n " + " apnType = " + apnType)


      val name = "Hello you requested a %s" format apnType

     sender ! msg.toString

  }
}