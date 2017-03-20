package daos.traits

import com.google.inject.ImplementedBy
import daos.UserDAOImpl
import models.Tables.UserRow

import scala.concurrent.Future

/**
  * Created by yukihirai on 2017/03/18.
  */
@ImplementedBy(classOf[UserDAOImpl])
trait UserDAO {

  def add(user: UserRow): Future[String]

  def get(id: Int): Future[Option[UserRow]]

  def delete(id: Int): Future[Int]

  def listAll: Future[Seq[UserRow]]

}