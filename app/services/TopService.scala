package services

import javax.inject.Inject

import daos.UserDAO
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.Controller

/**
  * Created by yukihirai on 2017/03/20.
  */
class TopService @Inject()(dbConfigProvider: DatabaseConfigProvider)
  extends UserDAO(dbConfigProvider) with Controller {

}

