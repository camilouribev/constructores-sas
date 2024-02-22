package com.constructores.utils

import com.constructores.Connection
import com.constructores.model._
import com.constructores.service.MaterialServices.{updateBuildingMaterials, updateFootballFieldMaterials, updateGymMaterials, updateHouseMaterials, updateLakeMaterials}
import com.constructores.service.{ConstructionServices, MaterialServices, PrivateExecutionContext}
import com.constructores.service.PrivateExecutionContext._
import slick.jdbc.PostgresProfile.api._

import scala.Console.println
import scala.io.StdIn
import scala.util.{Failure, Success}

object Validations {


  def coordinateValidationHandler(newConstructionType: String): Unit = {
    println(s"Please introduce the desired latitude coordinate:  \n")
    val latitude: Long = StdIn.readLine().toLong
    println(s"Please introduce the desired longitude coordinate:  \n")
    val longitude: Long = StdIn.readLine().toLong

    validateCoordinates(latitude, longitude)
    materialsValidationHandler(newConstructionType, latitude, longitude)

  }

  def validateCoordinates(newLatitude: Long, newLongitude: Long): Unit = {

    val resultFuture = Connection.db.run(SlickTables.constructionTable
      .filter(construction => construction.longitude === newLongitude && construction.latitude === newLatitude).result)
    resultFuture.onComplete {
      case Success(constructions) => {
        if (constructions.isEmpty) {
          println("The coordinates are available for the new construction")
        } else {
          println("The coordinates are unavailable, please try with a new location")
          PrivateExecutionContext.executor.shutdown()
        }
      }
      case Failure(e) =>
        println(s"Error reading coordinate from database")
        PrivateExecutionContext.executor.shutdown()

    }
    Thread.sleep(1000)
  }


  def materialsValidationHandler(newConstructionType: String, latitude: Long, longitude: Long): Unit = {
    newConstructionType match {
      case ConstructionType.HOUSE => validateHouseMaterials()
        updateHouseMaterials()

      case ConstructionType.LAKE => validateLakeMaterials()
        updateLakeMaterials()

      case ConstructionType.FOOTBALL_FIELD => validateFootballFieldMaterials()
        updateFootballFieldMaterials()

      case ConstructionType.BUILDING => validateBuildingMaterials()
        updateBuildingMaterials()

      case ConstructionType.GYM => validateGymMaterials()
        updateGymMaterials()

      case _ => println("Invalid construction type")
        PrivateExecutionContext.executor.shutdown()

    }
    ConstructionServices.createRetrievingLastDate(newConstructionType, longitude, latitude)
  }


  def validateHouseMaterials(): Unit = {
    validateMaterials(MaterialType.CEMENT, 100)
    validateMaterials(MaterialType.GRAVEL, 50)
    validateMaterials(MaterialType.SAND, 90)
    validateMaterials(MaterialType.WOOD, 20)
    validateMaterials(MaterialType.ADOBE, 100)
  }

  def validateLakeMaterials(): Unit = {
    validateMaterials(MaterialType.CEMENT, 50)
    validateMaterials(MaterialType.GRAVEL, 60)
    validateMaterials(MaterialType.SAND, 80)
    validateMaterials(MaterialType.WOOD, 10)
    validateMaterials(MaterialType.ADOBE, 20)

  }

  def validateFootballFieldMaterials(): Unit = {
    validateMaterials(MaterialType.CEMENT, 20)
    validateMaterials(MaterialType.GRAVEL, 20)
    validateMaterials(MaterialType.SAND, 20)
    validateMaterials(MaterialType.WOOD, 20)
    validateMaterials(MaterialType.ADOBE, 20)

  }

  def validateBuildingMaterials(): Unit = {
    validateMaterials(MaterialType.CEMENT, 200)
    validateMaterials(MaterialType.GRAVEL, 100)
    validateMaterials(MaterialType.SAND, 180)
    validateMaterials(MaterialType.WOOD, 40)
    validateMaterials(MaterialType.ADOBE, 200)

  }


  def validateGymMaterials(): Unit = {
    validateMaterials(MaterialType.CEMENT, 50)
    validateMaterials(MaterialType.GRAVEL, 25)
    validateMaterials(MaterialType.SAND, 45)
    validateMaterials(MaterialType.WOOD, 10)
    validateMaterials(MaterialType.ADOBE, 50)

  }

  def validateMaterials(materialType: String, amount: Int): Unit = {
    val resultFuture = Connection.db.run(SlickTables.materialTable.filter(fetchedMat => fetchedMat.material.like(s"%$materialType%") && fetchedMat.tons.value >= amount).result)
    resultFuture.onComplete {
      case Success(fetchedMaterials) => {
        if (fetchedMaterials.isEmpty) {
          println(s"Not enough $materialType")
          PrivateExecutionContext.executor.shutdown()
        } else {
          println(s"There's enough $materialType")
        }


      }
      case Failure(exc) => println(s"There was an error in the material fetching")
        PrivateExecutionContext.executor.shutdown()
    }

  }

}
