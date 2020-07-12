package phony.cli.template

import java.text.SimpleDateFormat

import cats.effect.IO
import phony.Phony
import phony.cli.template.parser.FunctionCall

class DefaultFunctionResolver(val P: Phony[IO]) extends FunctionResolver[IO] {

  override def resolve: PartialFunction[FunctionCall, IO[String]] = {
    case FunctionCall("alphanumeric.char", Nil)             => P.alphanumeric.char.map(_.toString)
    case FunctionCall("alphanumeric.boolean", Nil)          => P.alphanumeric.boolean.map(_.toString)
    case FunctionCall("alphanumeric.float", Nil)            => P.alphanumeric.float.map(_.toString)
    case FunctionCall("alphanumeric.custom", format :: Nil) => P.alphanumeric.custom(format)
    case FunctionCall("alphanumeric.hash", Nil)             => P.alphanumeric.hash
    case FunctionCall("alphanumeric.has", length :: Nil)    => P.alphanumeric.hash(length.toInt).map(_.toString)
    case FunctionCall("alphanumeric.number", Nil)           => P.alphanumeric.number.map(_.toString)
    case FunctionCall("alphanumeric.number", max :: Nil)    => P.alphanumeric.number(max.toInt).map(_.toString)
    case FunctionCall("alphanumeric.number", min :: max :: Nil) =>
      P.alphanumeric.number(min.toInt, max.toInt).map(_.toString)

    case FunctionCall("calendar.year", Nil)           => P.calendar.year.map(_.toString)
    case FunctionCall("calendar.day", Nil)            => P.calendar.day
    case FunctionCall("calendar.month", Nil)          => P.calendar.month
    case FunctionCall("calendar.time24h", Nil)        => P.calendar.time24h
    case FunctionCall("calendar.time12h", Nil)        => P.calendar.time12h
    case FunctionCall("calendar.date", Nil)           => P.calendar.date.map(d => new SimpleDateFormat("yyyy-MM-dd").format(d))
    case FunctionCall("calendar.date", format :: Nil) => P.calendar.date(format)
    case FunctionCall("calendar.iso8601", Nil)        => P.calendar.iso8601
    case FunctionCall("calendar.timezone", Nil)       => P.calendar.timezone

    case FunctionCall("contact.firstName", Nil)          => P.contact.firstName
    case FunctionCall("contact.lastName", Nil)           => P.contact.lastName
    case FunctionCall("contact.prefix", Nil)             => P.contact.prefix
    case FunctionCall("contact.suffix", Nil)             => P.contact.suffix
    case FunctionCall("contact.fullName", Nil)           => P.contact.fullName
    case FunctionCall("contact.fullName", p :: s :: Nil) => P.contact.fullName(p.toBoolean, s.toBoolean)
    case FunctionCall("contact.username", Nil)           => P.contact.username
    case FunctionCall("contact.phone", Nil)              => P.contact.phone
    case FunctionCall("contact.cellPhone", Nil)          => P.contact.cellPhone

    case FunctionCall("internet.uuid", Nil)     => P.internet.uuid
    case FunctionCall("internet.email", Nil)    => P.internet.email
    case FunctionCall("internet.password", Nil) => P.internet.password
    case FunctionCall("internet.domain", Nil)   => P.internet.domain
    case FunctionCall("internet.hostname", Nil) => P.internet.hostname
    case FunctionCall("internet.protocol", Nil) => P.internet.protocol
    case FunctionCall("internet.url", Nil)      => P.internet.url
    case FunctionCall("internet.ip", Nil)       => P.internet.ip
    case FunctionCall("internet.ipv6", Nil)     => P.internet.ipv6
    case FunctionCall("internet.hashtag", Nil)  => P.internet.hashtag

    case FunctionCall("lorem.word", Nil)                   => P.lorem.word
    case FunctionCall("lorem.words", sep :: Nil)           => P.lorem.words.map(_.mkString(sep))
    case FunctionCall("lorem.words", length :: sep :: Nil) => P.lorem.words(length.toInt).map(_.mkString(sep))
    case FunctionCall("lorem.text", Nil)                   => P.lorem.text
    case FunctionCall("lorem.text", lines :: Nil)          => P.lorem.text(lines.toInt)
    case FunctionCall("lorem.sentence", length :: Nil)     => P.lorem.sentence(length.toInt)
    case FunctionCall("lorem.sentence", Nil)               => P.lorem.sentence

    case FunctionCall("location.latitude", Nil)    => P.location.latitude
    case FunctionCall("location.longitude", Nil)   => P.location.longitude
    case FunctionCall("location.countryName", Nil) => P.location.countryName
    case FunctionCall("location.countryCode", Nil) => P.location.countryCode
    case FunctionCall("location.postalCode", Nil)  => P.location.postalCode

  }
}
