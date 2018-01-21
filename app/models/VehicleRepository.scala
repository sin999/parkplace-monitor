package models

import javax.inject.Singleton
import javax.inject.Inject

import org.slf4j.LoggerFactory
import slick.driver.JdbcProfile
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig

@Singleton
class VehicleRepository @Inject()(val provider: DatabaseConfigProvider)
  extends HasDatabaseConfig[JdbcProfile] with VehiclesTable {

  var logger = LoggerFactory.getLogger("VehicleRepository.class")
  val dbConfig = provider.get[JdbcProfile]
  def now = {System.currentTimeMillis()}
  def farFuture(date: Long) = date + 1000


  import dbConfig.driver.api._
  import scala.concurrent.ExecutionContext.Implicits.global

  def findAll() = getParked(vehicles, now)


  def checkInVehicle(vehicle: Vehicle) = {
    db.run(vehicles+= vehicle.copy(checkin = Some(now))).map(id => vehicle)
  }

  def checkOutVehicle(vehicle: Vehicle, date: Long = now) = {
    db.run(vehicles.filter(_.numberPlate === vehicle.numberPlate)
      .map(_.checkout).update(Some(date))).map(x => vehicle)
  }

  def currentlyParkedFilter(vehicles: TableQuery[Vehicles], date: Long) =
    (for(v <- vehicles.filter(x => x.checkout.ifNull(farFuture(date)) > date)) yield v)
    .result
    .transactionally

  def getParked(vehicles: TableQuery[Vehicles], date: Long) = db.run(currentlyParkedFilter(vehicles,date))


}
