package utils

import play.api.Logger.logger
import play.api.cache.CacheApi
import play.api.mvc.RequestHeader

import models.Entities.{AccountEntity, HistoryEntity}

import scala.concurrent.Future

/**
  * Created by yukihirai on 2017/03/18.
  */
object SessionUtil {

  /**
    * Get account from cache related with session
    */
  def getAccount(cache: CacheApi, r: RequestHeader) = {

    val account: AccountEntity = if (hasSessionId(r)) {
      val session: (String, String) = getSession(r)
      getCache(cache, session)
    } else {
      val session = makeSessionId
      logger.debug(s"::: SessionHelper makeSessionId. uuid = [$session]")
      AccountEntity(session = session)
    }

    // create histories
    val historyLimit: Int = 10
    val tmpHistory: Seq[HistoryEntity] = account.history.fold(Seq.empty[HistoryEntity]){e =>e.zipWithIndex.withFilter(x => x._2 > (e.size - historyLimit)).map(x => x._1)}
    val history: Seq[HistoryEntity] = tmpHistory :+ HistoryEntity(method = r.method, path= r.path)
    val entity = account.copy(history = Some(history))
    setAccount(cache, entity.session, entity)
    entity
  }

  // Get AccountEntity from cache. If there is no AccountEntity, Create new empty AccountEntity
  def getCache(cache: CacheApi, session: (String, String)): AccountEntity = {
    val cacheKey: String = getSessionCacheKey(session)
    cache.getOrElse[AccountEntity](cacheKey) {
      AccountEntity(session = session)
    }
  }

  def setAccount(cache: CacheApi, session: (String, String), account: AccountEntity): Unit = {
      setCache(cache, session, account)
  }

  def refreshSession(cache: CacheApi, account: AccountEntity): AccountEntity = {
    val newAccount = account.copy(session = makeSessionId)
    setAccount(cache, newAccount.session, newAccount)
    delCache(cache, account.session)
    newAccount
  }

  def delSession(cache: CacheApi, account: AccountEntity): Future[AccountEntity] = {
    delCache(cache, account.session)
    Future successful account
  }

  /*** private area ***/

  private def setCache(cache: CacheApi, session: (String, String), account: AccountEntity): Unit = {
    val cacheKey: String = getSessionCacheKey(session)
    logger.debug(s"::: set cache key is [$cacheKey]")
    cache.set(cacheKey, account)
  }

  private def delCache(cache: CacheApi, session: (String, String)): Unit = {
    val cacheKey: String = getSessionCacheKey(session)
    cache.remove(cacheKey)
  }

  // make key for searching inside cache
  private def getSessionCacheKey(uuid: (String, String)) = s"${getKey(uuid)}_${getValue(uuid)}"

  // make key for searching inside session
  private def getSessionInsideKey: String = "SESSION_ID"

  /**
    * Check sessionId is set
    */
  private def hasSessionId(request: RequestHeader): Boolean =  {
    request.session.data.get(getSessionInsideKey) match {
      case Some(x) =>
        logger.debug(s"::: session id is [$x]")
        true
      case None => false
    }
  }

  /**
    * Get sessionId(uuid) from session
    * if there is no sessionId, create new sessionId
    */
  private def getSession(request: RequestHeader): (String, String) =
    request.session.data.get(getSessionInsideKey).fold(makeSessionId){x => joinSessionIs(x)}

  private def getKey(s: (String, String)): String = s._1
  private def getValue(s: (String, String)): String = s._2

  /**
    * Create sessionId(uuid)
    */
  private def makeSessionId: (String, String) = joinSessionIs(HashUtil.createUuid)

  /**
    * session to play withSession format
    */
  private def joinSessionIs(uuid: String): (String, String) = (getSessionInsideKey, uuid)

}
