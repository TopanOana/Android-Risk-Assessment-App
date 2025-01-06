package com.example.androidproject.groupApps

fun createGroupsOfApplications(mapOfQueries: HashMap<String, HashSet<String>>): HashSet<Pair<String, HashSet<String>>> {

    var changesOccurred = true
    val setOfGroups = HashSet<Pair<String, HashSet<String>>>()
    var previousSetOfGroups = HashSet<Pair<String, HashSet<String>>>()

    while (changesOccurred) {
        changesOccurred = false

        for (app in mapOfQueries) {
            if (setOfGroups.isEmpty()) {
                setOfGroups.add(Pair(app.key, app.value))
                changesOccurred = true
            } else {
                val inPairsSecond = setOfGroups.firstOrNull { it.second.contains(app.key) }
                if (inPairsSecond != null) {
                    val restOfAppsInGroup = inPairsSecond.second.containsAll(app.value)
                    if (restOfAppsInGroup)
                        continue
                    else {
                        inPairsSecond.second.addAll(app.value)
                        changesOccurred = true
                    }
                } else {
                    val inPairsFirst = setOfGroups.firstOrNull { it.first == app.key }
                    if (inPairsFirst != null)
                        continue
                    else{
                        setOfGroups.add(Pair(app.key, app.value))
                        changesOccurred = true
                    }
                }

            }
        }
        if (previousSetOfGroups.equals(setOfGroups))
            break
        previousSetOfGroups = setOfGroups.clone() as HashSet<Pair<String, HashSet<String>>>
        mapOfQueries.clear()
        setOfGroups.forEach { pair -> mapOfQueries[pair.first] = pair.second }
        setOfGroups.clear()
    }
    return setOfGroups
}