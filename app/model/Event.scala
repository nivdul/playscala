package model

/**
 * Le modèle représente ce que fait le système (événements) et avec quelques identités
 * 
 * Listes des commandes sur le modèle :
 *  - ajouter une image dans un album ou non
 *  - supprimer une image
 *  - chercher une image
 *  - afficher toutes les images d'un album
 *  - trier les images suivant un critère (par ordre alphabétique, date...)
 */

// permettrai de voir la notion de trait, voire de sealed mais pas obligé
trait Event

// permet de voir clairement une utilisation du pattern matching
case class AddImage(image:Image) extends Event

case class RemoveImage(withId:ImageId) extends Event

case class SearchImages(term:String) extends Event

case class FetchAllImagesFromOneAlbum(albumId:AlbumId) extends Event

case class SortImages(criteria: Option[Criteria]) extends Event


// un post intéressant sur le sujet : http://blog.xebia.fr/2012/01/11/scala-jouer-avec-le-pattern-matching/
