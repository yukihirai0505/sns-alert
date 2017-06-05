package services

import java.io.File
import javax.inject.Inject

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.i18n.MessagesApi
import play.api.libs.Files
import play.api.mvc.{Controller, MultipartFormData, Request}

import com.yukihirai0505.iPost.iPostNatural
import constants.Constants
import controllers.BaseTrait
import daos.ReserveInstagramPostDAO
import dtos.ViewDto.{HeadTagInfo, ViewDto}
import forms.ReserveInstagramPostForms
import models.Entities.iPostEntity
import models.Tables.ReserveInstagramPostRow
import org.joda.time.DateTime
import utils.{HashUtil, SessionUtil}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yukihirai on 2017/03/20.
  */
class iPostService @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, implicit val cache: CacheApi, implicit val messagesApi: MessagesApi)
  extends ReserveInstagramPostDAO(dbConfigProvider) with Controller with BaseTrait {

  def getIndexViewDto(implicit req: Request[_]): Future[ViewDto] = {
    val account = SessionUtil.getAccount(req, cache)
    val v = createViewDto(req, account, HeadTagInfo(title = "iPost"))
    listAll.flatMap { posts =>
      val pageEntity = iPostEntity(reservePosts = Some(posts))
      Future successful v.copy(pageEntity = Some(pageEntity))
    }
  }


  def savePost(implicit req: Request[_]): Future[Boolean] = {
    val frm = ReserveInstagramPostForms.postForms.bindFromRequest()
    if (frm.hasErrors) Future successful false
    else {
      val username = frm.get.username
      val password = frm.get.password
      val caption = frm.get.caption
      import org.joda.time.format.DateTimeFormat
      val formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
      val dt = formatter.parseDateTime(frm.get.reserveTime.replace("T", " "))
      val reserveTime = dt
      val multipartReq = req.asInstanceOf[Request[MultipartFormData[Files.TemporaryFile]]]
      val picture = multipartReq.body.file(Constants.fileName).get
      val filename = s"${HashUtil.createUuid}-${picture.filename}"
      picture.ref.moveTo(new File(s"/tmp/$filename"))
      val row = ReserveInstagramPostRow(username, password, caption, filename, new DateTime(), reserveTime)
      add(row).flatMap { _ =>
        Future successful true
      }
    }
  }

  def postToInstagram: Future[Unit] = {
    getPostList.flatMap { posts =>
      posts.foreach { post =>
        val iPostNatural = new iPostNatural(post.username, post.password)
        iPostNatural.postNaturalWays(new File(s"/tmp/${post.filename}"), post.caption).flatMap { _ =>
          Future successful delete(post.id.get)
        }
      }
      Future successful Unit
    }
  }

}

