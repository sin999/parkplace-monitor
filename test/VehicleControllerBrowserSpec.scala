import org.scalatestplus.play._
import org.openqa.selenium.WebDriver

class VehicleControllerBrowserSpec extends PlaySpec
    with OneServerPerSuite with OneBrowserPerSuite 
      with HtmlUnitFactory {
  "Vehicles page" must {
    "should show emtpy vehicles" in {
      go to s"http://localhost:$port/"
      pageTitle mustBe "Manage Vehicles"

      textField("email").value = "A123BC99"
      submit()
      
      eventually { pageTitle mustBe "Manage Vehicles" }
      find(xpath(".//*[@class='table']/tbody/tr[1]/td[4]")) map {
        _.text mustBe ("A123BC99")
      }
    }
  }
}