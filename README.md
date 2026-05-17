ForgeGradle
===========

Minecraft mod development framework used by Forge and FML for the gradle build system

## Fix Checklist

- [x] ASM `5.0.3`→`9.7.1` — NPE in `ClassReader.readCode` during reobfJar (modular: `asm` + `asm-commons` + `asm-tree`; exclude from SpecialSource shade dep; force via resolutionStrategy)
- [x] `setupDecompWorkspace`
- [x] `setupCIWorkspace`
- [x] `reobfJar`
- [x] `deobfBinJar`
- [x] `genEclipseRuns`
- [x] `genIntellijRuns`
- [x] `jar` — build compiles and jars
- [x] `compileJava` / `compileKotlin`
- [x] `processResources`
- [x] `deobfCompileDummyTask` / `deobfProvidedDummyTask`
- [x] GradleStart classes
