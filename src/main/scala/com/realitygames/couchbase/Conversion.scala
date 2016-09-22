package com.realitygames.couchbase

import java.util
import java.util.{HashMap => JHashMap}

import com.couchbase.client.java.document.json.JsonObject
import play.api.libs.json._

import scala.collection.JavaConversions.{asScalaBuffer, mapAsScalaMap}
import scala.language.implicitConversions

protected[couchbase] object Conversion {

   implicit class JsonObjectExt(underlying: JsonObject) {

     def toPlayJson: JsObject = douchbaseJsonObject2playJsObject(underlying)
  }


  def value2jsValue(value: Any): JsValue = {
    value match {
      case s: String =>
        JsString(s)
      case b: Boolean =>
        JsBoolean(b)
      case null =>
        JsNull
      case a: util.ArrayList[_] =>
        JsArray(a map value2jsValue)
      case i: Number =>
        JsNumber(BigDecimal(i.toString))
      case obj: JsonObject =>
        douchbaseJsonObject2playJsObject(obj)
      case obj: JHashMap[String, _] @unchecked=>
        JsObject(obj mapValues value2jsValue)
    }
  }

  implicit def douchbaseJsonObject2playJsObject(obj: JsonObject): JsObject =
    JsObject(obj.toMap mapValues value2jsValue)

}