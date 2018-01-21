package models


import play.api.db.slick.HasDatabaseConfig
import slick.driver.JdbcProfile

case class Vehicle(id: Option[Int], numberPlate: String, checkin: Option[Long], checkout: Option[Long] )




trait VehiclesTable { this: HasDatabaseConfig[JdbcProfile] =>
  import dbConfig.driver.api._



  private[models] class Vehicles(tag: Tag) extends Table[Vehicle](tag, "VEHICLES") {

    // Columns
    def id = column[Int]("VEHICLE_ID", O.PrimaryKey, O.AutoInc)
    def numberPlate = column[String]("NUMBER_PLATE", O.Length(128))
    def checkin = column[Option[Long]]("CHECKIN_TIMESTAMP", O.Default(Some(System.currentTimeMillis())))
    def checkout = column[Option[Long]]("CHECKOUT_TIMESTAMP")

    
    // Select
    def * = (id.?, numberPlate, checkin, checkout) <> (Vehicle.tupled, Vehicle.unapply)
  }
  
  val vehicles = TableQuery[Vehicles]
}

