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

  val camel = CamelExtension(system)

  val camelContext = camel.context
  CamelExtension(system).context.addRoutes(new MobilePushRouteBuilder)

  camelContext.addComponent("activemq", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"))

  val pushDispatcherProducer = system.actorOf(Props[PushProducer])
  println("pushDispatcherProducer -- " + pushDispatcherProducer)

  val httpMobilePushConsumer = system.actorOf(Props(new HttpMobilePushConsumer(system, pushDispatcherProducer)), "httpMobilePushConsumer")
  println("httpMobilePushConsumer -- " + httpMobilePushConsumer)


  val applePushConsumer = system.actorOf(Props(new ApplePushConsumer(system,"activemq:queue:apple")), "applePush")
  println("applePushConsumer -- " + applePushConsumer)

  val androidPushConsumer = system.actorOf(Props(new AndroidPushConsumer(system,"activemq:queue:android")), "androidPush")
  println("applePushConsumer -- " + androidPushConsumer)



  val appleActivationFuture = camel.activationFutureFor(applePushConsumer)
  val androidActivationFuture = camel.activationFutureFor(androidPushConsumer)





  //val mobilePushRouteBuilderConsumer = system.actorOf(Props(new MobilePushRouteBuilder(camelContext)),"routeBuilder")

  camelContext.setTracing(true)


//
  Await.ready(camel.activationFutureFor(applePushConsumer), 20 seconds)
  Await.ready(camel.activationFutureFor(androidPushConsumer), 20 seconds)
  appleActivationFuture.foreach(println)
  println("AppleActivationFuture => " + appleActivationFuture.foreach(println))
  //system.awaitTermination()

}
