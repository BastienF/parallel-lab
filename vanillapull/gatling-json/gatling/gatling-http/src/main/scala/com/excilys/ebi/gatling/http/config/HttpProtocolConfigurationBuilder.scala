/**
 * Copyright 2011-2013 eBusiness Information, Groupe Excilys (www.ebusinessinformation.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.excilys.ebi.gatling.http.config

import scala.collection.mutable

import com.excilys.ebi.gatling.core.config.GatlingConfiguration.configuration
import com.excilys.ebi.gatling.http.Headers
import com.excilys.ebi.gatling.http.ahc.GatlingHttpClient
import com.excilys.ebi.gatling.http.response.ExtendedResponse
import com.ning.http.client.{ ProxyServer, Request, RequestBuilder }

import grizzled.slf4j.Logging

/**
 * HttpProtocolConfigurationBuilder class companion
 */
object HttpProtocolConfigurationBuilder {

	private[gatling] val default = new HttpProtocolConfigurationBuilder(Attributes(None, None, None, true, true, true, true, true, false, Map.empty, configuration.http.warmUpUrl, None, None, None))

	val warmUpUrls = mutable.Set.empty[String]
}

private case class Attributes(baseUrls: Option[List[String]],
	proxy: Option[ProxyServer],
	securedProxy: Option[ProxyServer],
	followRedirectEnabled: Boolean,
	automaticRefererEnabled: Boolean,
	cachingEnabled: Boolean,
	responseChunksDiscardingEnabled: Boolean,
	shareClient: Boolean,
	shareConnections: Boolean,
	baseHeaders: Map[String, String],
	warmUpUrl: Option[String],
	virtualHost: Option[String],
	extraRequestInfoExtractor: Option[Request => List[String]],
	extraResponseInfoExtractor: Option[ExtendedResponse => List[String]])

/**
 * Builder for HttpProtocolConfiguration used in DSL
 *
 * @param baseUrl the radix of all the URLs that will be used (eg: http://mywebsite.tld)
 * @param proxy a proxy through which all the requests must pass to succeed
 */
class HttpProtocolConfigurationBuilder(attributes: Attributes) extends Logging {

	/**
	 * Sets the baseURL of the future HttpProtocolConfiguration
	 *
	 * @param baseUrl the base url that will be set
	 */
	def baseURL(baseUrl: String) = new HttpProtocolConfigurationBuilder(attributes.copy(baseUrls = Some(List(baseUrl))))

	def baseURLs(baseUrl1: String, baseUrl2: String, baseUrls: String*) = new HttpProtocolConfigurationBuilder(attributes.copy(baseUrls = Some(baseUrl1 :: baseUrl2 :: baseUrls.toList)))

	def disableFollowRedirect = new HttpProtocolConfigurationBuilder(attributes.copy(followRedirectEnabled = false))

	def disableAutomaticReferer = new HttpProtocolConfigurationBuilder(attributes.copy(automaticRefererEnabled = false))

	def disableCaching = new HttpProtocolConfigurationBuilder(attributes.copy(cachingEnabled = false))

	def disableResponseChunksDiscarding = new HttpProtocolConfigurationBuilder(attributes.copy(responseChunksDiscardingEnabled = false))

	def disableClientSharing = new HttpProtocolConfigurationBuilder(attributes.copy(shareClient = false))

	def shareConnections = new HttpProtocolConfigurationBuilder(attributes.copy(shareConnections = true))

	def baseHeaders(headers: Map[String, String]) = new HttpProtocolConfigurationBuilder(attributes.copy(baseHeaders = attributes.baseHeaders ++ headers))

	def acceptHeader(value: String) = new HttpProtocolConfigurationBuilder(attributes.copy(baseHeaders = attributes.baseHeaders + (Headers.Names.ACCEPT -> value)))

	def acceptCharsetHeader(value: String) = new HttpProtocolConfigurationBuilder(attributes.copy(baseHeaders = attributes.baseHeaders + (Headers.Names.ACCEPT_CHARSET -> value)))

	def acceptEncodingHeader(value: String) = new HttpProtocolConfigurationBuilder(attributes.copy(baseHeaders = attributes.baseHeaders + (Headers.Names.ACCEPT_ENCODING -> value)))

	def acceptLanguageHeader(value: String) = new HttpProtocolConfigurationBuilder(attributes.copy(baseHeaders = attributes.baseHeaders + (Headers.Names.ACCEPT_LANGUAGE -> value)))

	def authorizationHeader(value: String) = new HttpProtocolConfigurationBuilder(attributes.copy(baseHeaders = attributes.baseHeaders + (Headers.Names.AUTHORIZATION -> value)))

	def connection(value: String) = new HttpProtocolConfigurationBuilder(attributes.copy(baseHeaders = attributes.baseHeaders + (Headers.Names.CONNECTION -> value)))

	def doNotTrackHeader(value: String) = new HttpProtocolConfigurationBuilder(attributes.copy(baseHeaders = attributes.baseHeaders + (Headers.Names.DO_NOT_TRACK -> value)))

	def userAgentHeader(value: String) = new HttpProtocolConfigurationBuilder(attributes.copy(baseHeaders = attributes.baseHeaders + (Headers.Names.USER_AGENT -> value)))

	def warmUp(warmUpUrl: String) = new HttpProtocolConfigurationBuilder(attributes.copy(warmUpUrl = Some(warmUpUrl)))

	def disableWarmUp = new HttpProtocolConfigurationBuilder(attributes.copy(warmUpUrl = None))

	def virtualHost(virtualHost: String) = new HttpProtocolConfigurationBuilder(attributes.copy(virtualHost = Some(virtualHost)))

	def requestInfoExtractor(f: Request => List[String]) = new HttpProtocolConfigurationBuilder(attributes.copy(extraRequestInfoExtractor = Some(f)))

	def responseInfoExtractor(f: ExtendedResponse => List[String]) = new HttpProtocolConfigurationBuilder(attributes.copy(extraResponseInfoExtractor = Some(f)))

	def proxy(host: String, port: Int) = new HttpProxyBuilder(this, host, port)

	private[http] def addProxies(httpProxy: ProxyServer, httpsProxy: Option[ProxyServer]) = new HttpProtocolConfigurationBuilder(attributes.copy(proxy = Some(httpProxy), securedProxy = httpsProxy))

	private[http] def build = {

		require(!(!attributes.shareClient && attributes.shareConnections), "Invalid configuration: can't stop sharing the HTTP client while still sharing connections!")

		attributes.warmUpUrl.map { url =>
			if (!HttpProtocolConfigurationBuilder.warmUpUrls.contains(url)) {
				HttpProtocolConfigurationBuilder.warmUpUrls += url
				val requestBuilder = new RequestBuilder().setUrl(url)

				attributes.proxy.map { proxy => if (url.startsWith("http://")) requestBuilder.setProxyServer(proxy) }
				attributes.securedProxy.map { proxy => if (url.startsWith("https://")) requestBuilder.setProxyServer(proxy) }

				try {
					GatlingHttpClient.defaultClient.executeRequest(requestBuilder.build).get
				} catch {
					case e: Exception => info("Couldn't execute warm up request " + url, e)
				}
			}
		}

		HttpProtocolConfiguration(attributes.baseUrls, attributes.proxy, attributes.securedProxy, attributes.followRedirectEnabled, attributes.automaticRefererEnabled, attributes.cachingEnabled, attributes.responseChunksDiscardingEnabled, attributes.shareClient, attributes.shareConnections, attributes.baseHeaders, attributes.virtualHost, attributes.extraRequestInfoExtractor, attributes.extraResponseInfoExtractor)
	}
}