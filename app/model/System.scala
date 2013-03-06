package model

import play.api.db.DB
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

/**
 *  classe principale qui va g�rer les diff�rentes commandes de mon syst�me via des l'objet Event
 */
case class System(event: Event, var images: List[Image], var albums: List[Album]) extends App {

  /**
   * action propre � chaque Event
   */
  def applyEvent(event: Event) = event match {
    case AddImage(image) => add(image)
    case RemoveImage(withId) => remove(withId)
    case SearchImages(term) => search(term)
    case FetchAllImagesFromOneAlbum(albumId) => fetchImages(albumId)
    // utilisation des options, et gestion de ces derni�res avec le pattern matching
    case SortImages(criteria) => criteria match {
      case Some(criteria) => sort(criteria)
      case None => this.images
    }
  }

  def add(image: Image) = { images = image :: images }

  // m�thode filter et utilisation des m�thodes anonymes
  def remove(withId: Int): Unit = { images = images.filter(x => x == withId) }

  def search(term: String): List[Image] = for (x <- images if x == term) yield x

  def fetchImages(albumId: Int): Unit = { images = images.filter(x => x == albumId) }

  def sort(criteria: Criteria): Unit = criteria match {
    case ByName => this.images // � faire
    case ByDate => this.images // � faire
    case _ => this.images // � faire
  }

  ///////////////////////
  // -- Requ�tes SQL-- //
  ///////////////////////
  /**
   * image � partir de l'id.
   */
  def findImageById(id: Int): Image = {
    DB.withConnection { implicit connection =>
      SQL("select * from image where id = {id}").on('id -> id).as(Image.simple)
    }
  }
  /**
   * album � partir de l'id.
   */
  def findAlbumById(id: Int): Album = {
    DB.withConnection { implicit connection =>
      SQL("select * from album where id = {id}").on('id -> id).as(Album.simple)
    }
  }
  /**
   * suppression image
   */
  def deleteImage(id: Int) = {
    DB.withConnection { implicit connection =>
      SQL("delete from image where id = {id}").on('id -> id).executeUpdate()
    }
  }
  /**
   * toutes les images d'un album id
   */
  def findImagesForAlbumId(id: Int): List[Image] = {
    DB.withConnection { implicit connection =>
      SQL("select * from image where albumId = {id}").on('id -> id).as(Image.simple *)
    }
  }
  /**
   * recherche par nom
   */
  def findImagesByName(name: String): List[Image] = {
        DB.withConnection { implicit connection =>
      SQL("select * from image where name = {name}").on('name -> name).as(Image.simple *)
    }
  }
   /**
   * Insert une image
   */
  def insertImage(image: Image) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into image values (
            (select next value for image_seq), 
            {name}, {albumId}
          )
        """
      ).on(
        'name -> image.name,
        'albumId -> image.albumId
      ).executeUpdate()
    }
  }
    /**
   * Insert un album
   */
  def insertAlbum(album: Album) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into album values (
            (select next value for album_seq), 
            {name}
          )
        """
      ).on(
        'name -> album.name
      ).executeUpdate()
    }
  }
}