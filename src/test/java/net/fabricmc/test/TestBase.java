/*
 * Copyright (c) 2023 FabricMC
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

import org.jetbrains.annotations.Nullable;

import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.impl.util.version.SemanticVersionImpl;

abstract class TestBase {
	protected static Exception tryParseSemantic(String s, boolean storeX) {
		try {
			new SemanticVersionImpl(s, storeX);
			return null;
		} catch (VersionParsingException e) {
			return e;
		}
	}

	protected static void assertNoError(@Nullable Exception b) {
		if (b != null) {
			throw new RuntimeException("Test failed!", b);
		}
	}

	protected static void assertError(@Nullable Exception b) {
		if (b == null) {
			throw new RuntimeException("Test failed!");
		}
	}

	protected static void assertTrue(boolean b) {
		if (!b) {
			throw new RuntimeException("Test failed!");
		}
	}

	protected static void assertFalse(boolean b) {
		if (b) {
			throw new RuntimeException("Test failed!");
		}
	}
}
