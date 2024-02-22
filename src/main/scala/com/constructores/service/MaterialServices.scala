package com.constructores.service

import com.constructores.Connection
import com.constructores.model.MaterialType.{ADOBE, CEMENT, GRAVEL, SAND, WOOD}
import com.constructores.model.{ConstructionType, Material, MaterialType, SlickTables}
import com.constructores.service.PrivateExecutionContext._
import com.constructores.utils.Validations.coordinateValidationHandler
import slick.jdbc.PostgresProfile.api._

import scala.Console.println
import scala.concurrent.Future
import scala.io.StdIn
import scala.util.{Failure, Success}

object MaterialServices {


  def handleUpdateMaterial(): Unit = {
    println(s"Please select the type of material you want to refill:  \n")
    println("1. Cement ")
    println("2. Gravel ")
    println("3. Sand")
    println("4. Wood ")
    println("5. Adobe ")

    val materialInput: String = StdIn.readLine()

    println(s"Please write the amount of tons you want to refill (only integers):  \n")

    val tons : Int = StdIn.readLine().toInt


    materialInput.trim().toLowerCase().toInt match {
      case 1 => materialUpdate(CEMENT, tons)

      case 2 => materialUpdate(GRAVEL, tons)
      case 3 => materialUpdate(SAND, tons)
      case 4 => materialUpdate(WOOD, tons)
      case 5 => materialUpdate(ADOBE, tons)
      case _ => println("Invalid command, please try again")
    }
  }

  def createMaterial(material: Material): Unit = {
    val queryDescriptor = SlickTables.materialTable += material

    val futureId: Future[Int] = Connection.db.run(queryDescriptor)
    futureId.onComplete {
      case Success(newMaterialId) => println(s"Query was succesful, new material id is $newMaterialId!")
      case Failure(exception) => println(s"Query for material failed, reason: $exception")
    }
    Thread.sleep(1000)
  }

  def getAllMaterials(): Unit = {
    val resultFuture = Connection.db.run(SlickTables.materialTable.result)
    resultFuture.onComplete {
      case Success(materials) => println(s"Available materials: ${materials.mkString("\n")}")
      case Failure(exception) => println(s"Fetching failed: $exception")
    }
    Thread.sleep(10000)
  }

  def materialUpdate(materialType: String, amount: Int): Unit = {
    val resultFuture = Connection.db.run(SlickTables.materialTable.filter(_.material.like(s"%$materialType%")).result)
    resultFuture.onComplete {
      case Success(materialList) =>
        val queryDescriptor = SlickTables.materialTable.filter(_.id === materialList.head.id)
          .update(Material(materialList.head.id, materialType, materialList.head.tons + amount))

        val futureId: Future[Int] = Connection.db.run(queryDescriptor)
        futureId.onComplete {
          case Success(updatedMaterialId) => println(s"Material updated:  ${materialList.head.material}")
          case Failure(exception) => println(s"Query failed, reason: $exception")
        }
      case Failure(exception) => println(s"Query failed, reason: $exception")
    }
    Thread.sleep(1000)
  }

  def updateGymMaterials(): Unit = {
    materialUpdate( MaterialType.CEMENT, -50)
    materialUpdate(MaterialType.GRAVEL, -25)
    materialUpdate(MaterialType.SAND, -45)
    materialUpdate(MaterialType.WOOD, -10)
    materialUpdate(MaterialType.ADOBE, -50)
  }

  def updateHouseMaterials(): Unit = {
    materialUpdate(MaterialType.CEMENT, -100)
    materialUpdate(MaterialType.GRAVEL, -50)
    materialUpdate(MaterialType.SAND, -90)
    materialUpdate(MaterialType.WOOD, -20)
    materialUpdate(MaterialType.ADOBE, -100)
  }

  def updateLakeMaterials(): Unit = {
    materialUpdate(MaterialType.CEMENT, -50)
    materialUpdate(MaterialType.GRAVEL, -60)
    materialUpdate(MaterialType.SAND, -80)
    materialUpdate(MaterialType.WOOD, -10)
    materialUpdate(MaterialType.ADOBE, -20)
  }

  def updateFootballFieldMaterials(): Unit = {
    materialUpdate(MaterialType.CEMENT, -20)
    materialUpdate(MaterialType.GRAVEL, -20)
    materialUpdate(MaterialType.SAND, -20)
    materialUpdate(MaterialType.WOOD, -20)
    materialUpdate(MaterialType.ADOBE, -20)

  }

  def updateBuildingMaterials(): Unit = {
    materialUpdate(MaterialType.CEMENT, -200)
    materialUpdate(MaterialType.GRAVEL, -100)
    materialUpdate(MaterialType.SAND, -180)
    materialUpdate(MaterialType.WOOD, -40)
    materialUpdate(MaterialType.ADOBE, -200)

  }


}
