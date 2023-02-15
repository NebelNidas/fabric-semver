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

package net.fabricmc.loader.impl.util.version;

import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;

import com.unascribed.flexver.FlexVerComparator;

import net.fabricmc.loader.api.Version;

class VersionComparer {
	static boolean areEqual(Version versionA, Version versionB) {
		boolean versionASemantic = versionA instanceof SemanticVersionImpl;
		boolean versionBSemantic = versionB instanceof SemanticVersionImpl;

		if (versionASemantic && versionBSemantic) {
			return areEqual((SemanticVersionImpl) versionA, (SemanticVersionImpl) versionB);
		}

		if (versionA.getFriendlyString().equals(versionB.getFriendlyString())) {
			return true;
		}

		return FlexVerComparator.compare(versionA.getFriendlyString(), versionB.getFriendlyString()) == 0;
	}

	private static boolean areEqual(SemanticVersionImpl version1, SemanticVersionImpl version2) {
		if (!version1.equalsComponentsExactly(version2)) {
			return false;
		}

		return Objects.equals(version1.prerelease, version2.prerelease)
				&& Objects.equals(version1.build, version2.build);
	}

	static int compare(Version versionA, Version versionB) {
		boolean versionASemantic = versionA instanceof SemanticVersionImpl;
		boolean versionBSemantic = versionB instanceof SemanticVersionImpl;

		if (versionASemantic && versionBSemantic) {
			return compare((SemanticVersionImpl) versionA, (SemanticVersionImpl) versionB);
		}

		return FlexVerComparator.compare(versionA.getFriendlyString(), versionB.getFriendlyString());
	}

	private static int compare(SemanticVersionImpl versionA, SemanticVersionImpl versionB) {
		for (int i = 0; i < Math.max(versionA.getVersionComponentCount(), versionB.getVersionComponentCount()); i++) {
			int first = versionA.getVersionComponent(i);
			int second = versionB.getVersionComponent(i);

			if (first == SemanticVersionImpl.COMPONENT_WILDCARD || second == SemanticVersionImpl.COMPONENT_WILDCARD) {
				continue;
			}

			int compare = Integer.compare(first, second);
			if (compare != 0) return compare;
		}

		Optional<String> prereleaseA = versionA.getPrereleaseKey();
		Optional<String> prereleaseB = versionB.getPrereleaseKey();

		if (prereleaseA.isPresent() || prereleaseB.isPresent()) {
			if (prereleaseA.isPresent() && prereleaseB.isPresent()) {
				StringTokenizer prereleaseATokenizer = new StringTokenizer(prereleaseA.get(), ".");
				StringTokenizer prereleaseBTokenizer = new StringTokenizer(prereleaseB.get(), ".");

				while (prereleaseATokenizer.hasMoreElements()) {
					if (prereleaseBTokenizer.hasMoreElements()) {
						String partA = prereleaseATokenizer.nextToken();
						String partB = prereleaseBTokenizer.nextToken();

						if (SemanticVersionImpl.UNSIGNED_INTEGER.matcher(partA).matches()) {
							if (SemanticVersionImpl.UNSIGNED_INTEGER.matcher(partB).matches()) {
								int compare = Integer.compare(partA.length(), partB.length());
								if (compare != 0) return compare;
							} else {
								return -1;
							}
						} else {
							if (SemanticVersionImpl.UNSIGNED_INTEGER.matcher(partB).matches()) {
								return 1;
							}
						}

						int compare = partA.compareTo(partB);
						if (compare != 0) return compare;
					} else {
						return 1;
					}
				}

				return prereleaseBTokenizer.hasMoreElements() ? -1 : 0;
			} else if (prereleaseA.isPresent()) {
				return versionB.hasWildcard() ? 0 : -1;
			} else { // prereleaseB.isPresent()
				return versionA.hasWildcard() ? 0 : 1;
			}
		} else {
			return 0;
		}
	}
}
