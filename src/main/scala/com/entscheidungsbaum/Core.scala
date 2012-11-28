package com.entscheidungsbaum

import akka.actor.{Props, ActorSystem}
import akka.util.Timeout

/**
 * com.entscheidungsbaum
 * marcus
 * Author Marcus Schlechter
 * 11/28/12
 */


case class Start()

case class Stop()

case class Started()


trait Core {

  /**
   * an implicit to a actorSystem
   */
  implicit def actorSystem: ActorSystem

  /**
   * the timeout for each actor
   */
  implicit def timeout = Timeout(20000)

  /**
   * hirarchy of the involved actors
   */
  val application = actorSystem.actorOf(props = Props[MainMobilePushActor], name = "application")

 // Await.ready(application ? Start(), timeout.duration)
}
