package models

import models.Tables.{SplashPostRow, UserRow}

/**
  * author Yuki Hirai on 2017/03/21.
  */
object Entities {

  case class HistoryEntity(method: String, path: String)

  case class AccountEntity(
                            session: (String, String)
                            , history: Option[Seq[HistoryEntity]] = None
                            , user: Option[UserRow] = None

                          ) {
    def isLogin: Boolean = {
      user.fold(false) { _ => true }
    }
  }

  case class VerifyMailEntity(
                               session: (String, String)
                               , email: Option[String] = None
                               , password: Option[String] = None
                             )

  case class SplashEntity(
                           splashPosts: Option[Seq[SplashPostRow]] = None
                         ) extends BaseEntity

}

trait BaseEntity