/**
 * Copyright 2011-2013 eBusiness Information, Groupe Excilys (www.ebusinessinformation.fr)
 *
 * Licensed under the Gatling Highcharts License
 */
package com.excilys.ebi.gatling.highcharts.series

class StackedColumnSeries(name: String, data: Seq[(String, Int)], color: String) extends ColumnSeries(name, data, List(color)) {

	def elements: Seq[String] = data.map { case (_, count) => count.toString }
}