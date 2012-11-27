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


  val pushDispatcherProducer = system.actorOf(Props[PushProducer])
  println("pushDispatcherProducer -- " + pushDispatcherProducer)

  val httpMobilePushConsumer = system.actorOf(Props(new HttpMobilePushConsumer(system, pushDispatcherProducer)), "httpMobilePushConsumer")
  println("httpMobilePushConsumer -- " + httpMobilePushConsumer)

  val applePushConsumer = system.actorOf(Props(new ApplePushConsumer(system,"activemq:queue:apple")), "applePush")
  println("applePushConsumer -- " + applePushConsumer)

  val androidPushConsumer = system.actorOf(Props(new AndroidPushConsumer(system,"activemq:queue:android")), "androidPush")
  println("applePushConsumer -- " + androidPushConsumer)

  CamelExtension(system).context.addRoutes(new MobilePushRouteBuilder)
  val camel = CamelExtension(system)

  val appleActivationFuture = camel.activationFutureFor(applePushConsumer)
  val androidActivationFuture = camel.activationFutureFor(androidPushConsumer)

  val camelContext = camel.context

  camelContext.addComponent("applePush", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"))
  camelContext.addComponent("androidPush", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"))
  camelContext.setTracing(true)


//
//  Await.ready(camel.activationFutureFor(applePushConsumer), 30 seconds)
//  Await.ready(camel.activationFutureFor(androidPushConsumer), 30 seconds)

  //system.awaitTermination()

}
