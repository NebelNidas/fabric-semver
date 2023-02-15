# Fabric SemVer
This is Fabric's library for parsing, interpreting and comparing version strings, as well as version ranges. While it's first-and-foremost targeted at working with [Semantic Versioning](https://semver.org) compatible strings, it also has a powerful fallback mode for comparing non-semver versions, using the [FlexVer](https://github.com/unascribed/FlexVer) library.<br>
Fabric SemVer has been split off from [Fabric Loader](https://github.com/FabricMC/fabric-loader), where the code was originally developed at. For backwards compatibility purposes, the package structure hasn't been altered since, though beware that this may change in the future.


## Capabilities & Usage
- Parse versions via `Version.parse(versionString)`.
- Compare versions against each other via `versionA.compareTo(versionB)`. If `versionA` is older than `versionB`, the returned int is negative; if they're the same, it's zero; otherwise a positive int gets returned.
- Running `SemanticVersion.parse(versionString)` directly can yield a `VersionParsingException`, if the passed string isn't SemVer compliant. A safer approach would be to parse the version normally (see above) and check via `version instanceof SemanticVersion` if it could indeed be represented as a semantic version.
- Access parsed `SemanticVersion` metadata via `version.getVersionComponent(position)`, `version.getPrereleaseKey()` etc. See the [SemanticVersion interface](src/main/java/net/fabricmc/loader/api/SemanticVersion.java) for more details.
- Parse version predicates (can be used for version ranges) via `VersionPredicate.parse(predicateString)`.
- `predicate.test(version)` checks whether the passed version satisfies the given predicate.

For more information, please check the Javadoc comments.


## Deviations from vanilla SemVer
Fabric SemVer implements a superset of vanilla Semantic Versioning. This superset allows additionally:
- Arbitrary number of version core components, but at least 1.
- `x`, `X` or `*` for the last version core component.
- Arbitrary build metadata.


## Depending on Fabric SemVer
To use this library in your project, simply add the following dependency to your `build.gradle` file:
```gradle
repositories {
    maven {
        url = "https://maven.fabricmc.net"
        name = "FabricMC"
    }
}

dependencies {
    api 'net.fabricmc:fabric-semver:<VERSION>'
}
```

If you want to depend on the fat JAR ("uberjar"), where all dependencies are already bundled in, use
```gradle
api 'net.fabricmc:fabric-semver:<VERSION>:fat'
```
instead.
