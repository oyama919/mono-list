package models

import java.time.ZonedDateTime

import scalikejdbc._
import skinny.orm.{Alias, SkinnyCRUDMapper}

case class Item(id: Option[Long],
  code: String,
  name: String,
  url: String,
  imageUrl: String,
  price: Int,
  createAt: ZonedDateTime,
  updateAt: ZonedDateTime)

object Item extends SkinnyCRUDMapper[Item] {

  override def tableName: String = "items"

  override def defaultAlias: Alias[Item] = createAlias("i")

  override def extract(rs: WrappedResultSet, n: ResultName[Item]): Item =
    autoConstruct(rs, n)

  private def toNamedValues(record: Item): Seq[(Symbol, Any)] = Seq(
    'code     -> record.code,
    'name     -> record.name,
    'url      -> record.url,
    'imageUrl -> record.imageUrl,
    'price    -> record.price,
    'createAt -> record.createAt,
    'updateAt -> record.updateAt
  )

  def create(item: Item)(implicit session: DBSession = AutoSession): Long =
    createWithAttributes(toNamedValues(item): _*)

  def update(item: Item)(implicit session: DBSession = AutoSession): Int =
    updateById(item.id.get).withAttributes(toNamedValues(item): _*)

}
