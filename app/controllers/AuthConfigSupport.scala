package controllers

import jp.t2v.lab.play2.auth.{AsyncIdContainer, AuthConfig, TransparentIdContainer}
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}
import services.UserService

trait AuthConfigSupport extends AuthConfig {

  val userService: UserService

  override type Id = String
  override type User = models.User
  override type Authority = Nothing
  override def idContainer: AsyncIdContainer[String] = AsyncIdContainer(new TransparentIdContainer[Id])
  override lazy val tokenAccessor = new RememberMeTokenAccessor(sessionTimeoutInSeconds)
  override def sessionTimeoutInSeconds: Int = 3600

  override def resolveUser(id: String)(implicit context: ExecutionContext): Future[Option[User]] =
    Future {
      userService.findByEmail(id).get
    }
  override def authenticationFailed(request: RequestHeader)(implicit context: ExecutionContext): Future[Result] =
    Future.successful(
      Redirect(routes.AuthController.index())
    )
  override def authorizationFailed(request: RequestHeader, user: User, authority: Option[Nothing])(
    implicit context: ExecutionContext
  ): Future[Result] = Future.successful(
    Forbidden("no permission")
  )
  override def authorize(user: User, authority: Nothing)(implicit context: ExecutionContext): Future[Boolean] =
    Future.successful {
      true
    }
}