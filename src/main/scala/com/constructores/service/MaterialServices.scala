package com.constructores.service

import com.constructores.Connection
import com.constructores.model.{Material, SlickTables}
import com.constructores.service.PrivateExecutionContext._
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future
import scala.util.{Failure, Success}

object MaterialServices {

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


}
