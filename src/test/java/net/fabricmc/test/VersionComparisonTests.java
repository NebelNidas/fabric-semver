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

import net.fabricmc.loader.api.Version;

public class VersionComparisonTests extends TestBase {
	public static void main(String[] args) throws Exception {
		Version v0_3_5_alpha_6_build_120 = Version.parse("0.3.5-alpha.6+build.120");
		Version v0_3_5_beta_2 = Version.parse("0.3.5-beta.2");
		Version v0_3_5 = Version.parse("0.3.5");
		Version v0_3_5_build_3000 = Version.parse("0.3.5+build.3000");
		Version v1_x = Version.parse("1.x");

		assertTrue(v0_3_5_alpha_6_build_120.compareTo(v0_3_5_alpha_6_build_120) == 0);
		assertTrue(v0_3_5_alpha_6_build_120.compareTo(v0_3_5_beta_2) < 0);
		assertTrue(v0_3_5_alpha_6_build_120.compareTo(v0_3_5) < 0);
		assertTrue(v0_3_5_alpha_6_build_120.compareTo(v0_3_5_build_3000) < 0);
		assertTrue(v0_3_5_alpha_6_build_120.compareTo(v1_x) < 0);

		assertTrue(v0_3_5_beta_2.compareTo(v0_3_5_beta_2) == 0);
		assertTrue(v0_3_5_beta_2.compareTo(v0_3_5) < 0);
		assertTrue(v0_3_5_beta_2.compareTo(v0_3_5_build_3000) < 0);
		assertTrue(v0_3_5_beta_2.compareTo(v1_x) < 0);

		assertTrue(v0_3_5.compareTo(v0_3_5) == 0);
		assertTrue(v0_3_5.compareTo(v0_3_5_build_3000) == 0);
		assertTrue(v0_3_5.compareTo(v1_x) < 0);

		assertTrue(v0_3_5_build_3000.compareTo(v0_3_5_build_3000) == 0);
		assertTrue(v0_3_5_build_3000.compareTo(v1_x) < 0);

		assertTrue(v1_x.compareTo(v1_x) == 0);
	}
}
