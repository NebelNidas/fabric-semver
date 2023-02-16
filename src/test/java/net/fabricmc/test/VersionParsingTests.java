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

public class VersionParsingTests extends TestBase {
	public static void main(String[] args) throws Exception {
		// Test: Semantic version creation.
		assertNoError(tryParseSemantic("0.3.5", false));
		assertNoError(tryParseSemantic("0.3.5-beta.2", false));
		assertNoError(tryParseSemantic("0.3.5-alpha.6+build.120", false));
		assertNoError(tryParseSemantic("0.3.5+build.3000", false));
		assertError(tryParseSemantic("0.0.-1", false));
		assertError(tryParseSemantic("0." + ((long) Integer.MAX_VALUE + 1) + ".0", false));
		assertError(tryParseSemantic("0.-1.0", false));
		assertError(tryParseSemantic("-1.0.0", false));
		assertError(tryParseSemantic("", false));
		assertError(tryParseSemantic("0.0.a", false));
		assertError(tryParseSemantic("0.a.0", false));
		assertError(tryParseSemantic("a.0.0", false));
		assertError(tryParseSemantic("x", true));
		assertNoError(tryParseSemantic("2.x", true));
		assertNoError(tryParseSemantic("2.x.x", true));
		assertNoError(tryParseSemantic("2.X", true));
		assertNoError(tryParseSemantic("2.*", true));
		assertError(tryParseSemantic("2.x.1", true));
		assertError(tryParseSemantic("2.*.1", true));
		assertError(tryParseSemantic("2.x-alpha.1", true));
		assertError(tryParseSemantic("2.*-alpha.1", true));
		assertError(tryParseSemantic("*-alpha.1", true));
		assertError(tryParseSemantic("2.x", false));
		assertError(tryParseSemantic("2.X", false));
		assertError(tryParseSemantic("2.*", false));

		// Test: Semantic version creation (spec).
		assertNoError(tryParseSemantic("1.0.0-0.3.7", false));
		assertNoError(tryParseSemantic("1.0.0-x.7.z.92", false));
		assertNoError(tryParseSemantic("1.0.0+20130313144700", false));
		assertNoError(tryParseSemantic("1.0.0-beta+exp.sha.5114f85", false));
	}
}
