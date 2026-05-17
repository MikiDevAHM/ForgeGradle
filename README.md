ForgeGradle
===========

Minecraft mod development framework used by Forge and FML for the gradle build system

## Fix Checklist

- [x] ASM `5.0.3`→`9.7.1` — NPE in `ClassReader.readCode` during reobfJar (modular: `asm` + `asm-commons` + `asm-tree`; exclude from SpecialSource shade dep; force via resolutionStrategy)
- [x] `setupDecompWorkspace`
- [x] `setupCIWorkspace`
- [ ] `reobfJar` — NPE fix applied, needs validation
- [ ] `deobfBinJar`
- [x] `genEclipseRuns`
- [x] `genIntellijRuns`
- [x] `jar` — build compiles and jars
- [x] `compileJava` / `compileKotlin`
- [x] `processResources`
- [x] `deobfCompileDummyTask` / `deobfProvidedDummyTask`
- [x] GradleStart classes (authlib, launchwrapper)
- [ ] Srg2Source integration
- [ ] MCInjector integration
- [ ] RetroGuard integration
- [ ] BinPatch generation / application
- [ ] ReobfExceptor (decomp reobf path)
- [ ] Exc modifier extraction (patcher)
- [ ] SRG mapping loading (primary + secondary + extra lines)
- [ ] SpecialSource fallback inheritance provider
- [ ] CSV field/method loading
