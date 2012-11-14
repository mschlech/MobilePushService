/**
 *
 */
package com.entscheidungsbaum

import org.apache.activemq.camel.component.ActiveMQComponent
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.builder.RouteBuilder
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.camel.CamelExtension
import akka.camel.Producer
import akka.util.Timeout
import akka.actor.Props
import akka.camel.Consumer
import akka.camel.CamelMessage

/**
 * @author marcus
 *
 */
case class ApplePush(a: String, b: String)
case class AndroidPush(a: String, b: String)

object PushNotifierApp extends App {

  implicit val timeoutDuration = 10
  implicit val timeout = Timeout(timeoutDuration)

  val pushServiceActor = ActorSystem("pushnotifier")

  implicit val ec = pushServiceActor.dispatcher

  val camel = CamelExtension(pushServiceActor)

  val camelContext = camel.context

  /**
   * dispatching the http request to a dedicated
   */
  camelContext.addRoutes(new RouteBuilder() {
    def configure() {

      // poor mans routing !!

      //      from("jetty:http://localhost:1112/apple").process(new Processor {
      //        def process(exchange: Exchange) {
      //          println("apple route : " + exchange.getIn().getHeaders())
      //
      //        }
      //
      //      }).to("activemq:queue:apple")
      //
      //      // uncomment for some post-processing
      //      from("jetty:http://localhost:1112/android").process(new Processor {
      //        def process(exchange: Exchange) {
      //
      //          println("android route : " + exchange.getIn().getHeaders())
      //
      //        }
      //      }).to("activemq:queue:android")

      from("jetty:http://localhost:11112/pushnotifier")
        .choice()
        .when(header("apnType").isEqualTo("apple")).process(new Processor {

          def process(exchange: Exchange) {
            println("inside = " + exchange.getIn().getHeaders())
            ApplePush(exchange.getIn().getHeader("apnType").toString, "an ApplePush")
            println("APPLEPUSH " + ApplePush.toString)
          }
        }).to("activemq:queue:activemqApple")

        // the android pipeline
        .when(header("apnType").isEqualTo("android")).process(new Processor {

          def process(androidExchange: Exchange) {
            println("inside android push process => " + androidExchange.getIn().getHeaders())
            AndroidPush(androidExchange.getIn().getHeader("apnType").toString, "an AndroidPush")
          }
        }).to("activemq:queue:activemqAndroid")

    }
  })
  camelContext.addComponent("activemqApple", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"))
  camelContext.addComponent("activemqAndroid", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"))
  
  camelContext.setTracing(true)
  /**
   * the actor subsystem
   */
  val pushStartTest = pushServiceActor.actorOf(Props(new PushHealthActor), "pushhealthactor")
  //  val futureActivationTask = camel.activationFutureFor(pushStartTest)(timeout = 10 seconds).onComplete {
  //    case Left(problem) => println("failed")
  //    case Right(pushStartTest) => println("ok")
  //  }
  //  println("the future gets back " + futureActivationTask)

  val applePushConsumer = pushServiceActor.actorOf(Props(new ApplePushConsumer("activemq:queue:activemqApple")), "applepushconsumer")
  val androidPushConsumer = pushServiceActor.actorOf(Props(new AndroidPushConsumer("activemq:queue:activemqAndroid")), "androidpushconsumer")

  
  //pushServiceActor.awaitTermination()
}