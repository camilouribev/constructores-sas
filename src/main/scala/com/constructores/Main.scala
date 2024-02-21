import com.constructores.model.{Construction, ConstructionStatus, ConstructionType}
import com.constructores.service.ConstructionServices.{handleCreateConstruction}
import com.constructores.service.{ConstructionServices, MaterialServices}

import java.time.LocalDate
import scala.io.StdIn


object Main {

  def main(args: Array[String]): Unit = {
    handleInput()
        /*MaterialServices.createMaterial(Material(1L, MaterialType.SAND, 20000))
       MaterialServices.createMaterial(Material(1L, MaterialType.GRAVEL, 20000))
       MaterialServices.createMaterial(Material(1L, MaterialType.ADOBE, 20000))
       MaterialServices.createMaterial(Material(1L, MaterialType.CEMENT, 20000))
       MaterialServices.createMaterial(Material(1L, MaterialType.WOOD, 20000))*/
     /*  val house1 = Construction(1L, ConstructionType.HOUSE, 80, 88, LocalDate.now(), LocalDate.now(), ConstructionStatus.PENDING)
        val foo = Construction(1L, ConstructionType.FOOTBALL_FIELD, 60, 70, LocalDate.of(2024,2,23), LocalDate.of(2024,2,25), ConstructionStatus.PENDING)
        ConstructionServices.createConstruction(house1)
        ConstructionServices.createConstruction(foo)*/
  }

  def handleInput(): Unit = {
    println(s"Welcome to the Future's Citadel. Please press a number to pick an option:  \n")
    println("1. Create a construction ")
    println("2. Check materials ")
    println("3. Check current finishing date ")

    val input: String = StdIn.readLine()

    input.trim().toLowerCase().toInt match {
      case 1 => handleCreateConstruction()
      case 2 => MaterialServices.getAllMaterials()
        handleInput()
      case 3 => ConstructionServices.getLastDate()
      case _ => println("Invalid command, please try again")
    }
  }






}