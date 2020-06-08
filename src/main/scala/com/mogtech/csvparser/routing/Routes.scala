package com.mogtech.csvparser.routing

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import com.mogtech.csvparser.utils.ConfigurationSettings

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, ExecutionContext, Future }

class Routes(settings: ConfigurationSettings)(implicit ec: ExecutionContext) {

  val httpRoutes = new HttpRoutes(settings)

  import Routes._

  implicit val actorSystem: ActorSystem = ActorSystem("CSV-parser")

  private def bindWithRoute(route: Route): Future[Http.ServerBinding] =
    Http().bindAndHandle(route, "127.0.0.1", 8080)

  def start(): Future[Shutdown] =
    bindWithRoute(httpRoutes.route).map { binding => timeout: Duration =>
      val eventualTermination = for {
        _ <- binding.unbind()
        _ <- actorSystem.terminate()
      } yield ()

      Await.result(eventualTermination, timeout)
    }

}

object Routes {

  type Shutdown = Duration => Unit
}
