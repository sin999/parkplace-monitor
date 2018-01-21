package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import services.VehicleService
import models.Vehicle
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi

@Singleton
class VehicleController @Inject()(val service: VehicleService, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  def toUpperCase(str: String):String = {
    str.toUpperCase
  }

  private final val PLATE_NUMBER_PATTERN = "[A-Za-z][0-9]{3}[A-Za-z]{2}[0-9]{2,3}"
  val vehicleForm = Form(
    mapping(
      "id" -> ignored[Option[Int]](None),
      "numberPlate" -> text(maxLength = 10)
        .transform[String](x => toUpperCase(x),x=>x)
        .verifying("Must look like A444AA44.", numberPlate => numberPlate.matches(PLATE_NUMBER_PATTERN)),
      "checkin" -> optional(longNumber),
      "checkout" -> optional(longNumber)
    )(Vehicle.apply)(Vehicle.unapply)
  )

  def getVehicles = Action.async {
    service.findAll().map { vehicles =>
      Ok(views.html.vehicles(vehicles)(vehicleForm))
    }
  }


  def addVehicle = Action.async { implicit request =>
    vehicleForm.bindFromRequest.fold(
      formWithErrors => {
        service.findAll().map { vehicles =>
          BadRequest(views.html.vehicles(vehicles)(formWithErrors))
        }
      },
      vehicle => {
        service.checkInVehicle(vehicle).map { vehicle =>
          Redirect(routes.VehicleController.getVehicles)
        } recoverWith {
          case _ => service.findAll().map { vehicles =>
            BadRequest(views.html.vehicles(vehicles)(vehicleForm
              .withGlobalError(s"Ошибка регистрации: ${vehicle.numberPlate}")))
          }
        }
      }
    )
  }


  def checkOutVehicle = Action.async { implicit request =>
    vehicleForm.bindFromRequest.fold(
      formWithErrors => {
        service.findAll().map { vehicles =>
          BadRequest(views.html.vehicles(vehicles)(formWithErrors))
        }
      },
      vehicle => {
        service.checkOutVehicle(vehicle).map { vehicle =>
          Redirect(routes.VehicleController.getVehicles)
        } recoverWith {
          case _ => service.findAll().map { vehicles =>
            BadRequest(views.html.vehicles(vehicles)(vehicleForm
              .withGlobalError(s"Ошибка регистрации: ${vehicle.numberPlate}")))
          }
        }
      }
    )
  }
}

