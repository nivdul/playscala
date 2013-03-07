package model

import play.api.db.DB
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

/**
 *  classe principale qui va gérer les différentes commandes de mon système via des l'objet Event
 */
// on peut à la place du var, utiliser une ListBuffer et faire liste.append(newElement)
// ou en val puis case class et utilisation du copy(listes = newelement::liste) initialisation à Nil pour la liste
case class System(event: Event, images: List[Image] = Nil, albums: List[Album] = Nil) extends App {

  /**
   * action propre à chaque Event
   */
  def applyEvent(event: Event) = event match {
    case AddImage(image) => add(image)
    case RemoveImage(withId) => remove(withId)
    case SearchImages(term) => search(term)
    case FetchAllImagesFromOneAlbum(albumId) => fetchImages(albumId)
    case SortImages(criteria) => criteria match {
      case Some(criteria) => sort(criteria)
      case None => this.images
    }
  }

  def add(image: Image) = copy(images = image :: images )

  // méthode filter et utilisation des méthodes anonymes
  def remove(withId: Int): Unit = copy(images = images.filter(x => x == withId))

  def search(term: String): List[Image] = for (x <- images if x == term) yield x

  def fetchImages(albumId: Int): Unit = copy(images = images.filter(x => x == albumId))

  def sort(criteria: Criteria): Unit = criteria match {
    case ByName => this.images // à faire
    case ByDate => this.images // à faire
    case _ => this.images // à faire
  }

  ///////////////////////
  // -- Requêtes SQL-- //
  ///////////////////////
  /**
   * image à partir de l'id.
   */
  def findImageById(id: Int): Image = {
    DB.withConnection { implicit connection =>
      SQL("select * from image where id = {id}").on('id -> id).as(Image.simple)
    }
  }
  /**
   * album à partir de l'id.
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