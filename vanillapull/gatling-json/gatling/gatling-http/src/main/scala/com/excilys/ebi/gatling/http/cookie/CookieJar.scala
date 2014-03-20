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
package com.excilys.ebi.gatling.http.cookie

import java.net.URI

import scala.annotation.tailrec

import com.ning.http.client.cookie.Cookie

object CookieJar {

	// rfc6265#section-1 Cookies for a given host are shared  across all the ports on that host
	private def requestDomain(uri: URI) = uri.getHost.toLowerCase

	// rfc6265#section-5.1.3
	// check "The string is a host name (i.e., not an IP address)" ignored
	def domainMatches(string: String, domain: String) = domain == string || string.endsWith("." + domain)

	// rfc6265#section-5.1.4
	def requestPath(requestURI: URI) =
		Option(requestURI.getPath).map {
			case requestPath if requestPath.count(_ == '/') <= 1 => "/"
			case requestPath => requestPath.substring(0, requestPath.lastIndexOf('/'))
		}.getOrElse("/")

	// rfc6265#section-5.1.4
	def pathMatches(cookiePath: String, requestPath: String) =
		cookiePath == requestPath ||
			requestPath.startsWith(cookiePath) &&
			(cookiePath.last == '/' || requestPath.charAt(cookiePath.length) == '/')

	// rfc6265#section-5.2.3
	def cookieDomain(cookieDomain: String, requestDomain: String) = Option(cookieDomain)
		// Let cookie-domain be the attribute-value without the leading %x2E (".") character.
		.map(dom => (if (dom.charAt(0) == '.') dom.substring(1) else dom).toLowerCase)
		.getOrElse(requestDomain)

	// rfc6265#section-5.2.4
	def cookiePath(rawCookiePath: String, defaultPath: String) = Option(rawCookiePath)
		.map {
			case path if path.startsWith("/") => path
			case _ => defaultPath
		}.getOrElse(defaultPath)

	def apply(uri: URI, cookies: List[Cookie]) = (new CookieJar(Map.empty)).add(uri, cookies)
}

case class CookieJar(store: Map[String, List[Cookie]]) {

	private val MAX_AGE_UNSPECIFIED = -1L

	/**
	 * @param uri       the uri this cookie associated with.
	 *                  if <tt>null</tt>, this cookie will not be associated
	 *                  with an URI
	 * @param cookie    the cookie to store
	 */
	def add(requestURI: URI, rawCookies: List[Cookie]): CookieJar = {

		val requestDomain = CookieJar.requestDomain(requestURI)
		val requestPath = CookieJar.requestPath(requestURI)

		val fixedCookies = rawCookies.map { cookie =>
			val cookieDomain = CookieJar.cookieDomain(cookie.getDomain, requestDomain)
			val cookiePath = CookieJar.cookiePath(cookie.getPath, requestPath)

			if (cookieDomain != cookie.getDomain || cookiePath != cookie.getPath)
				new Cookie(
					cookie.getName,
					cookie.getValue,
					cookie.getRawValue,
					cookieDomain,
					cookiePath,
					cookie.getExpires,
					cookie.getMaxAge,
					cookie.isSecure,
					cookie.isHttpOnly)
			else
				cookie
		} filter {
			// rfc6265#section-4.1.2.3 Reject the cookies when the domains don't match
			cookie => CookieJar.domainMatches(requestDomain, cookie.getDomain)
		}

		def cookiesEquals(c1: Cookie, c2: Cookie) = c1.getName.equalsIgnoreCase(c2.getName) && c1.getDomain == c2.getDomain && c1.getPath == c2.getPath

		@tailrec
		def addOrReplaceCookies(newCookies: List[Cookie], oldCookies: List[Cookie]): List[Cookie] = newCookies match {
			case Nil => oldCookies
			case newCookie :: moreNewCookies =>
				val updatedCookies = newCookie :: oldCookies.filterNot(cookiesEquals(_, newCookie))
				addOrReplaceCookies(moreNewCookies, updatedCookies)
		}

		def hasExpired(c: Cookie): Boolean = c.getMaxAge != MAX_AGE_UNSPECIFIED && c.getMaxAge <= 0

		val newCookies = addOrReplaceCookies(fixedCookies, store.get(requestDomain).getOrElse(Nil))
		val nonExpiredCookies = newCookies.filterNot(hasExpired)
		new CookieJar(store + (requestDomain -> nonExpiredCookies))
	}

	def get(requestURI: URI): List[Cookie] = {

		val requestDomain = CookieJar.requestDomain(requestURI)
		val requestPath = CookieJar.requestPath(requestURI)

		val cookiesWithExactDomain = store.get(requestDomain).getOrElse(Nil).filter(cookie => CookieJar.pathMatches(cookie.getPath, requestPath))
		val cookiesWithExactDomainNames = cookiesWithExactDomain.map(_.getName.toLowerCase)

		// known limitation: might return duplicates if more than 1 cookie with a given name with non exact domain
		def matchSubDomain(cookie: Cookie) = !cookiesWithExactDomainNames.contains(cookie.getName.toLowerCase) &&
			CookieJar.domainMatches(requestDomain, cookie.getDomain) &&
			CookieJar.pathMatches(cookie.getPath, requestPath)

		val cookiesWithMatchingSubDomain = store
			.collect { case (key, cookies) if key != requestDomain => cookies.filter(matchSubDomain) }
			.flatten

		// known limitation: don't handle runtime expiration, intended for stress test
		cookiesWithExactDomain ++ cookiesWithMatchingSubDomain
	}
}
