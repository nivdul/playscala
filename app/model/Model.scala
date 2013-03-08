package model

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Image(id: Pk[Int], name: String, albumId: Int)
case class Album(id: Pk[Int], name: String)

object Image {
  /**
   * parseur pour une image ResultSet
   */
  val simple = {
      get[Int]("image.id") ~
      get[String]("image.name") ~
      get[Int]("image.albumId") map {
        case id ~ name ~ albumId => Image(id, name, albumId)
      }
  }
}

object Album {
  /**
   * parseur pour un album ResultSet
   */
  val simple = {
      get[Int]("album.id") ~
      get[String]("album.name") map {
        case id ~ name => Album(id, name)
      }
  }

}


