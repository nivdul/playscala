package model

/**
 * Le mod�le repr�sente ce que fait le syst�me (�v�nements) et avec quelques identit�s
 * 
 * Listes des commandes sur le mod�le :
 *  - ajouter une image dans un album ou non
 *  - supprimer une image
 *  - chercher une image
 *  - afficher toutes les images d'un album
 *  - trier les images suivant un crit�re (par ordre alphab�tique, date...)
 */

// permettrai de voir la notion de trait, voire de sealed mais pas oblig�
trait Event

// permet de voir clairement une utilisation du pattern matching
case class AddImage(image:Image) extends Event

case class RemoveImage(withId:ImageId) extends Event

case class SearchImages(term:String) extends Event

case class FetchAllImagesFromOneAlbum(albumId:AlbumId) extends Event

case class SortImages(criteria: Option[Criteria]) extends Event


// un post int�ressant sur le sujet : http://blog.xebia.fr/2012/01/11/scala-jouer-avec-le-pattern-matching/
