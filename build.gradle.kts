import codechicken.diffpatch.util.PatchMode

plugins {
    id("java")
    id("com.hypherionmc.modutils.orion.porting") version "2.0.22"
}

orionporting {
    upstreamBranch = "dev"
    // Enable During Porting
    patchMode = PatchMode.FUZZY
    porting(
        "26.1.2",
            "1.21.11",
            "1.21.9",
            "1.21.6",
            "1.21.5",
            "1.21.4",
            "1.21.2",
            "1.21",
            "1.20.1"
    )
}