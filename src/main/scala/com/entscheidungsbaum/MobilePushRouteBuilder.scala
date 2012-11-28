package com.entscheidungsbaum

import org.apache.camel.builder.RouteBuilder
import org.apache.camel.{CamelContext, Processor, Exchange}
import akka.camel.{CamelMessage, Consumer}


class MobilePushRouteBuilder() extends RouteBuilder {

//  def endpointUri=""
//
//  def receive = {
//  camelContext.addRoutes(new RouteBuilder() {

    def configure() {
      from("direct:pushService")
        .choice()
        .when(header("apnType").isEqualTo("apple")).process(new Processor {

        def process(exchange: Exchange) {
          println("inside apple Pushservice= " + exchange.getIn().getBody(classOf[String]))
          val payload = exchange.getIn.getHeader("apnType")
          exchange.getOut.setBody(payload)
        }
      }).to("file:////Users/marcus/workspaceScalaAkka/MobilePushService/tmp/apple/") //.to("activemq:queue:activemqApple")

        // the android pipeline
        .when(header("apnType").isEqualTo("android")).process(new Processor {

        def process(androidExchange: Exchange) {
          println("inside android push process => " + androidExchange.getIn().getHeaders())
          val apnType = List("test", "apnType")
          androidExchange.getIn.setBody(apnType)
        }
      }).to("file:///Users/marcus/workspaceScalaAkka/MobilePushService/tmp/android/") //.to("activemq:queue:activemqAndroid")

    }


//  }
//  )
//    case msg: CamelMessage=
//  }
}
