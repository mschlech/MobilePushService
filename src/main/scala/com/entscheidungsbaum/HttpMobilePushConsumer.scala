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

/**
 * @author marcus
 *
 */
class HttpMobilePushConsumer(pushServiceActor: ActorSystem, dispatcher: ActorRef) extends Actor with Consumer {

  def endpointUri = "jetty:http://localhost:11112/pushnotifier"
  implicit val timeout = Timeout(30 seconds)

  override val camel = CamelExtension(pushServiceActor)

  override val camelContext = camel.context

  def receive = {
    case msg: CamelMessage => {
      val apnType = msg.getHeaders.get("apnType")
      println("camel Message Headers =" + msg.getHeaders.toString + " \n " + " for Dispatcher " + dispatcher)
      val name = "Hello you requested a %s" format apnType
      dispatcher.forward(msg)
      sender forward msg.toString
    }
  }
}