package models

import models.Tables.UserRow

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

}
