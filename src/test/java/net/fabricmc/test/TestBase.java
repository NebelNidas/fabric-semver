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
