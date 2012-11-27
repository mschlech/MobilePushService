/**
 * MainMobilePushActor.scala
 * @author marcus
 *
 * 11:17:44
 */
package com.entscheidungsbaum.com.entscheidungsbaum.core

import akka.actor.{Props, Actor}
import com.entscheidungsbaum.com.entscheidungsbaum.core.{Started, Start}

/**
 * the implementation which shall be provided
 */
case class GetImplementation()

/**
 * the implementation details
 */
case class Implementation(title: String, version: String, build: String)

class MainMobilePushActor extends Actor {

  def receive = {
    case GetImplementation() =>

      println("impl " + getClass().getName().toString)



//      case Start() =>

        /**
         * dummy healthActor to be replaced
         */
//      context.actorOf(Props[PushHealthActor], "androidPushActor")
//
//      context.actorOf(Props[PushHealthActor], "applePushActor")
//
//      context.actorOf(Props[PushHealthActor], "smsPushActor")
//
//      sender ! Started()

//      case Stop() => context.children.foreach(context.stop _)

//      case PoisonPill() =>
//        sys.exit(-1)
//      }



  }
}