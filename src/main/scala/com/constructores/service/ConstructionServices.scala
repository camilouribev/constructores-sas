package com.constructores.service

import com.constructores.Connection
import com.constructores.model.{Construction, ConstructionType, SlickTables, ConstructionStatus, ConstructionDuration}
import com.constructores.service.PrivateExecutionContext._
import com.constructores.utils.Validations.{coordinateValidationHandler}
import slick.jdbc.PostgresProfile.api._
import java.time.LocalDate


import scala.Console.println
import scala.concurrent.Future
import scala.io.StdIn
import scala.util.{Failure, Success}


object ConstructionServices {

  def createConstruction(construction: Construction): Unit = {
    val queryDescription = SlickTables.constructionTable += construction

    val futureId: Future[Int] = Connection.db.run(queryDescription)
    futureId.onComplete {
      case Success(newConstructionId) => println(s"Construction created succesfully, new id is $newConstructionId!")
      case Failure(exception) => println(s"Query failed, reason: $exception")
    }

    Thread.sleep(10000)
  }

  def getConstructions(): Unit = {
    val resultFuture = Connection.db.run(SlickTables.constructionTable.result)
    resultFuture.onComplete {
      case Success(constructions) => println(s"Fetched: ${constructions.mkString(",")}")
      case Failure(exception) => println(s"Fetching failed: $exception")
    }
    Thread.sleep(10000)
  }

  def getLastDate(): Unit = {
    val resultFuture = Connection.db.run(SlickTables.constructionTable.sorted(_.completionDate).result)
    resultFuture.onComplete {
      case Success(constructions) => println(s"Fetched: ${constructions.last.completionDate}")
      case Failure(exception) => println(s"Fetching failed: $exception")
    }
    Thread.sleep(10000)
  }

  def createRetrievingLastDate(newConstructionType: String, longitude: Long, latitude: Long): Unit = {
    val resultFuture = Connection.db.run(SlickTables.constructionTable.sorted(_.completionDate).result)
    resultFuture.onComplete {
      case Success(constructions) => {
        val constructionDuration: Int = ConstructionDuration.constructionDurationMap(newConstructionType)

        if (constructions.last.completionDate.isAfter(LocalDate.now())) {
          val startingDate = constructions.last.completionDate.plusDays(1)
          val completionDate = startingDate.plusDays(constructionDuration)
          val newConstruction: Construction = Construction(1L, newConstructionType, longitude, latitude,
            startingDate, completionDate, ConstructionStatus.PENDING)
          createConstruction(newConstruction)

        } else {
          val startingDate = LocalDate.now()
          val completionDate = startingDate.plusDays(constructionDuration)
          val newConstruction: Construction = Construction(1L, newConstructionType, longitude, latitude,
            startingDate, completionDate, ConstructionStatus.PENDING)
          createConstruction(newConstruction)
        }

      }
      case Failure(exception) => println(s"Fetching failed: $exception")
        PrivateExecutionContext.executor.shutdown()
    }
    Thread.sleep(10000)
  }


  def handleCreateConstruction(): Unit = {
    println(s"Please select the type of construction you want to build:  \n")
    println("1. House ")
    println("2. Lake ")
    println("3. Football field ")
    println("4. Building ")
    println("5. Gym ")

    val input: String = StdIn.readLine()

    input.trim().toLowerCase().toInt match {
      case 1 => coordinateValidationHandler(ConstructionType.HOUSE)
      case 2 => coordinateValidationHandler(ConstructionType.LAKE)
      case 3 => coordinateValidationHandler(ConstructionType.FOOTBALL_FIELD)
      case 4 => coordinateValidationHandler(ConstructionType.BUILDING)
      case 5 => coordinateValidationHandler(ConstructionType.GYM)
      case _ => println("Invalid command, please try again")
    }
  }


}
