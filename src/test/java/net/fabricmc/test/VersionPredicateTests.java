/*
 * Copyright (c) 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.test;

import java.util.function.Predicate;

import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.impl.util.version.SemanticVersionImpl;
import net.fabricmc.loader.impl.util.version.VersionPredicateParser;

public class VersionPredicateTests extends TestBase {
	public static void main(String[] args) throws Exception {
		// Test: comparator range with pre-releases.
		{
			Predicate<Version> predicate = VersionPredicateParser.parse(">=0.3.1-beta.2 <0.4.0");
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.1-beta.2", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.1-beta.2.1", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.1-beta.3", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.4+build.125", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.7", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.4.0-alpha.1", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.4-beta.7", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.1-beta.11", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.3.0", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.3.1-beta.1", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.4.0", false)));
		}

		{
			Predicate<Version> predicate = VersionPredicateParser.parse(">=0.3.1-beta.2 <0.4.0-");
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.1-beta.2", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.1-beta.2.1", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.1-beta.3", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.4+build.125", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.7", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.4-beta.7", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.1-beta.11", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.3.0", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.3.1-beta.1", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.4.0-alpha.1", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.4.0", false)));
		}

		{
			Predicate<Version> predicate = VersionPredicateParser.parse(">=1.4-");
			assertTrue(predicate.test(new SemanticVersionImpl("1.4-beta.2", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.4+build.125", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.4", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.4.2", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.3", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.3.5", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.3-alpha.1", false)));
		}

		{
			Predicate<Version> predicate = VersionPredicateParser.parse("<1.4");
			assertTrue(predicate.test(new SemanticVersionImpl("1.3", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.3.5", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.3-alpha.1", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.4-beta.2", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.4+build.125", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.4", false)));
		}

		{
			Predicate<Version> predicate = VersionPredicateParser.parse("<1.4-");
			assertTrue(predicate.test(new SemanticVersionImpl("1.3", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.3.5", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.3-alpha.1", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.4-beta.2", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.4+build.125", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.4", false)));
		}

		// Test: pre-release parts
		{
			Predicate<Version> predicate = VersionPredicateParser.parse(">=0.3.1-beta.8.d.10");
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.1-beta.9", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.1-beta.11", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.1-beta.8.e", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.1-beta.8.d.10", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.1-beta.9.d.5", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.1-beta.final", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.1-beta.-final-", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.3.1-beta.7", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.3.1-beta.8.d", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.3.1-beta.8.a", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.3.1-alpha.9", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.3.1-beta.8.8", false)));
		}

		// Test: x-range. "a.b.x" = ">=a.b.0- <a.(b+1).0-" (same major+minor, pre allowed)
		{
			Predicate<Version> predicate = VersionPredicateParser.parse("1.3.x");
			assertTrue(predicate.test(new SemanticVersionImpl("1.3.0", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.3.0-alpha.1", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.3.99", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.4.0", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.2.9", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.2.9-rc.6", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.4.0-alpha.1", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("2.0.0", false)));
		}

		// Test: smaller x-range. "a.x" = ">=a.0.0- <(a+1).0.0-" (same major, pre allowed)
		{
			Predicate<Version> predicate = VersionPredicateParser.parse("2.x");
			assertTrue(predicate.test(new SemanticVersionImpl("2.0.0", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("2.0.0-alpha.1", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("2.9.0-beta.2", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("2.2.4", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.99.99", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("3.0.0", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("3.0.0-alpha.1", false)));
		}

		// Test: tilde-ranges. "~a" = ">=a <(a[0]).(a[1]+1).0-" (at least a, same major+minor)
		{
			Predicate<Version> predicate = VersionPredicateParser.parse("~1.2.3");
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.3", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.4", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.4-alpha.1", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.2.2", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.2.3-rc.7", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.3.0", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("2.2.0", false)));
		}

		{
			Predicate<Version> predicate = VersionPredicateParser.parse("~1.2");
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.0", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.1-alpha.3", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.6", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.1.9", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.3.0", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.2.0-rc.2", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.3.0-alpha.3", false)));
		}

		{
			Predicate<Version> predicate = VersionPredicateParser.parse("~1.2-");
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.0", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.1-alpha.3", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.6", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.0-rc.2", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.1.9", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.3.0", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.3.0-alpha.3", false)));
		}

		{
			Predicate<Version> predicate = VersionPredicateParser.parse("~1");
			assertTrue(predicate.test(new SemanticVersionImpl("1.0.0", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.0.4", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.9.9", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.1.5", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("3.0.5", false)));
		}

		{
			Predicate<Version> predicate = VersionPredicateParser.parse("~1.2.3-beta.2");
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.3-beta.2", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.3-beta.2.1", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.3-beta.3", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.3-beta.11", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.3-rc.7", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.3", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.5", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.4-alpha.4", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.3.0", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.2.2", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.2.3-beta.1", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.2.3-beta.1.9", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.2.3-alpha.4", false)));
		}

		// Test: caret-range. "^a" = ">=a <(a[0]+1).0.0-" (at least a, same major)
		{
			Predicate<Version> predicate = VersionPredicateParser.parse("^1.2.3");
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.3", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.4", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.3.0", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.4-beta.2", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.2.2", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.2.3-beta.2", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("2.0.0", false)));
		}

		{
			Predicate<Version> predicate = VersionPredicateParser.parse("^0.2.3");
			assertTrue(predicate.test(new SemanticVersionImpl("0.2.3", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.2.4", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.2.8-beta.2", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("0.3.0", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.2.0", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.2.3-rc.8", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.2.0", false)));
		}

		{
			Predicate<Version> predicate = VersionPredicateParser.parse("^1.2.3-beta.2");
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.3-beta.2", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.3-beta.3", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.3-rc.7", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.3", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.5", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.3.0", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.4-alpha.4", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.2.2", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("2.0.0", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.2.3-alpha.4", false)));
		}

		{
			Predicate<Version> predicate = VersionPredicateParser.parse("^1");
			assertTrue(predicate.test(new SemanticVersionImpl("1.0.0", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.4", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.99.99", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.4-beta.2", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.9.6", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("1.0.0-rc.5", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("2.0.0", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("2.0.0-beta.2", false)));
		}

		{
			Predicate<Version> predicate = VersionPredicateParser.parse("^1-");
			assertTrue(predicate.test(new SemanticVersionImpl("1.0.0", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.0.0-rc.5", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.4", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.99.99", false)));
			assertTrue(predicate.test(new SemanticVersionImpl("1.2.4-beta.2", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.9.0", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("0.9.0-rc.5", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("2.0.0", false)));
			assertFalse(predicate.test(new SemanticVersionImpl("2.0.0-beta.2", false)));
		}
	}
}
