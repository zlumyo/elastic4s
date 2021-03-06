package com.sksamuel.elastic4s.http.search.queries.geo

import com.sksamuel.elastic4s.searches.queries.geo.GeoBoundingBoxQueryDefinition
import org.elasticsearch.common.xcontent.{XContentBuilder, XContentFactory}

object GeoBoundingBoxQueryBodyFn {

  def apply(q: GeoBoundingBoxQueryDefinition): XContentBuilder = {
    val builder = XContentFactory.jsonBuilder()
    builder.startObject()
    builder.startObject("geo_bounding_box")
    builder.startObject(q.field)

    q.corners.foreach { corners =>
      builder.startObject("top_left")
      builder.field("lat", corners.top)
      builder.field("lon", corners.left)
      builder.endObject()
      builder.startObject("bottom_right")
      builder.field("lat", corners.bottom)
      builder.field("lon", corners.right)
      builder.endObject()
    }

    q.geohash.foreach { case (topleft, bottomright) =>
      builder.field("top_left", topleft)
      builder.field("bottom_right", bottomright)
      builder.endObject()
    }

    q.geoExecType.map(_.name.toLowerCase).foreach(builder.field("type", _))
    q.ignoreUnmapped.foreach(builder.field("ignore_unmapped", _))
    q.validationMethod.map(_.name).foreach(builder.field("validation_method", _))
    q.queryName.foreach(builder.field("_name", _))

    builder.endObject()
    builder.endObject()
    builder.endObject()
    builder
  }
}
