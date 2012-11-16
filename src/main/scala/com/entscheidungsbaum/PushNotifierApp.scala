package com.entscheidungsbaum

import org.apache.activemq.camel.component.ActiveMQComponent
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.builder.RouteBuilder
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.camel.CamelExtension
import akka.camel.CamelMessage
import akka.camel.Producer
import akka.util.Timeout
import akka.actor.Props
import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * @author marcus
 *
 */
case class ApplePush(a: String, b: String)
case class AndroidPush(a: String, b: String)

object PushNotifierApp extends App {

  implicit val timeoutDuration = 10 seconds
  implicit val timeout = Timeout(timeoutDuration)
  val pushServiceActor = ActorSystem("pushnotifier")

  implicit val ec = pushServiceActor.dispatcher

  val camel = CamelExtension(pushServiceActor)

  val camelContext = camel.context

  camelContext.addComponent("activemqApple", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"))
  camelContext.addComponent("activemqAndroid", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"))
  camelContext.setTracing(true)

  /**
   * dispatching the http request to a dedicated
   */
  camelContext.addRoutes(new RouteBuilder() {
    def configure() {

      from("jetty:http://localhost:11112/pushnotifier")
        .choice()
        .when(header("apnType").isEqualTo("apple")).process(new Processor {

          def process(exchange: Exchange) {
            println("inside apple Pushservice= " + exchange.getIn().getHeaders())
            ApplePush(exchange.getIn().getHeader("apnType").toString, "an ApplePush")
            exchange.getIn.setBody(ApplePush(exchange.getIn().getHeader("apnType").toString, "an ApplePush"))
          }
        }).to("activemq:queue:activemqApple")

        // the android pipeline
        .when(header("apnType").isEqualTo("android")).process(new Processor {

          def process(androidExchange: Exchange) {
            println("inside android push process => " + androidExchange.getIn().getHeaders())
            val a = AndroidPush(androidExchange.getIn().getHeader("apnType").toString, "an AndroidPush")
            val apnType = List("test", "apnType")
            androidExchange.getIn.setBody(a)
          }
        }).to("activemq:queue:activemqAndroid")

    }
  })

  /**
   * the actor subsystem
   */
  val pushStartTest = pushServiceActor.actorOf(Props(new PushHealthActor), "pushhealthactor")
  //  val futureActivationTask = camel.activationFutureFor(pushStartTest)(timeout = 10 seconds).onComplete {
  //    case Left(problem) => println("failed")
  //    case Right(pushStartTest) => println("ok")
  //  }
  //  println("the future gets back " + futureActivationTask)

  val internalActorRef = pushServiceActor.actorOf(Props(new InternalActor), "internalActor")

  //val applePushConsumer = pushServiceActor.actorOf(Props(new ApplePushConsumer("activemq:queue:activemqApple")), "applepushconsumer")
  CamelExtension(pushServiceActor).context.addRoutes(new ApplePushConsumer("activemq:queue:activemqApple"))
  
  val androidPushConsumer = pushServiceActor.actorOf(Props(new AndroidPushConsumer("activemq:queue:activemqAndroid")), "androidpushconsumer")

  // val qProducerRef = pushServiceActor.actorOf(Props(new QProducer("activemq:queue:activemqApple")))

  //pushServiceActor.awaitTermination()

  //Await.ready(camel.activationFutureFor(applePushConsumer), 1 seconds)
  Await.ready(camel.activationFutureFor(androidPushConsumer), 1 seconds)
  //qProducerRef tell ("hallo")
}

class InternalActor extends Actor {
  def receive = {
    case msg: CamelMessage =>
      msg.mapBody { b: String =>
        // do some extremely intelligent processing
        b.map { c => if (c.isWhitespace) '_' else c }
        println("internal Actor " + msg)
      }
  }
}