package com.constructores.model

import java.time.LocalDate

case class Construction(  id:Long, constructionType: String,  longitude: Long, latitude: Long,
                          startingDate: LocalDate, completionDate: LocalDate, status: String )

case class Material(id:Long, material : String,  tons: Int)

object SlickTables{
  import slick.jdbc.PostgresProfile.api._

  class MaterialTable( tag: Tag) extends Table[Material](tag, Some("realestate"), "Material"){

    def id = column[Long]("material_id", O.PrimaryKey, O.AutoInc)
    def material = column[String]("name")
    def tons = column[Int]("available_quantity")


    override def * = (id, material, tons) <> (Material.tupled, Material.unapply)
  }

  lazy val materialTable = TableQuery[MaterialTable]

  class ConstructionTable(tag: Tag) extends Table[Construction](tag, Some("realestate"), "Construction") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def constructionType = column[String]("construction_type")
    def longitude = column[Long]("longitude")
    def latitude = column[Long]("latitude")
    def startingDate = column[LocalDate]("starting_date")
    def completionDate = column[LocalDate]("completion_date")
    def status = column[String]("status")

    override def * = {
      (id, constructionType,  longitude, latitude, startingDate, completionDate, status ) <> (Construction.tupled, Construction.unapply)
    }
  }

  lazy val constructionTable = TableQuery[ConstructionTable]


}

