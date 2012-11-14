package com.entscheidungsbaum

import akka.actor.actorRef2Scala
import akka.camel.CamelMessage
import akka.camel.Consumer

/**
 *
 */
class PushNotifierHttpAdapter extends Consumer {

  def endpointUri = "file://User/marcus/httpInbox"

  def receive = {
    case msg: CamelMessage => msg.body match {
      // extract the push message here
      case m: String =>
        println(m)
//        println(" message type " + msg)
//
//     //   val pushType = msg header ("apnType") getOrElse ("Unknown Apn Request type")
//
//        processApnRequest(msg)
//
//       // val message = ("Joooo message received") format pushType.toString
//       // println(message + "apnType " + pushType)
//
//        /**
//         * returns a message back to the sender in for instance to the browser with the above message
//         */
//       // sender ! message
//
//       // msg.header("apnPush") foreach {
//         // aMessage => println("a message " + aMessage.toString);
//
//        }
    }

  }

  def processApnRequest(apn: CamelMessage) {

    println(" in processApnRequest" + apn.getHeaders.toString)
  }

}