package com.entscheidungsbaum

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.actorRef2Scala
import akka.actor.Props
import akka.camel.CamelExtension
import akka.util.Timeout
import org.apache.activemq.camel.component.ActiveMQComponent
import scala.concurrent.Await
import scala.concurrent.duration._

object PushnotifierAppWith extends App {

  implicit val timeoutDuration = 10 seconds
  implicit val timeout = Timeout(timeoutDuration)

  val system = ActorSystem("MobilePush")
  implicit val ec = system.dispatcher

  //
  val pushDispatcher = system.actorOf(Props(new PushProducer("direct:pushService")))

  val httpMobilePushConsumer = system.actorOf(Props(new HttpMobilePushConsumer(system, applePushConsumer)), "httpMobilePushConsumer")

  val applePushConsumer = system.actorOf(Props(new ApplePushConsumer("acitvemq:queue:apple")))

  val androidPushConsumer = system.actorOf(Props(new AndroidPushConsumer("acitvemq:queue:apple")))
  
  CamelExtension(system).context.addRoutes(new MobilePushRouteBuilder)

  val camel = CamelExtension(system)

  val camelContext = camel.context

  camelContext.addComponent("applePush", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"))
  camelContext.addComponent("androidPush", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"))
  camelContext.setTracing(true)
  Await.ready(camel.activationFutureFor(applePushConsumer), 10 seconds)
  system.awaitTermination()

}
