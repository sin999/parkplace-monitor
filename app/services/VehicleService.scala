package services

import javax.inject.Singleton
import javax.inject.Inject

import scala.concurrent.{Future}
import models._
import akka.actor.ActorSystem


@Singleton
class VehicleService @Inject()(val repository: VehicleRepository, system: ActorSystem) {

  def findAll(): Future[Seq[Vehicle]] = repository.findAll

  def checkInVehicle(vehicle: Vehicle): Future[Vehicle] = repository
    .checkInVehicle(vehicle)

  def checkOutVehicle(vehicle: Vehicle): Future[Vehicle] = repository
    .checkOutVehicle(vehicle)



}