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

  }

  def handleInput(): Unit = {
    println(s"Welcome to the Future's Citadel. Please press a number to pick an option:  \n")
    println("1. Create a construction ")
    println("2. Check materials ")
    println("3. Check current finishing date ")
    println("4. Check Pending constructions ")
    println("5. Check currently building constructions ")
    println("6. Check Finished constructions ")
    println("7. Reload materials ")
    val input: String = StdIn.readLine()

    input.trim().toLowerCase().toInt match {
      case 1 => handleCreateConstruction()
        handleInput()
      case 2 => MaterialServices.getAllMaterials()
        handleInput()
      case 3 => ConstructionServices.getLastDate()
        handleInput()
      case 4 => ConstructionServices.getPropertiesByStatus(ConstructionStatus.PENDING)
        handleInput()
      case 5 => ConstructionServices.getPropertiesByStatus(ConstructionStatus.CURRENTLY_BUILDING)
        handleInput()
      case 6 => ConstructionServices.getPropertiesByStatus(ConstructionStatus.DONE)
        handleInput()
      case 7 => MaterialServices.handleUpdateMaterial()
        handleInput()
      case _ => println("Invalid command, please try again")
    }
  }






}